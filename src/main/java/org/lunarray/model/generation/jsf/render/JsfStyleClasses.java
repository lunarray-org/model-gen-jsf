/* 
 * Model tools.
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
package org.lunarray.model.generation.jsf.render;

/**
 * The style classes for the various components.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public enum JsfStyleClasses implements StyleClasses {

	/** Style classes for a form. */
	FORM("form", "mg_form"),
	/** The control block. */
	FORM_CONTROL_BLOCK("form.control.block", "mg_form_control_block"),
	/** The control block. */
	FORM_CONTROL_ROW("form.control.row", "mg_form_control_row"),
	/** The control block. */
	FORM_CONTROL_TABLE("form.control.table", "mg_form_control_table"),
	/** The form inner panel. */
	FORM_INNER_PANEL("form.panel.inner", "mg_form_inner_panel"),
	/** The form input multi-checkbox. */
	FORM_INPUT_MCHECKBOX("form.input.multi.checkbox", "mg_form_input_mcheckbox"),
	/** The form input multi-listbox. */
	FORM_INPUT_MLISTBOX("form.input.multi.listbox", "mg_form_input_mlistbox"),
	/** The form input multi-menu. */
	FORM_INPUT_MMENU("form.input.multi.menu", "mg_form_input_mmenu"),
	/** The form input radio. */
	FORM_INPUT_RADIO("form.input.radio", "mg_form_input_radio"),
	/** The form input read only text. */
	FORM_INPUT_ROTEXT("form.input.rotext", "mg_form_input_rotext"),
	/** The form input single-checkbox. */
	FORM_INPUT_SCHECKBOX("form.input.single.checkbox", "mg_form_input_scheckbox"),
	/** The form input secret. */
	FORM_INPUT_SECRET("form.input.secret", "mg_form_input_secret"),
	/** The form input single-listbox. */
	FORM_INPUT_SLISTBOX("form.input.single.listbox", "mg_form_input_slistbox"),
	/** The form input single-menu. */
	FORM_INPUT_SMENU("form.input.single.menu", "mg_form_input_smenu"),
	/** The form input text. */
	FORM_INPUT_TEXT("form.input.text", "mg_form_input_text"),
	/** The form input text area. */
	FORM_INPUT_TEXTAREA("form.input.textarea", "mg_form_input_textarea"),
	/** The form label. */
	FORM_LABEL("form.label", "mg_form_label"),
	/** The form block. */
	FORM_LAYOUT_BLOCK("form.layout.block", "mg_form_layout_block"),
	/** The form grid. */
	FORM_LAYOUT_TABLE("form.layout.table", "mg_form_layout_table"),
	/** The form output label. */
	FORM_OUTPUT_LABEL("form.output.label", "mg_form_output_label"),
	/** The form output message. */
	FORM_OUTPUT_MSG("form.output.msg", "mg_form_output_msg"),
	/** The form output message error. */
	FORM_OUTPUT_MSG_ERROR("form.output.msg.error", "mg_form_output_msg_error"),
	/** The form output message info. */
	FORM_OUTPUT_MSG_INFO("form.output.msg.info", "mg_form_output_msg_info"),
	/** The form output message panel. */
	FORM_OUTPUT_MSG_PANEL("form.output.msg.panel", "mg_form_output_msg_panel"),
	/** The form output message warn. */
	FORM_OUTPUT_MSG_WARN("form.output.msg.warn", "mg_form_output_msg_warn"),
	/** The form panel. */
	FORM_PANEL("form.panel", "mg_form_panel"),
	/** Style classes for a table. */
	TABLE("table", "mg_table"),
	/** The table footer. */
	TABLE_FOOTER("table.footer", "mg_table_footer"),
	/** The table form. */
	TABLE_FORM("table.form", "mg_table_form"),
	/** The table header. */
	TABLE_HEADER("table.header", "mg_table_header"),
	/** The table label. */
	TABLE_LABEL("table.label", "mg_table_label"),
	/** The table link. */
	TABLE_LINK("table.link", "mg_table_link"),
	/** The table output. */
	TABLE_OUTPUT("table.output", "mg_table_output"),
	/** The table output checkbox. */
	TABLE_OUTPUT_CHECKBOX("table.output.checkbox", "mg_table_output_checkbox"),
	/** The table output label. */
	TABLE_OUTPUT_LABEL("table.output.label", "mg_table_output_label"),
	/** The table output text. */
	TABLE_OUTPUT_TEXT("table.output.text", "mg_table_output_text"),
	/** The table panel. */
	TABLE_PANEL("table.panel", "mg_table_panel"), ;

	/** The default value. */
	private String defaultValue;
	/** The lookup key. */
	private String key;

	/**
	 * Default constructor.
	 * 
	 * @param key
	 *            The key.
	 * @param defaultValue
	 *            The default value.
	 */
	private JsfStyleClasses(final String key, final String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	/**
	 * Gets the value for the defaultValue field.
	 * 
	 * @return The value for the defaultValue field.
	 */
	@Override
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * Gets the value for the key field.
	 * 
	 * @return The value for the key field.
	 */
	@Override
	public String getKey() {
		return this.key;
	}
}
