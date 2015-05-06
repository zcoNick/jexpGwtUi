package com.javexpress.gwt.library.ui.dialog;

import com.javexpress.gwt.library.ui.js.JsUtil;

public class JexpPopupPanel extends com.google.gwt.user.client.ui.PopupPanel {

	public JexpPopupPanel() {
		super();
	}

	public JexpPopupPanel(boolean autoHide, boolean modal) {
		super(autoHide, modal);
	}

	public JexpPopupPanel(boolean autoHide) {
		super(autoHide);
	}

	@Override
	protected void onLoad() {
		addStyleName("ui-widget ui-widget-content");
		super.onLoad();
	}

	@Override
	public void show() {
		getElement().getStyle().setZIndex(JsUtil.calcDialogZIndex());
		super.show();
	}

}