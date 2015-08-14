package com.javexpress.gwt.library.ui.data;

import com.google.gwt.user.client.Event;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.menu.JqPopupMenu;

public class GridToolMenu extends GridToolItem {

	private JqPopupMenu	menu;

	public GridToolMenu(final String id, final String caption, final ICssIcon icon, final String hint) {
		super(id, caption, icon, hint);
	}

	public JqPopupMenu getMenu() {
		return menu;
	}

	public void setMenu(JqPopupMenu menu) {
		this.menu = menu;
	}

	@Override
	public void executeHandler(Event event) {
		if (menu != null)
			menu.popUp(event);
	}

	@Override
	public void unload() {
		if (menu != null)
			menu.removeFromParent();
		menu = null;
		super.unload();
	}

}