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

import javax.el.ExpressionFactory;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.myfaces.test.base.junit4.AbstractViewControllerTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunarray.model.descriptor.builder.annotation.presentation.builder.PresQualBuilder;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.resource.simpleresource.SimpleClazzResource;
import org.lunarray.model.generation.jsf.model.Sample01;
import org.lunarray.model.generation.jsf.model.Sample02;
import org.lunarray.model.generation.jsf.model.SampleEnum;

/**
 * Tests the entity name converter.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @see EntityNameConverter
 */
public class EntityNameConverterTest
		extends AbstractViewControllerTestCase {

	/** The converter. */
	private EntityNameConverter converter;
	/** The model. */
	private Model<Object> model;

	/** Setup the test. */
	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws Exception {
		this.model = PresQualBuilder.createBuilder()
				.resources(new SimpleClazzResource<Object>(Sample01.class, Sample02.class, SampleEnum.class)).build();
		this.externalContext.getRequestMap().put("model", this.model);
		this.converter = new EntityNameConverter(SampleEnum.class, ExpressionFactory.newInstance().createValueExpression(
				this.facesContext.getELContext(), "#{model}", Model.class));
	}

	/**
	 * Convert name.
	 * 
	 * @see Converter#getAsString(FacesContext, UIComponent, Object)
	 */
	@Test
	public void testFrom() {
		final String result = this.converter.getAsString(FacesContext.getCurrentInstance(), null, SampleEnum.VALUE_01);
		Assert.assertEquals("some value 01", result);
	}

	/**
	 * Convert name.
	 * 
	 * @see Converter#getAsString(FacesContext, UIComponent, Object)
	 */
	@Test(expected = ConverterException.class)
	public void testFromInvalid() {
		this.converter.getAsString(FacesContext.getCurrentInstance(), null, "test");
	}

	/**
	 * Empty conversion.
	 * 
	 * @see Converter#getAsObject(FacesContext, UIComponent, String)
	 */
	@Test
	public void testTo() {
		Assert.assertNull(this.converter.getAsObject(FacesContext.getCurrentInstance(), null, "SomeValue"));
	}
}
