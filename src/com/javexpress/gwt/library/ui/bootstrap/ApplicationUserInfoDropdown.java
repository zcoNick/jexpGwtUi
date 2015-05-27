package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;

public abstract class ApplicationUserInfoDropdown extends AbstractContainer {

	protected Element	anchor;
	private Element		dropdown;

	public ApplicationUserInfoDropdown(String id, String liClass, String ulClass) {
		super(DOM.createElement("li"));
		getElement().setClassName(liClass);

		anchor = DOM.createAnchor();
		anchor.setClassName("dropdown-toggle");
		anchor.setAttribute("data-toggle", "dropdown");
		getElement().appendChild(anchor);

		fillAnchor();

		dropdown = DOM.createElement("ul");
		dropdown.setClassName(ulClass);
		getElement().appendChild(dropdown);
	}

	protected abstract void fillAnchor();

	public abstract void setUser(String value);

	@Override
	protected void onUnload() {
		anchor = null;
		dropdown = null;
		super.onUnload();
	}

}