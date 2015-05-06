package com.javexpress.gwt.library.ui.data.jqgrid;

import com.javexpress.gwt.library.ui.data.ListColumn;

public class ExpandColumn extends ListColumn {

	public ExpandColumn(final String title, final String field, final ColumnAlign align, final String width, final boolean sortable, final boolean hidden, final Formatter formatter, final SummaryType summaryType) {
		super(title, field, align, width, sortable, hidden, formatter, summaryType);
	}

	/** Designer compatible constructor */
	public ExpandColumn(final String title, final String field, final boolean sortable) {
		super(title, field, null, null, sortable, false, null, null);
	}

	public ExpandColumn(final String title, final String field, final boolean sortable, final String width) {
		super(title, field, null, width, sortable, false, null, null);
	}

	public ExpandColumn(String title, String field, ColumnAlign align, String width, boolean sortable) {
		super(title, field, align, width, sortable);
	}

}