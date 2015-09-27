package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.menu.IMenuHandler;

public abstract class SideBarItem extends AbstractContainer {

	protected Element		ul;
	private IMenuHandler	handler;
	protected Element		anchor;
	protected Element		iconSpan;
	protected Element		textSpan;

	public IMenuHandler getHandler() {
		return handler;
	}

	public void setHandler(IMenuHandler handler) {
		this.handler = handler;
	}

	public SideBarItem(Widget sideBar, String id, String path) {
		super(DOM.createElement("li"));
		JsUtil.ensureId(sideBar, this, WidgetConst.SIDEBARITEM, id);
		anchor = DOM.createAnchor();
		anchor.setAttribute("href", "#");
		anchor.addClassName("sidebar-link");
		if (path != null)
			anchor.setAttribute("path", path);
		iconSpan = DOM.createElement("i");
		anchor.appendChild(iconSpan);
		textSpan = DOM.createSpan();
		anchor.appendChild(textSpan);
		getElement().appendChild(anchor);
	}

	public String getText() {
		return textSpan.getInnerText();
	}

	public void setText(String text) {
		textSpan.setInnerText(text);
	}

	public String getBpmnCode() {
		return anchor.getAttribute("bpmnCode");
	}

	public void setBpmnCode(String bpmnCode) {
		anchor.setAttribute("bpmnCode", bpmnCode);
	}

	public void setIcon(ICssIcon icon) {
		setIconClass(icon.getCssClass());
	}

	public abstract void setIconClass(String iconClass);

	@Override
	protected void onUnload() {
		ul = null;
		handler = null;
		super.onUnload();
	}

	public abstract SideBarItem createSubItem(String id, String path);

}