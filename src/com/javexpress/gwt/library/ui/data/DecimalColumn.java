package com.javexpress.gwt.library.ui.data;

public class DecimalColumn extends ListColumn {

	private int	decimalPlaces	= 2;

	public int getDecimalPlaces() {
		return decimalPlaces;
	}

	public DecimalColumn setDecimalPlaces(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
		return this;
	}

	public DecimalColumn(final String title, final String field, final boolean sortable, int decimalPlaces) {
		this(title, field, sortable);
		setDecimalPlaces(decimalPlaces);
		setWidth(String.valueOf(50 + (decimalPlaces * 8)));
	}

	public DecimalColumn(final String title, final String field, final boolean sortable) {
		this(title, field, "50", sortable);
	}

	public DecimalColumn(final String title, final String field, final String width, final boolean sortable, int decimalPlaces) {
		this(title, field, width, sortable);
		setDecimalPlaces(decimalPlaces);
	}

	public DecimalColumn(final String title, final String field, final String width, final boolean sortable) {
		super(title, field, ColumnAlign.right, width, sortable, false, Formatter.currency, null);
	}

}