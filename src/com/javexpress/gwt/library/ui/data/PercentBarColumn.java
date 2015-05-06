package com.javexpress.gwt.library.ui.data;

public class PercentBarColumn extends ListColumn {

	/** Designer compatible constructor */
	public PercentBarColumn(final String title, final String field, final boolean sortable) {
		this(title, field, "30", sortable);
	}

	public PercentBarColumn(final String title, final String field, final String width, final boolean sortable) {
		super(title, field, ColumnAlign.left, width, sortable, false, Formatter.percentBar, null);
	}

}