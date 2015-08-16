package com.javexpress.gwt.library.ui.menu;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
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
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement());
	}

	private native void createByJs(DropDownMenu x, Element element) /*-{
		var el = $wnd.$("a.jexpLink", element).click(function(e) {
			x.@com.javexpress.gwt.library.ui.menu.DropDownMenu::executeHandler(Ljava/lang/String;Lcom/google/gwt/user/client/Event;)($wnd.$(this).attr("v"), e);
		});
	}-*/;

	private void executeHandler(String id, Event event) {
		if (handler != null)
			handler.menuItemClicked(id, event);
	}

	@Override
	protected void onUnload() {
		handler = null;
		destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$("a.jexpLink", element).off();
	}-*/;

}