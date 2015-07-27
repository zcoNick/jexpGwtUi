package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.FaIcon;

public class IconPhoneBox extends IconMaskBox {

	private boolean	fax;

	public IconPhoneBox(Widget parent, String id) {
		super(parent, id, FaIcon.phone, "0(999) 999 9999");
		setWidth("8em");
	}

	public boolean isFax() {
		return fax;
	}

	public void setFax(boolean fax) {
		this.fax = fax;
	}

	@Override
	protected void doAttachChildren() {
		if (fax)
			setIcon(FaIcon.fax);
		super.doAttachChildren();
	}

}