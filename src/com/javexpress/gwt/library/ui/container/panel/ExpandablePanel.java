package com.javexpress.gwt.library.ui.container.panel;

import java.beans.Beans;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ExpandablePanel extends JexpSimplePanel {

	TableElement			table;
	TableRowElement			contentRow;
	SpanElement				span;
	Widget					content;
	IExpandCollapseListener	listener;

	public IExpandCollapseListener getListener() {
		return listener;
	}

	public void setListener(IExpandCollapseListener listener) {
		this.listener = listener;
	}

	public ExpandablePanel(Widget parent, String id, String headerText) {
		super(DOM.createTable());
		JsUtil.ensureId(parent, this, WidgetConst.EXPANDABLEPANEL_PREFIX, id);
		table = getElement().cast();
		table.addClassName("ui-widget ui-widget-content ui-corner-all jexpBorderBox jesExpandablePanel");

		Element tr = DOM.createTR();
		TableCellElement td = DOM.createTD().cast();
		span = DOM.createSpan().cast();
		span.addClassName("ui-icon ui-icon-only ui-icon-circle-plus jesExpandablePanelButton");
		td.appendChild(span);
		td.setAttribute("width", "2em");
		tr.appendChild(td);
		td = DOM.createTD().cast();
		td.setInnerHTML(headerText);
		tr.appendChild(td);
		table.appendChild(tr);

		contentRow = DOM.createTR().cast();
		td = DOM.createTD().cast();
		td.setColSpan(2);
		contentRow.appendChild(td);
		table.appendChild(contentRow);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (content != null)
			adopt(content);
		if (!Beans.isDesignTime())
			createByJs(this, span);
		contentRow.getStyle().setDisplay(Display.NONE);
	}

	private native void createByJs(ExpandablePanel x, SpanElement element) /*-{
		$wnd
				.$(element)
				.click(
						function() {
							var el = $wnd.$(this);
							if (el.hasClass("ui-icon-circle-plus")) {
								if (x.@com.javexpress.gwt.library.ui.container.panel.ExpandablePanel::fireOnExpand()())
									el.removeClass("ui-icon-circle-plus")
											.addClass("ui-icon-circle-minus");
							} else {
								if (x.@com.javexpress.gwt.library.ui.container.panel.ExpandablePanel::fireOnCollapse()())
									el.removeClass("ui-icon-circle-minus")
											.addClass("ui-icon-circle-plus");
							}
						});
	}-*/;

	@Override
	protected void onUnload() {
		remove(content);
		content = null;
		table = null;
		contentRow = null;
		span = null;
		listener = null;
		super.onUnload();
	}

	public void setContent(Widget content) {
		this.content = content;
		contentRow.getChild(0).appendChild(content.getElement());
		if (isAttached())
			adopt(content);
	}

	//---------- EVENTS
	public boolean fireOnExpand() {
		contentRow.getStyle().clearDisplay();
		if (listener != null)
			listener.panelExpanded();
		return true;
	}

	public boolean fireOnCollapse() {
		contentRow.getStyle().setDisplay(Display.NONE);
		if (listener != null)
			listener.panelCollapsed();
		return true;
	}

}