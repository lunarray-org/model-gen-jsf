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
package org.lunarray.model.generation.jsf.render.factories.table;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.generation.jsf.render.RenderContext;

/**
 * A property descriptor table.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public interface TablePropertyDescriptorRenderStrategy<P, E> {

	/**
	 * Build for a normal descriptor.
	 * 
	 * @param property
	 *            The property. May not be null.
	 * @param context
	 *            The context. May not be null.
	 */
	void build(final PropertyDescriptor<P, E> property, final RenderContext context);

	/**
	 * The property label.
	 * 
	 * @return The label.
	 */
	UIOutput getLabel();

	/**
	 * The property output.
	 * 
	 * @return The output.
	 */
	UIComponent getOutput();

	/**
	 * The factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	interface Factory {
		/**
		 * Creates a strategy.
		 * 
		 * @param context
		 *            The context. May not be null.
		 * @return The strategy.
		 * @param <P>
		 *            The property.
		 * @param <E>
		 *            The entity.
		 */
		<P, E> TablePropertyDescriptorRenderStrategy<P, E> createStrategy(RenderContext context);
	}
}
