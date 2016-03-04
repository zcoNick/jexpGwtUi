package com.javexpress.gwt.library.ui.data;

import com.google.gwt.core.client.JavaScriptObject;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class LinkColumn extends ListColumn {

	private ICssIcon			icon;
	private String				hint;
	private int					linkIndexInGrid	= -1;
	private LinkColumnListener	listener;
	private String				text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/** Designer compatible constructor */
	public LinkColumn(final ICssIcon icon, String text, String hint) {
		super(null, null, ColumnAlign.center, "16", false, false, Formatter.link, null);
		this.icon = icon;
		this.text = text;
		this.hint = hint;
	}

	public LinkColumn(final ICssIcon icon, String hint) {
		this(icon, null, hint);
	}

	public LinkColumnListener getListener() {
		return listener;
	}

	public void setListener(LinkColumnListener listener) {
		this.listener = listener;
	}

	public ICssIcon getIcon() {
		return icon;
	}

	public void setIcon(final ICssIcon icon) {
		this.icon = icon;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public boolean isRenderCell(final String rowId) {
		if (listener != null)
			return listener.isRenderCell(rowId);
		return true;
	}

	public int getLinkIndexInGrid() {
		return linkIndexInGrid;
	}

	public void setLinkIndexInGrid(int linkIndexInGrid) {
		this.linkIndexInGrid = linkIndexInGrid;
	}

	public final void cellClicked(final JavaScriptObject linkElement, final String rowId, final JsonMap rowData) {
		if (listener != null)
			listener.cellClicked(linkElement, rowId, rowData);
	}

}