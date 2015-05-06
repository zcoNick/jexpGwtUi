package com.javexpress.gwt.library.ui.data;

public class CurrencyColumn extends DecimalColumn {

	/** Designer compatible constructor */
	public CurrencyColumn(String title, String field, boolean sortable, int decimalPlaces) {
		super(title, field, sortable, decimalPlaces);
	}

	public CurrencyColumn(String title, String field, boolean sortable) {
		super(title, field, sortable);
	}

	public CurrencyColumn(String title, String field, String width, boolean sortable, int decimalPlaces) {
		super(title, field, width, sortable, decimalPlaces);
	}

	public CurrencyColumn(String title, String field, String width, boolean sortable) {
		super(title, field, width, sortable);
	}

}