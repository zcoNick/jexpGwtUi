package com.javexpress.gwt.library.ui.form.checkbox;

import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class CheckBoxJq extends com.google.gwt.user.client.ui.CheckBox {

	/** Designer compatible constructor */
	public CheckBoxJq(final Widget parent, final String id, final String label) {
		super(label);
		JsUtil.ensureId(parent, this, WidgetConst.CHECKBOX_PREFIX, id);
	}

	public CheckBoxJq(final Widget parent, final String id) {
		this(parent, id, null);
	}

}