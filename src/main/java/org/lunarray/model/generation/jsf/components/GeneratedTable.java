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
package org.lunarray.model.generation.jsf.components;

import javax.faces.context.FacesContext;

import org.lunarray.model.generation.jsf.render.factories.table.TablePropertyDescriptorRenderStrategy;

/**
 * A generated table.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            The super type.
 * @param <E>
 *            The entity type.
 */
public interface GeneratedTable<S, E extends S> {

	/**
	 * Process the table.
	 * 
	 * @param context
	 *            The faces context. May not be null.
	 * @param strategy
	 *            The row strategy. May not be null.
	 */
	void processStrategy(FacesContext context, TablePropertyDescriptorRenderStrategy<?, ?> strategy);
}