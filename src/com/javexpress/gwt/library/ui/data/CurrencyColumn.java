package com.javexpress.gwt.library.ui.data;

public class CurrencyColumn extends DecimalColumn {

	/** Designer compatible constructor */
	public CurrencyColumn(String title, String field, boolean sortable, int decimalPlaces) {
		super(title, field, sortable, decimalPlaces);
		setFormatter(Formatter.currency);
	}

	public CurrencyColumn(String title, String field, boolean sortable) {
		super(title, field, sortable);
		setFormatter(Formatter.currency);
	}

	public CurrencyColumn(String title, String field, String width, boolean sortable, int decimalPlaces) {
		super(title, field, width, sortable, decimalPlaces);
		setFormatter(Formatter.currency);
	}

	public CurrencyColumn(String title, String field, String width, boolean sortable) {
		super(title, field, width, sortable);
		setFormatter(Formatter.currency);
	}

}