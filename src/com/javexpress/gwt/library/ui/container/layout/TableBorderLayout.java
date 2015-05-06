package com.javexpress.gwt.library.ui.container.layout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class TableBorderLayout extends AbstractContainer implements IBorderLayout {

	private Element	topCell, bottomCell, leftCell, centerCell;
	private Widget	topWidget, leftWidget, centerWidget, bottomWidget;

	public TableBorderLayout(final boolean fitToParent) {
		this(0, fitToParent);
	}

	public TableBorderLayout(final int gap, final boolean fitToParent) {
		super(DOM.createTable());
		getElement().addClassName("ui-widget jexpBorderBox");
		getElement().setAttribute("cellpadding", "0");
		getElement().setAttribute("cellspacing", String.valueOf(gap));
		if (fitToParent) {
			getElement().setAttribute("width", "100%");
			getElement().setAttribute("height", "100%");
		}
		Element tr = DOM.createTR();
		tr.setAttribute("height", "0px");
		tr.addClassName("top");
		Element td = DOM.createTD();
		td.setAttribute("colspan", "2");
		tr.appendChild(td);
		topCell = td;
		getElement().appendChild(tr);

		tr = DOM.createTR();
		tr.setAttribute("valign", "top");
		tr.setAttribute("height", "100%");
		tr.addClassName("middle");
		td = DOM.createTD();
		td.setAttribute("width", "0px");
		tr.appendChild(td);
		leftCell = td;
		td = DOM.createTD();
		centerCell = td;
		tr.appendChild(td);
		getElement().appendChild(tr);

		tr = DOM.createTR();
		tr.setAttribute("height", "0px");
		tr.addClassName("bottom");
		td = DOM.createTD();
		td.setAttribute("colspan", "2");
		tr.appendChild(td);
		bottomCell = td;
		getElement().appendChild(tr);
	}

	@Override
	public void add(final Widget child) {
		if (child instanceof LayoutPanel)
			add(((LayoutPanel) child).getPosition(), child);
		else
			super.add(child);
	}

	public void add(final Position position, final Widget child) {
		if (position == Position.TOP)
			setTopWidget(child);
		else if (position == Position.CENTER)
			setCenterWidget(child);
		else if (position == Position.LEFT)
			setLeftWidget(child);
		else if (position == Position.BOTTOM)
			setBottomWidget(child);
	}

	@Override
	public Widget getTopWidget() {
		return topWidget;
	}

	@Override
	public void setTopWidget(final Widget widget) {
		JsUtil.clearChilds(topCell);
		if (topWidget != null && isAttached())
			orphan(topWidget);
		topWidget = widget;
		topCell.getParentElement().setAttribute("height", "1px");
		if (widget != null)
			add(widget, topCell);
	}

	@Override
	public Widget getLeftWidget() {
		return leftWidget;
	}

	@Override
	public void setLeftWidget(final Widget widget) {
		setLeftWidget(widget, null);
	}

	@Override
	public void setLeftWidget(final Widget widget, final String width) {
		JsUtil.clearChilds(leftCell);
		if (leftWidget != null && isAttached())
			orphan(leftWidget);
		leftWidget = widget;
		if (width != null)
			leftCell.setAttribute("width", width);
		if (widget != null)
			add(widget, leftCell);
	}

	@Override
	public Widget getCenterWidget() {
		return centerWidget;
	}

	@Override
	public void setCenterWidget(final Widget widget) {
		JsUtil.clearChilds(centerCell);
		//if (centerWidget!=null&&isAttached())
		//orphan(centerWidget);
		centerWidget = widget;
		Element div = DOM.createDiv();
		div.setAttribute("style", "width:auto;height:100%;padding:0;margin:0;border:0;" + (!JsUtil.isBrowserChrome() ? "overflow:auto" : ""));
		centerCell.appendChild(div);
		if (widget != null)
			add(widget, div);
	}

	@Override
	public Widget getBottomWidget() {
		return bottomWidget;
	}

	@Override
	public void setBottomWidget(final Widget widget) {
		JsUtil.clearChilds(bottomCell);
		if (bottomWidget != null && isAttached())
			orphan(bottomWidget);
		bottomWidget = widget;
		if (widget != null) {
			bottomCell.getParentElement().setAttribute("height", "1px");
			add(widget, bottomCell);
			if (isAttached() && widget instanceof RequiresResize)
				((RequiresResize) widget).onResize();
		}
	}

	@Override
	public void onResize() {
		_layout(getElement());
		for (Widget w : getChildren())
			if (w instanceof RequiresResize)
				((RequiresResize) w).onResize();
	}

	public static native void _layout(Element elm) /*-{
		var el = $wnd.$(elm);
		var cellspacing = el.attr("cellspacing");
		if (!cellspacing)
			cellspacing = 0;
		var topH = el.children(".top").height();
		var bottomH = el.children(".bottom").height();
		var parentH = el.parent().height();
		var middleH = parentH - topH - bottomH - (cellspacing * 4) - 1;
		el.children(".middle").attr("height", middleH).children("td").attr(
				"height", middleH);
	}-*/;

	@Override
	public boolean isLayoutFilled() {
		return getChildren() != null && getChildren().size() > 0;
	}

	@Override
	protected void onUnload() {
		topCell = null;
		bottomCell = null;
		leftCell = null;
		centerCell = null;
		topWidget = null;
		leftWidget = null;
		centerWidget = null;
		bottomWidget = null;
		super.onUnload();
	}

}