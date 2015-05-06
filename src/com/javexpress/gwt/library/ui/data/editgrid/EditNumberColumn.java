package com.javexpress.gwt.library.ui.data.editgrid;

import com.javexpress.gwt.library.ui.data.ListColumn.SummaryType;

public class EditNumberColumn extends EditColumn {

	/** Designer compatible constructor */
	public EditNumberColumn(final String title, final String field) {
		this(title, field, null);
	}

	public EditNumberColumn(final String title, final String field, final String width) {
		this(title, null, field, width, null);
	}

	public EditNumberColumn(final String title, final String field, final String width, final SummaryType summaryType) {
		this(title, null, field, width, summaryType);
	}

	public EditNumberColumn(final String title, final String hint, final String field, final String width, final SummaryType summaryType) {
		super(title, hint, field, ColumnAlign.right, width, EditorType.number, summaryType);
	}

}