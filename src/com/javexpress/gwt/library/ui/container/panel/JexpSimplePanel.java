package com.javexpress.gwt.library.ui.container.panel;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.javexpress.gwt.library.ui.IJexpWidget;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;

public class JexpSimplePanel extends SimplePanel implements IJexpWidget, ISizeAwareWidget {

	public JexpSimplePanel() {
		super();
	}

	public JexpSimplePanel(final Element el) {
		super(el);
	}

	@Override
	public String getId() {
		return getElement().getId();
	}

	@Override
	public void onResize() {
		if (getWidget() instanceof RequiresResize)
			((RequiresResize) getWidget()).onResize();
	}

	@Override
	protected void onUnload() {
		nullify();
		super.onUnload();
	}

	protected void nullify() {
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