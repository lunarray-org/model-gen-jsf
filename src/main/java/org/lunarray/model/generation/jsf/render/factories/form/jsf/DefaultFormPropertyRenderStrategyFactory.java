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
package org.lunarray.model.generation.jsf.render.factories.form.jsf;

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
import org.lunarray.model.generation.jsf.components.GeneratedForm;
import org.lunarray.model.generation.jsf.render.RenderContext;
import org.lunarray.model.generation.jsf.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.jsf.render.factories.form.jsf.components.DefaultReadOnlyOutput;
import org.lunarray.model.generation.jsf.util.RenderFactoryUtil;
import org.lunarray.model.generation.util.RenderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Row strategy factory.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity.
 */
public final class DefaultFormPropertyRenderStrategyFactory<E>
		implements RenderFactory<RenderContext, E> {

	/** The collection key. */
	private static final String COLLECTION_KEY = "collection.";
	/** Validation message. */
	private static final String CONTEXT_NULL = "Context may not be null.";
	/** The default key. */
	private static final String DEFAULT = "default";
	/** Validation message. */
	private static final String DESCRIPTOR_NULL = "Descriptor may not be null.";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFormPropertyRenderStrategyFactory.class);
	/** The single key. */
	private static final String SINGLE_KEY = "single.";
	/** The collection factories. */
	private EnumMap<RenderType, FormPropertyRenderStrategy.Factory> collectionFactories;
	/** The default factory. */
	private FormPropertyRenderStrategy.Factory defaultCollectionFactory;
	/** The default factory. */
	private FormPropertyRenderStrategy.Factory defaultSingleFactory;
	/** The form. */
	private GeneratedForm<? super E, E> form;
	/** The factories. */
	private EnumMap<RenderType, FormPropertyRenderStrategy.Factory> singleFactories;

	/**
	 * Default constructor.
	 * 
	 * @param form
	 *            The form.
	 */
	public DefaultFormPropertyRenderStrategyFactory(final GeneratedForm<? super E, E> form) {
		this.form = form;
		final Map<String, FormPropertyRenderStrategy.Factory> factories = RenderFactoryUtil.getFormComponents();
		this.defaultSingleFactory = factories.get(new StringBuilder(DefaultFormPropertyRenderStrategyFactory.SINGLE_KEY).append(
				DefaultFormPropertyRenderStrategyFactory.DEFAULT).toString());
		this.defaultCollectionFactory = factories.get(new StringBuilder(DefaultFormPropertyRenderStrategyFactory.COLLECTION_KEY).append(
				DefaultFormPropertyRenderStrategyFactory.DEFAULT).toString());
		this.singleFactories = new EnumMap<RenderType, FormPropertyRenderStrategy.Factory>(RenderType.class);
		this.collectionFactories = new EnumMap<RenderType, FormPropertyRenderStrategy.Factory>(RenderType.class);
		for (final RenderType rt : RenderType.values()) {
			this.singleFactories.put(rt, factories.get(this.getSingleKey(rt)));
			this.collectionFactories.put(rt, factories.get(this.getCollectionKey(rt)));
		}
	}

	/** {@inheritDoc} */
	@Override
	public void beginOperation(final RenderContext context, final OperationDescriptor<E> descriptor) {
		// Not implemented.
	}

	/** {@inheritDoc} */
	@Override
	public void endOperation(final RenderContext context, final OperationDescriptor<E> operation) {
		// Not implemented.
	}

	/**
	 * Gets the value for the collectionFactories field.
	 * 
	 * @return The value for the collectionFactories field.
	 */
	public EnumMap<RenderType, FormPropertyRenderStrategy.Factory> getCollectionFactories() {
		return this.collectionFactories;
	}

	/**
	 * Gets the value for the defaultCollectionFactory field.
	 * 
	 * @return The value for the defaultCollectionFactory field.
	 */
	public FormPropertyRenderStrategy.Factory getDefaultCollectionFactory() {
		return this.defaultCollectionFactory;
	}

	/**
	 * Gets the value for the defaultSingleFactory field.
	 * 
	 * @return The value for the defaultSingleFactory field.
	 */
	public FormPropertyRenderStrategy.Factory getDefaultSingleFactory() {
		return this.defaultSingleFactory;
	}

	/**
	 * Gets the value for the form field.
	 * 
	 * @return The value for the form field.
	 */
	public GeneratedForm<? super E, E> getForm() {
		return this.form;
	}

	/**
	 * Gets the value for the singleFactories field.
	 * 
	 * @return The value for the singleFactories field.
	 */
	public EnumMap<RenderType, FormPropertyRenderStrategy.Factory> getSingleFactories() {
		return this.singleFactories;
	}

	/** {@inheritDoc} */
	@Override
	public <D, P extends Collection<D>> void renderCollectionParameter(final RenderContext context, final OperationDescriptor<E> operation,
			final CollectionParameterDescriptor<D, P> descriptor, final RenderType renderType) {
		// Not implemented.
	}

	/** {@inheritDoc} */
	@Override
	public <D, P extends Collection<D>> void renderCollectionProperty(final RenderContext context,
			final CollectionPropertyDescriptor<D, P, E> descriptor, final RenderType renderType) {
		DefaultFormPropertyRenderStrategyFactory.LOGGER.debug("Render type {} for collection property {}", renderType, descriptor);
		Validate.notNull(context, DefaultFormPropertyRenderStrategyFactory.CONTEXT_NULL);
		Validate.notNull(descriptor, DefaultFormPropertyRenderStrategyFactory.DESCRIPTOR_NULL);
		FormPropertyRenderStrategy.Factory factory = null;
		if (this.collectionFactories.containsKey(renderType)) {
			factory = this.collectionFactories.get(renderType);
		}
		if (CheckUtil.isNull(factory)) {
			factory = this.defaultCollectionFactory;
		}
		final FormPropertyRenderStrategy<P, E> strategy = factory.createStrategy(context);
		strategy.build(descriptor, context);
		this.form.processProperty(strategy, context);
	}

	/** {@inheritDoc} */
	@Override
	public <D, R extends Collection<D>> void renderCollectionResultType(final RenderContext context,
			final OperationDescriptor<E> operation, final CollectionResultDescriptor<D, R> resultDescriptor, final RenderType renderType) {
		// Not implemented.
	}

	/** {@inheritDoc} */
	@Override
	public <P> void renderParameter(final RenderContext context, final ParameterDescriptor<P> descriptor,
			final OperationDescriptor<E> operation, final RenderType renderType) {
		// Not implemented.
	}

	/** {@inheritDoc} */
	@Override
	public <P> void renderProperty(final RenderContext context, final PropertyDescriptor<P, E> descriptor, final RenderType renderType) {
		DefaultFormPropertyRenderStrategyFactory.LOGGER.debug("Render type {} for property {}", renderType, descriptor);
		Validate.notNull(context, DefaultFormPropertyRenderStrategyFactory.CONTEXT_NULL);
		Validate.notNull(descriptor, DefaultFormPropertyRenderStrategyFactory.DESCRIPTOR_NULL);
		final boolean readonly = descriptor.isImmutable();
		FormPropertyRenderStrategy.Factory factory = null;
		if (readonly) {
			factory = new DefaultReadOnlyOutput.FactoryImpl();
		} else {
			if (this.collectionFactories.containsKey(renderType)) {
				factory = this.singleFactories.get(renderType);
			}
			if (CheckUtil.isNull(factory)) {
				factory = this.defaultSingleFactory;
			}
		}
		final FormPropertyRenderStrategy<P, E> strategy = factory.createStrategy(context);
		strategy.build(descriptor, context);
		this.form.processProperty(strategy, context);
	}

	/** {@inheritDoc} */
	@Override
	public <R> void renderResultType(final RenderContext context, final OperationDescriptor<E> operation,
			final ResultDescriptor<R> resultDescriptor, final RenderType renderType) {
		// Not implemented.
	}

	/**
	 * Sets a new value for the collectionFactories field.
	 * 
	 * @param collectionFactories
	 *            The new value for the collectionFactories field.
	 */
	public void setCollectionFactories(final EnumMap<RenderType, FormPropertyRenderStrategy.Factory> collectionFactories) {
		this.collectionFactories = collectionFactories;
	}

	/**
	 * Sets a new value for the defaultCollectionFactory field.
	 * 
	 * @param defaultCollectionFactory
	 *            The new value for the defaultCollectionFactory field.
	 */
	public void setDefaultCollectionFactory(final FormPropertyRenderStrategy.Factory defaultCollectionFactory) {
		this.defaultCollectionFactory = defaultCollectionFactory;
	}

	/**
	 * Sets a new value for the defaultSingleFactory field.
	 * 
	 * @param defaultSingleFactory
	 *            The new value for the defaultSingleFactory field.
	 */
	public void setDefaultSingleFactory(final FormPropertyRenderStrategy.Factory defaultSingleFactory) {
		this.defaultSingleFactory = defaultSingleFactory;
	}

	/**
	 * Sets a new value for the form field.
	 * 
	 * @param form
	 *            The new value for the form field.
	 */
	public void setForm(final GeneratedForm<? super E, E> form) {
		this.form = form;
	}

	/**
	 * Sets a new value for the singleFactories field.
	 * 
	 * @param singleFactories
	 *            The new value for the singleFactories field.
	 */
	public void setSingleFactories(final EnumMap<RenderType, FormPropertyRenderStrategy.Factory> singleFactories) {
		this.singleFactories = singleFactories;
	}

	/**
	 * Gets the collection key.
	 * 
	 * @param rendertype
	 *            The render type.
	 * @return The key.
	 */
	private String getCollectionKey(final RenderType rendertype) {
		return new StringBuilder(DefaultFormPropertyRenderStrategyFactory.COLLECTION_KEY).append(rendertype.name()).toString();
	}

	/**
	 * Gets the single key.
	 * 
	 * @param rendertype
	 *            The render type.
	 * @return The key.
	 */
	private String getSingleKey(final RenderType rendertype) {
		return new StringBuilder(DefaultFormPropertyRenderStrategyFactory.SINGLE_KEY).append(rendertype.name()).toString();
	}
}
