package com.javexpress.gwt.library.ui.data.editgrid;

import com.google.gwt.core.client.JavaScriptObject;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class EditRowComboColumn extends EditColumn {

	private String					rowDataField;
	private String					labelField;
	private IEditRowComboListener	listener;

	public IEditRowComboListener getListener() {
		return listener;
	}

	public void setListener(IEditRowComboListener listener) {
		this.listener = listener;
	}

	public String getRowDataField() {
		return rowDataField;
	}

	public void setRowDataField(String rowDataField) {
		this.rowDataField = rowDataField;
	}

	/** Designer compatible constructor */
	public EditRowComboColumn(final String title, final String valueField) {
		this(title, null, valueField, null);
	}

	public EditRowComboColumn(final String title, final String valueField, final String width) {
		this(title, null, valueField, width);
	}

	public EditRowComboColumn(final String title, final String hint, final String valueField, final String width) {
		super(title, hint, valueField, ColumnAlign.left, width, EditorType.rowcombo, null);
	}

	public void fireComboItemSelected(JavaScriptObject rowData, String value, JavaScriptObject selectedItemData) {
		if (listener != null)
			listener.itemSelected(new JsonMap(rowData), value, selectedItemData == null ? null : new JsonMap(selectedItemData));
	}

	public boolean hasListener() {
		return listener != null;
	}

	public String getLabelField() {
		return labelField;
	}

	public void setLabelField(String labelField) {
		this.labelField = labelField;
	}

}