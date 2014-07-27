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

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.generation.jsf.components.shared.TargetValueState;

/**
 * Describes a target value selection state.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class ValueState
		extends AbstractState {

	/** Validation message. */
	private static final String FACES_CONTEXT_NULL = "Faces context may not be null.";

	/**
	 * Default constructor.
	 */
	public ValueState() {
		super();
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
	public ValueState(final ValueExpression target, final ValueExpression value) {
		super(target, value);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSelection(final FacesContext context) {
		Validate.notNull(context, ValueState.FACES_CONTEXT_NULL);
		final ELContext elContext = context.getELContext();
		final TargetValueState state = this.getState();
		final Object target = state.getTarget().getValue(elContext);
		final Object value = state.getValue().getValue(elContext);
		boolean result;
		if (CheckUtil.isNull(value)) {
			result = CheckUtil.isNull(target);
		} else {
			result = value.equals(target);
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void setSelection(final FacesContext context, final boolean value) {
		Validate.notNull(context, ValueState.FACES_CONTEXT_NULL);
		final TargetValueState state = this.getState();
		final ELContext elContext = context.getELContext();
		if (value) {
			state.getTarget().setValue(elContext, state.getValue().getValue(elContext));
		} else if (this.isSelection(context)) {
			state.getTarget().setValue(elContext, null);
		}
	}
}
