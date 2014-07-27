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

import java.util.Deque;
import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;

/**
 * Utilities for variables.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public enum VariableUtil {
	/** The instance. */
	INSTANCE;

	/** Validation message. */
	private static final String PREFIXES_NULL = "Prefixes may not be null.";

	/**
	 * Compiles the name.
	 * 
	 * @param prefix
	 *            The current prefix.
	 * @param prefixes
	 *            The prefixes. May not be null.
	 * @return The compiled string.
	 */
	public static String compileName(final String prefix, final Deque<PropertyDescriptor<?, ?>> prefixes) {
		Validate.notNull(prefixes, VariableUtil.PREFIXES_NULL);
		final StringBuilder builder = new StringBuilder();
		builder.append(prefix);
		final Iterator<PropertyDescriptor<?, ?>> itPrefixes = prefixes.iterator();
		if (itPrefixes.hasNext()) {
			builder.append(itPrefixes.next().getName().replace('.', '_'));
		}
		while (itPrefixes.hasNext()) {
			builder.append("_");
			builder.append(itPrefixes.next().getName().replace('.', '_'));
		}
		return builder.toString();
	}

	/**
	 * Compiles a variable.
	 * 
	 * @param prefixes
	 *            The prefixes. May not be null.
	 * @param variable
	 *            The variable name.
	 * @return The compiled variable.
	 */
	public static String compileVariable(final String variable, final Deque<PropertyDescriptor<?, ?>> prefixes) {
		Validate.notNull(prefixes, VariableUtil.PREFIXES_NULL);
		final StringBuilder builder = new StringBuilder();
		final Iterator<PropertyDescriptor<?, ?>> itPrefixes = prefixes.iterator();
		builder.append("#{");
		builder.append(variable);
		while (itPrefixes.hasNext()) {
			builder.append('.');
			builder.append(itPrefixes.next().getName());
		}
		builder.append('}');
		return builder.toString();
	}

	/**
	 * Compiles the name.
	 * 
	 * @param prefix
	 *            The current prefix.
	 * @param prefixes
	 *            The prefixes. May not be null.
	 * @param variable
	 *            The variable name.
	 * @return The compiled string.
	 */
	public static String compileVariableName(final String prefix, final Deque<PropertyDescriptor<?, ?>> prefixes, final String variable) {
		Validate.notNull(prefixes, VariableUtil.PREFIXES_NULL);
		final StringBuilder builder = new StringBuilder();
		builder.append(prefix);
		final Iterator<PropertyDescriptor<?, ?>> itPrefixes = prefixes.iterator();
		if (itPrefixes.hasNext()) {
			builder.append(itPrefixes.next().getName().replace('.', '_'));
		}
		while (itPrefixes.hasNext()) {
			builder.append('_');
			builder.append(itPrefixes.next().getName().replace('.', '_'));
		}
		if (!CheckUtil.isNull(variable)) {
			if (!prefixes.isEmpty()) {
				builder.append('_');
			}
			builder.append(variable.replace('.', '_'));
		}
		return builder.toString();
	}

	/**
	 * Compiles the name.
	 * 
	 * @param prefix
	 *            The current prefix.
	 * @param prefixes
	 *            The prefixes. May not be null.
	 * @param variable
	 *            The variable name.
	 * @return The compiled string.
	 */
	public static String compileVariableNameStrings(final String prefix, final Deque<String> prefixes, final String variable) {
		Validate.notNull(prefixes, VariableUtil.PREFIXES_NULL);
		final StringBuilder builder = new StringBuilder();
		builder.append(prefix);
		final Iterator<String> itPrefixes = prefixes.iterator();
		if (itPrefixes.hasNext()) {
			builder.append(itPrefixes.next().replace('.', '_'));
		}
		while (itPrefixes.hasNext()) {
			builder.append('_');
			builder.append(itPrefixes.next().replace('.', '_'));
		}
		if (!CheckUtil.isNull(variable)) {
			builder.append('_');
			builder.append(variable.replace('.', '_'));
		}
		return builder.toString();
	}
}
