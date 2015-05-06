package com.javexpress.gwt.library.ui.data.editgrid;

import com.google.gwt.core.client.JavaScriptObject;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class EditLinkColumn extends EditColumn {

	private ICssIcon				icon;
	private int						linkIndexInGrid	= -1;
	private IEditLinkColumnListener	listener;

	public IEditLinkColumnListener getListener() {
		return listener;
	}

	public void setListener(IEditLinkColumnListener listener) {
		this.listener = listener;
	}

	public int getLinkIndexInGrid() {
		return linkIndexInGrid;
	}

	public void setLinkIndexInGrid(int linkIndexInGrid) {
		this.linkIndexInGrid = linkIndexInGrid;
	}

	/** Designer compatible constructor */
	public EditLinkColumn(ICssIcon icon, String hint) {
		super(null, hint, null, ColumnAlign.center, "16", EditorType.link, null);
		this.icon = icon;
		cannotTriggerInsert();
	}

	public ICssIcon getIcon() {
		return icon;
	}

	public void setIcon(ICssIcon icon) {
		this.icon = icon;
	}

	public final void cellClicked(JavaScriptObject linkElement, String cellValue, JsonMap rowData) {
		if (listener != null)
			listener.cellClicked(linkElement, cellValue, rowData);
	}

}