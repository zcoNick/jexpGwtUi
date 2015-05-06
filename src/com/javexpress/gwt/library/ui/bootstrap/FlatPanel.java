package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class FlatPanel extends AbstractContainer {

	private WContext	wcontext	= WContext.Default;
	private Element		header;
	private Element		body;
	private Element		footer;

	public WContext getWcontext() {
		return wcontext;
	}

	public void setWcontext(WContext wcontext) {
		if (isAttached() && this.wcontext != null)
			getElement().removeClassName("panel-" + this.wcontext.getValue());
		this.wcontext = wcontext;
		if (isAttached())
			getElement().removeClassName("panel-" + this.wcontext.getValue());
	}

	public FlatPanel(Widget parent, String id) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.PANEL_PREFIX, id);
		getElement().addClassName("panel");
		body = DOM.createDiv();
		body.setClassName("panel-body");
		getElement().appendChild(body);
	}

	public void setHeader(String title) {
		if (header == null) {
			header = DOM.createDiv();
			header.setClassName("panel-header");
			getElement().insertBefore(header, body);
		}
		header.setInnerHTML(title);
	}

	public void setFooter(String title) {
		if (footer == null) {
			footer = DOM.createDiv();
			footer.setClassName("panel-footer");
			getElement().insertAfter(footer, body);
		}
		header.setInnerHTML(title);
	}

	@Override
	public void add(Widget child) {
		super.add(child, body);
	}

	@Override
	protected void onLoad() {
		if (wcontext != null)
			getElement().removeClassName("panel-" + wcontext.getValue());
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		wcontext = null;
		header = null;
		body = null;
		footer = null;
		super.onUnload();
	}

}
