package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Clear;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.AbstractContainerFocusable;

public class ButtonSet extends AbstractContainerFocusable {

	private Element	left;
	private Element	center;
	private Element	right;

	public ButtonSet() {
		super(DOM.createDiv());
		getElement().setClassName("jexp-ui-buttonset");
		left = DOM.createDiv();
		left.setClassName("jexp-ui-buttonset-left");
		getElement().appendChild(left);
		center = DOM.createDiv();
		center.setClassName("jexp-ui-buttonset-center");
		getElement().appendChild(center);
		right = DOM.createDiv();
		right.setClassName("jexp-ui-buttonset-right");
		getElement().appendChild(right);
		Element clear = DOM.createDiv();
		clear.getStyle().setClear(Clear.BOTH);
		getElement().appendChild(clear);
	}

	@Override
	public void add(Widget child) {
		addRight(child,-1);
	}

	public void addRight(Widget child) {
		addRight(child, -1);
	}

	public void addRight(Widget child, int index) {
		if (index==-1||index==right.getChildCount())
			super.add(child, right);
		else
			super.insert(child, right, index, true);
	}

	public void addCenter(Widget child) {
		addCenter(child,-1);
	}

	public void addCenter(Widget child, int index) {
		if (index==-1||index==center.getChildCount())
			super.add(child, center);
		else
			super.insert(child, center, index, true);
	}

	public void addLeft(Widget child) {
		addLeft(child, -1);
	}

	public void addLeft(Widget child, int index) {
		if (index==-1||index==left.getChildCount())
			super.add(child, left);
		else
			super.insert(child, left, index, true);
	}

	@Override
	protected void onUnload() {
		left = null;
		center = null;
		right = null;
		super.onUnload();
	}

	public int getRightCount() {
		return right.getChildCount();
	}

	public int getLeftCount() {
		return left.getChildCount();
	}

	public int getCenterCount() {
		return center.getChildCount();
	}
	
}