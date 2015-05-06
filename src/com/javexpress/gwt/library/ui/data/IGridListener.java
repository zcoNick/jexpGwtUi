package com.javexpress.gwt.library.ui.data;

import com.google.gwt.core.client.JavaScriptObject;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.menu.PopupMenu;

public interface IGridListener {

	public void onGridBeforeDataRequest(JsonMap requestData) throws Exception;

	public boolean onGridBeforeRowSelect(String rowId, JsonMap data) throws Exception;

	public void onGridRowSelect(String rowId, boolean selected, JsonMap data) throws Exception;

	public void onGridRowDoubleClick(String rowId, JsonMap data) throws Exception;

	public void onGridDataLoaded(JavaScriptObject data);

	public boolean onGridContextMenu(PopupMenu menu, String selectedId, JsonMap selectedData);

	boolean hasRowStyler();

	public String getRowStyle(JsonMap rowData);

}