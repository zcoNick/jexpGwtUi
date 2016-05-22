package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;

public abstract class ApplicationUserInfoDropdown extends AbstractContainer {

	protected Element	anchor;

	public ApplicationUserInfoDropdown(String id, String liClass) {
		super(DOM.createElement("li"));
		if (liClass!=null)
			getElement().setClassName(liClass);

		anchor = DOM.createAnchor();
		anchor.setClassName("dropdown-toggle");
		anchor.setAttribute("data-toggle", "dropdown");
		getElement().appendChild(anchor);

		fillAnchor();
	}

	protected abstract void fillAnchor();

	public abstract void setUser(String value);

	@Override
	protected void onUnload() {
		anchor = null;
		super.onUnload();
	}

}