package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.bootstrap.NavBarItem;

public class NavBarItemAlte extends NavBarItem {

	public NavBarItemAlte(Widget sideBar, String id, String path) {
		super(sideBar, id, path);
	}

	@Override
	public NavBarItem createSubItem(String id, String path) {
		return null;
	}

}
