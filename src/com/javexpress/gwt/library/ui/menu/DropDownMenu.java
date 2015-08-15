package com.javexpress.gwt.library.ui.menu;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class DropDownMenu extends ComplexPanel {

	private IMenuHandler	handler;

	public IMenuHandler getHandler() {
		return handler;
	}

	public void setHandler(IMenuHandler handler) {
		this.handler = handler;
	}

	public DropDownMenu(Widget parent, String id) {
		super();
		setElement(DOM.createElement("ul"));
		getElement().addClassName("dropdown-menu");
		getElement().setAttribute("role", "menu");
		JsUtil.ensureId(parent, getElement(), WidgetConst.DROPDOWN_PREFIX, id);
	}

	@Override
	public void add(Widget child) {
		if (child instanceof DropDownMenuItem) {
			super.add(child, getElement());
		} else
			return;
	}

	@Override
	protected void onUnload() {
		handler = null;
		super.onUnload();
	}

}