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

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.html.HtmlInputText;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.myfaces.test.base.junit4.AbstractViewControllerTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lunarray.model.generation.jsf.model.Sample01;

/**
 * Tests the set property action listener.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @see SetPropertyActionListener
 */
public class SetPropertyActionListenerTest
		extends AbstractViewControllerTestCase {

	/** Source entity. */
	private Sample01 entity01;
	/** Target entity. */
	private Sample01 entity02;
	/** The listener. */
	private SetPropertyActionListener listener;

	/** Setup the test. */
	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws Exception {
		this.entity01 = new Sample01();
		this.entity01.setTestValue("test value");
		this.entity02 = new Sample01();
		this.externalContext.getRequestMap().put("entity01", this.entity01);
		this.externalContext.getRequestMap().put("entity02", this.entity02);
		final ValueExpression source = ExpressionFactory.newInstance().createValueExpression(this.facesContext.getELContext(),
				"#{entity01.testValue}", String.class);
		final ValueExpression target = ExpressionFactory.newInstance().createValueExpression(this.facesContext.getELContext(),
				"#{entity02.testValue}", String.class);
		this.listener = new SetPropertyActionListener(source, target);
	}

	/**
	 * Perform the test.
	 * 
	 * @see ActionListener#processAction(ActionEvent)
	 */
	@Test
	public void testPerform() {
		this.listener.processAction(new ActionEvent(new HtmlInputText()));
		Assert.assertEquals(this.entity01.getTestValue(), this.entity02.getTestValue());
	}
}
