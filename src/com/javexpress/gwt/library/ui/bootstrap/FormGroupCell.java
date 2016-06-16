package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.container.panel.SimplePanel;

public class FormGroupCell extends SimplePanel {

	public FormGroupCell() {
		super(DOM.createDiv());
		setStyleName("form-group");
	}

}