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
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.myfaces.test.base.junit4.AbstractViewControllerTestCase;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunarray.model.descriptor.builder.annotation.presentation.builder.PresQualBuilder;
import org.lunarray.model.descriptor.dictionary.Dictionary;
import org.lunarray.model.descriptor.dictionary.enumeration.EnumDictionary;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.KeyedEntityDescriptor;
import org.lunarray.model.descriptor.resource.simpleresource.SimpleClazzResource;
import org.lunarray.model.generation.jsf.model.Sample01;
import org.lunarray.model.generation.jsf.model.Sample02;
import org.lunarray.model.generation.jsf.model.Sample03;
import org.lunarray.model.generation.jsf.model.SampleEnum;

/**
 * Tests the dictionary converter.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @see DictionaryConverter
 */
public class DictionaryConverterTest
		extends AbstractViewControllerTestCase {

	/** The converter. */
	private DictionaryConverter converterEnum;
	/** The converter. */
	private DictionaryConverter converterSample01;
	/** The converter. */
	private DictionaryConverter converterSample03;
	/** The entity descriptor. */
	private KeyedEntityDescriptor<Sample03, Integer> descriptor03;
	/** The sample dictionary. */
	private Dictionary dictionary;
	/** The model. */
	private Model<Object> model;

	/** Setup the test. */
	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws Exception {
		this.dictionary = EasyMock.createMock(Dictionary.class);
		this.model = PresQualBuilder.createBuilder()
				.resources(new SimpleClazzResource<Object>(Sample01.class, Sample02.class, Sample03.class, SampleEnum.class))
				.extensions(new EnumDictionary(this.dictionary)).build();
		this.descriptor03 = this.model.getEntity(Sample03.class).adapt(KeyedEntityDescriptor.class);
		this.externalContext.getRequestMap().put("model", this.model);
		this.converterEnum = new DictionaryConverter(SampleEnum.class, ExpressionFactory.newInstance().createValueExpression(
				this.facesContext.getELContext(), "#{model}", Model.class));
		this.converterSample01 = new DictionaryConverter(Sample01.class, ExpressionFactory.newInstance().createValueExpression(
				this.facesContext.getELContext(), "#{model}", Model.class));
		this.converterSample03 = new DictionaryConverter(Sample03.class, ExpressionFactory.newInstance().createValueExpression(
				this.facesContext.getELContext(), "#{model}", Model.class));
	}

	/**
	 * Convert enum.
	 * 
	 * @see Converter#getAsString(FacesContext, UIComponent, Object)
	 */
	@Test
	public void testFromEnum() {
		final String result = this.converterEnum.getAsString(FacesContext.getCurrentInstance(), null, SampleEnum.VALUE_01);
		Assert.assertEquals("VALUE_01", result);
	}

	/**
	 * Convert enum.
	 * 
	 * @see Converter#getAsString(FacesContext, UIComponent, Object)
	 */
	@Test(expected = ConverterException.class)
	public void testFromInvalid() {
		this.converterEnum.getAsString(FacesContext.getCurrentInstance(), null, "test");
	}

	/**
	 * Convert enum.
	 * 
	 * @see Converter#getAsObject(FacesContext, UIComponent, String)
	 */
	@Test
	public void testFromNull() {
		Assert.assertEquals("", this.converterEnum.getAsString(FacesContext.getCurrentInstance(), null, null));
	}

	/**
	 * Convert sample.
	 * 
	 * @see Converter#getAsString(FacesContext, UIComponent, Object)
	 */
	@Test
	public void testFromSample() {
		final String result = this.converterSample01.getAsString(FacesContext.getCurrentInstance(), null, new Sample01());
		Assert.assertEquals("", result);
	}

	/**
	 * Convert enum.
	 * 
	 * @see Converter#getAsObject(FacesContext, UIComponent, String)
	 */
	@Test
	public void testToEnum() {
		final Object resultObj = this.converterEnum.getAsObject(FacesContext.getCurrentInstance(), null, "VALUE_01");
		Assert.assertTrue(resultObj instanceof SampleEnum);
		final SampleEnum result = (SampleEnum) resultObj;
		Assert.assertTrue(result == SampleEnum.VALUE_01);
	}

	/**
	 * Convert enum.
	 * 
	 * @see Converter#getAsObject(FacesContext, UIComponent, String)
	 */
	@Test
	public void testToInvalid() {
		Assert.assertNull(this.converterEnum.getAsObject(FacesContext.getCurrentInstance(), null, "SomeValue"));
	}

	/**
	 * Convert enum.
	 * 
	 * @see Converter#getAsObject(FacesContext, UIComponent, String)
	 */
	@Test
	public void testToNull() {
		Assert.assertNull(this.converterEnum.getAsObject(FacesContext.getCurrentInstance(), null, null));
	}

	/**
	 * Empty conversion.
	 * 
	 * @see Converter#getAsObject(FacesContext, UIComponent, String)
	 */
	@Test
	public void testToSample() {
		Assert.assertNull(this.converterSample01.getAsObject(FacesContext.getCurrentInstance(), null, "VALUE_01"));
	}

	/**
	 * Empty conversion.
	 * 
	 * @see Converter#getAsObject(FacesContext, UIComponent, String)
	 */
	@Test
	public void testToSampleIntKeyed() throws Exception {
		EasyMock.reset(this.dictionary);
		EasyMock.expect(this.dictionary.lookup(this.descriptor03, Integer.valueOf(1))).andReturn(Sample03.SAMPLE_01);
		EasyMock.replay(this.dictionary);
		final UIComponent component = new HtmlInputText();
		final Object resultObj = this.converterSample03.getAsObject(FacesContext.getCurrentInstance(), component, "1");
		Assert.assertTrue(resultObj instanceof Sample03);
		final Sample03 result = (Sample03) resultObj;
		Assert.assertTrue(result == Sample03.SAMPLE_01);
		EasyMock.verify(this.dictionary);
	}
}
