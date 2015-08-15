package com.javexpress.gwt.library.ui.menu;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class DropDownMenuItem extends ComplexPanel {

	private Element	ul;
	private Element	anchor;

	public DropDownMenuItem(String id, String label) {
		super();
		setElement(DOM.createElement("li"));
		anchor = DOM.createAnchor();
		anchor.setInnerHTML(label);
		getElement().appendChild(anchor);
	}

	public void add(DropDownMenuItem sub) {
		if (ul == null) {
			ul = DOM.createElement("ul");
			ul.addClassName("dropdown-menu");
			getElement().addClassName("dropdown-submenu");
			getElement().insertAfter(ul, anchor);
		}
		super.add(sub, ul);
	}

	protected DropDownMenu getMenu() {
		Widget parent = getParent();
		while (parent != null && parent instanceof DropDownMenuItem)
			parent = parent.getParent();
		if (parent != null && parent instanceof DropDownMenu)
			return (DropDownMenu) parent;
		return null;
	}

}