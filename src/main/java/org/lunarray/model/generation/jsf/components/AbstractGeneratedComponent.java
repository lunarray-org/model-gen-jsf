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
package org.lunarray.model.generation.jsf.components;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.NamingContainer;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.generation.jsf.render.RenderContext;
import org.lunarray.model.generation.jsf.util.Constants;
import org.lunarray.model.generation.util.VariableResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract component for generation.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            The super type.
 * @param <E>
 *            The entity type.
 */
public abstract class AbstractGeneratedComponent<S, E extends S>
		extends HtmlPanelGroup
		implements NamingContainer {

	/** Validation message. */
	private static final String FACES_CONTEXT_NULL = "Faces context may not be null.";

	/** Validation message. */
	private static final String KEY_NULL = "Key may not be null.";

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGeneratedComponent.class);

	/** The state. */
	private transient State state;

	/**
	 * Default constructor.
	 */
	public AbstractGeneratedComponent() {
		super();
		this.state = new State();
	}

	/** {@inheritDoc} */
	@Override
	public final void encodeChildren(final FacesContext context) throws IOException {
		if (!this.state.isProcessed()) {
			this.rerender(context);
			this.state.setProcessed(true);
		}
		super.encodeChildren(context);
	}

	/** {@inheritDoc} */
	@Override
	public final String getFamily() {
		return Constants.FAMILY;
	}

	/** {@inheritDoc} */
	@Override
	public final void restoreState(final FacesContext context, final Object state) {
		if (state instanceof State) {
			this.state = (State) state;
			super.restoreState(context, this.state.getSuperState());
			this.processVariable(context);
		}
	}

	/** {@inheritDoc} */
	@Override
	public final Object saveState(final FacesContext context) {
		this.state.setSuperState(super.saveState(context));
		return this.state;
	}

	/**
	 * Evaluate a property key to a type.
	 * 
	 * @param <T>
	 *            The result type.
	 * @param key
	 *            The property key. May not be null.
	 * @return An instance associated with the key.
	 */
	@SuppressWarnings("unchecked")
	// Just a helper.
	protected final <T> T evaluate(final PropertyKeys key) {
		Validate.notNull(key, AbstractGeneratedComponent.KEY_NULL);
		return (T) this.getStateHelper().eval(key.getName());
	}

	/**
	 * Gets an instance of a given class.
	 * 
	 * @param <T>
	 *            The instance type.
	 * @param clazzName
	 *            The class name. May not be empty.
	 * @return The instance type.
	 */
	protected final <T> T get(final String clazzName) {
		Validate.notEmpty(clazzName, "Clazzname may not be empty.");
		T result = null;
		try {
			@SuppressWarnings("unchecked")
			final Class<T> clazz = (Class<T>) Class.forName(clazzName);
			result = clazz.newInstance();
		} catch (final ClassNotFoundException e) {
			AbstractGeneratedComponent.LOGGER.warn("Could not get class.", e);
		} catch (final InstantiationException e) {
			AbstractGeneratedComponent.LOGGER.warn("Could not get instance.", e);
		} catch (final IllegalAccessException e) {
			AbstractGeneratedComponent.LOGGER.warn("Could not access constructor.", e);
		}
		return result;
	}

	/**
	 * Gets an expression.
	 * 
	 * @param key
	 *            The property key. May not be null.
	 * @return The expression associated with the key.
	 */
	protected final ValueExpression getExpression(final PropertyKeys key) {
		Validate.notNull(key, AbstractGeneratedComponent.KEY_NULL);
		return this.getValueExpression(key.getName());
	}

	/**
	 * Gets the value for the state field.
	 * 
	 * @return The value for the state field.
	 */
	protected final State getState() {
		return this.state;
	}

	/**
	 * Gets a value.
	 * 
	 * @param <T>
	 *            The result type.
	 * @param key
	 *            The property key. May not be null.
	 * @param context
	 *            The faces context. May not be null.
	 * @return The instance associated with a key.
	 */
	@SuppressWarnings("unchecked")
	// Just a helper.
	protected final <T> T getValue(final PropertyKeys key, final FacesContext context) {
		Validate.notNull(key, AbstractGeneratedComponent.KEY_NULL);
		Validate.notNull(context, AbstractGeneratedComponent.FACES_CONTEXT_NULL);
		return (T) this.getExpression(key).getValue(context.getELContext());
	}

	/**
	 * Gets a value.
	 * 
	 * @param <T>
	 *            The result type.
	 * @param key
	 *            The property key. May not be null.
	 * @param context
	 *            The faces context. May not be null.
	 * @param defaultValue
	 *            The default value if the key is not set.
	 * @return The instance associated with a key.
	 */
	@SuppressWarnings("unchecked")
	// Just a helper.
	protected final <T> T getValueOrDefault(final PropertyKeys key, final FacesContext context, final T defaultValue) {
		Validate.notNull(key, AbstractGeneratedComponent.KEY_NULL);
		Validate.notNull(context, AbstractGeneratedComponent.FACES_CONTEXT_NULL);
		final ValueExpression expression = this.getExpression(key);
		final T result;
		if (CheckUtil.isNull(expression)) {
			result = defaultValue;
		} else {
			result = (T) expression.getValue(context.getELContext());
		}
		return result;
	}

	/**
	 * Process the variables.
	 * 
	 * @param context
	 *            The context. May not be null.
	 */
	protected final void processVariable(final FacesContext context) {
		Validate.notNull(context, AbstractGeneratedComponent.FACES_CONTEXT_NULL);
		// Get variable.
		if (CheckUtil.isNull(this.state.getVariable())) {
			this.state.setVariable((String) this.evaluate(PropertyKeys.VARIABLE));
		}
		if (CheckUtil.isNull(this.state.getVariable())) {
			this.state.setVariable((String) this.getAttributes().get(PropertyKeys.VARIABLE.getName()));
		}
		// Post variable
		context.getExternalContext().getRequestMap().put(this.state.getVariable(), this.getValue(PropertyKeys.VALUE, context));
	}

	/**
	 * Rerender the component.
	 * 
	 * @param context
	 *            The faces context.
	 */
	protected abstract void rerender(FacesContext context);

	/**
	 * Resolve a string value.
	 * 
	 * @param key
	 *            The property key.
	 * @param context
	 *            The faces context.
	 * @return A best effort resolution of the string value.
	 */
	protected final String resolveStringValue(final PropertyKeys key, final FacesContext context) {
		Validate.notNull(key, AbstractGeneratedComponent.KEY_NULL);
		Validate.notNull(context, AbstractGeneratedComponent.FACES_CONTEXT_NULL);
		String result = null;
		final String keyName = key.getName();
		final ValueExpression expression = this.getExpression(key);
		if (CheckUtil.isNull(expression)) {
			final Map<String, Object> attributes = this.getAttributes();
			if (attributes.containsKey(keyName)) {
				result = attributes.get(keyName).toString();
			}
		} else {
			if (expression.isLiteralText()) {
				result = expression.getExpressionString();
			} else {
				result = expression.getValue(context.getELContext()).toString();
			}
		}
		return result;
	}

	/**
	 * Resolve a value.
	 * 
	 * @param <T>
	 *            The value type.
	 * @param key
	 *            The property key.
	 * @param context
	 *            The faces context.
	 * @return The value.
	 */
	@SuppressWarnings("unchecked")
	// Just a helper.
	protected final <T> T resolveValue(final PropertyKeys key, final FacesContext context) {
		Validate.notNull(key, AbstractGeneratedComponent.KEY_NULL);
		Validate.notNull(context, AbstractGeneratedComponent.FACES_CONTEXT_NULL);
		T result = null;
		if (!CheckUtil.isNull(this.getExpression(key))) {
			result = (T) this.getExpression(key).getValue(context.getELContext());
		}
		return result;
	}

	/**
	 * A variable resolver.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	protected final class JsfVariableResolver
			implements VariableResolver<RenderContext, S, E> {

		/** Validation message. */
		private static final String CONTEXT_NULL = "Context may not be null.";

		/**
		 * Default constructor.
		 */
		public JsfVariableResolver() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public EntityDescriptor<E> getDescriptor(final RenderContext context) {
			Validate.notNull(context, JsfVariableResolver.CONTEXT_NULL);
			return AbstractGeneratedComponent.this.evaluate(PropertyKeys.CLAZZ);
		}

		/** {@inheritDoc} */
		@Override
		public Locale getLocale(final RenderContext context) {
			Validate.notNull(context, JsfVariableResolver.CONTEXT_NULL);
			return context.getFacesContext().getViewRoot().getLocale();
		}

		/** {@inheritDoc} */
		@Override
		public Model<S> getModel(final RenderContext context) {
			Validate.notNull(context, JsfVariableResolver.CONTEXT_NULL);
			return AbstractGeneratedComponent.this.getValue(PropertyKeys.MODEL, context.getFacesContext());
		}

		/** {@inheritDoc} */
		@Override
		public Class<?> getQualifier(final RenderContext context) {
			Validate.notNull(context, JsfVariableResolver.CONTEXT_NULL);
			final ValueExpression expression = AbstractGeneratedComponent.this.getValueExpression(PropertyKeys.QUALIFIER.getName());
			Class<?> qualifier;
			if (expression.isLiteralText()) {
				try {
					qualifier = Class.forName(expression.getExpressionString());
				} catch (final ClassNotFoundException e) {
					AbstractGeneratedComponent.LOGGER.warn("Could not find class.", e);
					qualifier = null;
				}
			} else {
				qualifier = (Class<?>) expression.getValue(context.getFacesContext().getELContext());
			}
			return qualifier;
		}

		/** {@inheritDoc} */
		@Override
		public boolean hasQualifier(final RenderContext context) {
			Validate.notNull(context, JsfVariableResolver.CONTEXT_NULL);
			return AbstractGeneratedComponent.this.getValueExpression(PropertyKeys.QUALIFIER.getName()) != null;
		}
	}

	/**
	 * Property keys.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	protected enum PropertyKeys {
		/** The type of the entity. */
		CLAZZ("clazz"),
		/** Choose the layout, either div or table. */
		LAYOUT("layout"),
		/** The message location. */
		MESSAGE("messageLocation"),
		/** The model. */
		MODEL("model"),
		/** Whether or not to use the outer form. */
		OUTER_FORM("outerForm"),
		/** The qualifier. */
		QUALIFIER("qualifier"),
		/** The row count. */
		ROWS("rows"),
		/** Whether or not to show the form/table label. */
		SHOW_LABEL("showLabel"),
		/** The styleclass resolver. */
		STYLECLASS_RESOLVER("styleclassResolver"),
		/** The value. */
		VALUE("value"),
		/** A variable name to assign the value to. */
		VARIABLE("variable");
		/** The property name. */
		private final String name;

		/**
		 * Default constructor.
		 * 
		 * @param name
		 *            The property name.
		 */
		private PropertyKeys(final String name) {
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

	/**
	 * The state.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	protected static final class State
			implements Serializable {
		/** Serial id. */
		private static final long serialVersionUID = 8761780551901262473L;
		/** Indicates whether initial processing has been done. */
		private boolean processed;
		/** The super state. */
		private Object superState;
		/** The entity value. */
		private Object value;
		/** The variable name. */
		private String variable;

		/**
		 * Default constructor.
		 */
		public State() {
			this.processed = false;
		}

		/**
		 * Gets the value for the superState field.
		 * 
		 * @return The value for the superState field.
		 */
		public Object getSuperState() {
			return this.superState;
		}

		/**
		 * Gets the value for the value field.
		 * 
		 * @return The value for the value field.
		 */
		public Object getValue() {
			return this.value;
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
		 * Gets the value for the processed field.
		 * 
		 * @return The value for the processed field.
		 */
		public boolean isProcessed() {
			return this.processed;
		}

		/**
		 * Sets a new value for the processed field.
		 * 
		 * @param processed
		 *            The new value for the processed field.
		 */
		public void setProcessed(final boolean processed) {
			this.processed = processed;
		}

		/**
		 * Sets a new value for the superState field.
		 * 
		 * @param superState
		 *            The new value for the superState field.
		 */
		public void setSuperState(final Object superState) {
			this.superState = superState;
		}

		/**
		 * Sets a new value for the value field.
		 * 
		 * @param value
		 *            The new value for the value field.
		 */
		public void setValue(final Object value) {
			this.value = value;
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
	}

}
