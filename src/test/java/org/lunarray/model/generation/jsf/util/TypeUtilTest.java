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

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the type util.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @see TypeUtil
 */
public class TypeUtilTest {

	/**
	 * Test number types.
	 * 
	 * @see TypeUtil#isNumberType(Class)
	 */
	@Test
	public void testBigIntegerType() {
		Assert.assertTrue(TypeUtil.isNumberType(BigInteger.class));
	}

	/**
	 * Test date types.
	 * 
	 * @see TypeUtil#isDateType(Class)
	 */
	@Test
	public void testCalendarType() {
		Assert.assertTrue(TypeUtil.isDateType(Calendar.class));
	}

	/**
	 * Test date types.
	 * 
	 * @see TypeUtil#isDateType(Class)
	 */
	@Test
	public void testDateType() {
		Assert.assertTrue(TypeUtil.isDateType(Date.class));
	}

	/**
	 * Test number types.
	 * 
	 * @see TypeUtil#isNumberType(Class)
	 */
	@Test
	public void testDoubleType() {
		Assert.assertTrue(TypeUtil.isNumberType(Double.class));
	}

	/**
	 * Test number types.
	 * 
	 * @see TypeUtil#isNumberType(Class)
	 */
	@Test
	public void testIntegerType() {
		Assert.assertTrue(TypeUtil.isNumberType(Integer.class));
	}

	/**
	 * Test number types.
	 * 
	 * @see TypeUtil#isNumberType(Class)
	 */
	@Test
	public void testLongType() {
		Assert.assertTrue(TypeUtil.isNumberType(Long.class));
	}

	/**
	 * Test date types.
	 * 
	 * @see TypeUtil#isDateType(Class)
	 */
	@Test
	public void testSqlDateType() {
		Assert.assertTrue(TypeUtil.isDateType(java.sql.Date.class));
	}

	/**
	 * Test date types.
	 * 
	 * @see TypeUtil#isDateType(Class)
	 */
	@Test
	public void testStringInvalidDateType() {
		Assert.assertTrue(!TypeUtil.isDateType(String.class));
	}

	/**
	 * Test number types.
	 * 
	 * @see TypeUtil#isNumberType(Class)
	 */
	@Test
	public void testStringInvalidNumberType() {
		Assert.assertTrue(!TypeUtil.isNumberType(String.class));
	}
}
