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

import java.util.Collection;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.Validate;
import org.lunarray.model.generation.jsf.components.shared.TargetValueState;

/**
 * Abstracts a collection state.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class CollectionState
		extends AbstractState {

	/** Validation message. */
	private static final String FACES_CONTEXT_NULL = "Faces context may not be null.";

	/**
	 * Default constructor.
	 */
	public CollectionState() {
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
	public CollectionState(final ValueExpression target, final ValueExpression value) {
		super(target, value);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSelection(final FacesContext context) {
		Validate.notNull(context, CollectionState.FACES_CONTEXT_NULL);
		final ELContext elContext = context.getELContext();
		final TargetValueState state = this.getState();
		final Object target = state.getTarget().getValue(elContext);
		final Object value = state.getValue().getValue(elContext);
		boolean result;
		if (target instanceof Collection) {
			@SuppressWarnings("unchecked")
			// Information on type is not available.
			final Collection<Object> collection = (Collection<Object>) target;
			result = collection.contains(value);
		} else {
			result = false;
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void setSelection(final FacesContext context, final boolean selection) {
		Validate.notNull(context, CollectionState.FACES_CONTEXT_NULL);
		final ELContext elContext = context.getELContext();
		final TargetValueState state = this.getState();
		final Object target = state.getTarget().getValue(elContext);
		final Object value = state.getValue().getValue(elContext);
		if (target instanceof Collection) {
			@SuppressWarnings("unchecked")
			// Information on type is not available.
			final Collection<Object> collection = (Collection<Object>) target;
			if (selection && !collection.contains(value)) {
				collection.add(value);
			} else if (!selection) {
				collection.remove(value);
			}
		}
	}
}
