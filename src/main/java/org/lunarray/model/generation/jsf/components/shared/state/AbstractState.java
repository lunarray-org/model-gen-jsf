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
package org.lunarray.model.generation.jsf.components.shared.state;

import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.Validate;
import org.lunarray.model.generation.jsf.components.shared.TargetValueState;

/**
 * Abstract selection state.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public abstract class AbstractState
		implements StateHolder, SelectionState {

	/** The state. */
	private transient TargetValueState state;
	/** The transient indicator. */
	private transient boolean transience;

	/**
	 * Default constructor.
	 */
	public AbstractState() {
		// Default constructor.
	}

	/**
	 * Default constructor.
	 * 
	 * @param target
	 *            The target expression. May not be null.
	 * @param value
	 *            The value expression. May not be null.
	 */
	public AbstractState(final ValueExpression target, final ValueExpression value) {
		Validate.notNull(target, "Target may not be null.");
		Validate.notNull(value, "Value may not be null.");
		this.state = new TargetValueState();
		this.state.setTarget(target);
		this.state.setValue(value);
	}

	/** {@inheritDoc} */
	@Override
	public final boolean isTransient() {
		return this.transience;
	}

	/** {@inheritDoc} */
	@Override
	public final void restoreState(final FacesContext context, final Object state) {
		if (state instanceof TargetValueState) {
			this.state = (TargetValueState) state;
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
	 * Gets the value for the state field.
	 * 
	 * @return The value for the state field.
	 */
	protected final TargetValueState getState() {
		return this.state;
	}
}
