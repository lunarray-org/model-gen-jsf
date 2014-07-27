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
package org.lunarray.model.generation.jsf.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.lunarray.model.descriptor.builder.annotation.presentation.builder.PresQualBuilder;
import org.lunarray.model.descriptor.dictionary.composite.EntityDictionary;
import org.lunarray.model.descriptor.dictionary.composite.simple.CompositeDictionary;
import org.lunarray.model.descriptor.dictionary.enumeration.EnumDictionary;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.qualifier.QualifierEntityDescriptor;
import org.lunarray.model.descriptor.resource.Resource;
import org.lunarray.model.descriptor.resource.simpleresource.SimpleClazzResource;
import org.lunarray.model.descriptor.validator.beanvalidation.BeanValidationValidator;
import org.lunarray.model.generation.jsf.model.Sample01;
import org.lunarray.model.generation.jsf.model.Sample02;
import org.lunarray.model.generation.jsf.model.SampleEnum;

@ManagedBean(name = "formTestPB")
@SessionScoped
public class FormTestPB {

	private final EntityDescriptor<Sample01> clazz;
	private Object instance;
	private final Model<Object> model;
	private Class<?> qualifier;
	private Object select;
	private final Collection<Sample01> selected = new LinkedList<Sample01>();

	public FormTestPB() throws Exception {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Resource<Class<? extends Object>> resource = new SimpleClazzResource(Sample01.class, Sample02.class, SampleEnum.class);
		final List<EntityDictionary<?, ?>> dictionaries = new LinkedList<EntityDictionary<?, ?>>();
		dictionaries.add(new Sample02Dictionary());
		final BeanValidationValidator validator = new BeanValidationValidator();
		this.model = PresQualBuilder.createBuilder().extensions(new EnumDictionary(new CompositeDictionary(dictionaries)), validator)
				.resources(resource).build();
		this.clazz = this.model.getEntity(Sample01.class);
		this.instance = new Sample01();
	}

	public EntityDescriptor<?> getClazz() {
		return this.clazz;
	}

	public List<Sample01> getElements() {
		return Sample01.DATA;
	}

	public Object getInstance() {
		return this.instance;
	}

	public Model<Object> getModel() {
		return this.model;
	}

	public Class<?> getQualifier() {
		return this.qualifier;
	}

	public List<SelectItem> getQualifiers() {
		@SuppressWarnings("unchecked")
		final Set<Class<?>> qualifiers = this.getClazz().adapt(QualifierEntityDescriptor.class).getQualifiers();
		final List<SelectItem> results = new ArrayList<SelectItem>(qualifiers.size() + 1);
		results.add(new SelectItem(null, "None"));
		for (final Class<?> qualifier : qualifiers) {
			results.add(new SelectItem(qualifier.getCanonicalName(), qualifier.getSimpleName()));
		}
		return results;
	}

	public String getQualifierString() {
		if (this.qualifier == null) {
			return null;
		} else {
			return this.qualifier.getCanonicalName();
		}
	}

	/**
	 * Gets the value for the select field.
	 * 
	 * @return The value for the select field.
	 */
	public Object getSelect() {
		return this.select;
	}

	/**
	 * Gets the value for the selected field.
	 * 
	 * @return The value for the selected field.
	 */
	public Collection<Sample01> getSelected() {
		return this.selected;
	}

	public void setInstance(final Object instance) {
		this.instance = instance;
	}

	public void setQualifier(final Class<?> qualifier) {
		this.qualifier = qualifier;
	}

	public void setQualifierString(final String qualifier) {
		try {
			if (qualifier == null) {
				this.qualifier = null;
			} else {
				this.qualifier = Class.forName(qualifier);
			}
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets a new value for the select field.
	 * 
	 * @param select
	 *            The new value for the select field.
	 */
	public void setSelect(final Object select) {
		this.select = select;
		System.out.println(select);
	}
}
