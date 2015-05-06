package com.javexpress.gwt.library.ui.data.treetable;

import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.data.ListColumn;

public class TreeColumn extends ListColumn {

	private String		onclick;
	private ICssIcon	icon;

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(final String onclick) {
		this.onclick = onclick;
	}

	public ICssIcon getIcon() {
		return icon;
	}

	public void setIcon(ICssIcon icon) {
		this.icon = icon;
	}

	public TreeColumn(final String title, final String field, final ColumnAlign align, final String width, final boolean sortable, final boolean hidden, final Formatter formatter, final SummaryType summaryType) {
		super(title, field, align, width, sortable, hidden, formatter, summaryType);
	}

}