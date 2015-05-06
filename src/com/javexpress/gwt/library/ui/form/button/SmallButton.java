package com.javexpress.gwt.library.ui.form.button;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class SmallButton extends Button {

	public SmallButton(Widget parent, String id) {
		this(parent,id,null);
	}

	public SmallButton(Widget parent, String id, String text) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.BUTTON_PREFIX, id);
		addStyleName("ui-widget ui-state-default jesSmallButton");
		setText(text);
	}
	
	@Override
	protected void onLoad() {
		JsUtil.hoverStyle(getElement(), "ui-state-active", "ui-state-default");
		super.onLoad();
	}

}