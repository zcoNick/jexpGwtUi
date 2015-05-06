package com.javexpress.gwt.library.ui.container.buttonbar;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.AbstractContainer;

public class ToolBar extends AbstractContainer {

	/** Designer compatible constructor */
	public ToolBar(boolean center) {
		super(DOM.createDiv());
		addStyleName("ui-state-default jexpBorderBox jesToolBar");
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
	
	public void add(int index, Widget button) {
		if (getWidgetCount() <= index)
			add(button);
		else
			insert(button, getElement(), index, true);
	}

}