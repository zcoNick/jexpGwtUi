package com.javexpress.gwt.library.ui;

import com.google.gwt.user.client.ui.Widget;

public class JexpWidget extends Widget implements IJexpWidget {
	
	@Override
	public String getId() {
		return getElement().getId();
	}

}