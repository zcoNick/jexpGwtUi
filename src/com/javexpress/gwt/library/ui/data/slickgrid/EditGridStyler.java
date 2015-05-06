package com.javexpress.gwt.library.ui.data.slickgrid;

import java.util.List;

import com.javexpress.gwt.library.ui.js.JsonMap;

public abstract class EditGridStyler extends DataGridStyler {

	public boolean isRowEditable(int row, JsonMap data) {
		return true;
	}

	public List<String> getDisabledCellNames(int row, JsonMap data, boolean dirty) {
		return null;
	}

}