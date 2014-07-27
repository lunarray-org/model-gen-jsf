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
package org.lunarray.model.generation.jsf.listeners;

import java.io.Serializable;
import java.util.Collection;
import java.util.Deque;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.validator.EntityValidator;
import org.lunarray.model.descriptor.validator.PropertyViolation;
import org.lunarray.model.generation.jsf.util.VariableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validation listener.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class DefaultValidationListener
		implements ActionListener, StateHolder {

	/** Validation message. */
	private static final String FACES_CONTEXT_NOT_INJECTED = "Faces context was not injected.";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultValidationListener.class);
	/** The faces context. */
	private transient FacesContext facesContext;
	/** The state. */
	private transient State state;

	/**
	 * Default constructor.
	 */
	public DefaultValidationListener() {
		// Default constructor.
	}

	/**
	 * Default constructor.
	 * 
	 * @param idBase
	 *            The id base.
	 * @param entityKey
	 *            The entity key. May not be null.
	 * @param modelExpression
	 *            The model expression. May not be null.
	 * @param valueExpression
	 *            The value expression. May not be null.
	 * @param prefixes
	 *            The property prefixes. May not be null.
	 */
	public DefaultValidationListener(final String idBase, final String entityKey, final ValueExpression modelExpression,
			final ValueExpression valueExpression, final Deque<PropertyDescriptor<?, ?>> prefixes) {
		super();
		Validate.notNull(entityKey, "Entity key may not be null.");
		Validate.notNull(modelExpression, "Model expression may not be null.");
		Validate.notNull(valueExpression, "Value expression may not be null.");
		Validate.notNull(prefixes, "Prefixes may not be null.");
		this.state = new State();
		this.state.setPrefixes(prefixes);
		this.state.setIdBase(idBase);
		this.state.setEntityKey(entityKey);
		this.state.setModelExpression(modelExpression);
		this.state.setValueExpression(valueExpression);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isTransient() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void processAction(final ActionEvent actionEvent) {
		Validate.notNull(this.facesContext, DefaultValidationListener.FACES_CONTEXT_NOT_INJECTED);
		final Model<?> model = (Model<?>) this.state.getModelExpression().getValue(this.facesContext.getELContext());
		if (!CheckUtil.isNull(model.getExtension(EntityValidator.class))) {
			final Collection<PropertyViolation<Object, ?>> violations = this.validate();
			for (final PropertyViolation<Object, ?> violation : violations) {
				this.processViolation(violation);
			}
			if (!violations.isEmpty()) {
				throw new AbortProcessingException(String.format("Validation failed, %d violations.", violations.size()));
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void restoreState(final FacesContext context, final Object state) {
		Validate.notNull(context, DefaultValidationListener.FACES_CONTEXT_NOT_INJECTED);
		if (state instanceof State) {
			this.state = (State) state;
		}
		this.facesContext = context;
	}

	/** {@inheritDoc} */
	@Override
	public Object saveState(final FacesContext context) {
		this.facesContext = context;
		return this.state;
	}

	/**
	 * Sets a new value for the facesContext field.
	 * 
	 * @param facesContext
	 *            The new value for the facesContext field.
	 */
	public void setFacesContext(final FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	/** {@inheritDoc} */
	@Override
	public void setTransient(final boolean newTransientValue) {
		// No transience.
	}

	/**
	 * Process the violation.
	 * 
	 * @param violation
	 *            The violation.
	 */
	private void processViolation(final PropertyViolation<Object, ?> violation) {
		final String violationMsg = violation.getMessage();
		final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, violationMsg, violationMsg);
		this.facesContext.addMessage(
				this.state.getIdBase()
						+ VariableUtil.compileVariableName(":input_", this.state.getPrefixes(), violation.getProperty().getName()), msg);
	}

	/**
	 * Validate the value.
	 * 
	 * @param <T>
	 *            The value type.
	 * @return The constraint violations.
	 */
	private <T> Collection<PropertyViolation<T, ?>> validate() {
		final ELContext elc = this.facesContext.getELContext();
		final Model<?> model = (Model<?>) this.state.getModelExpression().getValue(elc);
		final EntityValidator validator = model.getExtension(EntityValidator.class);
		@SuppressWarnings("unchecked")
		final EntityDescriptor<T> entity = (EntityDescriptor<T>) model.getEntity(this.state.getEntityKey());
		@SuppressWarnings("unchecked")
		final T value = (T) this.state.getValueExpression().getValue(elc);
		DefaultValidationListener.LOGGER.debug("Validating value {} with validator {}.", value, validator);
		return validator.validate(entity, value, this.facesContext.getViewRoot().getLocale());
	}

	/**
	 * The state.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	private static class State
			implements Serializable {
		/** Serial id. */
		private static final long serialVersionUID = 2214014589767008346L;
		/** The entity key. */
		private String entityKey;
		/** The base for the ids. */
		private String idBase;
		/** The model expression. */
		private ValueExpression modelExpression;
		/** The variable prefixes. */
		private Deque<PropertyDescriptor<?, ?>> prefixes;
		/** The value expression. */
		private ValueExpression valueExpression;

		/**
		 * Default constructor.
		 */
		public State() {
			// Default constructor.
		}

		/**
		 * Gets the value for the entityKey field.
		 * 
		 * @return The value for the entityKey field.
		 */
		public String getEntityKey() {
			return this.entityKey;
		}

		/**
		 * Gets the value for the idBase field.
		 * 
		 * @return The value for the idBase field.
		 */
		public String getIdBase() {
			return this.idBase;
		}

		/**
		 * Gets the value for the modelExpression field.
		 * 
		 * @return The value for the modelExpression field.
		 */
		public ValueExpression getModelExpression() {
			return this.modelExpression;
		}

		/**
		 * Gets the value for the prefixes field.
		 * 
		 * @return The value for the prefixes field.
		 */
		public Deque<PropertyDescriptor<?, ?>> getPrefixes() {
			return this.prefixes;
		}

		/**
		 * Gets the value for the valueExpression field.
		 * 
		 * @return The value for the valueExpression field.
		 */
		public ValueExpression getValueExpression() {
			return this.valueExpression;
		}

		/**
		 * Sets a new value for the entityKey field.
		 * 
		 * @param entityKey
		 *            The new value for the entityKey field.
		 */
		public void setEntityKey(final String entityKey) {
			this.entityKey = entityKey;
		}

		/**
		 * Sets a new value for the idBase field.
		 * 
		 * @param idBase
		 *            The new value for the idBase field.
		 */
		public void setIdBase(final String idBase) {
			this.idBase = idBase;
		}

		/**
		 * Sets a new value for the modelExpression field.
		 * 
		 * @param modelExpression
		 *            The new value for the modelExpression field.
		 */
		public void setModelExpression(final ValueExpression modelExpression) {
			this.modelExpression = modelExpression;
		}

		/**
		 * Sets a new value for the prefixes field.
		 * 
		 * @param prefixes
		 *            The new value for the prefixes field.
		 */
		public void setPrefixes(final Deque<PropertyDescriptor<?, ?>> prefixes) {
			this.prefixes = prefixes;
		}

		/**
		 * Sets a new value for the valueExpression field.
		 * 
		 * @param valueExpression
		 *            The new value for the valueExpression field.
		 */
		public void setValueExpression(final ValueExpression valueExpression) {
			this.valueExpression = valueExpression;
		}
	}
}
