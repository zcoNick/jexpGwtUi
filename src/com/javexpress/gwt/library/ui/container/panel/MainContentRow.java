package com.javexpress.gwt.library.ui.container.panel;

import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;

public class MainContentRow extends AbstractContainer {

	public MainContentRow() {
		super(DOM.createDiv());
		getElement().setClassName("row");
	}

}