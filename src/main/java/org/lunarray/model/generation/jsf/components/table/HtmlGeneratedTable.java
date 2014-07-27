/* 
 * Model Tools.
 * Copyright (C) 2013 Pal Hargitai (pal@lunarray.org)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lunarray.model.generation.jsf.components.table;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutcomeTargetButton;
import javax.faces.component.html.HtmlOutcomeTargetLink;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.dictionary.PaginatedDictionary;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.generation.jsf.components.AbstractGeneratedComponent;
import org.lunarray.model.generation.jsf.components.GeneratedTable;
import org.lunarray.model.generation.jsf.components.shared.action.HtmlAction;
import org.lunarray.model.generation.jsf.components.shared.check.HtmlSelectingCheckbox;
import org.lunarray.model.generation.jsf.components.shared.state.CollectionState;
import org.lunarray.model.generation.jsf.components.shared.state.ValueState;
import org.lunarray.model.generation.jsf.listeners.SetPropertyActionListener;
import org.lunarray.model.generation.jsf.render.JsfStyleClasses;
import org.lunarray.model.generation.jsf.render.RenderContext;
import org.lunarray.model.generation.jsf.render.StyleClassResolver;
import org.lunarray.model.generation.jsf.render.def.PropertyFileStyleClassResolver;
import org.lunarray.model.generation.jsf.render.factories.datatable.DataTableBuilder;
import org.lunarray.model.generation.jsf.render.factories.table.TablePropertyDescriptorRenderStrategy;
import org.lunarray.model.generation.jsf.render.factories.table.jsf.DefaultTablePropertyRenderStrategyFactory;
import org.lunarray.model.generation.jsf.util.PaginatedDictionaryDataTableModel;
import org.lunarray.model.generation.jsf.util.RenderFactoryUtil;
import org.lunarray.model.generation.util.Composer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A faces generated table.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            The super type.
 * @param <E>
 *            The entity type.
 */
