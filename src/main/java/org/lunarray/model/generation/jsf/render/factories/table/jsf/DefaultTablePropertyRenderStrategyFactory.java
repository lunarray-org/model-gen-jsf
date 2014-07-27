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
package org.lunarray.model.generation.jsf.render.factories.table.jsf;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.operation.OperationDescriptor;
import org.lunarray.model.descriptor.model.operation.parameters.CollectionParameterDescriptor;
import org.lunarray.model.descriptor.model.operation.parameters.ParameterDescriptor;
import org.lunarray.model.descriptor.model.operation.result.CollectionResultDescriptor;
import org.lunarray.model.descriptor.model.operation.result.ResultDescriptor;
import org.lunarray.model.descriptor.model.property.CollectionPropertyDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.RenderType;
import org.lunarray.model.generation.jsf.components.GeneratedTable;
import org.lunarray.model.generation.jsf.render.RenderContext;
import org.lunarray.model.generation.jsf.render.factories.table.TablePropertyDescriptorRenderStrategy;
import org.lunarray.model.generation.jsf.util.RenderFactoryUtil;
import org.lunarray.model.generation.util.RenderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Row strategy factory.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity type.
 */
public final class DefaultTablePropertyRenderStrategyFactory<E>
		implements RenderFactory<RenderContext, E> {

	/** The collection prefix. */
	private static final String COLLECTION_KEY = "collection.";
	/** Validation message. */
	private static final String CONTEXT_NULL = "Context may not be null.";
	/** The default key. */
	private static final String DEFAULT = "default";
	/** Validation message. */
	private static final String DESCRIPTOR_NULL = "Descriptor may not be null.";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTablePropertyRenderStrategyFactory.class);
	/** The single prefix. */
	private static final String SINGLE_KEY = "single.";
	/** Collection factories. */
	private EnumMap<RenderType, TablePropertyDescriptorRenderStrategy.Factory> collectionFactories;
	/** Default factory. */
	private TablePropertyDescriptorRenderStrategy.Factory defaultCollectionFactory;
	/** Default factory. */
	private TablePropertyDescriptorRenderStrategy.Factory defaultSingleFactory;
	/** Single factory. */
	private EnumMap<RenderType, TablePropertyDescriptorRenderStrategy.Factory> singleFactories;
	/** The table. */
	private GeneratedTable<? super E, E> table;

	/**
	 * Default constructor.
	 * 
	 * @param table
	 *            The table. May not be null.
	 */
	public DefaultTablePropertyRenderStrategyFactory(final GeneratedTable<? super E, E> table) {
		Validate.notNull(table, "The table may not be null.");
		this.table = table;
		final Map<String, TablePropertyDescriptorRenderStrategy.Factory> factories = RenderFactoryUtil.getTableComponents();
		this.defaultSingleFactory = factories.get(new StringBuilder(DefaultTablePropertyRenderStrategyFactory.SINGLE_KEY).append(
				DefaultTablePropertyRenderStrategyFactory.DEFAULT).toString());
		this.defaultCollectionFactory = factories.get(new StringBuilder(DefaultTablePropertyRenderStrategyFactory.COLLECTION_KEY).append(
				DefaultTablePropertyRenderStrategyFactory.DEFAULT).toString());
		this.singleFactories = new EnumMap<RenderType, TablePropertyDescriptorRenderStrategy.Factory>(RenderType.class);
		this.collectionFactories = new EnumMap<RenderType, TablePropertyDescriptorRenderStrategy.Factory>(RenderType.class);
		for (final RenderType rt : RenderType.values()) {
			this.singleFactories.put(rt, factories.get(this.getSingleKey(rt)));
			this.collectionFactories.put(rt, factories.get(this.getCollectionKey(rt)));
		}
	}

	/** {@inheritDoc} */
	@Override
	public void beginOperation(final RenderContext context, final OperationDescriptor<E> descriptor) {
		// Ignore
	}

	/** {@inheritDoc} */
	@Override
	public void endOperation(final RenderContext context, final OperationDescriptor<E> operation) {
		// Ignore
	}

	/**
	 * Gets the value for the collectionFactories field.
	 * 
	 * @return The value for the collectionFactories field.
	 */
	public EnumMap<RenderType, TablePropertyDescriptorRenderStrategy.Factory> getCollectionFactories() {
		return this.collectionFactories;
	}

	/**
	 * Gets the value for the defaultCollectionFactory field.
	 * 
	 * @return The value for the defaultCollectionFactory field.
	 */
	public TablePropertyDescriptorRenderStrategy.Factory getDefaultCollectionFactory() {
		return this.defaultCollectionFactory;
	}

	/**
	 * Gets the value for the defaultSingleFactory field.
	 * 
	 * @return The value for the defaultSingleFactory field.
	 */
	public TablePropertyDescriptorRenderStrategy.Factory getDefaultSingleFactory() {
		return this.defaultSingleFactory;
	}

	/**
	 * Gets the value for the singleFactories field.
	 * 
	 * @return The value for the singleFactories field.
	 */
	public EnumMap<RenderType, TablePropertyDescriptorRenderStrategy.Factory> getSingleFactories() {
		return this.singleFactories;
	}

	/**
	 * Gets the value for the table field.
	 * 
	 * @return The value for the table field.
	 */
	public GeneratedTable<? super E, E> getTable() {
		return this.table;
	}

	/** {@inheritDoc} */
	@Override
	public <D, P extends Collection<D>> void renderCollectionParameter(final RenderContext context, final OperationDescriptor<E> operation,
			final CollectionParameterDescriptor<D, P> descriptor, final RenderType renderType) {
		// Ignore
	}

	/** {@inheritDoc} */
	@Override
	public <D, P extends Collection<D>> void renderCollectionProperty(final RenderContext context,
			final CollectionPropertyDescriptor<D, P, E> descriptor, final RenderType renderType) {
		DefaultTablePropertyRenderStrategyFactory.LOGGER.debug("Render type {} for collection property {}", renderType, descriptor);
		Validate.notNull(context, DefaultTablePropertyRenderStrategyFactory.CONTEXT_NULL);
		Validate.notNull(descriptor, DefaultTablePropertyRenderStrategyFactory.DESCRIPTOR_NULL);
		TablePropertyDescriptorRenderStrategy.Factory factory = null;
		if (this.collectionFactories.containsKey(renderType)) {
			factory = this.collectionFactories.get(renderType);
		}
		if (CheckUtil.isNull(factory)) {
			factory = this.defaultCollectionFactory;
		}
		final TablePropertyDescriptorRenderStrategy<P, E> strategy = factory.createStrategy(context);
		strategy.build(descriptor, context);
		this.table.processStrategy(context.getFacesContext(), strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <D, R extends Collection<D>> void renderCollectionResultType(final RenderContext context,
			final OperationDescriptor<E> operation, final CollectionResultDescriptor<D, R> resultDescriptor, final RenderType renderType) {
		// Ignore
	}

	/** {@inheritDoc} */
	@Override
	public <P> void renderParameter(final RenderContext context, final ParameterDescriptor<P> descriptor,
			final OperationDescriptor<E> operation, final RenderType renderType) {
		// Ignore
	}

	/** {@inheritDoc} */
	@Override
	public <P> void renderProperty(final RenderContext context, final PropertyDescriptor<P, E> descriptor, final RenderType renderType) {
		DefaultTablePropertyRenderStrategyFactory.LOGGER.debug("Render type {} for property {}", renderType, descriptor);
		Validate.notNull(context, DefaultTablePropertyRenderStrategyFactory.CONTEXT_NULL);
		Validate.notNull(descriptor, DefaultTablePropertyRenderStrategyFactory.DESCRIPTOR_NULL);
		TablePropertyDescriptorRenderStrategy.Factory factory = null;
		if (this.singleFactories.containsKey(renderType)) {
			factory = this.singleFactories.get(renderType);
		}
		if (CheckUtil.isNull(factory)) {
			factory = this.defaultSingleFactory;
		}
		final TablePropertyDescriptorRenderStrategy<P, E> strategy = factory.createStrategy(context);
		strategy.build(descriptor, context);
		this.table.processStrategy(context.getFacesContext(), strategy);
	}

	/** {@inheritDoc} */
	@Override
	public <R> void renderResultType(final RenderContext context, final OperationDescriptor<E> operation,
			final ResultDescriptor<R> resultDescriptor, final RenderType renderType) {
		// Ignore
	}

	/**
	 * Sets a new value for the collectionFactories field.
	 * 
	 * @param collectionFactories
	 *            The new value for the collectionFactories field.
	 */
	public void setCollectionFactories(final EnumMap<RenderType, TablePropertyDescriptorRenderStrategy.Factory> collectionFactories) {
		this.collectionFactories = collectionFactories;
	}

	/**
	 * Sets a new value for the defaultCollectionFactory field.
	 * 
	 * @param defaultCollectionFactory
	 *            The new value for the defaultCollectionFactory field.
	 */
	public void setDefaultCollectionFactory(final TablePropertyDescriptorRenderStrategy.Factory defaultCollectionFactory) {
		this.defaultCollectionFactory = defaultCollectionFactory;
	}

	/**
	 * Sets a new value for the defaultSingleFactory field.
	 * 
	 * @param defaultSingleFactory
	 *            The new value for the defaultSingleFactory field.
	 */
	public void setDefaultSingleFactory(final TablePropertyDescriptorRenderStrategy.Factory defaultSingleFactory) {
		this.defaultSingleFactory = defaultSingleFactory;
	}

	/**
	 * Sets a new value for the singleFactories field.
	 * 
	 * @param singleFactories
	 *            The new value for the singleFactories field.
	 */
	public void setSingleFactories(final EnumMap<RenderType, TablePropertyDescriptorRenderStrategy.Factory> singleFactories) {
		this.singleFactories = singleFactories;
	}

	/**
	 * Sets a new value for the table field.
	 * 
	 * @param table
	 *            The new value for the table field.
	 */
	public void setTable(final GeneratedTable<? super E, E> table) {
		this.table = table;
	}

	/**
	 * Gets the collection key.
	 * 
	 * @param rendertype
	 *            The render type.
	 * @return The key.
	 */
	private String getCollectionKey(final RenderType rendertype) {
		return new StringBuilder(DefaultTablePropertyRenderStrategyFactory.COLLECTION_KEY).append(rendertype.name()).toString();
	}

	/**
	 * Gets the single key.
	 * 
	 * @param rendertype
	 *            The render type.
	 * @return The key.
	 */
	private String getSingleKey(final RenderType rendertype) {
		return new StringBuilder(DefaultTablePropertyRenderStrategyFactory.SINGLE_KEY).append(rendertype.name()).toString();
	}
}
