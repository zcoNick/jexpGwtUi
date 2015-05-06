package com.javexpress.gwt.library.ui.form.maskedit;

import com.google.gwt.user.client.ui.Widget;

public class PhoneBox extends MaskEditBox {

	public PhoneBox(final Widget parent, final String id) {
		super(parent, id, "0(999) 999 9999");
		setClearIncomplete(true);
		setWidth("8em");
	}

}