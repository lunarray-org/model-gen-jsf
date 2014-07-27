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
package org.lunarray.model.generation.jsf.render;

import java.util.Deque;
import java.util.LinkedList;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.generation.util.Context;

/**
 * The jsf context.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class RenderContext
		implements Context {

	/** The faces context. */
	private transient FacesContext facesContextValue;
	/** The value expression . */
	private transient ValueExpression modelExpressionValue;
	/** The model. */
	private transient Model<Object> modelValue;
	/** A prefix deque. */
	private final transient Deque<PropertyDescriptor<?, ?>> prefixDequeValue = new LinkedList<PropertyDescriptor<?, ?>>();
	/** The resolver. */
	private transient StyleClassResolver styleClassResolverValue;
	/** The variable. */
	private transient String variable;

	/**
	 * Default constructor.
	 */
	public RenderContext() {
		// Default constructor.
	}

	/**
	 * Sets a new value for the facesContext field.
	 * 
	 * @param facesContext
	 *            The new value for the facesContext field. May not be null.
	 * @return The context.
	 */
	public RenderContext facesContext(final FacesContext facesContext) {
		Validate.notNull(facesContext, "Faces context may not be null.");
		this.facesContextValue = facesContext;
		return this;
	}

	/**
	 * Gets the value for the facesContext field.
	 * 
	 * @return The value for the facesContext field.
	 */
	public FacesContext getFacesContext() {
		return this.facesContextValue;
	}

	/**
	 * Gets the value for the model field.
	 * 
	 * @return The value for the model field.
	 */
	public Model<Object> getModel() {
		return this.modelValue;
	}

	/**
	 * Gets the value for the modelExpression field.
	 * 
	 * @return The value for the modelExpression field.
	 */
	public ValueExpression getModelExpression() {
		return this.modelExpressionValue;
	}

	/**
	 * Gets the value for the prefixDeque field.
	 * 
	 * @return The value for the prefixDeque field.
	 */
	public Deque<PropertyDescriptor<?, ?>> getPrefixDeque() {
		return this.prefixDequeValue;
	}

	/**
	 * Gets the value for the variable field.
	 * 
	 * @return The value for the variable field.
	 */
	public String getVariable() {
		return this.variable;
	}

	/**
	 * Sets a new value for the model field.
	 * 
	 * @param model
	 *            The new value for the model field.
	 * @return The context.
	 */
	public RenderContext model(final Model<Object> model) {
		this.modelValue = model;
		return this;
	}

	/**
	 * Sets a new value for the modelExpression field.
	 * 
	 * @param modelExpression
	 *            The new value for the modelExpression field.
	 * @return The context.
	 */
	public RenderContext modelExpression(final ValueExpression modelExpression) {
		this.modelExpressionValue = modelExpression;
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public PropertyDescriptor<?, ?> popPrefix() {
		return this.prefixDequeValue.removeLast();
	}

	/** {@inheritDoc} */
	@Override
	public void pushPrefix(final PropertyDescriptor<?, ?> prefix) {
		this.prefixDequeValue.addLast(prefix);
	}

	/**
	 * Resolve a style class.
	 * 
	 * @param classes
	 *            The style classes.
	 * @return The resolved style.
	 */
	public String resolveStyleClass(final StyleClasses classes) {
		String result = classes.getDefaultValue();
		if (!CheckUtil.isNull(this.styleClassResolverValue)) {
			result = this.styleClassResolverValue.resolve(classes);
		}
		return result;
	}

	/**
	 * Sets a new value for the variable field.
	 * 
	 * @param variable
	 *            The new value for the variable field.
	 */
	public void setVariable(final String variable) {
		this.variable = variable;
	}

	/**
	 * Sets a new value for the styleClassResolver field.
	 * 
	 * @param styleClassResolver
	 *            The new value for the styleClassResolver field.
	 * @return The context.
	 */
	public RenderContext styleClassResolver(final StyleClassResolver styleClassResolver) {
		this.styleClassResolverValue = styleClassResolver;
		return this;
	}
}
