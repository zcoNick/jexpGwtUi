package com.javexpress.gwt.library.ui.data.editgrid;

import com.javexpress.gwt.library.ui.js.JsonMap;

public interface IEditComboItemListener {

	boolean isItemShowing(JsonMap rowData, String value, String label, String data);

	void itemSelected(JsonMap rowData, String value, String selectedItemData);

}