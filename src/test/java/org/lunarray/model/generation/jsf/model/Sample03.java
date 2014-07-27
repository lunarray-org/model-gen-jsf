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
package org.lunarray.model.generation.jsf.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.lunarray.model.descriptor.model.annotations.Key;
import org.lunarray.model.descriptor.presentation.annotations.PresentationHint;
import org.lunarray.model.descriptor.presentation.annotations.QualifierPresentationHint;
import org.lunarray.model.descriptor.presentation.annotations.QualifierPresentationHints;
import org.lunarray.model.descriptor.util.BooleanInherit;

/**
 * Sample entity.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public class Sample03
		implements Serializable {

	public static final Sample03 SAMPLE_01;
	public static final Sample03 SAMPLE_02;
	public static final Sample03 SAMPLE_03;
	public static final List<Sample03> SAMPLES;
	/** Serial id. */
	private static final long serialVersionUID = -6807595966898520695L;

	@Key
	private int id;

	@QualifierPresentationHints({ @QualifierPresentationHint(name = Qualifier01.class, hint = @PresentationHint(visible = BooleanInherit.FALSE)) })
	private Integer othervalue = 2;

	@NotEmpty
	@PresentationHint(name = BooleanInherit.TRUE)
	private String testValue;

	static {
		SAMPLE_01 = new Sample03();
		Sample03.SAMPLE_01.id = 1;
		Sample03.SAMPLE_01.testValue = "test 1";
		SAMPLE_02 = new Sample03();
		Sample03.SAMPLE_02.id = 2;
		Sample03.SAMPLE_02.testValue = "test 2";
		SAMPLE_03 = new Sample03();
		Sample03.SAMPLE_03.id = 3;
		Sample03.SAMPLE_03.testValue = "test 3";
		SAMPLES = new LinkedList<Sample03>();
		Sample03.SAMPLES.add(Sample03.SAMPLE_01);
		Sample03.SAMPLES.add(Sample03.SAMPLE_02);
		Sample03.SAMPLES.add(Sample03.SAMPLE_03);
	}

	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * Gets the value for the id field.
	 * 
	 * @return The value for the id field.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the value for the othervalue field.
	 * 
	 * @return The value for the othervalue field.
	 */
	public Integer getOthervalue() {
		return this.othervalue;
	}

	/**
	 * Gets the value for the testValue field.
	 * 
	 * @return The value for the testValue field.
	 */
	public String getTestValue() {
		return this.testValue;
	}

	/**
	 * Sets a new value for the id field.
	 * 
	 * @param id
	 *            The new value for the id field.
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * Sets a new value for the othervalue field.
	 * 
	 * @param othervalue
	 *            The new value for the othervalue field.
	 */
	public void setOthervalue(final Integer othervalue) {
		this.othervalue = othervalue;
	}

	/**
	 * Sets a new value for the testValue field.
	 * 
	 * @param testValue
	 *            The new value for the testValue field.
	 */
	public void setTestValue(final String testValue) {
		this.testValue = testValue;
	}
}
