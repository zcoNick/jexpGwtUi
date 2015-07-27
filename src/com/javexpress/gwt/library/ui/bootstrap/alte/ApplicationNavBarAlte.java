package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationNavBar;
import com.javexpress.gwt.library.ui.bootstrap.NavBarItem;

public class ApplicationNavBarAlte extends ApplicationNavBar {

	public ApplicationNavBarAlte(String id) {
		super(DOM.createElement("ul"), id);
		getElement().setClassName("nav navbar-nav");
	}

	@Override
	public void addItem(NavBarItem sbi) {
		add(sbi);
	}

	@Override
	public NavBarItem createNavBarItem(String id, String path) {
		return new NavBarItemAlte(this, id, path);
	}

}