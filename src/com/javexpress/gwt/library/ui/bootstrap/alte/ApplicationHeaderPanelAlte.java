package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationHeaderPanel;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationNavBar;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationNotificationDropdown;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationUserInfoDropdown;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;

public class ApplicationHeaderPanelAlte extends ApplicationHeaderPanel {

	private Element					brand;
	private Element					navRightUl;
	private ApplicationNavBarAlte	navbar;

	public ApplicationHeaderPanelAlte(String id) {
		super(DOM.createElement("header"), id);
		getElement().setClassName("main-header");

		brand = DOM.createAnchor();
		brand.setClassName("logo");
		getElement().appendChild(brand);

		Element navMain = DOM.createElement("nav");
		navMain.setClassName("navbar navbar-fixed-top");
		navMain.setAttribute("role", "navigation");

		Element atgl = DOM.createAnchor();
		atgl.setAttribute("href", "#");
		atgl.setClassName("sidebar-toggle");
		atgl.setAttribute("role", "button");
		Element stgl = DOM.createSpan();
		stgl.setClassName("sr-only");
		stgl.setInnerText("Toggle navigation");
		atgl.appendChild(stgl);
		navMain.appendChild(atgl);

		Element div = DOM.createDiv();
		div.setClassName("collapse navbar-collapse");
		div.setId("navbar-collapse");

		navbar = new ApplicationNavBarAlte("navbar");
		add(navbar, div);

		navRightUl = DOM.createElement("ul");
		navRightUl.setClassName("nav navbar-nav navbar-right");
		navRightUl.getStyle().setCursor(Cursor.POINTER);
		div.appendChild(navRightUl);

		navMain.appendChild(div);
		getElement().appendChild(navMain);
	}

	@Override
	public void setBrand(ICssIcon icon, String appName) {
		brand.setInnerHTML("<small><i class=\"" + icon.getCssClass() + "\"></i> " + appName + "</small>");
	}

	@Override
	public ApplicationNotificationDropdown createNotificationDropdown(String id, WContext styleName, ICssIcon iconClass) {
		ApplicationNotificationDropdown andd = new ApplicationNotificationDropdownAlte(id, styleName, iconClass);
		add(andd, navRightUl);
		return andd;
	}

	@Override
	public ApplicationUserInfoDropdown createUserInfoDropdown(String id, WContext styleName) {
		ApplicationUserInfoDropdown user = new ApplicationUserInfoDropdownAlte(id);
		add(user, navRightUl);
		return user;
	}

	@Override
	public ApplicationNavBar getNavBar() {
		return navbar;
	}

}