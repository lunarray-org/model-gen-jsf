/* 
 * Model tools.
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
package org.lunarray.model.generation.jsf.render;

/**
 * A resolver for style classes.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public interface StyleClassResolver {
	/**
	 * Resolve a style class.
	 * 
	 * @param styleClasses
	 *            Style classes.
	 * @return The resolved styleclass.
	 */
	String resolve(StyleClasses styleClasses);
}
