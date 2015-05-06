package com.javexpress.gwt.library.ui.menu;

import com.google.gwt.event.dom.client.HasContextMenuHandlers;

public interface IHasPopupMenu extends HasContextMenuHandlers {

	public void setPopupMenu(PopupMenu menu);

	public boolean canOpenContextMenu(PopupMenu menu) throws Exception;

}