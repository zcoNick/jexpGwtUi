package com.javexpress.gwt.library.ui.data.cellview;

import java.io.Serializable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class CellPanel extends AbstractContainer {

	private ClickHandler	handler;
	private Serializable	data;

	public CellPanel(final Serializable data) {
		this(null, data);
	}

	public CellPanel(final String id, final Serializable data) {
		super(DOM.createDiv());
		this.data = data;
		getElement().setId(id == null ? DOM.createUniqueId() : id);
		setStyleName((JsUtil.USE_BOOTSTRAP ? "jexpHandCursor" : "ui-widget ui-widget-content ui-corner-all ui-cursor-hand") + " jexpCellPanel");
		sinkEvents(Event.ONCLICK);
	}

	public void addElement(final Element elem) {
		getElement().appendChild(elem);
	}

	public void setClickHandler(final ClickHandler handler) {
		this.handler = handler;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		_bindClick(this, getElement().getId());
	}

	private native void _bindClick(CellPanel x, String id) /*-{
															$wnd.$("#"+id).click(function(event){
															x.@com.javexpress.gwt.library.ui.data.cellview.CellPanel::fireOnClick()();
															});
															}-*/;

	private void fireOnClick() {
		setSelected(true);
		getElement().addClassName("ui-state-highlight");
		if (handler != null)
			handler.onClick(null);
	}

	public Serializable getData() {
		return data;
	}

	public void setSelected(final boolean selected) {
		_setSelected(getElement().getId(), selected);
	}

	public native void _setSelected(String id, boolean selected) /*-{
																	var self = $wnd.$("#"+id);
																	$wnd.$(".jexpCellPanel", self.parent().parent()).removeClass("ui-state-highlight");
																	if (selected)
																	self.addClass("ui-state-highlight");
																	}-*/;

	@Override
	protected void onUnload() {
		handler = null;
		data = null;
		super.onUnload();
	}

}