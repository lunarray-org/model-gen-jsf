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
package org.lunarray.model.generation.jsf.components.shared.action;

import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;

import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.generation.jsf.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Named actions.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class HtmlAction
		extends UICommand {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlAction.class);

	/**
	 * Default constructor.
	 */
	public HtmlAction() {
		super();
		// Default constructor.
	}

	/** {@inheritDoc} */
	@Override
	public String getFamily() {
		return Constants.FAMILY;
	}

	/**
	 * Gets the outcome expression.
	 * 
	 * @return The outcome expression.
	 * @param ctx
	 *            The faces context.
	 */
	public String getOutcome(final FacesContext ctx) {
		final ValueExpression expression = this.get(PropertyKeys.OUTCOME);
		Object outcome = null;
		if (CheckUtil.isNull(expression)) {
			final Map<String, Object> attrs = this.getAttributes();
			if (attrs.containsKey(PropertyKeys.OUTCOME.getName())) {
				outcome = attrs.get(PropertyKeys.OUTCOME.getName());
			}
		} else {
			outcome = this.get(PropertyKeys.OUTCOME).getValue(ctx.getELContext());
		}
		HtmlAction.LOGGER.warn("Outcome is: {}", outcome);
		String result;
		if ((outcome == null) && this.isInView()) {
			result = this.getFacesContext().getViewRoot().getViewId();
		} else if (outcome instanceof String) {
			result = outcome.toString();
		} else {
			result = "";
		}
		HtmlAction.LOGGER.warn("Result is: {}", outcome);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public String getRendererType() {
		return "HtmlActionRenderer";
	}

	/**
	 * Gets the render type.
	 * 
	 * @return The render type.
	 */
	public ValueExpression getRenderType() {
		return this.get(PropertyKeys.ACTION_TYPE);
	}

	/**
	 * Gets the target expression.
	 * 
	 * @return The target expression.
	 */
	public ValueExpression getTargetExpression() {
		return this.get(PropertyKeys.TARGET);
	}

	/**
	 * Gets a value expression.
	 * 
	 * @param key
	 *            The expression key.
	 * @return The expression itself.
	 */
	private ValueExpression get(final PropertyKeys key) {
		return this.getValueExpression(key.getName());
	}

	/**
	 * Enum for properties.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	private enum PropertyKeys {
		/**
		 * The target property, may be link, button, commandLink and
		 * commandButton.
		 */
		ACTION_TYPE("actionType"),
		/** The outcome property. */
		OUTCOME("outcome"),
		/** The target property. */
		TARGET("target");
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
		 * Gets the property name.
		 * 
		 * @return The property name.
		 */
		public String getName() {
			return this.name;
		}
	}
}
