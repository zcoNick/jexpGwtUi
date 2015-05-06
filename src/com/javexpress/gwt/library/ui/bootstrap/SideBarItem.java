package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.menu.IMenuHandler;

public class SideBarItem extends AbstractContainer {

	private Element			ul;
	private IMenuHandler	handler;
	private String			text;
	private String			iconClass;
	private String			path;

	public IMenuHandler getHandler() {
		return handler;
	}

	public void setHandler(IMenuHandler handler) {
		this.handler = handler;
	}

	public SideBarItem(Widget sideBar, String id, String path) {
		super(DOM.createElement("li"));
		this.path = path;
		JsUtil.ensureId(sideBar, this, WidgetConst.SIDEBARITEM, id);
	}

	public void addSub(SideBarItem child) {
		if (ul == null) {
			ul = DOM.createElement("ul");
			ul.setClassName("submenu");
		}
		add(child, ul);
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
	protected void onLoad() {
		if (ul != null)
			getElement().addClassName("hsub");
		Element a = DOM.createAnchor();
		a.setAttribute("href", "#");
		if (ul == null) {
			a.setAttribute("path", path);
			a.setClassName("sidebar-link");
		}
		Element i = DOM.createElement("i");
		i.setClassName("menu-icon " + (iconClass != null ? iconClass : "fa fa-caret-right"));
		a.appendChild(i);
		Element s = DOM.createSpan();
		s.setClassName("menu-text");
		s.setInnerText(" " + text + " ");
		a.appendChild(s);
		getElement().appendChild(a);
		if (ul != null) {
			a.addClassName("dropdown-toggle");
			Element b1 = DOM.createElement("b");
			b1.setClassName("arrow fa fa-angle-down");
			a.appendChild(b1);
		}

		Element b = DOM.createElement("b");
		b.setClassName("arrow");
		getElement().appendChild(b);

		if (ul != null)
			getElement().appendChild(ul);

		super.onLoad();
	}

	@Override
	protected void onUnload() {
		ul = null;
		handler = null;
		text = null;
		iconClass = null;
		super.onUnload();
	}

}