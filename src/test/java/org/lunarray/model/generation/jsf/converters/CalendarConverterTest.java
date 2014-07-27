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

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.myfaces.test.base.junit4.AbstractViewControllerTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the calendar converter.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @see CalendarConverter
 */
public class CalendarConverterTest
		extends AbstractViewControllerTestCase {

	/** The converter. */
	private CalendarConverter converter;

	/** Sets up the test. */
	@Before
	public void setup() {
		this.converter = new CalendarConverter();
		this.application.setMessageBundle("org.lunarray.model.generation.jsf.test.Labels");
	}

	/**
	 * Convert to Calendar.
	 * 
	 * @see Converter#getAsString(FacesContext, UIComponent, Object)
	 */
	@Test
	public void testFrom() {
		final Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH);
		calendar.setTimeInMillis(1000000000000L);
		final String result = this.converter.getAsString(FacesContext.getCurrentInstance(), null, calendar);
		Assert.assertEquals("Sunday, September 9, 2001", result);
	}

	/**
	 * Convert to Calendar.
	 * 
	 * @see Converter#getAsString(FacesContext, UIComponent, Object)
	 */
	@Test(expected = ConverterException.class)
	public void testFromInvalid() {
		this.converter.getAsString(FacesContext.getCurrentInstance(), null, "test");
	}

	/**
	 * Convert to Calendar.
	 * 
	 * @see Converter#getAsObject(FacesContext, UIComponent, String)
	 */
	@Test
	public void testTo() {
		final Object calendarObj = this.converter.getAsObject(FacesContext.getCurrentInstance(), null, "Sunday, September 9, 2001");
		Assert.assertTrue(calendarObj instanceof Calendar);
		final Calendar calendar = (Calendar) calendarObj;
		Assert.assertEquals(999986400000L, calendar.getTimeInMillis());
	}

	/**
	 * Convert to Calendar.
	 * 
	 * @see Converter#getAsObject(FacesContext, UIComponent, String)
	 */
	@Test(expected = ConverterException.class)
	public void testToInvalid() {
		this.converter.getAsObject(FacesContext.getCurrentInstance(), null, "Sunday, September 9");
	}
}
