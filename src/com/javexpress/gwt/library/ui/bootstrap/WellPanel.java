package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WSize;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class WellPanel extends AbstractContainer {

	private WSize	size	= WSize.normal;

	public WellPanel(Widget parent, String id) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, getElement(), WidgetConst.WELL_PREFIX, id);
		addStyleName("well jexpWell");
	}

	public WSize getSize() {
		return size;
	}

	public void setSize(WSize size) {
		this.size = size;
	}

	@Override
	protected void onLoad() {
		switch (size) {
			case small:
				addStyleName("well-small");
				break;
			case large:
				addStyleName("well-large");
				break;
		}
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		size = null;
		super.onUnload();
	}

}