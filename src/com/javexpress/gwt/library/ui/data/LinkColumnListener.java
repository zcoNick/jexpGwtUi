package com.javexpress.gwt.library.ui.data;

import com.google.gwt.core.client.JavaScriptObject;
import com.javexpress.gwt.library.ui.js.JsonMap;

public abstract class LinkColumnListener {

	public boolean isRenderCell(final String rowId) {
		return true;
	}

	public abstract void cellClicked(final JavaScriptObject linkElement, final String rowId, final JsonMap rowData);

}