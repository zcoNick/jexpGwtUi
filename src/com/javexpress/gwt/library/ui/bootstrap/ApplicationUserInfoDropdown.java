package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;

public class ApplicationUserInfoDropdown extends AbstractContainer {

	private Element	anchor;
	private Element	icon;
	private Element	dropdown;
	private Element	user;

	public ApplicationUserInfoDropdown(Widget parent, String id, WContext styleName, ICssIcon iconClass) {
		super(DOM.createElement("li"));
		getElement().setClassName(styleName.getValue());

		anchor = DOM.createAnchor();
		anchor.setClassName("dropdown-toggle");
		anchor.setAttribute("data-toggle", "dropdown");
		getElement().appendChild(anchor);

		icon = DOM.createElement("i");
		setIcon(iconClass);
		anchor.appendChild(icon);

		user = DOM.createSpan();
		user.setClassName("user-info");
		anchor.appendChild(user);

		Element ddicon = DOM.createElement("i");
		ddicon.setClassName("ace-icon " + FaIcon.caret_down.getCssClass());
		anchor.appendChild(ddicon);

		dropdown = DOM.createElement("ul");
		dropdown.setClassName("dropdown-menu-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close");
		getElement().appendChild(dropdown);
	}

	public void setIcon(ICssIcon iconClass) {
		icon.setClassName("ace-icon " + iconClass.getCssClass());
	}

	public void setUser(String value) {
		user.setInnerHTML("<small>" + ClientContext.nlsCommon.hosgeldiniz() + ",<br/>" + value + "</small>");
	}

	@Override
	protected void onUnload() {
		anchor = null;
		icon = null;
		user = null;
		dropdown = null;
		super.onUnload();
	}
}
