package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.FaIcon;

public class IconEmbeddedPassword extends IconEmbeddedInput {

	public IconEmbeddedPassword(Widget parent, String id) {
		super(parent, id, FaIcon.lock);
	}

	@Override
	protected String getIdPrefix() {
		return WidgetConst.PASSWORDBOX_PREFIX;
	}

	@Override
	protected Element createInput() {
		return DOM.createInputPassword();
	}

}