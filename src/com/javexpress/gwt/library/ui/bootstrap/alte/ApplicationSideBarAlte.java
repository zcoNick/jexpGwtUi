package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationSideBar;
import com.javexpress.gwt.library.ui.bootstrap.SideBarItem;

public class ApplicationSideBarAlte extends ApplicationSideBar {

	private Element	area;
	private Element	buttonsUl;

	public ApplicationSideBarAlte(String id) {
		super(DOM.createElement("aside"), id);
		getElement().setClassName("main-sidebar");
		getElement().getStyle().setZIndex(1);

		area = DOM.createElement("section");
		area.setClassName("sidebar");
		getElement().appendChild(area);

		buttonsUl = DOM.createElement("ul");
		buttonsUl.setClassName("sidebar-menu");
		getElement().appendChild(buttonsUl);//was adding to contents
	}

	@Override
	public void addItem(SideBarItem sbi) {
		add(sbi, buttonsUl);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement());
	}

	private native void createByJs(ApplicationSideBarAlte x, Element element) /*-{
		var opts = {};
		$wnd.$(element);
	}-*/;

	@Override
	protected void onUnload() {
		area = null;
		buttonsUl = null;
		super.onUnload();
	}

}