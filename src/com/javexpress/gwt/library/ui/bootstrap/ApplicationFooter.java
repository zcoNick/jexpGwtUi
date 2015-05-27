package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;

public class ApplicationFooter extends AbstractContainer {

	public ApplicationFooter(String className) {
		super(DOM.createDiv());
		getElement().setClassName(className);
	}

}
