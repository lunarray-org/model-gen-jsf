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
package org.lunarray.model.generation.jsf.util;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.presentation.PresentationEntityDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default name converter. Handles names for entities.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class EntityNameConverter
		extends AbstractConverter {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(EntityNameConverter.class);

	/**
	 * Default constructor.
	 */
	public EntityNameConverter() {
		super();
	}

	/**
	 * Default constructor.
	 * 
	 * @param targetDescriptorType
	 *            The descriptor type. May not be null.
	 * @param modelExpression
	 *            The model expression. May not be null.
	 */
	public EntityNameConverter(final Class<? extends Object> targetDescriptorType, final ValueExpression modelExpression) {
		super(targetDescriptorType, modelExpression);
	}

	/** {@inheritDoc} */
	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		// Not supported.
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		final PresentationEntityDescriptor<Object> descriptor = this.getDescriptor(context);
		EntityNameConverter.LOGGER.debug("Getting string value of object {} with descriptor {}.", value, descriptor);
		String result = null;
		if (CheckUtil.isNull(value)) {
			result = "";
		} else {
			if (CheckUtil.isNull(descriptor) || CheckUtil.isNull(descriptor.getNameProperty())) {
				result = value.toString();
			} else {
				try {
					result = descriptor.getNameProperty().getValue(value).toString();
				} catch (final ValueAccessException e) {
					throw new ConverterException(e);
				}
			}
		}
		return result;
	}

	/**
	 * Gets the descriptor.
	 * 
	 * @param context
	 *            The faces context.
	 * @return The descriptor.
	 */
	@SuppressWarnings("unchecked")
	/* We have set this, it should be fine. */
	public PresentationEntityDescriptor<Object> getDescriptor(final FacesContext context) {
		return this.getModel(context).getEntity(super.getTargetDescriptorType()).adapt(PresentationEntityDescriptor.class);
	}
}
