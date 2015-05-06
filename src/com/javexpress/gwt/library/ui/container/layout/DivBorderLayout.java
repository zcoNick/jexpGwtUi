package com.javexpress.gwt.library.ui.container.layout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class DivBorderLayout extends AbstractContainer implements ISizeAwareWidget {

	private Element	top, left, center, bottom, right;
	private Widget	topWidget, leftWidget, centerWidget,
					bottomWidget, rightWidget;
	private String	topHeight, bottomHeight, leftWidth, rightWidth;

	public DivBorderLayout() {
		this(false);
	}

	public DivBorderLayout(final boolean fitToParent) {
		super(DOM.createDiv());
		getElement().addClassName("jexpBorderBox");
		getElement().getStyle().setPosition(Position.RELATIVE);
		getElement().getStyle().setDisplay(Display.BLOCK);
		getElement().getStyle().setOverflow(Overflow.HIDDEN);
		setFitToParent(fitToParent);
	}

	public void setFitToParent(boolean fitToParent) {
		if (fitToParent) {
			getElement().getStyle().setWidth(100, Unit.PCT);
			getElement().getStyle().setHeight(100, Unit.PCT);
		}
	}

	public void setTopWidget(DivPanelInfo divPanel) throws Exception {
		setTopWidget(divPanel != null ? divPanel.widget : null, divPanel != null ? divPanel.size : null);
		applyPanelStyle(divPanel, top);
	}

	public void setTopWidget(Widget widget) throws Exception {
		if (widget==null){
			setTopWidget(null, null);
			return;
		}
		String height = widget.getElement().getStyle().getHeight();
		if (JsUtil.isEmpty(height))
			throw new Exception("Height is not defined for " + widget.getElement().getId());
		widget.setHeight("100%");
		setTopWidget(widget, height);
	}

	private void applyPanelStyle(DivPanelInfo divPanel, Element elm) {
		if (elm != null && divPanel != null && divPanel.panelStyle)
			elm.addClassName("ui-widget-content ui-corner-all");
	}

	public void setTopWidget(final IsWidget widget, String height) throws Exception {
		setTopWidget((Widget) widget, height);
	}

	public void setTopWidget(final Widget widget, String height) throws Exception {
		if (topWidget != null) {
			remove(topWidget);
			topWidget = null;
		}
		if (top != null) {
			top.removeFromParent();
			top = null;
		}
		if (widget == null)
			return;
		if (left != null)
			throw new Exception("Top widget cant be set after the Left widget is set");
		if (center != null)
			throw new Exception("Center widget cant be set after the Center widget is set");
		topWidget = widget;
		top = DOM.createDiv();
		top.getStyle().setPosition(Position.ABSOLUTE);
		top.getStyle().setDisplay(Display.BLOCK);
		top.getStyle().setWidth(100, Unit.PCT);
		topHeight = height;
		top.getStyle().setProperty("height", topHeight);
		getElement().appendChild(top);
		if (topWidget != null) {
			add(widget, top);
		}
	}

	public void setBottomWidget(DivPanelInfo divPanel) throws Exception {
		setBottomWidget(divPanel != null ? divPanel.widget : null, divPanel != null ? divPanel.size : null);
		applyPanelStyle(divPanel, bottom);
	}

	public void setBottomWidget(Widget widget) throws Exception {
		if (widget==null){
			setBottomWidget(null, null);
			return;
		}
		String height = widget.getElement().getStyle().getHeight();
		if (JsUtil.isEmpty(height))
			throw new Exception("Height is not defined for " + widget.getElement().getId());
		widget.setHeight("100%");
		setBottomWidget(widget, height);
	}

	public void setBottomWidget(final Widget widget, String height) throws Exception {
		if (bottomWidget != null) {
			remove(bottomWidget);
			bottomWidget = null;
		}
		if (bottom != null) {
			bottom.removeFromParent();
			bottom = null;
		}
		if (widget == null)
			return;
		if (left != null)
			throw new Exception("Bottom widget cant be set after the Left widget is set");
		if (right != null)
			throw new Exception("Bottom widget cant be set after the Right widget is set");
		if (center != null)
			throw new Exception("Bottom widget cant be set after the Center widget is set");
		bottomWidget = widget;
		bottom = DOM.createDiv();
		bottom.getStyle().setPosition(Position.ABSOLUTE);
		bottom.getStyle().setDisplay(Display.BLOCK);
		bottom.getStyle().setWidth(100, Unit.PCT);
		bottom.getStyle().setBottom(0, Unit.EM);
		bottomHeight = height;
		//bottomHeight = increaseDecimal(height);
		bottom.getStyle().setProperty("height", bottomHeight);
		getElement().appendChild(bottom);
		if (bottomWidget != null) {
			add(widget, bottom);
		}
	}

	public void setLeftWidget(DivPanelInfo divPanel) throws Exception {
		setLeftWidget(divPanel != null ? divPanel.widget : null, divPanel != null ? divPanel.size : null);
		applyPanelStyle(divPanel, left);
	}

	public void setLeftWidget(Widget widget) throws Exception {
		if (widget==null){
			setLeftWidget(null, null);
			return;
		}
		String width = widget.getElement().getStyle().getWidth();
		if (JsUtil.isEmpty(width))
			throw new Exception("Width is not defined for " + widget.getElement().getId());
		widget.setWidth("100%");
		setLeftWidget(widget, width);
	}

	public void setLeftWidget(final Widget widget, String width) throws Exception {
		if (leftWidget != null) {
			remove(leftWidget);
			leftWidget = null;
		}
		if (left != null) {
			left.removeFromParent();
			left = null;
		}
		if (widget == null)
			return;
		if (center != null)
			throw new Exception("Left widget cant be set after the Center widget is set");
		leftWidget = widget;
		left = DOM.createDiv();
		left.getStyle().setPosition(Position.ABSOLUTE);
		left.getStyle().setDisplay(Display.INLINE_BLOCK);
		if (topHeight != null)
			left.getStyle().setProperty("top", topHeight);
		else
			left.getStyle().setTop(0, Unit.PX);
		if (bottomHeight != null)
			left.getStyle().setProperty("bottom", bottomHeight);
		else
			left.getStyle().setBottom(0, Unit.PX);

		left.getStyle().setProperty("width", width);
		getElement().appendChild(left);
		leftWidth = width;
		if (leftWidget != null) {
			add(widget, left);
		}
	}

	public void setRightWidget(DivPanelInfo divPanel) throws Exception {
		setRightWidget(divPanel != null ? divPanel.widget : null, divPanel != null ? divPanel.size : null);
		applyPanelStyle(divPanel, right);
	}

	public void setRightWidget(Widget widget) throws Exception {
		if (widget==null){
			setRightWidget(null, null);
			return;
		}
		String width = widget.getElement().getStyle().getWidth();
		if (JsUtil.isEmpty(width))
			throw new Exception("Width is not defined for " + widget.getElement().getId());
		widget.setWidth("100%");
		setRightWidget(widget, width);
	}

	public void setRightWidget(final Widget widget, String width) throws Exception {
		if (rightWidget != null) {
			remove(rightWidget);
			rightWidget = null;
		}
		if (right != null) {
			right.removeFromParent();
			right = null;
		}
		if (widget == null)
			return;
		if (center != null)
			throw new Exception("Right widget cant be set after the Center widget is set");
		rightWidget = widget;
		right = DOM.createDiv();
		right.getStyle().setPosition(Position.ABSOLUTE);
		right.getStyle().setDisplay(Display.INLINE_BLOCK);
		if (topHeight != null)
			right.getStyle().setProperty("top", topHeight);
		else
			right.getStyle().setTop(0, Unit.PX);
		if (bottomHeight != null)
			right.getStyle().setProperty("bottom", bottomHeight);
		else
			right.getStyle().setBottom(0, Unit.PX);
		right.getStyle().setProperty("width", width);
		right.getStyle().setRight(0, Unit.PX);
		getElement().appendChild(right);
		rightWidth = width;
		if (rightWidget != null) {
			add(widget, right);
		}
	}

	public void setCenterWidget(final IsWidget widget) {
		setCenterWidget((Widget) widget, false);
	}

	public void setCenterWidget(final Widget widget) {
		if (widget!=null){
			widget.setWidth("100%");
			widget.setHeight("100%");
		}
		setCenterWidget(widget, false);
	}

	public void setCenterWidget(final Widget widget, Boolean overflowAuto) {
		if (centerWidget != null) {
			remove(centerWidget);
			centerWidget = null;
		}
		if (center != null) {
			center.removeFromParent();
			center = null;
		}
		if (widget == null)
			return;
		centerWidget = widget;
		center = DOM.createDiv();
		center.getStyle().setPosition(Position.ABSOLUTE);
		center.getStyle().setDisplay(Display.INLINE_BLOCK);
		if (topHeight != null)
			center.getStyle().setProperty("top", topHeight);
		else
			center.getStyle().setTop(0, Unit.PX);
		if (bottomHeight != null)
			center.getStyle().setProperty("bottom", bottomHeight);
		else
			center.getStyle().setBottom(0, Unit.PX);
		if (leftWidth != null)
			center.getStyle().setProperty("left", leftWidth);
		else
			center.getStyle().setLeft(0, Unit.PX);
		if (rightWidth != null)
			center.getStyle().setProperty("right", rightWidth);
		else
			center.getStyle().setRight(0, Unit.PX);
		if (overflowAuto)
			center.getStyle().setOverflow(Overflow.AUTO);
		getElement().appendChild(center);
		if (centerWidget != null) {
			add(widget, center);
		}
	}

	private String increaseDecimal(String h) {
		if (h.endsWith("%")) {
			return h.substring(0, h.length() - 1) + (h.indexOf(".") > -1 ? "" : ".0") + "1%";
		}
		if (h.endsWith("em")) {
			return h.substring(0, h.length() - 2) + (h.indexOf(".") > -1 ? "" : ".0") + "1em";
		}
		return h;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		top = null;
		if (topWidget != null)
			remove(topWidget);
		topWidget = null;
		topHeight = null;
		left = null;
		if (leftWidget != null)
			remove(leftWidget);
		leftWidget = null;
		leftWidth = null;
		right = null;
		if (rightWidget != null)
			remove(rightWidget);
		rightWidget = null;
		rightWidth = null;
		center = null;
		if (centerWidget != null)
			remove(centerWidget);
		centerWidget = null;
		bottom = null;
		if (bottomWidget != null)
			remove(bottomWidget);
		bottomWidget = null;
		bottomHeight = null;
		super.onUnload();
	}

	@Override
	public void onResize() {
		performChildOnResize(topWidget);
		performChildOnResize(leftWidget);
		performChildOnResize(centerWidget);
		performChildOnResize(rightWidget);
		performChildOnResize(bottomWidget);
	}

	private void performChildOnResize(Widget w) {
		if (w != null && w instanceof RequiresResize) {
			((RequiresResize) w).onResize();
		}
	}

	public Widget getTopWidget() {
		return topWidget;
	}

	public Widget getLeftWidget() {
		return leftWidget;
	}

	public Widget getRightWidget() {
		return leftWidget;
	}

	public Widget getCenterWidget() {
		return centerWidget;
	}

	public Widget getBottomWidget() {
		return bottomWidget;
	}

	@Override
	public void add(Widget widget) {
		setCenterWidget(widget);
	}

	public void addAbsolutePositionedChild(Widget widget) {
		super.add(widget, getElement());
	}

}