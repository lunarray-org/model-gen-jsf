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

import java.util.List;

import javax.faces.model.DataModel;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.dictionary.PaginatedDictionary;
import org.lunarray.model.descriptor.dictionary.exceptions.DictionaryException;
import org.lunarray.model.descriptor.model.entity.EntityDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data model for a paginated dictionary.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity entity.
 */
public final class PaginatedDictionaryDataTableModel<E>
		extends DataModel<E> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PaginatedDictionaryDataTableModel.class);
	/** The dictionary. */
	private transient PaginatedDictionary dictionary;
	/** The entity. */
	private transient EntityDescriptor<E> entity;
	/** The row. */
	private transient int row;

	/**
	 * Default constructor.
	 */
	public PaginatedDictionaryDataTableModel() {
		super();
		this.row = -1;
	}

	/**
	 * Gets the value for the dictionary field.
	 * 
	 * @return The value for the dictionary field.
	 */
	public PaginatedDictionary getDictionary() {
		Validate.notNull(this.dictionary, "Dictionary may not be null.");
		return this.dictionary;
	}

	/**
	 * Gets the value for the entity field.
	 * 
	 * @return The value for the entity field.
	 */
	public EntityDescriptor<E> getEntity() {
		return this.entity;
	}

	/**
	 * Gets the value for the row field.
	 * 
	 * @return The value for the row field.
	 */
	public int getRow() {
		return this.row;
	}

	/** {@inheritDoc} */
	@Override
	public int getRowCount() {
		int result = -1;
		try {
			result = this.getDictionary().lookupTotals(this.entity);
		} catch (final DictionaryException e) {
			PaginatedDictionaryDataTableModel.LOGGER.warn("Could not count entities.", e);
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public E getRowData() {
		E result = null;
		try {
			final List<E> results = this.getDictionary().lookupPaginated(this.entity, this.row, 1);
			if (!results.isEmpty()) {
				result = results.iterator().next();
			}
		} catch (final DictionaryException e) {
			PaginatedDictionaryDataTableModel.LOGGER.warn("Could not get entities.", e);
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public int getRowIndex() {
		return this.row;
	}

	/** {@inheritDoc} */
	@Override
	public Object getWrappedData() {
		return this.dictionary;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isRowAvailable() {
		boolean result = false;
		try {
			final List<E> results = this.getDictionary().lookupPaginated(this.entity, this.row, 1);
			result ^= results.isEmpty();
		} catch (final DictionaryException e) {
			PaginatedDictionaryDataTableModel.LOGGER.warn("Could not get entity.", e);
		}
		return result;
	}

	/**
	 * Sets a new value for the dictionary field.
	 * 
	 * @param dictionary
	 *            The new value for the dictionary field.
	 */
	public void setDictionary(final PaginatedDictionary dictionary) {
		this.dictionary = dictionary;
	}

	/**
	 * Sets a new value for the entity field.
	 * 
	 * @param entity
	 *            The new value for the entity field.
	 */
	public void setEntity(final EntityDescriptor<E> entity) {
		this.entity = entity;
	}

	/**
	 * Sets a new value for the row field.
	 * 
	 * @param row
	 *            The new value for the row field.
	 */
	public void setRow(final int row) {
		this.row = row;
	}

	/** {@inheritDoc} */
	@Override
	public void setRowIndex(final int row) {
		this.row = row;
	}

	/** {@inheritDoc} */
	@Override
	public void setWrappedData(final Object data) {
		if (data instanceof PaginatedDictionary) {
			this.dictionary = (PaginatedDictionary) data;
		} else {
			throw new IllegalArgumentException(String.format("Invalid type, expected PaginatedDictionary, got '%s'.", data));
		}
	}
}
