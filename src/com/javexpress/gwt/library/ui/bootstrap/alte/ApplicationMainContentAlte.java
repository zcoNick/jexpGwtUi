package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationBreadcrumb;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationMainContent;

public class ApplicationMainContentAlte extends ApplicationMainContent {

	private Element	inner;
	private Element	page;

	public ApplicationMainContentAlte() {
		super(DOM.createDiv());
		getElement().setClassName("main-content");

		inner = DOM.createDiv();
		inner.setClassName("main-content-inner");
		getElement().appendChild(inner);

		ApplicationBreadcrumb breadcrumb = new ApplicationBreadcrumb();
		add(breadcrumb, inner);

		page = DOM.createDiv();
		page.setClassName("page-content");
		inner.appendChild(page);
	}

	@Override
	protected void onUnload() {
		inner = null;
		page = null;
		super.onUnload();
	}

}