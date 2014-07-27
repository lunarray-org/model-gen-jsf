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
package org.lunarray.model.generation.jsf.render.factories.form.jsf.components;

import javax.el.ExpressionFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.convert.NumberConverter;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationPropertyDescriptor;
import org.lunarray.model.generation.jsf.render.JsfStyleClasses;
import org.lunarray.model.generation.jsf.render.RenderContext;
import org.lunarray.model.generation.jsf.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.jsf.util.TypeUtil;
import org.lunarray.model.generation.jsf.util.VariableUtil;

/**
 * An abstract descriptor strategy.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public abstract class AbstractRenderStrategy<P, E>
		implements FormPropertyRenderStrategy<P, E> {

	/** Validation message. */
	private static final String CONTEXT_NULL = "Context may not be null.";
	/** Validation message. */
	private static final String PROPERTY_NULL = "Property may not be null.";
	/** Separator char. */
	private static final String SEPARATOR = ":";
	/** An expression factory. */
	private final transient ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
	/** The input. */
	private transient UIInput input;
	/** The label. */
	private transient HtmlOutputLabel label;
	/** The message. */
	private transient HtmlPanelGroup messages;

	/**
	 * Default constructor.
	 */
	public AbstractRenderStrategy() {
		// Default constructor.
	}

	/**
	 * Build a label.
	 * 
	 * @param property
	 *            The property. May not be null.
	 * @param context
	 *            The jsf context. May not be null.
	 */
	public final void buildLabel(final PresentationPropertyDescriptor<?, ?> property, final RenderContext context) {
		Validate.notNull(property, AbstractRenderStrategy.PROPERTY_NULL);
		Validate.notNull(context, AbstractRenderStrategy.CONTEXT_NULL);
		this.label = new HtmlOutputLabel();
		this.label.setId(this.createLabelName(context));
		this.label.setFor(this.createInputName(context));
		final StringBuilder builder = new StringBuilder();
		builder.append(property.getDescription(context.getFacesContext().getViewRoot().getLocale()));
		if (property.isRequiredIndication()) {
			builder.append("*");
		}
		builder.append(AbstractRenderStrategy.SEPARATOR);
		this.label.setValue(builder.toString());
		this.label.setStyleClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_LABEL));
	}

	/**
	 * Build a label.
	 * 
	 * @param property
	 *            The property. May not be null.
	 * @param context
	 *            The jsf context. May not be null.
	 */
	public final void buildLabel(final PropertyDescriptor<?, ?> property, final RenderContext context) {
		Validate.notNull(property, AbstractRenderStrategy.PROPERTY_NULL);
		Validate.notNull(context, AbstractRenderStrategy.CONTEXT_NULL);
		this.label = new HtmlOutputLabel();
		this.label.setId(this.createLabelName(context));
		this.label.setFor(this.createInputName(context));
		String labelValue = property.getName();
		if (property.adaptable(PresentationPropertyDescriptor.class)) {
			@SuppressWarnings("unchecked")
			final PresentationPropertyDescriptor<P, E> descriptor = property.adapt(PresentationPropertyDescriptor.class);
			labelValue = descriptor.getDescription(context.getFacesContext().getViewRoot().getLocale());
		}
		final StringBuilder builder = new StringBuilder();
		builder.append(labelValue);
		builder.append(AbstractRenderStrategy.SEPARATOR);
		this.label.setValue(builder.toString());
		this.label.setStyleClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_LABEL));
	}

	/**
	 * Build the massages.
	 * 
	 * @param property
	 *            The property.
	 * @param context
	 *            The jsf context. May not be null.
	 */
	public final void buildMessages(final PropertyDescriptor<?, ?> property, final RenderContext context) {
		Validate.notNull(context, AbstractRenderStrategy.CONTEXT_NULL);
		this.messages = new HtmlPanelGroup();
		this.messages.setStyleClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_MSG_PANEL));
		this.messages.setLayout("block");
		final HtmlMessage imessages = new HtmlMessage();
		imessages.setId(VariableUtil.compileName("msg_", context.getPrefixDeque()));
		imessages.setFor(this.createInputName(context));
		imessages.setErrorClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_MSG_ERROR));
		imessages.setWarnClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_MSG_WARN));
		imessages.setInfoClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_MSG_INFO));
		imessages.setStyleClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_MSG));
		this.messages.getChildren().add(imessages);
	}

	/** {@inheritDoc} */
	@Override
	public final UIInput getInput() {
		return this.input;
	}

	/** {@inheritDoc} */
	@Override
	public final UIOutput getLabel() {
		return this.label;
	}

	/** {@inheritDoc} */
	@Override
	public final UIComponent getMessages() {
		return this.messages;
	}

	/**
	 * Sets a value to the input.
	 * 
	 * @param input
	 *            The input to set the value to. May not be null.
	 * @param property
	 *            The property. May not be null.
	 * @param context
	 *            The jsf context. May not be null.
	 */
	protected final void addValue(final UIInput input, final PropertyDescriptor<?, ?> property, final RenderContext context) {
		Validate.notNull(input, "Input may not be null.");
		Validate.notNull(property, AbstractRenderStrategy.PROPERTY_NULL);
		Validate.notNull(context, AbstractRenderStrategy.CONTEXT_NULL);
		input.setValueExpression(
				"value",
				this.expressionFactory.createValueExpression(context.getFacesContext().getELContext(),
						VariableUtil.compileVariable(context.getVariable(), context.getPrefixDeque()), Object.class));
		final PresentationPropertyDescriptor<?, ?> presentationProperty = property.adapt(PresentationPropertyDescriptor.class);
		if (TypeUtil.isNumberType(property.getPropertyType()) && !CheckUtil.isNull(presentationProperty)) {
			final NumberConverter converter = new NumberConverter();
			converter.setPattern(presentationProperty.getFormat());
			input.setConverter(converter);
		}
	}

	/**
	 * Create the input element.
	 * 
	 * @param property
	 *            The property.
	 * @param context
	 *            The render context.
	 * @return The input element.
	 */
	protected abstract UIInput createInput(PropertyDescriptor<P, E> property, RenderContext context);

	/**
	 * Creates the input name.
	 * 
	 * @param context
	 *            The context. May not be null.
	 * @return The input name.
	 */
	protected final String createInputName(final RenderContext context) {
		Validate.notNull(context, AbstractRenderStrategy.CONTEXT_NULL);
		return VariableUtil.compileName("input_", context.getPrefixDeque());
	}

	/**
	 * Creates the label name.
	 * 
	 * @param context
	 *            The context. May not be null.
	 * @return The label name.
	 */
	protected final String createLabelName(final RenderContext context) {
		Validate.notNull(context, AbstractRenderStrategy.CONTEXT_NULL);
		return VariableUtil.compileName("label_", context.getPrefixDeque());
	}

	/**
	 * Sets a new value for the input field.
	 * 
	 * @param input
	 *            The new value for the input field.
	 */
	protected final void setInput(final UIInput input) {
		this.input = input;
	}
}
