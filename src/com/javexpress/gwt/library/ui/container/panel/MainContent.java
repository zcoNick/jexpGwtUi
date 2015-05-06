package com.javexpress.gwt.library.ui.container.panel;

import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;

public class MainContent extends AbstractContainer {

	public MainContent() {
		super(DOM.createDiv());
		getElement().setClassName("main-content");
	}

}