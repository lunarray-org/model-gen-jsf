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
package org.lunarray.model.generation.jsf.render.factories.table.jsf.components;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlOutputText;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationPropertyDescriptor;
import org.lunarray.model.generation.jsf.render.JsfStyleClasses;
import org.lunarray.model.generation.jsf.render.RenderContext;
import org.lunarray.model.generation.jsf.render.factories.table.TablePropertyDescriptorRenderStrategy;
import org.lunarray.model.generation.jsf.util.VariableUtil;

/**
 * Default text output.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public abstract class AbstractOutput<P, E>
		implements TablePropertyDescriptorRenderStrategy<P, E> {

	/** The output. */
	private transient HtmlOutputText label;
	/** The model expression. */
	private final transient ValueExpression modelExpression;
	/** The output. */
	private transient UIOutput output;

	/**
	 * Constructs the text output.
	 * 
	 * @param modelExpression
	 *            The model expression. May not be null.
	 */
	public AbstractOutput(final ValueExpression modelExpression) {
		Validate.notNull(modelExpression, "Model expression may not be null.");
		this.modelExpression = modelExpression;
	}

	/** {@inheritDoc} */
	@Override
	public final void build(final PropertyDescriptor<P, E> property, final RenderContext jsfContext) {
		Validate.notNull(property, "Property may not be null.");
		Validate.notNull(jsfContext, "Context may not be null.");
		String labelValue = property.getName();
		if (property.adaptable(PresentationPropertyDescriptor.class)) {
			@SuppressWarnings("unchecked")
			final PresentationPropertyDescriptor<P, E> descriptor = property.adapt(PresentationPropertyDescriptor.class);
			labelValue = descriptor.getDescription(jsfContext.getFacesContext().getViewRoot().getLocale());
		}
		this.setLabel(jsfContext, labelValue);
		this.output = this.getOutput(property, jsfContext);
	}

	/** {@inheritDoc} */
	@Override
	public final UIOutput getLabel() {
		return this.label;
	}

	/** {@inheritDoc} */
	@Override
	public final UIComponent getOutput() {
		return this.output;
	}

	/**
	 * Sets the label.
	 * 
	 * @param jsfContext
	 *            The context.
	 * @param label
	 *            The label.
	 */
	private void setLabel(final RenderContext jsfContext, final String label) {
		this.label = new HtmlOutputText();
		this.label.setId(VariableUtil.compileName("label_", jsfContext.getPrefixDeque()));
		this.label.setValue(label);
		this.label.setStyleClass(jsfContext.resolveStyleClass(JsfStyleClasses.TABLE_OUTPUT_LABEL));
	}

	/**
	 * Gets the value for the modelExpression field.
	 * 
	 * @return The value for the modelExpression field.
	 */
	protected final ValueExpression getModelExpression() {
		return this.modelExpression;
	}

	/**
	 * Sets the output.
	 * 
	 * @param property
	 *            The property.
	 * @param jsfContext
	 *            The context.
	 * @return The output.
	 */
	protected abstract UIOutput getOutput(final PropertyDescriptor<?, ?> property, final RenderContext jsfContext);
}
