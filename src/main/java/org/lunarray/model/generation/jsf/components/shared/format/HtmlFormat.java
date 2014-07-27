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
package org.lunarray.model.generation.jsf.components.shared.format;

import java.io.Serializable;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.Validate;
import org.lunarray.model.generation.jsf.util.Constants;

/**
 * Html format for configuring formatting for generated components.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class HtmlFormat
		extends UIOutput {

	/** The format. */
	private String format;

	/**
	 * Default constructor.
	 */
	public HtmlFormat() {
		super();
		// Default constructor.
	}

	/** {@inheritDoc} */
	@Override
	public String getFamily() {
		return Constants.FAMILY;
	}

	/**
	 * Gets the target expression.
	 * 
	 * @return The target expression.
	 */
	public String getFormat() {
		return this.format;
	}

	/** {@inheritDoc} */
	@Override
	public String getRendererType() {
		return "HtmlFormatRenderer";
	}

	/** {@inheritDoc} */
	@Override
	public void restoreState(final FacesContext facesContext, final Object state) {
		Validate.isTrue(state instanceof State, "State is of an invalid type.");
		final State restoredState = (State) state;
		this.format = restoredState.getFormat();
		super.restoreState(facesContext, restoredState.getSuperState());
	}

	/** {@inheritDoc} */
	@Override
	public Object saveState(final FacesContext facesContext) {
		final State state = new State();
		state.setFormat(this.format);
		state.setSuperState(super.saveState(facesContext));
		return state;
	}

	/**
	 * Sets a new value for the format field.
	 * 
	 * @param format
	 *            The new value for the format field.
	 */
	public void setFormat(final String format) {
		this.format = format;
	}

	/**
	 * Represents the persistent state.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	private static class State
			implements Serializable {
		/** The serial id. */
		private static final long serialVersionUID = -7017408047953744480L;
		/** The format. */
		private String format;
		/** The state of the super. */
		private Object superState;

		/**
		 * Default constructor.
		 */
		public State() {
			// Default constructor.
		}

		/**
		 * Gets the value for the format field.
		 * 
		 * @return The value for the format field.
		 */
		public final String getFormat() {
			return this.format;
		}

		/**
		 * Gets the value for the superState field.
		 * 
		 * @return The value for the superState field.
		 */
		public final Object getSuperState() {
			return this.superState;
		}

		/**
		 * Sets a new value for the format field.
		 * 
		 * @param format
		 *            The new value for the format field.
		 */
		public final void setFormat(final String format) {
			this.format = format;
		}

		/**
		 * Sets a new value for the superState field.
		 * 
		 * @param superState
		 *            The new value for the superState field.
		 */
		public final void setSuperState(final Object superState) {
			this.superState = superState;
		}
	}
}
