package com.javexpress.gwt.library.ui.form.button;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.dialog.ConfirmDialog;
import com.javexpress.gwt.library.ui.dialog.ConfirmationListener;
import com.javexpress.gwt.library.ui.form.IFormFactory;

public abstract class ConfirmButton extends Button {

	public ConfirmButton(Widget parent, String id, String buttonText, final String confirmMessage) {
		super(parent, id, buttonText);
		addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new ConfirmDialog(ConfirmButton.this, "cnfrm", IFormFactory.nlsCommon.onay(), confirmMessage, new ConfirmationListener() {
					@Override
					public void onOk() throws Exception {
						onConfirm();
					}
				});
			}
		});
	}

	protected abstract void onConfirm() throws Exception;

}