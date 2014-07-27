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

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.lunarray.model.generation.jsf.render.factories.datatable.DataTableBuilder;
import org.lunarray.model.generation.jsf.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.jsf.render.factories.form.jsf.components.DefaultMultipleCheckboxInput;
import org.lunarray.model.generation.jsf.render.factories.form.jsf.components.DefaultMultipleListInput;
import org.lunarray.model.generation.jsf.render.factories.form.jsf.components.DefaultMultipleMenuInput;
import org.lunarray.model.generation.jsf.render.factories.form.jsf.components.DefaultRadioInput;
import org.lunarray.model.generation.jsf.render.factories.form.jsf.components.DefaultSingleCheckboxInput;
import org.lunarray.model.generation.jsf.render.factories.form.jsf.components.DefaultSingleListInput;
import org.lunarray.model.generation.jsf.render.factories.form.jsf.components.DefaultSingleMenuInput;
import org.lunarray.model.generation.jsf.render.factories.form.jsf.components.DefaultTextInput;
import org.lunarray.model.generation.jsf.render.factories.form.jsf.components.DefaultTextareaInput;
import org.lunarray.model.generation.jsf.render.factories.table.TablePropertyDescriptorRenderStrategy;
import org.lunarray.model.generation.jsf.render.factories.table.jsf.components.DefaultCheckboxOutput;
import org.lunarray.model.generation.jsf.render.factories.table.jsf.components.DefaultTextOutput;

/**
 * Tests the render factory util.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @see RenderFactoryUtil
 */
public class RenderFactoryUtilTest {

	/**
	 * Test getting the data table.
	 * 
	 * @see RenderFactoryUtil#getDataTableBuilder()
	 */
	@Test
	public void testGetDataTable() {
		final DataTableBuilder builder = RenderFactoryUtil.getDataTableBuilder();
		Assert.assertNotNull(builder);
	}

	/**
	 * Test getting the form components.
	 * 
	 * @see RenderFactoryUtil#getFormComponents()
	 */
	@Test
	public void testGetFormComponents() {
		final Map<String, FormPropertyRenderStrategy.Factory> result = RenderFactoryUtil.getFormComponents();
		Assert.assertTrue(result.get("collection.default") instanceof DefaultMultipleListInput.FactoryImpl);
		Assert.assertTrue(result.get("collection.CHECKBOX") instanceof DefaultMultipleCheckboxInput.FactoryImpl);
		Assert.assertTrue(result.get("collection.DROPDOWN") instanceof DefaultMultipleMenuInput.FactoryImpl);
		Assert.assertTrue(result.get("collection.PICKLIST") instanceof DefaultMultipleListInput.FactoryImpl);
		Assert.assertTrue(result.get("single.default") instanceof DefaultTextInput.FactoryImpl);
		Assert.assertTrue(result.get("single.TEXT") instanceof DefaultTextInput.FactoryImpl);
		Assert.assertTrue(result.get("single.TEXT_AREA") instanceof DefaultTextareaInput.FactoryImpl);
		Assert.assertTrue(result.get("single.RADIO") instanceof DefaultRadioInput.FactoryImpl);
		Assert.assertTrue(result.get("single.PICKLIST") instanceof DefaultSingleListInput.FactoryImpl);
		Assert.assertTrue(result.get("single.DROPDOWN") instanceof DefaultSingleMenuInput.FactoryImpl);
		Assert.assertTrue(result.get("single.CHECKBOX") instanceof DefaultSingleCheckboxInput.FactoryImpl);
	}

	/**
	 * Test getting the table components.
	 * 
	 * @see RenderFactoryUtil#getTableComponents()
	 */
	@Test
	public void testGetTableComponents() {
		final Map<String, TablePropertyDescriptorRenderStrategy.Factory> result = RenderFactoryUtil.getTableComponents();
		Assert.assertTrue(result.get("collection.default") instanceof DefaultTextOutput.FactoryImpl);
		Assert.assertTrue(result.get("single.default") instanceof DefaultTextOutput.FactoryImpl);
		Assert.assertTrue(result.get("single.CHECKBOX") instanceof DefaultCheckboxOutput.FactoryImpl);
	}

	/**
	 * Test getting the view extensions.
	 * 
	 * @see RenderFactoryUtil#getViewExtensions()
	 */
	@Test
	public void testGetViewExtensions() {
		Assert.assertTrue(RenderFactoryUtil.getViewExtensions().isEmpty());
	}
}
