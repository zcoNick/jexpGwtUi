package com.javexpress.gwt.library.ui.data;

public class NumberColumn extends ListColumn {

	/** Designer compatible constructor */
	public NumberColumn(final String title, final String field, final boolean sortable) {
		this(title, field, "40", sortable);
	}

	public NumberColumn(final String title, final String field, final String width, final boolean sortable) {
		super(title, field, ColumnAlign.right, width, sortable, false, Formatter.number, null);
	}

}