package com.javexpress.gwt.library.ui.container.buttonbar;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ToolBar extends AbstractContainer {

	/** Designer compatible constructor */
	public ToolBar() {
		super(DOM.createDiv());
		addStyleName((JsUtil.USE_BOOTSTRAP ? "" : "ui-state-default ") + "jexpToolBar");
		getElement().getStyle().setBorderStyle(BorderStyle.NONE);
	}

	public void setAbsolutePositionPx(Double top, Double left, Double bottom, Double right) {
		Style s = getElement().getStyle();
		s.setPosition(Position.ABSOLUTE);
		if (top != null)
			s.setTop(top, Unit.PX);
		if (left != null)
			s.setLeft(left, Unit.PX);
		if (bottom != null)
			s.setBottom(bottom, Unit.PX);
		if (right != null)
			s.setRight(right, Unit.PX);
	}

	@Override
	public void add(Widget child) {
		child.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		child.getElement().getStyle().setMarginLeft(4, Unit.PX);
		super.add(child);
	}

	public void add(int index, Widget button) {
		if (getWidgetCount() <= index)
			add(button);
		else {
			button.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
			button.getElement().getStyle().setMarginLeft(4, Unit.PX);
			insert(button, getElement(), index, true);
		}
	}

}