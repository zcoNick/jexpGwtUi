package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ApplicationBreadcrumb extends SimplePanel {

	private Element	ul;

	public ApplicationBreadcrumb() {
		super(DOM.createDiv());
		getElement().setId("breadcrumbs");
		getElement().setClassName("breadcrumbs breadcrumbs-fixed");
		getElement().getStyle().setZIndex(1);

		ul = DOM.createElement("ul");
		ul.setClassName("breadcrumb");
		getElement().appendChild(ul);

		reset();
	}

	private void reset() {
		JsUtil.clearChilds(ul);
		addItem(FaIcon.home, "home-icon", "#", "Home");
	}

	public void addItem(ICssIcon icon, String extraClass, String href, String label) {
		Element li = DOM.createElement("li");
		Element i = DOM.createElement("i");
		i.setClassName("ace-icon " + icon.getCssClass() + (extraClass != null ? " " + extraClass : ""));
		li.appendChild(i);
		Element a = DOM.createAnchor();
		a.setAttribute("href", "#" + href);
		a.setInnerHTML(label);
		li.appendChild(a);
		ul.appendChild(li);
	}

}
