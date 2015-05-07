package com.javexpress.gwt.library.ui.form.numericbox;

import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class TCIdBox extends NumericBox {

	/** Designer compatible constructor */
	public TCIdBox(final Widget parent, final String id) {
		super(parent, id);
		setWidth("10em");
		setMaxLength(11);
	}

	private native boolean validateTc(String tc) /*-{
		var KimlikNo = String(tc);
		if (!KimlikNo.match(/^[0-9]{11}$/))
			return false;
		pr1 = parseInt(KimlikNo.substr(0, 1));
		pr2 = parseInt(KimlikNo.substr(1, 1));
		pr3 = parseInt(KimlikNo.substr(2, 1));
		pr4 = parseInt(KimlikNo.substr(3, 1));
		pr5 = parseInt(KimlikNo.substr(4, 1));
		pr6 = parseInt(KimlikNo.substr(5, 1));
		pr7 = parseInt(KimlikNo.substr(6, 1));
		pr8 = parseInt(KimlikNo.substr(7, 1));
		pr9 = parseInt(KimlikNo.substr(8, 1));
		pr10 = parseInt(KimlikNo.substr(9, 1));
		pr11 = parseInt(KimlikNo.substr(10, 1));
		if ((pr1 + pr3 + pr5 + pr7 + pr9 + pr2 + pr4 + pr6 + pr8 + pr10) % 10 != pr11)
			return false;
		if (((pr1 + pr3 + pr5 + pr7 + pr9) * 7 + (pr2 + pr4 + pr6 + pr8) * 9) % 10 != pr10)
			return false;
		if (((pr1 + pr3 + pr5 + pr7 + pr9) * 8) % 10 != pr11)
			return false;
		return true;
	}-*/;

	@Override
	public boolean validate(final boolean focusedBefore) {
		boolean validated = JsUtil.validateWidget(this, focusedBefore);
		if (validated) {
			if (!JsUtil.isEmpty(getText()))
				if (!validateTc(getText().trim())) {
					JsUtil.flagInvalid(this, ClientContext.nlsCommon.gecersizTCKimlik(), focusedBefore);
					validated = false;
				}
		}
		return validated;
	}

}