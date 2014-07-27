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
package org.lunarray.model.generation.jsf.components.shared;

import java.io.Serializable;

import javax.el.ValueExpression;

/**
 * Describes a state with a target and a value.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class TargetValueState
		implements Serializable {
	/** Serial id. */
	private static final long serialVersionUID = -4046216694377390294L;
	/** The target expression. */
	private ValueExpression target;
	/** The value expression. */
	private ValueExpression value;

	/**
	 * Default constructor.
	 */
	public TargetValueState() {
		// Default constructor.
	}

	/**
	 * Gets the value for the target field.
	 * 
	 * @return The value for the target field.
	 */
	public ValueExpression getTarget() {
		return this.target;
	}

	/**
	 * Gets the value for the value field.
	 * 
	 * @return The value for the value field.
	 */
	public ValueExpression getValue() {
		return this.value;
	}

	/**
	 * Sets a new value for the target field.
	 * 
	 * @param target
	 *            The new value for the target field.
	 */
	public void setTarget(final ValueExpression target) {
		this.target = target;
	}

	/**
	 * Sets a new value for the value field.
	 * 
	 * @param value
	 *            The new value for the value field.
	 */
	public void setValue(final ValueExpression value) {
		this.value = value;
	}
}
