package com.javexpress.gwt.library.ui.form.filterpanel;

import java.io.Serializable;

public interface IFilterPanelListener {
	
	Serializable getHiddenFilterValue(String field);

	void applyFilters();
	
}