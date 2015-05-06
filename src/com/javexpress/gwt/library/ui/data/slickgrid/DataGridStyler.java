package com.javexpress.gwt.library.ui.data.slickgrid;

import com.javexpress.gwt.library.ui.js.JsonMap;

public abstract class DataGridStyler {
	
	/**
	 * @param row row index
	 * @param data row's data
	 * @param dirty
	 * @param styleData styleData.cssClasses is the classes of the styles to apply/remove
	 */
	public void prepareRowStyle(int row, JsonMap data, boolean dirty, JsonMap styleData){
	}

}