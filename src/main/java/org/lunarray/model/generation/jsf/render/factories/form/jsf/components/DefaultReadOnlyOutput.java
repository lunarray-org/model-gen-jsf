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

import java.util.Deque;
import java.util.Locale;

import javax.el.ExpressionFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlMessage;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationPropertyDescriptor;
import org.lunarray.model.generation.jsf.components.shared.format.HtmlFormat;
import org.lunarray.model.generation.jsf.render.JsfStyleClasses;
import org.lunarray.model.generation.jsf.render.RenderContext;
import org.lunarray.model.generation.jsf.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.jsf.util.VariableUtil;

/**
 * Default read only output.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public final class DefaultReadOnlyOutput<P, E>
		implements FormPropertyRenderStrategy<P, E> {

	/** Separator. */
	private static final String LABEL_SEPARATOR = ":";

	/** An expression factory. */
	private final transient ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
	/** The input. */
	private transient HtmlOutputText input;
	/** The label. */
	private transient HtmlOutputLabel label;
	/** The message. */
	private transient HtmlMessage messages;

	/**
	 * Default constructor.
	 */
	protected DefaultReadOnlyOutput() {
		// Default constructor.
	}

	/** {@inheritDoc} */
	@Override
	public void build(final PropertyDescriptor<P, E> property, final RenderContext context) {
		Validate.notNull(property, "Descriptor may not be null.");
		Validate.notNull(context, "Render context may not be null.");
		final Deque<PropertyDescriptor<?, ?>> prefixes = context.getPrefixDeque();
		this.buildLabel(property, context);
		this.buildMessages(property, context);
		this.input = new HtmlOutputText();
		this.addValue(this.input, prefixes, property, context);
		this.input.setId(this.compileInputName(prefixes));
		this.input.setStyleClass(context.resolveStyleClass(JsfStyleClasses.FORM_INPUT_ROTEXT));
		final PresentationPropertyDescriptor<?, ?> presentationProperty = property.adapt(PresentationPropertyDescriptor.class);
		String format = null;
		if (!CheckUtil.isNull(presentationProperty)) {
			format = presentationProperty.getFormat();
		}
		if (!CheckUtil.isNull(format)) {
			final HtmlFormat htmlFormat = new HtmlFormat();
			htmlFormat.setFormat(format);
			this.input.getFacets().put("format", htmlFormat);
		}
	}

	/**
	 * Build the label.
	 * 
	 * @param property
	 *            The property.
	 * @param context
	 *            The render context.
	 * @param locale
	 *            The locale.
	 */
	public void buildLabel(final PresentationPropertyDescriptor<?, ?> property, final RenderContext context, final Locale locale) {
		this.label = new HtmlOutputLabel();
		this.label.setId(this.compileLabelName(context.getPrefixDeque()));
		this.label.setFor(this.compileInputName(context.getPrefixDeque()));
		final StringBuilder builder = new StringBuilder();
		builder.append(property.getDescription(locale));
		builder.append(DefaultReadOnlyOutput.LABEL_SEPARATOR);
		this.label.setValue(builder.toString());
		this.label.setStyleClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_LABEL));
	}

	/**
	 * Build the label.
	 * 
	 * @param property
	 *            The property.
	 * @param context
	 *            The render context.
	 */
	public void buildLabel(final PropertyDescriptor<?, ?> property, final RenderContext context) {
		property.getName();
		this.label = new HtmlOutputLabel();
		this.label.setId(this.compileLabelName(context.getPrefixDeque()));
		this.label.setFor(this.compileInputName(context.getPrefixDeque()));
		String labelValue = property.getName();
		if (property.adaptable(PresentationPropertyDescriptor.class)) {
			@SuppressWarnings("unchecked")
			final PresentationPropertyDescriptor<P, E> descriptor = property.adapt(PresentationPropertyDescriptor.class);
			labelValue = descriptor.getDescription(context.getFacesContext().getViewRoot().getLocale());
		}
		final StringBuilder builder = new StringBuilder();
		builder.append(labelValue);
		builder.append(DefaultReadOnlyOutput.LABEL_SEPARATOR);
		this.label.setValue(builder.toString());
		this.label.setStyleClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_LABEL));
	}

	/**
	 * Build the messages.
	 * 
	 * @param property
	 *            The property.
	 * @param context
	 *            The render context.
	 */
	public void buildMessages(final PropertyDescriptor<?, ?> property, final RenderContext context) {
		this.messages = new HtmlMessage();
		this.messages.setId(VariableUtil.compileName("msg_", context.getPrefixDeque()));
		this.messages.setFor(this.compileInputName(context.getPrefixDeque()));
		this.messages.setErrorClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_MSG_ERROR));
		this.messages.setWarnClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_MSG_WARN));
		this.messages.setInfoClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_MSG_INFO));
		this.messages.setStyleClass(context.resolveStyleClass(JsfStyleClasses.FORM_OUTPUT_MSG));
	}

	/** {@inheritDoc} */
	@Override
	public UIComponent getInput() {
		return this.input;
	}

	/** {@inheritDoc} */
	@Override
	public UIOutput getLabel() {
		return this.label;
	}

	/** {@inheritDoc} */
	@Override
	public UIMessage getMessages() {
		return this.messages;
	}

	/**
	 * Make the input name.
	 * 
	 * @param prefixes
	 *            The variable prefixes.
	 * @return The name.
	 */
	private String compileInputName(final Deque<PropertyDescriptor<?, ?>> prefixes) {
		return VariableUtil.compileName("input_", prefixes);
	}

	/**
	 * Makes the label name.
	 * 
	 * @param prefixes
	 *            The variable prefixes.
	 * @return The name.
	 */
	private String compileLabelName(final Deque<PropertyDescriptor<?, ?>> prefixes) {
		return VariableUtil.compileName("label_", prefixes);
	}

	/**
	 * Sets a value.
	 * 
	 * @param input
	 *            The input.
	 * @param prefixes
	 *            The variable prefixes.
	 * @param property
	 *            The property.
	 * @param context
	 *            The faces context.
	 */
	protected void addValue(final UIOutput input, final Deque<PropertyDescriptor<?, ?>> prefixes, final PropertyDescriptor<?, ?> property,
			final RenderContext context) {
		input.setValueExpression(
				"value",
				this.expressionFactory.createValueExpression(context.getFacesContext().getELContext(),
						VariableUtil.compileVariable(context.getVariable(), prefixes), Object.class));
	}

	/**
	 * A factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class FactoryImpl
			implements Factory {

		/**
		 * Default constructor.
		 */
		public FactoryImpl() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public <P, E> FormPropertyRenderStrategy<P, E> createStrategy(final RenderContext context) {
			return new DefaultReadOnlyOutput<P, E>();
		}
	}
}
