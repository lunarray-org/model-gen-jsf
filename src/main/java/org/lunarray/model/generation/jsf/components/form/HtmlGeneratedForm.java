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
package org.lunarray.model.generation.jsf.components.form;

import java.util.LinkedList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.validator.EntityValidator;
import org.lunarray.model.generation.jsf.components.AbstractGeneratedComponent;
import org.lunarray.model.generation.jsf.components.GeneratedForm;
import org.lunarray.model.generation.jsf.listeners.DefaultValidationListener;
import org.lunarray.model.generation.jsf.render.JsfStyleClasses;
import org.lunarray.model.generation.jsf.render.RenderContext;
import org.lunarray.model.generation.jsf.render.StyleClassResolver;
import org.lunarray.model.generation.jsf.render.def.PropertyFileStyleClassResolver;
import org.lunarray.model.generation.jsf.render.extensions.ViewExtension;
import org.lunarray.model.generation.jsf.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.jsf.render.factories.form.jsf.DefaultFormPropertyRenderStrategyFactory;
import org.lunarray.model.generation.jsf.util.RenderFactoryUtil;
import org.lunarray.model.generation.util.Composer;
import org.lunarray.model.generation.util.VariableResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A JSF generated form.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            The super type.
 * @param <E>
 *            The entity type.
 */
