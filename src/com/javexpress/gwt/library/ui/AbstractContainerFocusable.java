package com.javexpress.gwt.library.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Focusable;

public abstract class AbstractContainerFocusable extends AbstractContainer implements Focusable {

	public AbstractContainerFocusable(Element elem) {
		super(elem);
	}

	@Override
	public int getTabIndex() {
		return getElement().getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		getElement().setAttribute("accessKey", String.valueOf(key));
	}

	@Override
	public void setFocus(boolean focused) {
		if (focused)
			getElement().focus();
	}

	@Override
	public void setTabIndex(int index) {
		getElement().setTabIndex(index);
	}

	protected Element getSinkElement() {
		return getElement();
	}

	@Override
	public void sinkEvents(int eventBitsToAdd) {
		if (isOrWasAttached()) {
			Event.sinkEvents(getSinkElement(), eventBitsToAdd | Event.getEventsSunk(getSinkElement()));
		} else {
			super.sinkEvents(eventBitsToAdd);
		}
	}

	@Override
	public void unsinkEvents(int eventBitsToRemove) {
		if (isOrWasAttached()) {
			DOM.sinkEvents(getSinkElement(), DOM.getEventsSunk(getSinkElement()) & (~eventBitsToRemove));
		} else {
			super.unsinkEvents(eventBitsToRemove);
		}
	}

}