package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;

public class ApplicationMainContainer extends AbstractContainer {

	public ApplicationMainContainer(String id) {
		super(DOM.createDiv());
		getElement().setId(id);
		getElement().setClassName("main-container");
	}

}
