package com.javexpress.gwt.library.ui.data.editgrid;

import com.javexpress.gwt.library.ui.js.JsonMap;

public interface IEditAutoCompleteListener {

	public void onAutoCompleteBeforeDataRequest(JsonMap rowData, JsonMap postData);

	void itemSelected(JsonMap rowData, JsonMap selectedItemData);

	public boolean isItemSelectable(JsonMap rowData, JsonMap selectedItemData);

}