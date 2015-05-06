package com.javexpress.gwt.library.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractContainer extends ComplexPanel implements RequiresResize {

	public AbstractContainer(final Element elem) {
		setElement(elem);
	}

	@Override
	public void add(final Widget child) {
		super.add(child, getElement());
	}

	public void add(final Widget child, int index) {
		super.insert(child, getElement(), index, true);
	}
	
	public String getId() {
		return getElement().getId();
	}
	
	public void onResize() {
		for (Widget w : getChildren())
			if (w instanceof RequiresResize)
				((RequiresResize) w).onResize();
	}
	
	@Override
	protected void onUnload() {
		clear();
		super.onUnload();
	}

}