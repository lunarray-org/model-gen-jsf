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
package org.lunarray.model.generation.jsf.render.factories.datatable;

import javax.faces.component.html.HtmlColumn;
import javax.faces.component.html.HtmlDataTable;

import org.lunarray.model.generation.jsf.render.RenderContext;

/**
 * The data table builder.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public interface DataTableBuilder {

	/**
	 * Add a column.
	 * 
	 * @param htmlColumn
	 *            The column.
	 */
	void addColumn(HtmlColumn htmlColumn);

	/**
	 * Build the table.
	 * 
	 * @param context
	 *            The render context.
	 * @return The table.
	 */
	HtmlDataTable build(RenderContext context);
}
