package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;

public abstract class ApplicationHeaderPanel extends AbstractContainer {

	protected Element	navDiv;

	public ApplicationHeaderPanel(Element element, String id) {
		super(element);
		getElement().setId(id);
	}

	public void setBrand(ICssIcon icon, String appName) {
		setBrand(icon.getCssClass(), appName);
	}

	public abstract void setBrand(String icon, String appName);

	public abstract ApplicationNavBar createNavBar();

	public abstract ApplicationNotificationDropdown createNotificationDropdown(String id, WContext styleName, ICssIcon iconClass);

	public abstract ApplicationUserInfoDropdown createUserInfoDropdown(String id, WContext styleName);

	public void addNavBar(ApplicationNavBar navbar) {
		add(navbar, navDiv);
	}

	@Override
	protected void onUnload() {
		navDiv = null;
		super.onUnload();
	}

}