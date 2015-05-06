package com.javexpress.gwt.library.ui.container.panel;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Focusable;

public abstract class SimplePanelFocusable extends JexpSimplePanel implements Focusable {

	public SimplePanelFocusable() {
		super();
	}

	public SimplePanelFocusable(Element el) {
		super(el);
	}

	@Override
	public void setFocus(boolean focused) {
		Focusable widget = getFocusWidget();
		if (widget != null) {
			widget.setFocus(focused);
			return;
		}
	}

	protected abstract Focusable getFocusWidget();

	@Override
	public int getTabIndex() {
		return getElement().getTabIndex();
	}

	@Override
	public void setTabIndex(int index) {
		getElement().setTabIndex(index);
	}

	@Override
	public void setAccessKey(char key) {
		getElement().setAttribute("accessKey", String.valueOf(key));
	}

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return addDomHandler(handler, KeyDownEvent.getType());
	}

}