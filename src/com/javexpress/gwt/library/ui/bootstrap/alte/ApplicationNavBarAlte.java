package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
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
	protected void onLoad() {
		super.onLoad();
		initialize();
	}

	@Override
	public void initialize() {
		createByJs(this, getElement());
	}

	private native void createByJs(ApplicationNavBarAlte x, Element element) /*-{
		$wnd.$("a.navbar-link", $wnd.$(element)).click(
				function(e) {
					var a = $wnd.$(this);
					x.@com.javexpress.gwt.library.ui.bootstrap.alte.ApplicationNavBarAlte::fireLinkClicked(Ljava/lang/String;)(a.attr("path"));
				});		
	}-*/;

	@Override
	public NavBarItem createNavBarItem(String id, String path) {
		return new NavBarItemAlte(this, id, path);
	}

}