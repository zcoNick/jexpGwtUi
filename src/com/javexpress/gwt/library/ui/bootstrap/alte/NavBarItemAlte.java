package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.bootstrap.NavBarItem;

public class NavBarItemAlte extends NavBarItem {

	private Element	anchor;
	private Element	textEl;

	public NavBarItemAlte(Widget navBar, String id, String path) {
		super(navBar, id, path);
		addStyleName("jexpNavBarItem");
		anchor = DOM.createAnchor();
		anchor.setAttribute("href", "#" + path);
		getElement().appendChild(anchor);
	}

	@Override
	protected void onLoad() {
		if (iconClass != null) {
			Element i = DOM.createElement("span");
			i.setClassName(iconClass);
			anchor.appendChild(i);
		}
		textEl = DOM.createSpan();
		textEl.setClassName("menu-text");
		textEl.setInnerText(text);
		anchor.appendChild(textEl);
		if (ul != null) {
			anchor.addClassName("dropdown-toggle");
			anchor.setAttribute("data-toggle", "dropdown");
			Element b1 = DOM.createElement("span");
			b1.setClassName("caret");
			anchor.appendChild(b1);
		} else {
			anchor.setClassName("navbar-link");
			if (path != null)
				anchor.setAttribute("path", path);
			if (bpmnCode != null)
				anchor.setAttribute("bpmnCode", bpmnCode);
		}
		super.onLoad();
	}

	@Override
	public NavBarItem createSubItem(String id, String path) {
		NavBarItem nbi = new NavBarItemAlte(this, id, path);
		if (ul == null) {
			ul = DOM.createElement("ul");
			ul.setClassName("dropdown-menu");
			ul.setAttribute("role", "menu");
			getElement().appendChild(ul);
		}
		add(nbi, ul);
		return nbi;
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		if (textEl != null)
			textEl.setInnerText(text);
	}

}