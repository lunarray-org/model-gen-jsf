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

import java.io.Serializable;

import org.lunarray.model.descriptor.util.DateTimeType;

/**
 * The date conversion state.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public class DateState
		implements Serializable {

	/** The serial id. */
	private static final long serialVersionUID = 6855755234780979652L;
	/** The format. */
	private String format;
	/** The type. */
	private DateTimeType type;

	/**
	 * Default constructor.
	 */
	public DateState() {
		// Default constructor.
	}

	/**
	 * Gets the value for the format field.
	 * 
	 * @return The value for the format field.
	 */
	public final String getFormat() {
		return this.format;
	}

	/**
	 * Gets the value for the type field.
	 * 
	 * @return The value for the type field.
	 */
	public final DateTimeType getType() {
		return this.type;
	}

	/**
	 * Sets a new value for the format field.
	 * 
	 * @param format
	 *            The new value for the format field.
	 */
	public final void setFormat(final String format) {
		this.format = format;
	}

	/**
	 * Sets a new value for the type field.
	 * 
	 * @param type
	 *            The new value for the type field.
	 */
	public final void setType(final DateTimeType type) {
		this.type = type;
	}
}
