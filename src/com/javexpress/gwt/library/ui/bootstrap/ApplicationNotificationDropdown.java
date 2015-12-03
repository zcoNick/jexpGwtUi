package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;

public abstract class ApplicationNotificationDropdown extends AbstractContainer {

	protected Element	anchor;
	protected Element	icon;
	protected Element	badge;
	private Element		dropdown;
	protected Element	header, footer;
	private String		badgeClass;

	public ApplicationNotificationDropdown(String id, WContext styleName, ICssIcon iconClass, String ulClass, String badgeClass) {
		super(DOM.createElement("li"));
		if (styleName != null)
			getElement().setClassName(styleName.getValue());

		anchor = DOM.createAnchor();
		anchor.setClassName("dropdown-toggle");
		anchor.setAttribute("data-toggle", "dropdown");
		getElement().appendChild(anchor);

		icon = DOM.createElement("i");
		setIcon(iconClass);
		anchor.appendChild(icon);

		dropdown = DOM.createElement("ul");
		dropdown.setClassName(ulClass);
		getElement().appendChild(dropdown);
		this.badgeClass = badgeClass;
	}

	public abstract void setIcon(ICssIcon iconClass);

	public void setBadge(WContext context, String value) {
		if (badge != null) {
			if (value != null)
				badge.setInnerHTML(value);
			else {
				badge.removeFromParent();
				badge = null;
			}
			return;
		}
		if (value == null)
			return;
		badge = DOM.createSpan();
		badge.setClassName(badgeClass + " " + badgeClass + "-" + context.getValue());
		badge.setInnerHTML(value);
		anchor.appendChild(badge);
	}

	@Override
	protected void onUnload() {
		anchor = null;
		icon = null;
		badge = null;
		dropdown = null;
		header = null;
		footer = null;
		super.onUnload();
	}

	public abstract void setHeader(ICssIcon icon, String text);

	public abstract void setFooter(ICssIcon icon, String text);

}