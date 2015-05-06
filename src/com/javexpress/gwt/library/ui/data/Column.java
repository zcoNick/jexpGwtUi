package com.javexpress.gwt.library.ui.data;

public abstract class Column {

	public static enum ColumnAlign {
		left, right, center;
	}

	private String		title;
	private String		field;
	private ColumnAlign	align;
	private String		width;
	private boolean		hidden, frozen;

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getField() {
		return field;
	}

	public void setField(final String field) {
		this.field = field;
	}

	public ColumnAlign getAlign() {
		return align;
	}

	public void setAlign(final ColumnAlign align) {
		this.align = align;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(final String width) {
		this.width = width.replace("px", "");
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(final boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public Column(final String title, final String field, final ColumnAlign align, final String width, final boolean hidden, boolean frozen) {
		super();
		this.title = title;
		this.field = field;
		this.align = align;
		this.width = width;
		this.hidden = hidden;
		this.frozen = frozen;
	}

}