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
package org.lunarray.model.generation.jsf.render.factories.form.jsf.components;

import javax.faces.component.UIInput;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationPropertyDescriptor;
import org.lunarray.model.generation.jsf.components.shared.format.HtmlFormat;
import org.lunarray.model.generation.jsf.render.RenderContext;
import org.lunarray.model.generation.jsf.util.EntityNameConverter;

/**
 * An abstract descriptor strategy.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public abstract class AbstractPropertyRenderStrategy<P, E>
		extends AbstractRenderStrategy<P, E> {

	/** {@inheritDoc} */
	@Override
	public final void build(final PropertyDescriptor<P, E> property, final RenderContext context) {
		Validate.notNull(property, "Descriptor may not be null.");
		Validate.notNull(context, "Render context may not be null.");
		UIInput input;
		this.buildLabel(property, context);
		this.buildMessages(property, context);
		input = this.createInput(property, context);
		this.addValue(input, property, context);
		input.setId(this.createInputName(context));
		final PresentationPropertyDescriptor<?, ?> presentationProperty = property.adapt(PresentationPropertyDescriptor.class);
		if (property.isRelation()) {
			property.adapt(RelationDescriptor.class);
			input.setConverter(new EntityNameConverter(property.getPropertyType(), context.getModelExpression()));
		} else {
			String format = null;
			if (!CheckUtil.isNull(presentationProperty)) {
				format = presentationProperty.getFormat();
			}
			if (!CheckUtil.isNull(format)) {
				final HtmlFormat htmlFormat = new HtmlFormat();
				htmlFormat.setFormat(format);
				input.getFacets().put("format", htmlFormat);
			}
		}
		this.setInput(input);
	}
}
