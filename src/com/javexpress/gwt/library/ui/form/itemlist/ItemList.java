package com.javexpress.gwt.library.ui.form.itemlist;

import java.beans.Beans;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.form.label.Label;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ItemList extends AbstractContainer {

	public static enum ItemState {
		None, Default, Highlight, Active;
	}

	private JavaScriptObject	widget;
	private String				dndGroupId;

	public ItemList(final boolean useNumberedList, final boolean fitToParent) {
		this(null, null, useNumberedList, fitToParent);
	}

	public ItemList(final String dndGroupId) {
		this(null, dndGroupId, false);
	}

	public ItemList(final String dndGroupId, final boolean fitToParent) {
		this(null, dndGroupId, false, fitToParent);
	}

	public ItemList(final String id, final String dndGroupId, final boolean fitToParent) {
		this(id, dndGroupId, false, fitToParent);
	}

	public ItemList(final String id, final String dndGroupId, final boolean useNumberedList, final boolean fitToParent) {
		super(DOM.createElement(useNumberedList ? "ol" : "ul"));
		getElement().setId(id == null ? DOM.createUniqueId() : id);
		setStyleName("ui-widget ui-widget-content ui-corner-all jexpBorderBox");
		if (dndGroupId != null)
			getElement().setAttribute("dndgroup", dndGroupId);
		this.dndGroupId = dndGroupId;
		if (fitToParent) {
			getElement().getStyle().setWidth(100, Unit.PCT);
			getElement().getStyle().setHeight(100, Unit.PCT);
		}
	}

	public Element addItem(final String text) {
		return addItem(text, ItemState.None);
	}

	public Element addItem(final String text, final ItemState state) {
		if (dndGroupId != null)
			return addItem(new Label(text), state);
		else {
			Element el = DOM.createElement("li");
			el.getStyle().setMargin(5, Unit.PX);
			el.setInnerText(text);
			if (state != ItemState.None)
				el.setClassName("ui-state-" + (state == ItemState.Default ? "default" : (state == ItemState.Active ? "active" : "highlight")));
			getElement().appendChild(el);
			return el;
		}
	}

	public Element addItem(final Widget child, final ItemState state) {
		Element el = DOM.createElement("li");
		if (state != ItemState.None)
			el.setClassName("ui-state-" + (state == ItemState.Default ? "default" : (state == ItemState.Active ? "active" : "highlight")));
		el.getStyle().setMargin(2, Unit.PX);
		add(child, el);
		return el;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!Beans.isDesignTime())
			widget = createByJs(this, getElement(), dndGroupId);
	}

	private native JavaScriptObject createByJs(ItemList x, Element elm, String dndGroupId) /*-{
		var w = $wnd.$(elm);
		if (dndGroupId != null) {
			var options = {
				placeholder : "ui-state-highlight"
			};
			if (dndGroupId != null)
				options.connectWith = "ul[dndgroup='" + dndGroupId + "']";
			w = w.sortable(options).disableSelection();
		}
		return w;
	}-*/;

	public void setItems(final String[] items) {
		clear();
		JsUtil.clearChilds(getElement());
		for (String s : items)
			addItem(s);
	}

	@Override
	protected void onUnload() {
		clear();
		widget = null;
		dndGroupId = null;
		destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element elm) /*-{
		$wnd.$(elm).empty().off();
	}-*/;

}