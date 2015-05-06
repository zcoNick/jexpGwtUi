package com.javexpress.gwt.library.ui.data.editgrid;

import com.google.gwt.core.client.JavaScriptObject;
import com.javexpress.gwt.library.ui.js.JsonMap;

public interface IEditLinkColumnListener {

	void cellClicked(final JavaScriptObject linkElement, final String cellValue, final JsonMap rowData);

}