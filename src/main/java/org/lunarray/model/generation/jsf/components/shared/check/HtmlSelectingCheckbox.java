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
package org.lunarray.model.generation.jsf.components.shared.check;

import java.io.Serializable;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.generation.jsf.components.shared.state.SelectionState;
import org.lunarray.model.generation.jsf.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A selecting checkbox for data tables.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class HtmlSelectingCheckbox
		extends javax.faces.component.UISelectBoolean
		implements SelectionState {

	/** A logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlSelectingCheckbox.class);

	/** The listener. */
	private transient SelectionState listener;
	/** The state. */
	private transient State state;

	/**
	 * Default constructor.
	 */
	public HtmlSelectingCheckbox() {
		super();
		this.state = new State();
		this.setRendererType("HtmlSelectingCheckboxRenderer");
	}

	@Override
	public String getFamily() {
		return Constants.FAMILY;
	}

	/**
	 * Gets the value for the onChange field.
	 * 
	 * @return The value for the onChange field.
	 */
	public String getOnChange() {
		return this.state.getOnChange();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isSelection(final FacesContext context) {
		boolean result = false;
		if (!CheckUtil.isNull(this.listener)) {
			result = this.listener.isSelection(context);
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void restoreState(final FacesContext facesContext, final Object savedState) {
		Validate.isTrue(savedState instanceof State, "State is of invalid type.");
		this.state = (State) savedState;
		try {
			this.listener = (SelectionState) this.state.getListenerType().newInstance();
			if (this.listener instanceof StateHolder) {
				((StateHolder) this.listener).restoreState(facesContext, this.state.getListenerState());
			}
		} catch (final InstantiationException e) {
			HtmlSelectingCheckbox.LOGGER.warn("Could not create listener instance.", e);
		} catch (final IllegalAccessException e) {
			HtmlSelectingCheckbox.LOGGER.warn("Could not access listener constructor.", e);
		}
		super.restoreState(facesContext, this.state.getSuperState());
	}

	/** {@inheritDoc} */
	@Override
	public Object saveState(final FacesContext facesContext) {
		this.state.setSuperState(super.saveState(facesContext));
		this.state.setListenerType(this.listener.getClass());
		if (this.listener instanceof StateHolder) {
			this.state.setListenerState(((StateHolder) this.listener).saveState(facesContext));
		}
		return this.state;
	}

	/**
	 * Sets a new value for the listener field.
	 * 
	 * @param listener
	 *            The new value for the listener field.
	 */
	public void setListener(final SelectionState listener) {
		this.listener = listener;
	}

	/**
	 * Sets a new value for the onChange field.
	 * 
	 * @param onChange
	 *            The new value for the onChange field.
	 */
	public void setOnChange(final String onChange) {
		this.state.setOnChange(onChange);
	}

	/** {@inheritDoc} */
	@Override
	public void setSelection(final FacesContext context, final boolean value) {
		if (!CheckUtil.isNull(this.listener)) {
			this.listener.setSelection(context, value);
		}
	}

	/**
	 * The serializable state.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	private static class State
			implements Serializable {

		/** Serial id. */
		private static final long serialVersionUID = 3348574308442325645L;

		/** The listener state. */
		private Object listenerState;
		/** The listener type. */
		private Class<?> listenerType;
		/** The onchange value. */
		private String onChange;
		/** The state of the super. */
		private Object superState;

		/**
		 * Default constructor.
		 */
		public State() {
			// Default constructor.
		}

		/**
		 * Gets the value for the listenerState field.
		 * 
		 * @return The value for the listenerState field.
		 */
		public Object getListenerState() {
			return this.listenerState;
		}

		/**
		 * Gets the value for the listenerType field.
		 * 
		 * @return The value for the listenerType field.
		 */
		public Class<?> getListenerType() {
			return this.listenerType;
		}

		/**
		 * Gets the value for the onChange field.
		 * 
		 * @return The value for the onChange field.
		 */
		public String getOnChange() {
			return this.onChange;
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
		 * Sets a new value for the listenerState field.
		 * 
		 * @param listenerState
		 *            The new value for the listenerState field.
		 */
		public void setListenerState(final Object listenerState) {
			this.listenerState = listenerState;
		}

		/**
		 * Sets a new value for the listenerType field.
		 * 
		 * @param listenerType
		 *            The new value for the listenerType field.
		 */
		public void setListenerType(final Class<?> listenerType) {
			this.listenerType = listenerType;
		}

		/**
		 * Sets a new value for the onChange field.
		 * 
		 * @param onChange
		 *            The new value for the onChange field.
		 */
		public void setOnChange(final String onChange) {
			this.onChange = onChange;
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
	}
}
