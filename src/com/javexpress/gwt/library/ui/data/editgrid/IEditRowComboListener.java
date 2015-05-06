package com.javexpress.gwt.library.ui.data.editgrid;

import com.javexpress.gwt.library.ui.js.JsonMap;

public interface IEditRowComboListener {
	
	void itemSelected(JsonMap rowData, String value, JsonMap selectedItemData);

}