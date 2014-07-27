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
package org.lunarray.model.generation.jsf.render.def;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.generation.jsf.render.StyleClassResolver;
import org.lunarray.model.generation.jsf.render.StyleClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default style class resolver implementation.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public final class PropertyFileStyleClassResolver
		implements StyleClassResolver {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFileStyleClassResolver.class);
	/** The style class properties. */
	private Properties properties;

	/**
	 * Default constructor.
	 */
	public PropertyFileStyleClassResolver() {
		this.properties = new Properties();
	}

	/**
	 * Gets the value for the properties field.
	 * 
	 * @return The value for the properties field.
	 */
	public Properties getProperties() {
		return this.properties;
	}

	/** {@inheritDoc} */
	@Override
	public String resolve(final StyleClasses classes) {
		return this.properties.getProperty(classes.getKey(), classes.getDefaultValue());
	}

	/**
	 * Sets a classpath location.
	 * 
	 * @param path
	 *            The class path.
	 */
	public void setClasspathLocation(final String path) {
		final InputStream iStream = this.getClass().getResourceAsStream(path);
		if (!CheckUtil.isNull(iStream)) {
			try {
				this.properties.load(iStream);
			} catch (final IOException e) {
				throw new IllegalArgumentException("Could not load properties.", e);
			} finally {
				try {
					iStream.close();
				} catch (final IOException e) {
					PropertyFileStyleClassResolver.LOGGER.warn("Could not close stream.", e);
				}
			}
		}
	}

	/**
	 * Sets a new value for the properties field.
	 * 
	 * @param properties
	 *            The new value for the properties field.
	 */
	public void setProperties(final Properties properties) {
		this.properties = properties;
	}
}
