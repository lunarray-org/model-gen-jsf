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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.generation.jsf.render.extensions.ViewExtension;
import org.lunarray.model.generation.jsf.render.factories.datatable.DataTableBuilder;
import org.lunarray.model.generation.jsf.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.jsf.render.factories.table.TablePropertyDescriptorRenderStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A strategy factory factory.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public enum RenderFactoryUtil {
	/** The instance. */
	INSTANCE;

	/** The table key. */
	private static final String DATATABLE_KEY = "datatable";

	/** The view extensions file. */
	private static final String FORM_FACTORY_FILE = "META-INF/generation-jsf-form.properties";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RenderFactoryUtil.class);
	/** The view extensions file. */
	private static final String TABLE_FACTORY_FILE = "META-INF/generation-jsf-table.properties";
	/** The view extensions file. */
	private static final String VIEW_EXTENSIONS_FILE = "META-INF/generation-jsf-view-extensions.properties";

	/**
	 * Gets the value for the dataTableBuilder field.
	 * 
	 * @return The value for the dataTableBuilder field.
	 */
	public static DataTableBuilder getDataTableBuilder() {
		return INSTANCE.getDataTableBuilderInner();
	}

	/**
	 * Gets the value for the formComponents field.
	 * 
	 * @return The value for the formComponents field.
	 */
	public static Map<String, FormPropertyRenderStrategy.Factory> getFormComponents() {
		return INSTANCE.getFormComponentsInner();
	}

	/**
	 * Gets the value for the tableComponents field.
	 * 
	 * @return The value for the tableComponents field.
	 */
	public static Map<String, TablePropertyDescriptorRenderStrategy.Factory> getTableComponents() {
		return INSTANCE.getTableComponentsInner();
	}

	/**
	 * Gets all view extensions.
	 * 
	 * @return The view extensions.
	 */
	public static Map<String, ViewExtension> getViewExtensions() {
		return INSTANCE.getViewExtensionsInner();
	}

	/** The datatable builder. */
	private Class<DataTableBuilder> dataTableBuilder;

	/** The view extensions. */
	private Map<String, Class<FormPropertyRenderStrategy.Factory>> formComponents;

	/** The view extensions. */
	private Map<String, Class<TablePropertyDescriptorRenderStrategy.Factory>> tableComponents;

	/** The view extensions. */
	private Map<String, Class<ViewExtension>> viewExtensions;

	/**
	 * Constructs the factory factory.
	 */
	private RenderFactoryUtil() {
		final Properties viewExtensionProperties = new Properties();
		final Properties tableFactoryProperties = new Properties();
		final Properties formFactoryProperties = new Properties();
		AccessController.doPrivileged(new FillPropertiesAction(viewExtensionProperties, RenderFactoryUtil.VIEW_EXTENSIONS_FILE));
		AccessController.doPrivileged(new FillPropertiesAction(tableFactoryProperties, RenderFactoryUtil.TABLE_FACTORY_FILE));
		AccessController.doPrivileged(new FillPropertiesAction(formFactoryProperties, RenderFactoryUtil.FORM_FACTORY_FILE));
		this.viewExtensions = this.getClassList(viewExtensionProperties, ViewExtension.class);
		this.formComponents = this.getClassList(formFactoryProperties, FormPropertyRenderStrategy.Factory.class);
		this.tableComponents = this.getClassList(tableFactoryProperties, TablePropertyDescriptorRenderStrategy.Factory.class);
		this.dataTableBuilder = this.getClassList(tableFactoryProperties, DataTableBuilder.class).get(RenderFactoryUtil.DATATABLE_KEY);
	}

	/**
	 * Gets the value for the dataTableBuilder field.
	 * 
	 * @return The value for the dataTableBuilder field.
	 */
	public DataTableBuilder getDataTableBuilderInner() {
		return this.getInstance(this.dataTableBuilder);
	}

	/**
	 * Gets the value for the formComponents field.
	 * 
	 * @return The value for the formComponents field.
	 */
	public Map<String, FormPropertyRenderStrategy.Factory> getFormComponentsInner() {
		return this.getInstances(this.formComponents);
	}

	/**
	 * Gets the value for the tableComponents field.
	 * 
	 * @return The value for the tableComponents field.
	 */
	public Map<String, TablePropertyDescriptorRenderStrategy.Factory> getTableComponentsInner() {
		return this.getInstances(this.tableComponents);
	}

	/**
	 * Gets all view extensions.
	 * 
	 * @return The view extensions.
	 */
	public Map<String, ViewExtension> getViewExtensionsInner() {
		return this.getInstances(this.viewExtensions);
	}

	/**
	 * Gets a list of classes.
	 * 
	 * @param properties
	 *            The property.
	 * @param superType
	 *            The class super type.
	 * @return The list of classes.
	 * @param <T>
	 *            The super type.
	 */
	private <T> Map<String, Class<T>> getClassList(final Properties properties, final Class<T> superType) {
		final Map<String, Class<T>> results = new HashMap<String, Class<T>>();
		for (final String key : properties.stringPropertyNames()) {
			final String value = properties.getProperty(key);
			results.put(key, this.processProperty(superType, value));
		}
		return results;
	}

	/**
	 * Gets all instances.
	 * 
	 * @param type
	 *            The type.
	 * @param <T>
	 *            The type.
	 * @return The instances.
	 */
	private <T> T getInstance(final Class<T> type) {
		T result = null;
		try {
			if (!CheckUtil.isNull(type)) {
				result = type.newInstance();
			}
		} catch (final InstantiationException e) {
			RenderFactoryUtil.LOGGER.warn("Invalid configuration, could not instantiate.", e);
		} catch (final IllegalAccessException e) {
			RenderFactoryUtil.LOGGER.warn("Invalid configuration, could not access.", e);
		}
		return result;
	}

	/**
	 * Gets all view extensions.
	 * 
	 * @param types
	 *            The types.
	 * @param <T>
	 *            The type.
	 * @return The view extensions.
	 */
	private <T> Map<String, T> getInstances(final Map<String, Class<T>> types) {
		final Map<String, T> extensions = new HashMap<String, T>();
		for (final Map.Entry<String, Class<T>> clazz : types.entrySet()) {
			extensions.put(clazz.getKey(), this.getInstance(clazz.getValue()));
		}
		return extensions;
	}

	/**
	 * Process a single property.
	 * 
	 * @param superType
	 *            The super type.
	 * @param value
	 *            the value.
	 * @param <T>
	 *            The super type.
	 * @return The result.
	 */
	@SuppressWarnings("unchecked")
	// We checked.
	private <T> Class<T> processProperty(final Class<T> superType, final String value) {
		Class<T> result = null;
		try {
			final Class<?> clazz = Class.forName(value);
			if (superType.isAssignableFrom(clazz)) {
				result = (Class<T>) clazz;
			}
		} catch (final ClassNotFoundException e) {
			RenderFactoryUtil.LOGGER.warn("Invalid configuration, could not find class.", e);
		}
		return result;
	}

	/**
	 * The action that fills the properties.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	private static class FillPropertiesAction
			implements PrivilegedAction<Void> {

		/** The properties file name. */
		private final transient String name;
		/** The properties to fill. */
		private final transient Properties properties;

		/**
		 * Constructs the property action.
		 * 
		 * @param properties
		 *            The properties.
		 * @param name
		 *            The file name.
		 */
		public FillPropertiesAction(final Properties properties, final String name) {
			this.properties = properties;
			this.name = name;
		}

		/** {@inheritDoc} */
		@Override
		public Void run() {
			try {
				final Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(this.name);
				while (resources.hasMoreElements()) {
					final InputStream inputStream = resources.nextElement().openStream();
					if (!CheckUtil.isNull(inputStream)) {
						try {
							this.properties.load(inputStream);
						} catch (final IOException e) {
							RenderFactoryUtil.LOGGER.warn("Could not read configuration.", e);
						} finally {
							inputStream.close();
						}
					}
				}
			} catch (final IOException e) {
				RenderFactoryUtil.LOGGER.warn("Could not read URL.", e);
			}
			return null;
		}
	}
}