public final class HtmlGeneratedTable<S, E extends S>
		extends AbstractGeneratedComponent<S, E>
		implements GeneratedTable<S, E> {
	/** Validation message. */
	private static final String FACES_CONTEXT_NULL = "Faces context may not be null.";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlGeneratedTable.class);
	/** The action. */
	private transient HtmlAction actionGenerated;
	/** The table. */
	private transient DataTableBuilder builderGenerated;
	/** An expression factory. */
	private final transient ExpressionFactory expressionFactory = ExpressionFactory.newInstance();

	/**
	 * Default constructor.
	 */
	public HtmlGeneratedTable() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public String getRendererType() {
		return "HtmlGeneratedTableRenderer";
	}

	/** {@inheritDoc} */
	@Override
	public void processStrategy(final FacesContext context, final TablePropertyDescriptorRenderStrategy<?, ?> strategy) {
		Validate.notNull(context, HtmlGeneratedTable.FACES_CONTEXT_NULL);
		Validate.notNull(strategy, "Strategy may not be null.");
		final HtmlColumn column = new HtmlColumn();
		column.setHeader(strategy.getLabel());
		final StyleClassResolver resolver = this.getValueOrDefault(PropertyKeys.STYLECLASS_RESOLVER, context,
				new PropertyFileStyleClassResolver());
		String tableFooter = JsfStyleClasses.TABLE_FOOTER.getDefaultValue();
		String tableHeader = JsfStyleClasses.TABLE_HEADER.getDefaultValue();
		String tableLink = JsfStyleClasses.TABLE_LINK.getDefaultValue();
		if (!CheckUtil.isNull(resolver)) {
			tableFooter = resolver.resolve(JsfStyleClasses.TABLE_FOOTER);
			tableHeader = resolver.resolve(JsfStyleClasses.TABLE_HEADER);
			tableLink = resolver.resolve(JsfStyleClasses.TABLE_LINK);
		}
		column.setFooterClass(tableFooter);
		column.setHeaderClass(tableHeader);
		if (CheckUtil.isNull(this.actionGenerated)) {
			column.getChildren().add(strategy.getOutput());
		} else {
			this.processAction(context, strategy, column, tableLink);
		}
		this.builderGenerated.addColumn(column);
	}

	/**
	 * Rerender the table.
	 * 
	 * @param context
	 *            The faces context.
	 */
	@Override
	protected void rerender(final FacesContext context) {
		Validate.notNull(context, HtmlGeneratedTable.FACES_CONTEXT_NULL);
		final StyleClassResolver resolver = this.getValueOrDefault(PropertyKeys.STYLECLASS_RESOLVER, context,
				new PropertyFileStyleClassResolver());
		final List<UIComponent> components = new LinkedList<UIComponent>(this.getChildren());
		// Resolve model.
		final Model<Object> model = this.getValue(PropertyKeys.MODEL, context);
		// Instantiate the composer.
		final RenderContext jsfContext = new RenderContext().facesContext(context).model(model)
				.modelExpression(this.getExpression(PropertyKeys.MODEL));
		jsfContext.styleClassResolver(resolver);
		final Composer<RenderContext, S, E> composer = this.setupComposer(jsfContext);
		// Resolve value.
		this.getState().setValue(this.getValue(PropertyKeys.VALUE, context));
		// Resolve use outer form.
		boolean outerForm;
		final String outerFormString = this.resolveStringValue(PropertyKeys.OUTER_FORM, context);
		outerForm = Boolean.parseBoolean(outerFormString);
		final UIComponent htmlForm;
		String parentId;
		HtmlGeneratedTable.LOGGER.debug("Rerendering using outer form {}", outerForm);
		// Create panel.
		if (outerForm) {
			HtmlPanelGroup htmlFormTmp;
			htmlFormTmp = new HtmlPanelGroup();
			htmlFormTmp.setStyleClass(jsfContext.resolveStyleClass(JsfStyleClasses.TABLE_FORM));
			htmlForm = htmlFormTmp;
			parentId = this.getClientId(context);
		} else {
			HtmlForm htmlFormTmp;
			htmlFormTmp = new HtmlForm();
			htmlFormTmp.setId(this.getId() + "_form");
			htmlFormTmp.setStyleClass(jsfContext.resolveStyleClass(JsfStyleClasses.TABLE_FORM));
			parentId = htmlFormTmp.getClientId(context);
			htmlForm = htmlFormTmp;
		}
		this.getChildren().add(htmlForm);
		this.processVariable(context);
		boolean showLabel = false;
		if (this.getAttributes().containsKey(PropertyKeys.SHOW_LABEL.getName())) {
			showLabel = Boolean.parseBoolean(this.getAttributes().get(PropertyKeys.SHOW_LABEL.getName()).toString());
		}
		if (showLabel) {
			this.createLabel(htmlForm, composer);
		}
		this.processTable(context, composer, jsfContext, htmlForm, parentId);
		// Add children as actions in a new group.
		final HtmlPanelGroup panelGroup = new HtmlPanelGroup();
		panelGroup.getChildren().addAll(components);
		this.getChildren().add(panelGroup);
	}

	/**
	 * Creates the label.
	 * 
	 * @param composer
	 *            The composer.
	 * @param htmlForm
	 *            The form.
	 */
	private void createLabel(final UIComponent htmlForm, final Composer<RenderContext, ?, ?> composer) {
		final HtmlOutputText labelText = new HtmlOutputText();
		labelText.setValue(composer.getLabel());
		labelText.setStyleClass(composer.getContext().resolveStyleClass(JsfStyleClasses.TABLE_LABEL));
		htmlForm.getChildren().add(labelText);
	}

	/**
	 * Creates the table.
	 * 
	 * @param htmlForm
	 *            The form.
	 * @param context
	 *            The context.
	 * @param <E>
	 *            The entity type.
	 */
	@SuppressWarnings("hiding")
	private <E> void createTable(final UIComponent htmlForm, final RenderContext context) {
		final HtmlDataTable table = this.builderGenerated.build(context);
		table.setStyleClass(context.resolveStyleClass(JsfStyleClasses.TABLE));
		final ELContext elCtx = context.getFacesContext().getELContext();
		final Object value = this.getExpression(PropertyKeys.VALUE).getValue(elCtx);
		if (CheckUtil.isNull(value) || (value instanceof PaginatedDictionary)) {
			final PaginatedDictionaryDataTableModel<E> tableModel = new PaginatedDictionaryDataTableModel<E>();
			final Model<Object> model = context.getModel();
			if (CheckUtil.isNull(value)) {
				tableModel.setDictionary(model.getExtension(PaginatedDictionary.class));
			} else {
				tableModel.setDictionary((PaginatedDictionary) value);
			}
			@SuppressWarnings("unchecked")
			final Class<E> type = (Class<E>) this.getExpression(PropertyKeys.CLAZZ).getValue(elCtx);
			final EntityDescriptor<E> descriptor = model.getEntity(type);
			tableModel.setEntity(descriptor);
			table.setValue(tableModel);
		} else {
			table.setValueExpression(PropertyKeys.VALUE.getName(), this.getExpression(PropertyKeys.VALUE));
		}
		table.setValueExpression(PropertyKeys.ROWS.getName(), this.getExpression(PropertyKeys.ROWS));
		table.setVar(this.getState().getVariable());
		htmlForm.getChildren().add(table);
	}

	/**
	 * Gets a variable reference.
	 * 
	 * @param context
	 *            The faces context.
	 * @return The value expression referring to the variable.
	 */
	private ValueExpression getVariableReference(final FacesContext context) {
		return this.expressionFactory.createValueExpression(context.getELContext(), String.format("#{%s}", this.getState().getVariable()),
				Object.class);
	}

	/**
	 * Process the action.
	 * 
	 * @param context
	 *            The context.
	 * @param strategy
	 *            The strategy.
	 * @param column
	 *            The column.
	 * @param tableLink
	 *            The table link.
	 */
	private void processAction(final FacesContext context, final TablePropertyDescriptorRenderStrategy<?, ?> strategy,
			final HtmlColumn column, final String tableLink) {
		final ValueExpression renderTypeExpression = this.actionGenerated.getRenderType();
		String renderType = "link";
		if (!CheckUtil.isNull(renderTypeExpression)) {
			final Object renderObj = renderTypeExpression.getValue(context.getELContext());
			if (!CheckUtil.isNull(renderObj)) {
				renderType = renderObj.toString();
			}
		}
		final List<UIComponent> components = new LinkedList<UIComponent>();
		for (final UIComponent component : this.actionGenerated.getChildren()) {
			this.processComponentAction(components, component);
		}
		HtmlGeneratedTable.LOGGER.debug("Processing command for strategy {}", strategy);
		UIComponent component;
		if ("commandLink".equals(renderType)) {
			final HtmlCommandLink link = new HtmlCommandLink();
			link.setStyleClass(tableLink);
			link.setActionExpression(this.actionGenerated.getActionExpression());
			link.addActionListener(new SetPropertyActionListener(this.actionGenerated.getTargetExpression(), this
					.getVariableReference(context)));
			component = link;
		} else if ("commandButton".equals(renderType)) {
			final HtmlCommandButton button = new HtmlCommandButton();
			button.setStyleClass(tableLink);
			button.setActionExpression(this.actionGenerated.getActionExpression());
			button.addActionListener(new SetPropertyActionListener(this.actionGenerated.getTargetExpression(), this
					.getVariableReference(context)));
			component = button;
		} else if ("button".equals(renderType)) {
			final HtmlOutcomeTargetButton button = new HtmlOutcomeTargetButton();
			button.setOutcome(this.actionGenerated.getOutcome(context));
			button.setStyleClass(tableLink);
			component = button;
		} else {
			final HtmlOutcomeTargetLink link = new HtmlOutcomeTargetLink();
			link.setOutcome(this.actionGenerated.getOutcome(context));
			link.setStyleClass(tableLink);
			component = link;
		}
		component.getChildren().addAll(components);
		component.getChildren().add(strategy.getOutput());
		column.getChildren().add(component);
	}

	/**
	 * Process the action.
	 * 
	 * @param components
	 *            The component list.
	 * @param component
	 *            The selected component.
	 */
	private void processComponentAction(final List<UIComponent> components, final UIComponent component) {
		if (component instanceof UIParameter) {
			final UIParameter source = (UIParameter) component;
			final UIParameter param = new UIParameter();
			param.setName(source.getName());
			final String[] attrs = { "value", "name" };
			for (final String attr : attrs) {
				param.setValueExpression(attr, source.getValueExpression(attr));
			}
			components.add(param);
		} else {
			components.add(component);
		}
	}

	/**
	 * Process the first column checkbox.
	 * 
	 * @param context
	 *            The context.
	 * @param parentId
	 *            The parent id.
	 */
	private void processFirstCheckbox(final FacesContext context, final String parentId) {
		final HtmlAction action = this.resolveFacet(FacetKeys.SELECT_ACTION, HtmlAction.class);
		if (!CheckUtil.isNull(action)) {
			// Add first checkbox.
			final HtmlSelectingCheckbox checkbox = new HtmlSelectingCheckbox();
			final ValueExpression targetExpression = action.getTargetExpression();
			final Object value = targetExpression.getValue(context.getELContext());
			final ValueExpression valueExpression = this.getVariableReference(context);
			if (value instanceof Collection) {
				// Multiples.
				checkbox.setListener(new CollectionState(targetExpression, valueExpression));
			} else {
				// Singles
				checkbox.setOnChange(String.format("%s.checkSingleRadio();submit()", parentId.replace("_", "")));
				checkbox.setListener(new ValueState(targetExpression, valueExpression));
			}
			final HtmlColumn firstColumn = new HtmlColumn();
			firstColumn.getChildren().add(checkbox);
			this.builderGenerated.addColumn(firstColumn);
		}
	}

	/**
	 * Sets the table.
	 * 
	 * @param context
	 *            The context.
	 * @param composer
	 *            The composer.
	 * @param jsfContext
	 *            The jsf context.
	 * @param htmlForm
	 *            The html form.
	 * @param parentId
	 *            The parent id.
	 */
	private void processTable(final FacesContext context, final Composer<RenderContext, ?, ?> composer, final RenderContext jsfContext,
			final UIComponent htmlForm, final String parentId) {
		// Use add table.
		this.builderGenerated = RenderFactoryUtil.getDataTableBuilder();
		this.actionGenerated = this.resolveFacet(FacetKeys.ROW_ACTION, HtmlAction.class);
		// Render properties.
		jsfContext.setVariable(this.getState().getVariable());
		this.processFirstCheckbox(context, parentId);
		composer.compose(false);
		this.createTable(htmlForm, jsfContext);
	}

	/**
	 * Resolve a faces.
	 * 
	 * @param <T>
	 *            The facet type.
	 * @param key
	 *            The facet key.
	 * @param clazz
	 *            The facet type.
	 * @return The facet.
	 */
	private <T> T resolveFacet(final FacetKeys key, final Class<T> clazz) {
		T result = null;
		final String facetName = key.getName();
		if (!CheckUtil.isNull(this.getFacet(facetName)) && clazz.isInstance(this.getFacet(facetName))) {
			result = clazz.cast(this.getFacet(facetName));
		}
		return result;
	}

	/**
	 * Sets up the composer.
	 * 
	 * @param jsfContext
	 *            The jsf context.
	 * @return The composer.
	 */
	private Composer<RenderContext, S, E> setupComposer(final RenderContext jsfContext) {
		final Composer<RenderContext, S, E> composer = new Composer<RenderContext, S, E>();
		composer.setContext(jsfContext);
		final JsfVariableResolver variableResolver = new JsfVariableResolver();
		composer.setVariableResolver(variableResolver);
		composer.setPropertyRenderStrategyFactory(new DefaultTablePropertyRenderStrategyFactory<E>(this));
		return composer;
	}

	/**
	 * The facet keys.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	private enum FacetKeys {
		/** The row action. */
		ROW_ACTION("rowAction"),
		/** A select action. */
		SELECT_ACTION("selectAction");
		/** The facet name. */
		private final String name;

		/**
		 * Default constructor.
		 * 
		 * @param name
		 *            The facet name.
		 */
		private FacetKeys(final String name) {
			this.name = name;
		}

		/**
		 * Gets the value for the name field.
		 * 
		 * @return The value for the name field.
		 */
		public String getName() {
			return this.name;
		}
	}
}
