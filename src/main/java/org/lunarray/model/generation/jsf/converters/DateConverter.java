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
package org.lunarray.model.generation.jsf.converters;

import java.text.ParseException;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.util.DateFormatUtil;
import org.lunarray.model.descriptor.util.DateTimeType;
import org.lunarray.model.generation.jsf.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A JSF converter for {@link Date}.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class DateConverter
		implements Converter {

	/** Validation message. */
	private static final String FACES_CONTEXT_NULL = "Faces context may not be null.";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DateConverter.class);
	/** The date time type. */
	private transient DateTimeType type;

	/**
	 * Default constructor.
	 */
	public DateConverter() {
		this.type = DateTimeType.DATE;
	}

	/** {@inheritDoc} */
	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		DateConverter.LOGGER.debug("Getting date as object {}", value);
		Validate.notNull(context, DateConverter.FACES_CONTEXT_NULL);
		try {
			return DateFormatUtil.parse(this.type, value, context.getViewRoot().getLocale());
		} catch (final ParseException e) {
			throw new ConverterException(MessageUtil.formatMessage(context, "org.lunarray.model.converters.date.invalid.format", value), e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		DateConverter.LOGGER.debug("Getting date as string {}", value);
		Validate.notNull(context, DateConverter.FACES_CONTEXT_NULL);
		if (value instanceof Date) {
			return DateFormatUtil.format(this.type, (Date) value, context.getViewRoot().getLocale());
		} else {
			throw new ConverterException(MessageUtil.formatMessage(context, "org.lunarray.model.converters.date.invalid.type", value));
		}
	}

	/**
	 * Sets a new value for the type field.
	 * 
	 * @param type
	 *            The new value for the type field.
	 */
	public void setType(final DateTimeType type) {
		this.type = type;
	}
}
