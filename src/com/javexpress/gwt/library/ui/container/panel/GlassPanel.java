package com.javexpress.gwt.library.ui.container.panel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class GlassPanel extends JexpSimplePanel {

	public GlassPanel(final Widget parent, final String id) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.GLASSPANEL_PREFIX, id);
		getElement().setClassName("ui-widget jexpBorderBox jesGlassPanel");
	}

}