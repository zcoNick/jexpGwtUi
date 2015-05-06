package com.javexpress.gwt.library.ui.data;

public class DateColumn extends ListColumn {

	public DateColumn(final String title, final String field, final boolean sortable) {
		this(title, field, null, sortable);
	}

	public DateColumn(final String title, final String field, final String width, final boolean sortable) {
		super(title, field, width, false, Formatter.date, null);
		if (width == null)
			setWidth("75");
	}

}