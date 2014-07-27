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
package org.lunarray.model.generation.jsf.converters;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.myfaces.test.base.junit4.AbstractViewControllerTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the date converter.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @see DateConverter
 */
public class DateConverterTest
		extends AbstractViewControllerTestCase {

	/** The converter. */
	private DateConverter converter;

	/** Sets up the test. */
	@Before
	public void setup() {
		this.converter = new DateConverter();
		this.application.setMessageBundle("org.lunarray.model.generation.jsf.test.Labels");
	}

	/**
	 * Convert to Date.
	 * 
	 * @see Converter#getAsString(FacesContext, UIComponent, Object)
	 */
	@Test
	public void testFrom() {
		final Date date = new Date();
		date.setTime(1000000000000L);
		final String result = this.converter.getAsString(FacesContext.getCurrentInstance(), null, date);
		Assert.assertEquals("Sunday, September 9, 2001", result);
	}

	/**
	 * Convert to Date.
	 * 
	 * @see Converter#getAsString(FacesContext, UIComponent, Object)
	 */
	@Test(expected = ConverterException.class)
	public void testFromInvalid() {
		this.converter.getAsString(FacesContext.getCurrentInstance(), null, "test");
	}

	/**
	 * Convert to Date.
	 * 
	 * @see Converter#getAsObject(FacesContext, UIComponent, String)
	 */
	@Test
	public void testTo() {
		final Object dateObj = this.converter.getAsObject(FacesContext.getCurrentInstance(), null, "Sunday, September 9, 2001");
		Assert.assertTrue(dateObj instanceof Date);
		final Date date = (Date) dateObj;
		Assert.assertEquals(999986400000L, date.getTime());
	}

	/**
	 * Convert to Date.
	 * 
	 * @see Converter#getAsObject(FacesContext, UIComponent, String)
	 */
	@Test(expected = ConverterException.class)
	public void testToInvalid() {
		this.converter.getAsObject(FacesContext.getCurrentInstance(), null, "Sunday, September 9");
	}
}
