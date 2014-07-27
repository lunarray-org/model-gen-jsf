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
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunarray.model.descriptor.builder.annotation.presentation.builder.PresQualBuilder;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.resource.simpleresource.SimpleClazzResource;
import org.lunarray.model.generation.jsf.model.Sample01;
import org.lunarray.model.generation.jsf.model.Sample02;
import org.lunarray.model.generation.jsf.model.SampleEnum;

/**
 * Tests the variable util.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @see VariableUtil
 */
public class VariableUtilTest {
	/** The model. */
	private Model<Object> model;
	/** Sample 01 property. */
	private PropertyDescriptor<String, Sample01> propertyDescriptor01;
	/** Sample 01 inline property. */
	private PropertyDescriptor<Sample02, Sample01> propertyDescriptorEmbedded01;
	/** Sample 02 inline property. */
	private PropertyDescriptor<String, Sample02> propertyDescriptorEmbedded02;
	/** Sample 01 descriptor. */
	private EntityDescriptor<Sample01> sampleDescriptor01;
	/** Sample 02 descriptor. */
	private EntityDescriptor<Sample02> sampleDescriptor02;

	/** Setup the test. */
	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws Exception {
		this.model = PresQualBuilder.createBuilder()
				.resources(new SimpleClazzResource<Object>(Sample01.class, Sample02.class, SampleEnum.class)).build();
		this.sampleDescriptor01 = this.model.getEntity(Sample01.class);
		this.sampleDescriptor02 = this.model.getEntity(Sample02.class);
		this.propertyDescriptor01 = this.sampleDescriptor01.getProperty("inlineValue.testValue", String.class);
		this.propertyDescriptorEmbedded01 = this.sampleDescriptor01.getProperty("inlineValue2", Sample02.class);
		this.propertyDescriptorEmbedded02 = this.sampleDescriptor02.getProperty("testValue", String.class);
	}

	/**
	 * Test name composition.
	 * 
	 * @see VariableUtil#compileName(String, Deque)
	 */
	@Test
	public void testComposeName() {
		final Deque<PropertyDescriptor<?, ?>> deque = new LinkedList<PropertyDescriptor<?, ?>>();
		deque.add(this.propertyDescriptor01);
		final String result = VariableUtil.compileName("test:", deque);
		Assert.assertEquals("test:inlineValue_testValue", result);
	}

	/**
	 * Test variable composition.
	 * 
	 * @see VariableUtil#compileVariable(String, Deque)
	 */
	@Test
	public void testComposeVariable() {
		final Deque<PropertyDescriptor<?, ?>> deque = new LinkedList<PropertyDescriptor<?, ?>>();
		deque.add(this.propertyDescriptor01);
		final String result = VariableUtil.compileVariable("var", deque);
		Assert.assertEquals("#{var.inlineValue.testValue}", result);
	}

	/**
	 * Test variable composition.
	 * 
	 * @see VariableUtil#compileVariableName(String, Deque, String)
	 */
	@Test
	public void testComposeVariableName() {
		final Deque<PropertyDescriptor<?, ?>> deque = new LinkedList<PropertyDescriptor<?, ?>>();
		deque.add(this.propertyDescriptor01);
		final String result = VariableUtil.compileVariableName("test:", deque, "var");
		Assert.assertEquals("test:inlineValue_testValue_var", result);
	}

	/**
	 * Test variable composition.
	 * 
	 * @see VariableUtil#compileVariableNameStrings(String, Deque, String)
	 */
	@Test
	public void testComposeVariableNameStrings() {
		final Deque<String> deque = new LinkedList<String>();
		deque.add(this.propertyDescriptor01.getName());
		final String result = VariableUtil.compileVariableNameStrings("test:", deque, "var");
		Assert.assertEquals("test:inlineValue_testValue_var", result);
	}

