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

@Deprecated
public class DivBorderLayoutCalc extends AbstractContainer implements ISizeAwareWidget {

	//http://jsfiddle.net/C2SdJ/7/

	private Element	top, left, center, bottom, right;
	private Widget	topWidget, leftWidget, centerWidget,
					bottomWidget, rightWidget;
	private String	topHeight, bottomHeight, leftWidth, rightWidth;

	public DivBorderLayoutCalc() {
		this(false);
	}

	public DivBorderLayoutCalc(final boolean fitToParent) {
		super(DOM.createDiv());
		getElement().addClassName("jexpBorderBox");
		getElement().getStyle().setPosition(Position.RELATIVE);
		getElement().getStyle().setDisplay(Display.BLOCK);
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

	private void applyPanelStyle(DivPanelInfo divPanel, Element elm) {
		if (elm != null && divPanel != null && divPanel.panelStyle)
			elm.addClassName("ui-widget-content ui-corner-all");
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
		top.addClassName("jesBorderFix");
		top.getStyle().setPosition(Position.ABSOLUTE);
		top.getStyle().setDisplay(Display.BLOCK);
		top.getStyle().setWidth(100, Unit.PCT);
		top.getStyle().setProperty("height", height);
		topHeight = height;
		getElement().appendChild(top);
		if (topWidget != null) {
			add(widget, top);
		}
	}

	public void setBottomWidget(DivPanelInfo divPanel) throws Exception {
		setBottomWidget(divPanel != null ? divPanel.widget : null, divPanel != null ? divPanel.size : null);
		applyPanelStyle(divPanel, bottom);
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
			throw new Exception("Bottom wigdet cant be set after the Left widget is set");
		if (right != null)
			throw new Exception("Bottom wigdet cant be set after the Right widget is set");
		if (center != null)
			throw new Exception("Bottom wigdet cant be set after the Center widget is set");
		bottomWidget = widget;
		bottom = DOM.createDiv();
		bottom.addClassName("jesBorderFix");
		bottom.getStyle().setPosition(Position.ABSOLUTE);
		bottom.getStyle().setDisplay(Display.BLOCK);
		bottom.getStyle().setWidth(100, Unit.PCT);
		bottom.getStyle().setProperty("height", height);
		bottomHeight = height;
		getElement().appendChild(bottom);
		if (bottomWidget != null) {
			add(widget, bottom);
		}
	}

	public void setLeftWidget(DivPanelInfo divPanel) throws Exception {
		setLeftWidget(divPanel != null ? divPanel.widget : null, divPanel != null ? divPanel.size : null);
		applyPanelStyle(divPanel, left);
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
			throw new Exception("Left wigdet cant be set after the Center widget is set");
		leftWidget = widget;
		left = DOM.createDiv();
		left.addClassName("jesBorderFix");
		left.getStyle().setPosition(Position.ABSOLUTE);
		left.getStyle().setDisplay(Display.INLINE_BLOCK);
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
			throw new Exception("Right wigdet cant be set after the Center widget is set");
		rightWidget = widget;
		right = DOM.createDiv();
		right.addClassName("jesBorderFix");
		right.getStyle().setPosition(Position.ABSOLUTE);
		right.getStyle().setDisplay(Display.INLINE_BLOCK);
		right.getStyle().setProperty("width", width);
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
		center.addClassName("jesBorderFix");
		center.getStyle().setPosition(Position.ABSOLUTE);
		center.getStyle().setDisplay(Display.INLINE_BLOCK);
		if (overflowAuto)
			center.getStyle().setOverflow(Overflow.AUTO);
		getElement().appendChild(center);
		if (centerWidget != null) {
			add(widget, center);
		}
	}

	@Override
	protected void onLoad() {
		_updateSize(getElement(), top, topHeight, bottom, bottomHeight, left, leftWidth, right, rightWidth, center);
		super.onLoad();
	}

	private native void _updateSize(Element container, Element top, String topHeight, Element bottom, String bottomHeight, Element left, String leftWidth, Element right, String rightWidth, Element center) /*-{
		var elm = $wnd.$(container);
		var oldOverflow = elm.css("overflow");
		elm.css("overflow", "hidden");
		var w = elm.width();
		var h = elm.height();

		var t = top != null ? $wnd.$(top).css("height", topHeight) : null;
		var th = t != null ? Math.ceil(t.height()) : 0;

		var b = bottom != null ? $wnd.$(bottom).css("height", bottomHeight)
				: null;
		var bh = b != null ? Math.ceil(b.height()) : 0;

		var mh = h - th - bh;

		var l = left != null ? $wnd.$(left).css({
			height : mh,
			width : leftWidth
		}) : null;
		var lw = l != null ? Math.ceil(l.width()) : 0;

		var r = right != null ? $wnd.$(right).css({
			height : mh,
			width : rightWidth
		}) : null;
		var rw = r != null ? Math.ceil(r.width()) : 0;

		if (t != null)
			t.css("height", th);
		if (b != null)
			b.css({
				height : bh,
				top : th + mh
			});
		if (l != null)
			l.css({
				width : lw,
				top : th
			});
		if (r != null)
			r.css({
				width : rw,
				top : th
			});
		if (center != null) {
			$wnd.$(center).css({
				width : w - lw - rw,
				height : mh,
				left : lw,
				top : th
			});
		}
		elm.css("overflow", oldOverflow);

		$wnd.console.debug("w:" + w + ",h:" + h + ",th:" + th + ",bh:" + bh
				+ ",mh:" + mh + ",lw:" + lw + ",rw:" + rw);
	}-*/;

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
		_updateSize(getElement(), top, topHeight, bottom, bottomHeight, left, leftWidth, right, rightWidth, center);
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