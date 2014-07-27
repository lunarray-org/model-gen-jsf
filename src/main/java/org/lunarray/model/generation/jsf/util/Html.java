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

/**
 * Html tags.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public enum Html {
	/** The instance. */
	INSTANCE;

	/**
	 * Attributes.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public enum Attributes {
		/** The instance. */
		INSTANCE;
		/** Whether or not it is checked. */
		public static final String CHECKED = "checked";
		/** The id attribute. */
		public static final String IDENTIFIER = "id";
		/** The name attribute. */
		public static final String NAME = "name";
		/** The onchange attribute. */
		public static final String ONCHANGE = "onchange";
		/** The type. */
		public static final String TYPE = "type";
	}

	/**
	 * The elements.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public enum Elements {
		/** The instance. */
		INSTANCE;
		/** Input element. */
		public static final String INPUT = "input";
		/** Script element. */
		public static final String SCRIPT = "script";
	}

	/**
	 * Attribute values types.
	 * 
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public enum Types {
		/** The instance. */
		INSTANCE;
		/** Indication an input is a checkbox. */
		public static final String INPUT_CHECKBOX = "checkbox";
		/** Indication a script is javascript. */
		public static final String SCRIPT_JS = "text/javascript";
	}
}
