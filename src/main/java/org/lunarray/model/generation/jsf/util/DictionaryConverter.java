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

import java.io.Serializable;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.dictionary.Dictionary;
import org.lunarray.model.descriptor.dictionary.exceptions.DictionaryException;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.lunarray.model.descriptor.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A default converter.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class DictionaryConverter
		extends AbstractConverter {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryConverter.class);

	/**
	 * Default constructor.
	 */
	public DictionaryConverter() {
		super();
	}

	/**
	 * Constructs the converter.
	 * 
	 * @param targetDescriptorType
	 *            The descriptor type. May not be null.
	 * @param modelExpression
	 *            The model expression. May not be null.
	 */
	public DictionaryConverter(final Class<? extends Object> targetDescriptorType, final ValueExpression modelExpression) {
		super(targetDescriptorType, modelExpression);
	}

	/** {@inheritDoc} */
	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		final KeyedEntityDescriptor<Object, Serializable> descriptor = this.getDescriptor(context);
		DictionaryConverter.LOGGER.debug("Getting object value of {}, for descriptor {}.", value, descriptor);
		final Class<?> targetIdClazz = descriptor.getKeyProperty().getPropertyType();
		Converter converter = null;
		if (!String.class.equals(targetIdClazz)) {
			converter = context.getApplication().createConverter(targetIdClazz);
		}
		Object result = null;
		if (!StringUtil.isEmptyString(value)) {
			final Serializable identifier;
			if (CheckUtil.isNull(converter)) {
				identifier = String.class.cast(value);
			} else {
				identifier = (Serializable) converter.getAsObject(context, component, value);
			}
			try {
				final Dictionary dictionary = this.getModel(context).getExtension(Dictionary.class);
				result = dictionary.lookup(descriptor, identifier);
			} catch (final DictionaryException e) {
				throw new ConverterException(e);
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		final KeyedEntityDescriptor<Object, ?> descriptor = this.getDescriptor(context);
		DictionaryConverter.LOGGER.debug("Getting string value of {}, for descriptor {}.", value, descriptor);
		String result = null;
		if (CheckUtil.isNull(value)) {
			result = "";
		} else {
			try {
				if (CheckUtil.isNull(descriptor.getKeyProperty().getValue(value))) {
					result = "";
				} else {
					result = descriptor.getKeyProperty().getValue(value).toString();
				}
			} catch (final ValueAccessException e) {
				throw new ConverterException(e);
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
	public KeyedEntityDescriptor<Object, Serializable> getDescriptor(final FacesContext context) {
		return this.getModel(context).getEntity(super.getTargetDescriptorType()).adapt(KeyedEntityDescriptor.class);
	}
}
