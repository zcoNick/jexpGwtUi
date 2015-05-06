package com.javexpress.gwt.library.ui.container.accordion;

import java.beans.Beans;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class AccordionBox extends AbstractContainer implements ISizeAwareWidget {

	private JavaScriptObject	jsObject;
	private JsonMap				options	= new JsonMap();

	public AccordionBox(final Widget parent, final String id) {
		this(parent, id, false);
	}

	public AccordionBox(final Widget parent, final String id, boolean heightStyleFill) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.ACCORDIONBOX_PREFIX, id);
		getElement().addClassName("jexpBorderBox");
		if (heightStyleFill)
			setHeightStyle("fill");
	}

	public void setHeightStyle(final String val) {
		options.set("heightStyle", val);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		jsObject = createByJs(this, getElement(), options.getJavaScriptObject());
	}

	private native JavaScriptObject createByJs(AccordionBox x, Element el, JavaScriptObject options) /*-{
		return $wnd.$(el).accordion(options);
	}-*/;

	public void refresh() {
		_refresh(getElement());
	}

	private native void _refresh(Element el) /*-{
		$wnd.$(el).accordion("refresh");
	}-*/;

	public void addSection(final String title, final Widget widget) {
		Element a = DOM.createAnchor();
		a.setAttribute("href", "#");
		a.setInnerText(title);
		Element h = DOM.createElement("h3");
		h.appendChild(a);
		getElement().appendChild(h);

		Element d = DOM.createDiv();
		d.addClassName("jesBorderFix");
		getElement().appendChild(d);
		add(widget, d);
	}

	public void addSection(AccordionSection section) {
		addSection(section.getTitle(), section.getWidget());
	}
	
	@Override
	protected void onUnload() {
		clear();
		jsObject = null;
		options = null;
		if (!Beans.isDesignTime())
			destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).accordion('destroy');
	}-*/;

	public void setSelectedSection(final int index) {
		_activate(jsObject, index);
	}

	private native JavaScriptObject _activate(JavaScriptObject obj, int sel) /*-{
		obj.activate(sel);
	}-*/;

	@Override
	public void onResize() {
		super.onResize();
		refresh();
	}

}