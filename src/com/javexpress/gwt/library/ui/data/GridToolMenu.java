package com.javexpress.gwt.library.ui.data;

import com.google.gwt.user.client.Event;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.menu.DropDownMenu;
import com.javexpress.gwt.library.ui.menu.IMenuHandler;

public class GridToolMenu extends GridToolItem {

	private DropDownMenu	dropDown;

	public GridToolMenu(final String id, final String caption, final ICssIcon icon, final String hint) {
		super(id, caption, icon, hint);
	}

	public DropDownMenu getDropDown() {
		return dropDown;
	}

	@Override
	public void executeHandler(Event event) {
	}

	@Override
	public void unload() {
		super.unload();
	}

	public void add(DropDownMenu dropDown) {
		dropDown.setHandler(new IMenuHandler() {
			@Override
			public void menuItemClicked(String code, Event event) {
				if (getHandler() != null)
					getHandler().execute(code, event);
			}
		});
		this.dropDown = dropDown;
	}

}