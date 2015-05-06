package com.javexpress.gwt.library.ui.data.editgrid;

import com.google.gwt.core.client.JavaScriptObject;
import com.javexpress.gwt.library.shared.model.IJsonServicePoint;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class EditAutoCompleteColumn extends EditColumn {

	private String						labelField;
	private IEditAutoCompleteListener	listener;

	public void setListener(IEditAutoCompleteListener listener) {
		this.listener = listener;
	}

	public String getLabelField() {
		return labelField;
	}

	public void setLabelField(final String labelField) {
		this.labelField = labelField;
	}

	/** Designer compatible constructor */
	public EditAutoCompleteColumn(final String title, final String valueField, final String labelField) {
		this(title, valueField, labelField, null, null, null);
	}

	public EditAutoCompleteColumn(final String title, final String valueField, final String labelField, String hint, final String width, final IJsonServicePoint listMethod) {
		super(title, hint, valueField, ColumnAlign.left, width, EditorType.autocomplete, null);
		this.labelField = labelField;
		JsonMap options = new JsonMap();
		setOptions(options);
		options.setInt("minLength", 2);
		setListing(listMethod);
	}

	public void setListing(IJsonServicePoint listMethod) {
		if (listMethod != null) {
			String url = JsUtil.getServiceUrl(listMethod);
			getOptions().set("source", url);
			getOptions().set("url", url);
		} else {
			getOptions().clear("source");
			getOptions().clear("url");
		}
	}

	public void setMinLength(int value) {
		getOptions().setInt("minLength", value);
	}

	public void fireAutoCompleteItemSelected(JavaScriptObject rowData, JavaScriptObject selectedItemData) {
		if (listener != null)
			listener.itemSelected(new JsonMap(rowData), selectedItemData != null ? new JsonMap(selectedItemData) : null);
	}

	public void fireAutoCompleteBeforeRequest(JavaScriptObject rowData, JavaScriptObject postData) {
		if (listener != null)
			listener.onAutoCompleteBeforeDataRequest(new JsonMap(rowData),
					new JsonMap(postData));
	}

	public boolean hasListener() {
		return listener != null;
	}

	public boolean fireAutoCompleteItemSelectable(JavaScriptObject rowData, JavaScriptObject selectedItemData) {
		return listener == null || listener.isItemSelectable(new JsonMap(rowData), selectedItemData != null ? new JsonMap(selectedItemData) : null);
	}

}