	/**
	 * Test name composition.
	 * 
	 * @see VariableUtil#compileName(String, Deque)
	 */
	@Test
	public void testEmbeddedComposeName() {
		final Deque<PropertyDescriptor<?, ?>> deque = new LinkedList<PropertyDescriptor<?, ?>>();
		deque.add(this.propertyDescriptorEmbedded01);
		deque.add(this.propertyDescriptorEmbedded02);
		final String result = VariableUtil.compileName("test:", deque);
		Assert.assertEquals("test:inlineValue2_testValue", result);
	}

	/**
	 * Test variable composition.
	 * 
	 * @see VariableUtil#compileVariable(String, Deque)
	 */
	@Test
	public void testEmbeddedComposeVariable() {
		final Deque<PropertyDescriptor<?, ?>> deque = new LinkedList<PropertyDescriptor<?, ?>>();
		deque.add(this.propertyDescriptorEmbedded01);
		deque.add(this.propertyDescriptorEmbedded02);
		final String result = VariableUtil.compileVariable("var", deque);
		Assert.assertEquals("#{var.inlineValue2.testValue}", result);
	}

	/**
	 * Test variable composition.
	 * 
	 * @see VariableUtil#compileVariableName(String, Deque, String)
	 */
	@Test
	public void testEmbeddedComposeVariableName() {
		final Deque<PropertyDescriptor<?, ?>> deque = new LinkedList<PropertyDescriptor<?, ?>>();
		deque.add(this.propertyDescriptorEmbedded01);
		deque.add(this.propertyDescriptorEmbedded02);
		final String result = VariableUtil.compileVariableName("test:", deque, "var");
		Assert.assertEquals("test:inlineValue2_testValue_var", result);
	}

	/**
	 * Test variable composition.
	 * 
	 * @see VariableUtil#compileVariableNameStrings(String, Deque, String)
	 */
	@Test
	public void testEmbeddedComposeVariableNameStrings() {
		final Deque<String> deque = new LinkedList<String>();
		deque.add(this.propertyDescriptorEmbedded01.getName());
		deque.add(this.propertyDescriptorEmbedded02.getName());
		final String result = VariableUtil.compileVariableNameStrings("test:", deque, "var");
		Assert.assertEquals("test:inlineValue2_testValue_var", result);
	}

	/**
	 * Test name composition.
	 * 
	 * @see VariableUtil#compileName(String, Deque)
	 */
	@Test
	public void testEmptyComposeName() {
		final Deque<PropertyDescriptor<?, ?>> deque = new LinkedList<PropertyDescriptor<?, ?>>();
		final String result = VariableUtil.compileName("test:", deque);
		Assert.assertEquals("test:", result);
	}

	/**
	 * Test variable composition.
	 * 
	 * @see VariableUtil#compileVariable(String, Deque)
	 */
	@Test
	public void testEmptyComposeVariable() {
		final Deque<PropertyDescriptor<?, ?>> deque = new LinkedList<PropertyDescriptor<?, ?>>();
		final String result = VariableUtil.compileVariable("var", deque);
		Assert.assertEquals("#{var}", result);
	}

	/**
	 * Test variable composition.
	 * 
	 * @see VariableUtil#compileVariableName(String, Deque, String)
	 */
	@Test
	public void testEmptyComposeVariableName() {
		final Deque<PropertyDescriptor<?, ?>> deque = new LinkedList<PropertyDescriptor<?, ?>>();
		final String result = VariableUtil.compileVariableName("test:", deque, null);
		Assert.assertEquals("test:", result);
	}

	/**
	 * Test variable composition.
	 * 
	 * @see VariableUtil#compileVariableNameStrings(String, Deque, String)
	 */
	@Test
	public void testEmptyComposeVariableNameStrings() {
		final Deque<String> deque = new LinkedList<String>();
		final String result = VariableUtil.compileVariableNameStrings("test:", deque, null);
		Assert.assertEquals("test:", result);
	}
}
