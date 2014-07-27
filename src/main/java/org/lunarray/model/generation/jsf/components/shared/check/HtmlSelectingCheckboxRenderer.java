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

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.commons.lang.Validate;
import org.lunarray.model.generation.jsf.components.shared.state.SelectionState;
import org.lunarray.model.generation.jsf.util.Html;

/**
 * Rendered for the checkbox.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class HtmlSelectingCheckboxRenderer
		extends Renderer {

	/** Validation message. */
	private static final String COMPONENT_NULL = "Component may not be null.";
	/** Validation message. */
	private static final String FACES_CONTEXT_NULL = "Faces context may not be null.";

	/**
	 * Default constructor.
	 */
	public HtmlSelectingCheckboxRenderer() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public void decode(final FacesContext context, final UIComponent component) {
		Validate.notNull(context, HtmlSelectingCheckboxRenderer.FACES_CONTEXT_NULL);
		Validate.notNull(component, HtmlSelectingCheckboxRenderer.COMPONENT_NULL);
		super.decode(context, component);
		final Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
		final String clientId = component.getClientId(context);
		final String value = requestMap.get(clientId);
		final boolean result = this.isChecked(value);
		if (component instanceof SelectionState) {
			final SelectionState state = (SelectionState) component;
			state.setSelection(context, result);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
		Validate.notNull(context, HtmlSelectingCheckboxRenderer.FACES_CONTEXT_NULL);
		Validate.notNull(component, HtmlSelectingCheckboxRenderer.COMPONENT_NULL);
		super.encodeEnd(context, component);
		final ResponseWriter writer = context.getResponseWriter();
		final String clientId = component.getClientId(context);
		writer.startElement(Html.Elements.INPUT, component);
		writer.writeAttribute(Html.Attributes.IDENTIFIER, clientId, null);
		writer.writeAttribute(Html.Attributes.TYPE, Html.Types.INPUT_CHECKBOX, null);
		writer.writeAttribute(Html.Attributes.NAME, clientId, null);
		if (component instanceof HtmlSelectingCheckbox) {
			final HtmlSelectingCheckbox check = (HtmlSelectingCheckbox) component;
			writer.writeAttribute(Html.Attributes.ONCHANGE, check.getOnChange(), null);
			if (check.isSelection(context)) {
				writer.writeAttribute(Html.Attributes.CHECKED, Html.Attributes.CHECKED, null);
			}
		}
		writer.endElement(Html.Elements.INPUT);
	}

	/**
	 * Tests whether a check box is checked.
	 * 
	 * @param value
	 *            The string value.
	 * @return The checked value.
	 */
	private boolean isChecked(final String value) {
		return "on".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "checked".equalsIgnoreCase(value);
	}
}
