package com.javexpress.gwt.library.ui.form.radiogroup;

import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class RadioBox extends RadioButton {

	/** Designer compatible constructor */
	public RadioBox(final Widget parent, final String id, String name, final String label) {
		super(name, label);
		JsUtil.ensureId(parent, this, WidgetConst.RADIOBOX_PREFIX, id);
	}

}