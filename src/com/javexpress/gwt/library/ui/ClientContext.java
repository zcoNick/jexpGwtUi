package com.javexpress.gwt.library.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.javexpress.common.model.item.exception.AppException;
import com.javexpress.gwt.library.shared.nls.CommonResources;
import com.javexpress.gwt.library.ui.event.ExceptionThrownEvent;
import com.javexpress.gwt.library.ui.event.FormShowInWindowRequest;
import com.javexpress.gwt.library.ui.event.HelpRequest;
import com.javexpress.gwt.library.ui.form.IUIComposite;

public abstract class ClientContext implements EntryPoint {

	public static ClientContext		instance	= null;
	public static CommonResources	nlsCommon	= GWT.create(CommonResources.class);
	public static EventBus			EVENT_BUS	= GWT.create(SimpleEventBus.class);

	public void openHelp(IUIComposite form) {
		EVENT_BUS.fireEvent(new HelpRequest(form));
	}

	public void showError(String windowId, AppException ae) {
		EVENT_BUS.fireEvent(new ExceptionThrownEvent(windowId, ae));
	}

	public void showInWindow(IUIComposite form, boolean modal) {
		EVENT_BUS.fireEvent(new FormShowInWindowRequest(form, modal));
	}

}