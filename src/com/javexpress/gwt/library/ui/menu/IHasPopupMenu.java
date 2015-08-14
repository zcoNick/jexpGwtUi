package com.javexpress.gwt.library.ui.menu;

import com.google.gwt.event.dom.client.HasContextMenuHandlers;

public interface IHasPopupMenu extends HasContextMenuHandlers {

	public void setPopupMenu(JqPopupMenu menu);

	public boolean canOpenContextMenu(JqPopupMenu menu) throws Exception;

}