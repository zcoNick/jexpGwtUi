package com.javexpress.gwt.library.ui.data;

import com.google.gwt.core.client.JavaScriptObject;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.menu.JqPopupMenu;

public class BaseGridListener implements IGridListener {

	@Override
	public void onGridBeforeDataRequest(final JsonMap requestData) throws Exception {
	}
	
	@Override
	public boolean onGridBeforeRowSelect(final String rowId, final JsonMap rowData) throws Exception {
		return true;
	}

	@Override
	public void onGridRowSelect(final String rowId, final boolean selected, final JsonMap rowData) throws Exception {
	}

	@Override
	public void onGridRowDoubleClick(final String rowId, final JsonMap data) throws Exception {
	}

	@Override
	public void onGridDataLoaded(final JavaScriptObject data) {
	}

	@Override
	public boolean onGridContextMenu(final JqPopupMenu menu, final String selectedId, final JsonMap rowData) {
		return false;
	}

	@Override
	public boolean hasRowStyler() {
		return false;
	}

	@Override
	public String getRowStyle(final JsonMap rowData) {
		return null;
	}

}