package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;

public class SwitchBox extends JexpSimplePanel {
	
	private Element check;
	private Element label;

	public SwitchBox() {
		super(DOM.createLabel());
		check = DOM.createInputCheck();
		check.setClassName("ace ace-switch ace-switch-4");
		getElement().appendChild(check);
		label = DOM.createSpan();
		label.setClassName("lbl");
		getElement().appendChild(label);
	}

}