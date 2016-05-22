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
	protected Element	navDiv;


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

		navDiv = DOM.createDiv();
		navDiv.setClassName("collapse navbar-collapse");
		navDiv.setId("navbar-collapse");

		navRightUl = DOM.createElement("ul");
		navRightUl.setClassName("nav navbar-nav navbar-right jexpNavbarNotifications");
		navRightUl.getStyle().setCursor(Cursor.POINTER);
		navDiv.appendChild(navRightUl);

		navMain.appendChild(navDiv);
		getElement().appendChild(navMain);
	}

	@Override
	public void setBrand(String icon, String appName) {
		brand.setInnerHTML("<small><i class=\"" + icon + "\"></i> " + appName + "</small>");
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
	public ApplicationNavBarAlte createNavBar() {
		navbar = new ApplicationNavBarAlte("navbar");
		return navbar;
	}
	
	public void addNavBar(ApplicationNavBar navbar) {
		add(navbar, navDiv);
	}

	@Override
	protected void onUnload() {
		brand = null;
		navRightUl = null;
		navbar = null;
		navDiv = null;
		super.onUnload();
	}

}