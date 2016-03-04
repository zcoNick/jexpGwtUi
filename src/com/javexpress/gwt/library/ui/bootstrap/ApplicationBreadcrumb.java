package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class ApplicationBreadcrumb extends SimplePanel {

	private Element	ul;

	public ApplicationBreadcrumb(Element element, String id, String classNames) {
		super(element);
		getElement().setId(id);
		getElement().setClassName(classNames);
	}

	public void reset() {
		JsUtil.clearChilds(ul);
		addItem(FaIcon.home, "home-icon", "#", "Home");
	}

	public abstract void addItem(ICssIcon icon, String extraClass, String href, String label);

}