package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.menu.IMenuHandler;

public abstract class NavBarItem extends AbstractContainer {

	protected Element		ul;
	private IMenuHandler	handler;
	protected String		text;
	protected String		iconClass;
	protected String		path;

	public IMenuHandler getHandler() {
		return handler;
	}

	public void setHandler(IMenuHandler handler) {
		this.handler = handler;
	}

	public NavBarItem(Widget navBar, String id, String path) {
		super(DOM.createElement("li"));
		this.path = path;
		JsUtil.ensureId(navBar, this, WidgetConst.NAVBARITEM, id);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setIcon(ICssIcon icon) {
		this.iconClass = icon.getCssClass();
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	@Override
	protected void onUnload() {
		ul = null;
		handler = null;
		text = null;
		iconClass = null;
		super.onUnload();
	}

	public void setAttribute(String key, String value) {
		getElement().setAttribute(key, value);
	}

	public abstract NavBarItem createSubItem(String id, String path);

}