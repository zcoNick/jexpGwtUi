package com.javexpress.gwt.library.ui.form.maskedit;

import com.google.gwt.user.client.ui.Widget;

public class IPBox extends MaskEditBox {

	public IPBox(final Widget parent, final String id) {
		super(parent, id, "ip");
		setWidth("8em");
	}

}