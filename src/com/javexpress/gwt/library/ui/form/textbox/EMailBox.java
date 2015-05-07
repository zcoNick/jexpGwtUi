package com.javexpress.gwt.library.ui.form.textbox;

import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class EMailBox extends TextBox {

	/** Designer compatible constructor */
	public EMailBox(final Widget parent, final String id) {
		super(parent, id);
		JsUtil.ensureId(parent, this, WidgetConst.EMAIL_PREFIX, id);
		setWidth("16em");
		setMaxLength(100);
	}

	private native boolean validateEmail(String email) /*-{
		var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		return re.test(email);
	}-*/;

	@Override
	public boolean validate(boolean focusedBefore) {
		boolean validated = JsUtil.validateWidget(this, focusedBefore);
		if (validated) {
			if (!JsUtil.isEmpty(getText()))
				if (!validateEmail(getText().trim())) {
					JsUtil.flagInvalid(this, ClientContext.nlsCommon.gecersizEposta(), focusedBefore);
					validated = false;
				}
		}
		return validated;
	}

}