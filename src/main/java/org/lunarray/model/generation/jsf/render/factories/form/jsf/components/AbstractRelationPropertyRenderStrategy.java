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
package org.lunarray.model.generation.jsf.render.factories.form.jsf.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItems;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.dictionary.Dictionary;
import org.lunarray.model.descriptor.dictionary.exceptions.DictionaryException;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationType;
import org.lunarray.model.descriptor.presentation.PresentationEntityDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationPropertyDescriptor;
import org.lunarray.model.generation.jsf.components.shared.format.HtmlFormat;
import org.lunarray.model.generation.jsf.render.RenderContext;
import org.lunarray.model.generation.jsf.util.DictionaryConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Strategy for select items.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public abstract class AbstractRelationPropertyRenderStrategy<P, E>
		extends AbstractRenderStrategy<P, E> {

	/** A logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRelationPropertyRenderStrategy.class);
	/** Validation message. */
	private static final String RENDER_CONTEXT_NULL = "Render context may not be null.";
	/** The model. */
	private final transient Model<Object> model;
	/** The model expression. */
	private final transient ValueExpression modelExpression;

	/**
	 * Default constructor.
	 * 
	 * @param jsfContext
	 *            The build context. May not be null.
	 */
	public AbstractRelationPropertyRenderStrategy(final RenderContext jsfContext) {
		super();
		Validate.notNull(jsfContext, AbstractRelationPropertyRenderStrategy.RENDER_CONTEXT_NULL);
		this.model = jsfContext.getModel();
		this.modelExpression = jsfContext.getModelExpression();
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	/* Can't be more sure of the type. */
	@Override
	public final void build(final PropertyDescriptor<P, E> property, final RenderContext context) {
		Validate.notNull(property, "Descriptor may not be null.");
		Validate.notNull(context, AbstractRelationPropertyRenderStrategy.RENDER_CONTEXT_NULL);
		final UIInput input;
		this.buildLabel(property, context);
		this.buildMessages(property, context);
		input = this.createInput(property, context);
		final UISelectItems items = new UISelectItems();
		final PresentationPropertyDescriptor<P, E> presentationDescriptor = property.adapt(PresentationPropertyDescriptor.class);
		if (property.isRelation()) {
			final RelationDescriptor relationDescriptor = property.adapt(RelationDescriptor.class);
			items.setValue(this.convert(relationDescriptor.getRelatedName(), relationDescriptor.getRelationType()));
			input.setConverter(new DictionaryConverter(property.getPropertyType(), this.modelExpression));
		} else {
			String format = null;
			if (!CheckUtil.isNull(presentationDescriptor)) {
				format = presentationDescriptor.getFormat();
			}
			if (!CheckUtil.isNull(format)) {
				final HtmlFormat htmlFormat = new HtmlFormat();
				htmlFormat.setFormat(format);
				input.getFacets().put("format", htmlFormat);
			}
		}
		input.getChildren().add(items);
		this.addValue(input, property, context);

		if (!CheckUtil.isNull(presentationDescriptor) && presentationDescriptor.isRequiredIndication()) {
			input.setRequired(true);
		}
		input.setId(this.createInputName(context));
		this.setInput(input);
	}

	/**
	 * Create a select item.
	 * 
	 * @param <T>
	 *            The descriptor type.
	 * @param items
	 *            The items.
	 * @param presentationDescriptor
	 *            The presentation descriptor.
	 * @param identifiableDescriptor
	 *            The identifiable Descriptor.
	 * @param entity
	 *            The element.
	 * @param relationType
	 *            The relation type.
	 * @throws ValueAccessException
	 *             Thrown if the value could not be accessed.
	 */
	private <T> void createSelectItem(final List<SelectItem> items, final PresentationEntityDescriptor<T> presentationDescriptor,
			final KeyedEntityDescriptor<T, ?> identifiableDescriptor, final T entity, final RelationType relationType)
			throws ValueAccessException {
		Object value;
		if ((RelationType.CONCRETE == relationType) || CheckUtil.isNull(identifiableDescriptor)) {
			value = entity;
		} else {
			value = identifiableDescriptor.getKeyProperty().getValue(entity);
		}
		Validate.notNull(value, "Value must be set.");
		String name;
		name = this.resolveName(presentationDescriptor, identifiableDescriptor, entity);
		items.add(new SelectItem(value, name));
	}

	/**
	 * Process an identifiable descriptor.
	 * 
	 * @param identifiableDescriptor
	 *            The identifiable descriptor.
	 * @param entity
	 *            The entity.
	 * @param <T>
	 *            The entity type.
	 * @return The string value.
	 * @throws ValueAccessException
	 *             Thrown if the value could not be accessed.
	 */
	private <T> String processIdentifiable(final KeyedEntityDescriptor<T, ?> identifiableDescriptor, final T entity)
			throws ValueAccessException {
		String name;
		if (CheckUtil.isNull(identifiableDescriptor)) {
			name = entity.toString();
		} else {
			final PropertyDescriptor<?, T> keyProperty = identifiableDescriptor.getKeyProperty();
			if (CheckUtil.isNull(keyProperty)) {
				name = entity.toString();
			} else {
				name = identifiableDescriptor.getKeyProperty().getValue(entity).toString();
			}
		}
		return name;
	}

	/**
	 * Resolve the property name.
	 * 
	 * @param presentationDescriptor
	 *            The presentation descriptor.
	 * @param identifiableDescriptor
	 *            The identification descriptor.
	 * @param entity
	 *            The entity.
	 * @param <T>
	 *            The entity type.
	 * @return The name.
	 * @throws ValueAccessException
	 *             Thrown if the value could not be accessed.
	 */
	private <T> String resolveName(final PresentationEntityDescriptor<T> presentationDescriptor,
			final KeyedEntityDescriptor<T, ?> identifiableDescriptor, final T entity) throws ValueAccessException {
		String name;
		if (CheckUtil.isNull(presentationDescriptor) || CheckUtil.isNull(presentationDescriptor.getNameProperty())) {
			name = this.processIdentifiable(identifiableDescriptor, entity);
		} else {
			final PropertyDescriptor<?, T> nameProperty = presentationDescriptor.getNameProperty();
			name = nameProperty.getValue(entity).toString();
		}
		return name;
	}

	/**
	 * Lookup all of the given type in the dictionary.
	 * 
	 * @param <T>
	 *            The type.
	 * @param key
	 *            The key. May not be null and must be well defined.
	 * @param relationType
	 *            The relation type.
	 * @return Select items.
	 */
	@SuppressWarnings("unchecked")
	protected final <T> List<SelectItem> convert(final String key, final RelationType relationType) {
		Validate.notNull(key, "Key may not be null.");
		final List<SelectItem> items;
		final Dictionary dictionary = this.model.getExtension(Dictionary.class);
		if (CheckUtil.isNull(dictionary)) {
			items = new ArrayList<SelectItem>(0);
		} else {
			final EntityDescriptor<?> tmpDescriptor = this.model.getEntity(key);
			Validate.notNull(tmpDescriptor, "Entity key is not valid.");
			final EntityDescriptor<T> descriptor = (EntityDescriptor<T>) tmpDescriptor;
			final PresentationEntityDescriptor<T> presentationDescriptor = descriptor.adapt(PresentationEntityDescriptor.class);
			final KeyedEntityDescriptor<T, ?> identifiableDescriptor = descriptor.adapt(KeyedEntityDescriptor.class);
			Collection<T> objects;
			try {
				objects = dictionary.lookup(descriptor);
			} catch (final DictionaryException e) {
				AbstractRelationPropertyRenderStrategy.LOGGER.warn("Could not look up in dictionary '{}'.", descriptor.getName(), e);
				objects = new LinkedList<T>();
			}
			items = new ArrayList<SelectItem>(objects.size());
			try {
				for (final T e : objects) {
					this.createSelectItem(items, presentationDescriptor, identifiableDescriptor, e, relationType);
				}
			} catch (final ValueAccessException e) {
				AbstractRelationPropertyRenderStrategy.LOGGER.warn("Could not access value.", e);
			}
		}
		return items;
	}
}
