package com.javexpress.gwt.library.ui.data;

public class BoolColumn extends ListColumn {

	/**
	 * Designer compatible constructor
	 */
	public BoolColumn(final String title, final String field, final boolean sortable) {
		this(title, field, "25", sortable);
	}

	public BoolColumn(final String title, final String field, final String width, final boolean sortable) {
		super(title, field, ColumnAlign.center, width, sortable, Formatter.bool);
	}

}