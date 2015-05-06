package com.javexpress.gwt.library.ui.form.filterpanel;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class PopFilterPanel extends AbstractFilterPanel {

	private Element	buttonsDiv;

	public PopFilterPanel(final Widget parent, final String id) {
		super(DOM.createDiv(), parent, id);
		setStyleName("jexpPopFilterPanel");
		setMargin(0);

		buttonsDiv = DOM.createDiv().cast();
		getElement().appendChild(buttonsDiv);
	}

	@Override
	protected void onUnload() {
		buttonsDiv = null;
		super.onUnload();
	}

}