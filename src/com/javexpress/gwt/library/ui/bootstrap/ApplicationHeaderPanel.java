package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ICssIcon;

public class ApplicationHeaderPanel extends AbstractContainer {

	private Element	container;
	private Element	header;
	private Element	buttons;
	private Element	navUl;

	public ApplicationHeaderPanel(String id) {
		super(DOM.createDiv());
		getElement().setId(id);
		getElement().setClassName("navbar navbar-default navbar-fixed-top");
		getElement().getStyle().setZIndex(1);

		container = DOM.createDiv();
		container.setClassName("navbar-container");
		getElement().appendChild(container);

		Element sideButton = DOM.createButton();
		sideButton.setId("menu-toggler");
		sideButton.setAttribute("data-target", "#sidebar");
		sideButton.setClassName("navbar-toggle menu-toggler pull-left");
		sideButton.setInnerHTML("<span class=\"sr-only\">Toggle sidebar</span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span>");
		container.appendChild(sideButton);

		header = DOM.createDiv();
		header.setClassName("navbar-header pull-left");
		container.appendChild(header);

		buttons = DOM.createDiv();
		buttons.setClassName("navbar-buttons navbar-header pull-right");
		buttons.setAttribute("role", "navigation");

		navUl = DOM.createElement("ul");
		navUl.setClassName("nav ace-nav");
		buttons.appendChild(navUl);

		container.appendChild(buttons);
	}

	public void setBrand(ICssIcon icon, String appName) {
		Element a = DOM.createAnchor();
		a.setClassName("navbar-brand");
		a.setInnerHTML("<small><i class=\"" + icon.getCssClass() + "\"></i> " + appName + "</small>");
		header.appendChild(a);
	}

	public void addNotification(ApplicationNotificationDropdown andd) {
		add(andd, navUl);
	}

	public void addUserInfo(ApplicationUserInfoDropdown uidd) {
		add(uidd, navUl);
	}

	@Override
	protected void onUnload() {
		container = null;
		header = null;
		buttons = null;
		navUl = null;
		super.onUnload();
	}

}