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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.Validate;

/**
 * A jsf message utility.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public enum MessageUtil {
	/** The instance. */
	INSTANCE;

	/**
	 * Formats a message.
	 * 
	 * @param ctx
	 *            The jsf context. May not be null.
	 * @param key
	 *            The message key. May not be null.
	 * @param args
	 *            The message arguments.
	 * @return The message.
	 */
	public static String formatMessage(final FacesContext ctx, final String key, final Object... args) {
		Validate.notNull(ctx, "Faces context may not be null.");
		Validate.notNull(key, "Key may not be null.");
		final Application app = ctx.getApplication();
		final String messageBundle = app.getMessageBundle();
		final ResourceBundle resourceBundle = app.getResourceBundle(ctx, messageBundle);
		final String message = resourceBundle.getString(key);
		final Locale locale = ctx.getViewRoot().getLocale();
		return new MessageFormat(message, locale).format(args);
	}
}
