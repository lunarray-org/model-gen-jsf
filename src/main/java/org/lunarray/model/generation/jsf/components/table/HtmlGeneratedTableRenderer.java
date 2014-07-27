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
package org.lunarray.model.generation.jsf.components.table;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.commons.lang.Validate;
import org.lunarray.model.generation.jsf.util.Html;

/**
 * Renderer.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class HtmlGeneratedTableRenderer
		extends Renderer {

	/**
	 * Default constructor.
	 */
	public HtmlGeneratedTableRenderer() {
		super();
	}

	/** {@inheritDoc} */
	@Override
	public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
		Validate.notNull(context, "Context may not be null.");
		Validate.notNull(component, "Component may not be null.");
		final ResponseWriter writer = context.getResponseWriter();
		writer.startElement(Html.Elements.SCRIPT, null);
		writer.writeAttribute(Html.Attributes.TYPE, Html.Types.SCRIPT_JS, null);
		writer.write(String.format("var %sform = {", component.getClientId(context).replace("_", "")));
		writer.write("checkSingleRadio: function() {elements = document.getElementsByTagName(\"input\");");
		writer.write("for (i = 0; i < elements.length; i++) {");
		writer.write(String.format("if (elements[i].id.match(\"%s_form\")) {", component.getClientId(context)));
		writer.write("elements[i].removeAttribute(\"checked\");}}}}\n");
		writer.endElement(Html.Elements.SCRIPT);
		super.encodeBegin(context, component);
	}

	/** {@inheritDoc} */
	@Override
	public boolean getRendersChildren() {
		return true;
	}
}
