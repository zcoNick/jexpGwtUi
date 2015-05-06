package com.javexpress.gwt.library.ui.data.editgrid;

public class EditTextAreaColumn extends EditColumn {

	/** Designer compatible constructor */
	public EditTextAreaColumn(final String title, final String field) {
		this(title, null, field, null);
	}

	public EditTextAreaColumn(final String title, final String field, final String width) {
		this(title, null, field, width);
	}

	public EditTextAreaColumn(final String title, final String hint, final String field, final String width) {
		super(title, hint, field, ColumnAlign.left, width, EditorType.longtext, null);
	}

}