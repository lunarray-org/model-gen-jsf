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
package org.lunarray.model.generation.jsf.listeners;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Locale;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.html.HtmlInputText;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;

import org.apache.myfaces.test.base.junit4.AbstractViewControllerTestCase;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.lunarray.model.descriptor.builder.annotation.presentation.builder.PresQualBuilder;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.resource.simpleresource.SimpleClazzResource;
import org.lunarray.model.descriptor.validator.EntityValidator;
import org.lunarray.model.descriptor.validator.PropertyViolation;
import org.lunarray.model.generation.jsf.model.Sample01;
import org.lunarray.model.generation.jsf.model.Sample02;

/**
 * Tests the set property action listener.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @see DefaultValidationListener
 */
public class DefaultValidationListenerTest
		extends AbstractViewControllerTestCase {

	/** The entity. */
	private Sample02 entity;
	/** The listener. */
	private DefaultValidationListener listener;
	/** The model. */
	private Model<Object> model;
	/** The validator. */
	private EntityValidator validator;

	/** Setup the test. */
	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws Exception {
		this.validator = EasyMock.createMock(EntityValidator.class);
		this.model = PresQualBuilder.createBuilder().resources(new SimpleClazzResource<Object>(Sample01.class, Sample02.class))
				.extensions(this.validator).build();
		this.entity = new Sample02();
		this.externalContext.getRequestMap().put("model", this.model);
		this.externalContext.getRequestMap().put("entity", this.entity);
		final ELContext ctx = this.facesContext.getELContext();
		final ExpressionFactory factory = ExpressionFactory.newInstance();
		final ValueExpression modelExp = factory.createValueExpression(ctx, "#{model}", Model.class);
		final ValueExpression entityExp = factory.createValueExpression(ctx, "#{entity}", Sample02.class);
		final Deque<PropertyDescriptor<?, ?>> depth = new LinkedList<PropertyDescriptor<?, ?>>();
		this.listener = new DefaultValidationListener("test", this.model.getEntity(Sample02.class).getName(), modelExp, entityExp, depth);
		this.listener.setFacesContext(this.facesContext);
	}

	/**
	 * Test validation.
	 * 
	 * @see DefaultValidationListener#processAction(ActionEvent)
	 */
	@Test
	public void testValidation() {
		EasyMock.reset(this.validator);
		final Collection<PropertyViolation<Sample02, ?>> violations = new LinkedList<PropertyViolation<Sample02, ?>>();
		EasyMock.expect(this.validator.validate(this.model.getEntity(Sample02.class), this.entity, Locale.getDefault())).andReturn(
				violations);
		EasyMock.replay(this.validator);
		this.listener.processAction(new ActionEvent(new HtmlInputText()));
		EasyMock.verify(this.validator);
	}

	/**
	 * Test validation.
	 * 
	 * @see DefaultValidationListener#processAction(ActionEvent)
	 */
	@Test(expected = AbortProcessingException.class)
	public void testValidationErrors() {
		@SuppressWarnings("unchecked")
		final PropertyViolation<Sample02, String> violation = EasyMock.createMock(PropertyViolation.class);
		EasyMock.reset(this.validator, violation);
		final Collection<PropertyViolation<Sample02, ?>> violations = new LinkedList<PropertyViolation<Sample02, ?>>();
		violations.add(violation);
		final EntityDescriptor<Sample02> descriptor = this.model.getEntity(Sample02.class);
		EasyMock.expect(this.validator.validate(descriptor, this.entity, Locale.getDefault())).andReturn(violations);
		EasyMock.expect(violation.getMessage()).andReturn("invalid");
		EasyMock.expect(violation.getProperty()).andReturn(descriptor.getProperty("testValue", String.class));
		EasyMock.replay(this.validator, violation);
		try {
			this.listener.processAction(new ActionEvent(new HtmlInputText()));
		} finally {
			EasyMock.verify(this.validator, violation);
		}
	}
}
