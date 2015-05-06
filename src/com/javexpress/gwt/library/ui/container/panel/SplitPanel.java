package com.javexpress.gwt.library.ui.container.panel;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class SplitPanel extends AbstractContainer implements ISizeAwareWidget {

	public static void fillResources(final WidgetBundles wb) {
		wb.addStyleSheet("scripts/splitter/jquery.splitter.css");
		wb.addJavaScript("scripts/splitter/jquery.splitter-1.1.js");
	}

	private JsonMap			options;
	private final boolean	horizontal;
	private Element			leftOrTopDiv;
	private Element			rightOrBottomDiv;
	private Widget			leftOrTop, rightOrBottom;

	public SplitPanel(Widget parent, String id, boolean horizontal, boolean fitToParent) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.SPLITPANEL_PREFIX, id);
		getElement().addClassName("jexpBorderBox");
		this.horizontal = horizontal;
		options = new JsonMap();

		leftOrTopDiv = DOM.createDiv();
		leftOrTopDiv.getStyle().setPosition(Position.RELATIVE);
		setLeftOrTopSize(50, Unit.PCT);
		if (horizontal)
			leftOrTopDiv.getStyle().setWidth(100, Unit.PCT);
		else {
			leftOrTopDiv.getStyle().setHeight(100, Unit.PCT);
			leftOrTopDiv.getStyle().setFloat(Float.LEFT);
		}
		JsUtil.ensureSubId(getElement(), leftOrTopDiv, "l");
		getElement().appendChild(leftOrTopDiv);

		rightOrBottomDiv = DOM.createDiv();
		rightOrBottomDiv.getStyle().setPosition(Position.RELATIVE);
		setRightOrBottomSize(50, Unit.PCT);
		if (horizontal)
			rightOrBottomDiv.getStyle().setWidth(100, Unit.PCT);
		else {
			rightOrBottomDiv.getStyle().setHeight(100, Unit.PCT);
			rightOrBottomDiv.getStyle().setFloat(Float.RIGHT);
		}
		JsUtil.ensureSubId(getElement(), rightOrBottomDiv, "r");
		getElement().appendChild(rightOrBottomDiv);

		if (fitToParent) {
			getElement().getStyle().setWidth(100, Unit.PCT);
			getElement().getStyle().setHeight(100, Unit.PCT);
		}
	}

	public void setLeftOrTopSize(double size, Unit unit) {
		if (!horizontal) {
			leftOrTopDiv.getStyle().setWidth(size, unit);
		} else {
			leftOrTopDiv.getStyle().setHeight(size, unit);
		}
	}

	public void setLeftOrTopMinSize(int pct) {
		options.setInt("minAsize", pct);
	}

	public void setLeftOrTopMaxSize(int pct) {
		options.setInt("maxAsize", pct);
	}

	public void setRightOrBottomMinSize(int pct) {
		options.setInt("minBsize", pct);
	}

	public void setRightOrBottomMaxSize(int pct) {
		options.setInt("maxBsize", pct);
	}

	public void setRightOrBottomSize(double size, Unit unit) {
		if (!horizontal) {
			rightOrBottomDiv.getStyle().setWidth(size, unit);
		} else {
			rightOrBottomDiv.getStyle().setHeight(size, unit);
		}
	}

	public void setLeftOrTopWidget(final Widget widget, String size) {
		if (leftOrTop != null) {
			remove(leftOrTop);
			leftOrTop = null;
		}
		if (widget == null)
			return;
		leftOrTop = widget;
		leftOrTopDiv.getStyle().setProperty(horizontal ? "height" : "width", size);
		add(widget, leftOrTopDiv);
	}

	public void setRightOrBottomWidget(final Widget widget, String size) {
		if (rightOrBottom != null) {
			remove(rightOrBottom);
			rightOrBottom = null;
		}
		if (widget == null)
			return;
		rightOrBottom = widget;
		rightOrBottomDiv.getStyle().setProperty(horizontal ? "height" : "width", size);
		add(widget, rightOrBottomDiv);
	}

	public void setLeftOrTopCloseable(boolean value) {
		if (value)
			options.setInt("closeableto", 0);
		else
			options.clear("closeableto");
	}

	public void setRightOrBottomCloseable(boolean value) {
		if (value)
			options.setInt("closeableto", 1);
		else
			options.clear("closeableto");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement(), leftOrTopDiv, rightOrBottomDiv, options.getJavaScriptObject(), horizontal);
	}

	private native void createByJs(SplitPanel x, Element el, Element left, Element right, JavaScriptObject options, boolean horizontal) /*-{
		options.splitHorizontal = horizontal;
		options.onResize = function(pct) {
			x.@com.javexpress.gwt.library.ui.container.panel.SplitPanel::fireOnResize()();
			return false;
		};
		options.A = $wnd.$(left);
		options.B = $wnd.$(right);
		//,slave:$("#rightSplitterContainer")
		$wnd.$(el).splitter(options);
	}-*/;

	@Override
	protected void onUnload() {
		options = null;
		clear();
		leftOrTop = null;
		rightOrBottom = null;
		destroyByJs(getElement());
		leftOrTopDiv = null;
		rightOrBottomDiv = null;
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).splitter("destroy");
	}-*/;

	private native void _updatePosition(Element element) /*-{
		$wnd.$(element).splitter("updatePosition", true);
	}-*/;

	//---EVENTS
	private void fireOnResize() {
		if (isAttached()) {
			if (leftOrTop instanceof RequiresResize)
				((RequiresResize) leftOrTop).onResize();
			if (rightOrBottom instanceof RequiresResize)
				((RequiresResize) rightOrBottom).onResize();
		}
	}

	@Override
	public void onResize() {
		_updatePosition(getElement());
	}

	public Widget getLeftOrTopWidget() {
		return leftOrTop;
	}

	public Widget getRightOrBottomWidget() {
		return rightOrBottom;
	}

}