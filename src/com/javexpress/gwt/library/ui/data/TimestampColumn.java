package com.javexpress.gwt.library.ui.data;

public class TimestampColumn extends ListColumn {
	
	public TimestampColumn(final String title, final String field, final boolean sortable) {
		this(title, field, null, sortable);
	}
	
	public TimestampColumn(final String title, final String field, final String width, final boolean sortable) {
		super(title, field, width, false, Formatter.timestamp, null);
		setWidth("105");
	}

}