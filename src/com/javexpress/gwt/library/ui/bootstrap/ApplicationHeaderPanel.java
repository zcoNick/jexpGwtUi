package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;

public abstract class ApplicationHeaderPanel extends AbstractContainer {

	public ApplicationHeaderPanel(Element element, String id) {
		super(element);
		getElement().setId(id);
	}

	public abstract void setBrand(ICssIcon icon, String appName);

	public abstract ApplicationNavBar getNavBar();

	public abstract ApplicationNotificationDropdown createNotificationDropdown(String id, WContext styleName, ICssIcon iconClass);

	public abstract ApplicationUserInfoDropdown createUserInfoDropdown(String id, WContext styleName);

}