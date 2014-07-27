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
package org.lunarray.model.generation.jsf.render.factories.table.jsf.components;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationPropertyDescriptor;
import org.lunarray.model.generation.jsf.components.shared.format.HtmlFormat;
import org.lunarray.model.generation.jsf.render.JsfStyleClasses;
import org.lunarray.model.generation.jsf.render.RenderContext;
import org.lunarray.model.generation.jsf.render.factories.table.TablePropertyDescriptorRenderStrategy;
import org.lunarray.model.generation.jsf.util.EntityNameConverter;
import org.lunarray.model.generation.jsf.util.VariableUtil;

/**
 * Default text output.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public final class DefaultCheckboxOutput<P, E>
		extends AbstractOutput<P, E> {

	/** The expression factory. */
	private final transient ExpressionFactory expressionFactory = ExpressionFactory.newInstance();

	/**
	 * Constructs the text output.
	 * 
	 * @param modelExpression
	 *            The model expression. May not be null.
	 */
	protected DefaultCheckboxOutput(final ValueExpression modelExpression) {
		super(modelExpression);
	}

	/** {@inheritDoc} */
	@Override
	protected UIOutput getOutput(final PropertyDescriptor<?, ?> property, final RenderContext jsfContext) {
		Validate.notNull(property, "Property may not be null.");
		Validate.notNull(jsfContext, "Render context may not be null.");
		HtmlSelectBooleanCheckbox output;
		output = new HtmlSelectBooleanCheckbox();
		output.setStyleClass(jsfContext.resolveStyleClass(JsfStyleClasses.TABLE_OUTPUT_CHECKBOX));
		output.setReadonly(true);
		output.setValueExpression(
				"value",
				this.expressionFactory.createValueExpression(jsfContext.getFacesContext().getELContext(),
						VariableUtil.compileVariable(jsfContext.getVariable(), jsfContext.getPrefixDeque()), Object.class));
		final PresentationPropertyDescriptor<?, ?> presentationProperty = property.adapt(PresentationPropertyDescriptor.class);
		String format = null;
		if (!CheckUtil.isNull(presentationProperty)) {
			format = presentationProperty.getFormat();
		}
		if (!CheckUtil.isNull(format)) {
			final HtmlFormat htmlFormat = new HtmlFormat();
			htmlFormat.setFormat(format);
			output.getFacets().put("format", htmlFormat);
		}
		if (property.isRelation()) {
			output.setConverter(new EntityNameConverter(property.getPropertyType(), this.getModelExpression()));
		}
		return output;
	}

	/**
	 * A factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class FactoryImpl
			implements Factory {

		/**
		 * Default constructor.
		 */
		public FactoryImpl() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public <P, E> TablePropertyDescriptorRenderStrategy<P, E> createStrategy(final RenderContext context) {
			Validate.notNull(context, "Context may not be null.");
			return new DefaultCheckboxOutput<P, E>(context.getModelExpression());
		}
	}
}
