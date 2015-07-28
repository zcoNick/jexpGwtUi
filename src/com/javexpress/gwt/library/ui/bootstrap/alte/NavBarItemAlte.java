package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.bootstrap.NavBarItem;

public class NavBarItemAlte extends NavBarItem {

	public NavBarItemAlte(Widget navBar, String id, String path) {
		super(navBar, id, path);
	}

	@Override
	public NavBarItem createSubItem(String id, String path) {
		NavBarItem nbi = new NavBarItemAlte(this, id, path);
		if (ul == null) {
			ul = DOM.createElement("ul");
			ul.setClassName("dropdown");
		}
		add(nbi, ul);
		return nbi;
	}

}
