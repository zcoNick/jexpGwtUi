package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;

public class ApplicationNotificationDropdown extends AbstractContainer {

	private Element	anchor;
	private Element	icon;
	private Element	badge;
	private Element	dropdown;

	public ApplicationNotificationDropdown(Widget parent, String id, WContext styleName, ICssIcon iconClass) {
		super(DOM.createElement("li"));
		getElement().setClassName(styleName.getValue());

		anchor = DOM.createAnchor();
		anchor.setClassName("dropdown-toggle");
		anchor.setAttribute("data-toggle", "dropdown");
		getElement().appendChild(anchor);

		icon = DOM.createElement("i");
		setIcon(iconClass);
		anchor.appendChild(icon);

		dropdown = DOM.createElement("ul");
		dropdown.setClassName("dropdown-menu-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close");
		getElement().appendChild(dropdown);
	}

	public void setIcon(ICssIcon iconClass) {
		icon.setClassName("ace-icon " + iconClass.getCssClass());
	}

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
		badge.setClassName("badge badge-" + context.getValue());
		badge.setInnerHTML(value);
		anchor.appendChild(badge);
	}

	@Override
	protected void onUnload() {
		anchor = null;
		icon = null;
		badge = null;
		dropdown = null;
		super.onUnload();
	}
}