public final class HtmlGeneratedForm<S, E extends S>
		extends AbstractGeneratedComponent<S, E>
		implements GeneratedForm<S, E> {

	/** The layout type. */
	private static final String BLOCK_LAYOUT = "block";
	/** Column count horizontal message. */
	private static final int COLUMN_COUNT_HOR = 3;
	/** Column count for a single column. */
	private static final int COLUMN_COUNT_SINGLE = 1;
	/** Column count vertical message. */
	private static final int COLUMN_COUNT_VER = 2;
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlGeneratedForm.class);
	/** Validation message. */
	private static final String STRATEGY_NULL = "Strategy may not be null.";
	/** The layout type. */
	private static final String TABLE_LAYOUT = "table";
	/** Component list. */
	private transient List<UIComponent> components;
	/** The message position. */
	private transient MessagePosition messagePosition;
	/** The orientation. */
	private transient Orientation orientation;

	/**
	 * Default constructor.
	 */
	public HtmlGeneratedForm() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public String getRendererType() {
		return "HtmlGeneratedFormRenderer";
	}

	/** {@inheritDoc} */
	@Override
	public void processProperty(final FormPropertyRenderStrategy<?, ?> strategy, final RenderContext renderContext) {
		Validate.notNull(strategy, HtmlGeneratedForm.STRATEGY_NULL);
		Validate.notNull(renderContext, "Render context may not be null.");
		String layoutType = HtmlGeneratedForm.BLOCK_LAYOUT;
		if (!CheckUtil.isNull(this.getAttributes().get(PropertyKeys.LAYOUT.getName()))) {
			layoutType = this.getAttributes().get(PropertyKeys.LAYOUT.getName()).toString();
		}
		List<UIComponent> parent = this.components;
		HtmlGeneratedForm.LOGGER.debug("Rendering with layout {}.", layoutType);
		if (HtmlGeneratedForm.BLOCK_LAYOUT.equals(layoutType)) {
			final HtmlPanelGroup parentGroup = new HtmlPanelGroup();
			parentGroup.setLayout(HtmlGeneratedForm.BLOCK_LAYOUT);
			parentGroup.setStyleClass(renderContext.resolveStyleClass(JsfStyleClasses.FORM_CONTROL_ROW));
			parent = parentGroup.getChildren();
			this.components.add(parentGroup);
		}
		parent.add(strategy.getLabel());
		parent.add(this.processLayout(strategy, renderContext, layoutType));
	}

	/** {@inheritDoc} */
	@Override
	protected void rerender(final FacesContext context) {
		Validate.notNull(context, "Faces context may not be null.");
		final StyleClassResolver resolver = this.getValueOrDefault(PropertyKeys.STYLECLASS_RESOLVER, context,
				new PropertyFileStyleClassResolver());
		final List<UIComponent> childComponents = new LinkedList<UIComponent>(this.getChildren());
		// Resolve model.
		final Model<Object> model = this.getValue(PropertyKeys.MODEL, context);
		// Instantiate the composer.
		final Composer<RenderContext, S, E> composer = new Composer<RenderContext, S, E>();
		final RenderContext jsfContext = new RenderContext().facesContext(context).model(model)
				.modelExpression(this.getExpression(PropertyKeys.MODEL));
		jsfContext.styleClassResolver(resolver);
		composer.setContext(jsfContext);
		final JsfVariableResolver variableResolver = new JsfVariableResolver();
		composer.setVariableResolver(variableResolver);
		composer.setPropertyRenderStrategyFactory(new DefaultFormPropertyRenderStrategyFactory<E>(this));
		// Resolve value.
		final ValueExpression valueExpression = this.getExpression(PropertyKeys.VALUE);
		this.getState().setValue(valueExpression.getValue(context.getELContext()));
		final UIComponent htmlForm;
		// Resolve use outer form.
		boolean outerForm;
		final String outerFormString = this.resolveStringValue(PropertyKeys.OUTER_FORM, context);
		outerForm = Boolean.parseBoolean(outerFormString);
		// Create panel.
		HtmlGeneratedForm.LOGGER.debug("Rerendering using outer form {}.", outerForm);
		if (outerForm) {
			HtmlPanelGroup tmpHtmlForm;
			tmpHtmlForm = new HtmlPanelGroup();
			tmpHtmlForm.setStyleClass(resolver.resolve(JsfStyleClasses.FORM_PANEL));
			htmlForm = tmpHtmlForm;
		} else {
			final HtmlForm tmpHtmlForm = new HtmlForm();
			tmpHtmlForm.setId(this.getId() + "_form");
			tmpHtmlForm.setStyleClass(resolver.resolve(JsfStyleClasses.FORM));
			htmlForm = tmpHtmlForm;
		}
		this.process(childComponents, model, composer, valueExpression, htmlForm);
	}

	/**
	 * Process.
	 * 
	 * @param components
	 *            The embedded components.
	 * @param model
	 *            The model.
	 * @param composer
	 *            The composer.
	 * @param valueExpression
	 *            The value expression.
	 * @param htmlForm
	 *            The form.
	 */
	private void process(final List<UIComponent> components, final Model<Object> model, final Composer<RenderContext, S, E> composer,
			final ValueExpression valueExpression, final UIComponent htmlForm) {
		final RenderContext jsfContext = composer.getContext();
		final FacesContext context = jsfContext.getFacesContext();
		this.getChildren().add(htmlForm);
		this.processVariable(context);
		boolean showLabel = false;
		if (this.getAttributes().containsKey(PropertyKeys.SHOW_LABEL.getName())) {
			showLabel = Boolean.parseBoolean(this.getAttributes().get(PropertyKeys.SHOW_LABEL.getName()).toString());
		}
		if (showLabel) {
			this.processLabel(composer, htmlForm);
		}
		this.processExtensions(context, valueExpression, htmlForm);
		this.processTable(composer, jsfContext, htmlForm);
		// Add children as actions in a new group.
		final HtmlPanelGroup panelGroup = new HtmlPanelGroup();
		panelGroup.setLayout(HtmlGeneratedForm.BLOCK_LAYOUT);
		panelGroup.setStyleClass(jsfContext.resolveStyleClass(JsfStyleClasses.FORM_INNER_PANEL));
		if (!CheckUtil.isNull(model.getExtension(EntityValidator.class))) {
			for (final UIComponent component : components) {
				this.processOperation(jsfContext, composer.getVariableResolver(), htmlForm.getClientId(context), component);
			}
		}
		htmlForm.getChildren().addAll(components);
		this.getChildren().add(panelGroup);
	}

	/**
	 * Process the extensions.
	 * 
	 * @param context
	 *            The context.
	 * @param valueExpression
	 *            The value.
	 * @param htmlForm
	 *            The form.
	 */
	private void processExtensions(final FacesContext context, final ValueExpression valueExpression, final UIComponent htmlForm) {
		for (final ViewExtension extension : RenderFactoryUtil.getViewExtensions().values()) {
			HtmlGeneratedForm.LOGGER.debug("Processing view extension: {}", extension);
			extension.addExtension(htmlForm, valueExpression, context);
		}
	}

	/**
	 * Process the label.
	 * 
	 * @param composer
	 *            The composer.
	 * @param htmlForm
	 *            The form.
	 */
	private void processLabel(final Composer<RenderContext, S, E> composer, final UIComponent htmlForm) {
		final HtmlOutputText labelText = new HtmlOutputText();
		labelText.setValue(composer.getLabel());
		labelText.setStyleClass(composer.getContext().resolveStyleClass(JsfStyleClasses.FORM_LABEL));
		htmlForm.getChildren().add(labelText);
	}

	/**
	 * Process the layout.
	 * 
	 * @param strategy
	 *            The strategy to process.
	 * @param renderContext
	 *            The render context.
	 * @param layoutType
	 *            The layout type.
	 * @return The component.
	 */
	private UIComponent processLayout(final FormPropertyRenderStrategy<?, ?> strategy, final RenderContext renderContext,
			final String layoutType) {
		UIComponent container;
		if (HtmlGeneratedForm.TABLE_LAYOUT.equals(layoutType)) {
			final HtmlPanelGrid grid = new HtmlPanelGrid();
			grid.setStyleClass(renderContext.resolveStyleClass(JsfStyleClasses.FORM_CONTROL_TABLE));
			if (Orientation.HORIZONTAL == this.orientation) {
				grid.setColumns(HtmlGeneratedForm.COLUMN_COUNT_VER);
			} else {
				grid.setColumns(HtmlGeneratedForm.COLUMN_COUNT_SINGLE);
			}
			container = grid;
		} else {
			final HtmlPanelGroup group = new HtmlPanelGroup();
			group.setLayout(HtmlGeneratedForm.BLOCK_LAYOUT);
			group.setStyleClass(renderContext.resolveStyleClass(JsfStyleClasses.FORM_CONTROL_BLOCK));
			container = group;
		}
		final List<UIComponent> containerComponents = container.getChildren();
		if ((MessagePosition.TOP == this.messagePosition) || (MessagePosition.LEFT == this.messagePosition)) {
			containerComponents.add(strategy.getMessages());
			containerComponents.add(strategy.getInput());
		} else {
			containerComponents.add(strategy.getInput());
			containerComponents.add(strategy.getMessages());
		}
		return container;
	}

	/**
	 * Process the operation.
	 * 
	 * @param jsfContext
	 *            The jsf context.
	 * @param variableResolver
	 *            The variable resolver.
	 * @param parentId
	 *            The parent id.
	 * @param component
	 *            The component.
	 */
	private void processOperation(final RenderContext jsfContext, final VariableResolver<RenderContext, S, E> variableResolver,
			final String parentId, final UIComponent component) {
		if (component instanceof UICommand) {
			final UICommand operation = (UICommand) component;
			HtmlGeneratedForm.LOGGER.debug("Processing command {}", component);
			operation.addActionListener(new DefaultValidationListener(parentId, variableResolver.getDescriptor(jsfContext).getName(), this
					.getExpression(PropertyKeys.MODEL), this.getExpression(PropertyKeys.VALUE), jsfContext.getPrefixDeque()));
		}
	}

	/**
	 * Process the table.
	 * 
	 * @param composer
	 *            The composer.
	 * @param jsfContext
	 *            The context.
	 * @param htmlForm
	 *            The form.
	 */
	private void processTable(final Composer<?, S, E> composer, final RenderContext jsfContext, final UIComponent htmlForm) {
		this.messagePosition = MessagePosition.getPosition(this.resolveStringValue(PropertyKeys.MESSAGE, jsfContext.getFacesContext()));
		this.orientation = this.messagePosition.getOrientation();
		String layoutType = HtmlGeneratedForm.BLOCK_LAYOUT;
		if (!CheckUtil.isNull(this.getAttributes().get(PropertyKeys.LAYOUT.getName()))) {
			layoutType = this.getAttributes().get(PropertyKeys.LAYOUT.getName()).toString();
		}
		UIComponent layout;
		if (HtmlGeneratedForm.TABLE_LAYOUT.equals(layoutType)) {
			// Use grid.
			final HtmlPanelGrid htmlPanelGrid = new HtmlPanelGrid();
			layout = htmlPanelGrid;
			htmlPanelGrid.setStyleClass(jsfContext.resolveStyleClass(JsfStyleClasses.FORM_LAYOUT_TABLE));
			if (Orientation.HORIZONTAL == this.orientation) {
				htmlPanelGrid.setColumns(HtmlGeneratedForm.COLUMN_COUNT_HOR);
			} else {
				htmlPanelGrid.setColumns(HtmlGeneratedForm.COLUMN_COUNT_VER);
			}
		} else {
			// Use block.
			final HtmlPanelGroup htmlPanelGrid = new HtmlPanelGroup();
			htmlPanelGrid.setLayout(HtmlGeneratedForm.BLOCK_LAYOUT);
			layout = htmlPanelGrid;
			htmlPanelGrid.setStyleClass(jsfContext.resolveStyleClass(JsfStyleClasses.FORM_LAYOUT_BLOCK));
		}
		// Render properties.
		jsfContext.setVariable(this.getState().getVariable());
		htmlForm.getChildren().add(layout);
		this.components = layout.getChildren();
		composer.compose(false);
	}

	/**
	 * Message position.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public enum MessagePosition {
		/** Messages below the input. */
		BOTTOM(Orientation.VERTICAL),
		/** Messages to the left of the input. */
		LEFT(Orientation.HORIZONTAL),
		/** Messages to the right of the input. */
		RIGHT(Orientation.HORIZONTAL),
		/** Messages above the input. */
		TOP(Orientation.VERTICAL);

		/** The message orientation. */
		private Orientation orientation;

		/**
		 * Default constructor.
		 * 
		 * @param orientation
		 *            The orientation.
		 */
		private MessagePosition(final Orientation orientation) {
			this.orientation = orientation;
		}

		/**
		 * Guesses the message position, returns {@link MessagePosition#RIGHT}.
		 * 
		 * @param stringValue
		 *            The string value to guess for.
		 * @return The position matching the stringValue, or
		 *         {@link MessagePosition#RIGHT}.
		 */
		public static MessagePosition getPosition(final String stringValue) {
			MessagePosition result = RIGHT;
			for (final MessagePosition value : MessagePosition.values()) {
				if (value.name().equalsIgnoreCase(stringValue)) {
					result = value;
				}
			}
			return result;
		}

		/**
		 * Gets the orientation.
		 * 
		 * @return The orientation.
		 */
		public Orientation getOrientation() {
			return this.orientation;
		}
	}

	/**
	 * Describes an orientation.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public enum Orientation {
		/** Horizontal orientation. */
		HORIZONTAL,
		/** Vertical orientation. */
		VERTICAL;
	}
}
