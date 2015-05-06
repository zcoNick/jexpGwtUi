package com.javexpress.gwt.library.ui.data.editgrid;

public class EditCheckColumn extends EditColumn {

	/** Designer compatible constructor */
	public EditCheckColumn(final String title, final String field) {
		this(title, field, "10");
	}
	
	public EditCheckColumn(final String title, final String field, String width) {
		super(title, null, field, null, width, EditorType.check, null);
	}
	
}