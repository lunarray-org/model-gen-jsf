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
package org.lunarray.model.generation.jsf.util;

import java.io.Serializable;

import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.model.Model;

/**
 * Abstract converter base.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public abstract class AbstractConverter
		implements Converter, StateHolder {

	/** The state. */
	private transient State state;
	/** The transience. */
	private transient boolean transience;

	/**
	 * Default constructor.
	 */
	public AbstractConverter() {
		this.transience = false;
	}

	/**
	 * Constructs the converter.
	 * 
	 * @param targetDescriptorType
	 *            The descriptor type. May not be null.
	 * @param modelExpression
	 *            The model expression. May not be null.
	 */
	public AbstractConverter(final Class<? extends Object> targetDescriptorType, final ValueExpression modelExpression) {
		Validate.notNull(targetDescriptorType, "Target descriptor type may not be null.");
		Validate.notNull(modelExpression, "Model expression type may not be null.");
		this.state = new State();
		this.state.setTargetDescriptorType(targetDescriptorType);
		this.state.setModelExpression(modelExpression);
	}

	/**
	 * Gets the model.
	 * 
	 * @param context
	 *            The faces context.
	 * @return The model.
	 */
	@SuppressWarnings("unchecked")
	/* It's the super type. */
	public final Model<Object> getModel(final FacesContext context) {
		return (Model<Object>) this.state.getModelExpression().getValue(context.getELContext());
	}

	/**
	 * Gets the target descriptor type.
	 * 
	 * @return The target descriptor type.
	 */
	public final Class<? extends Object> getTargetDescriptorType() {
		return this.state.getTargetDescriptorType();
	}

	/** {@inheritDoc} */
	@Override
	public final boolean isTransient() {
		return this.transience;
	}

	/** {@inheritDoc} */
	@Override
	public final void restoreState(final FacesContext context, final Object state) {
		if (state instanceof State) {
			this.state = (State) state;
		}
	}

	/** {@inheritDoc} */
	@Override
	public final Object saveState(final FacesContext context) {
		return this.state;
	}

	/** {@inheritDoc} */
	@Override
	public final void setTransient(final boolean newTransientValue) {
		this.transience = newTransientValue;
	}

	/**
	 * The converter state.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	private static class State
			implements Serializable {
		/** Serial id. */
		private static final long serialVersionUID = 1L;
		/** The value expression. */
		private ValueExpression modelExpression;
		/** The target descriptor type. */
		private Class<? extends Object> targetDescriptorType;

		/**
		 * Default constructor.
		 */
		public State() {
			// Default constructor.
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
		 * Gets the value for the targetDescriptorType field.
		 * 
		 * @return The value for the targetDescriptorType field.
		 */
		public Class<? extends Object> getTargetDescriptorType() {
			return this.targetDescriptorType;
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
		 * Sets a new value for the targetDescriptorType field.
		 * 
		 * @param targetDescriptorType
		 *            The new value for the targetDescriptorType field.
		 */
		public void setTargetDescriptorType(final Class<? extends Object> targetDescriptorType) {
			this.targetDescriptorType = targetDescriptorType;
		}
	}
}
