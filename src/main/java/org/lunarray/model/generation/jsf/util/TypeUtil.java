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

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.Validate;

/**
 * A type utility.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public enum TypeUtil {

	/** The instance. */
	INSTANCE;

	/** Type may not be null. */
	private static final String TYPE_NULL = "Type may not be null.";

	/**
	 * Tests if the given type is a date type.
	 * 
	 * @param type
	 *            The type. May not be null.
	 * @return True if and only if it's a date type.
	 */
	public static boolean isDateType(final Class<?> type) {
		Validate.notNull(type, TypeUtil.TYPE_NULL);
		boolean result = false;
		result = result | Date.class.isAssignableFrom(type);
		result = result | java.sql.Date.class.isAssignableFrom(type);
		return result | Calendar.class.isAssignableFrom(type);
	}

	/**
	 * Tests if the given type is a number type.
	 * 
	 * @param type
	 *            The type. May not be null.
	 * @return True if and only if it's a number type.
	 */
	public static boolean isNumberType(final Class<?> type) {
		Validate.notNull(type, TypeUtil.TYPE_NULL);
		return Number.class.isAssignableFrom(type);
	}
}
