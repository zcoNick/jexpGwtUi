package com.javexpress.gwt.library.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.javexpress.common.model.item.exception.AppException;
import com.javexpress.gwt.library.shared.nls.CommonResources;
import com.javexpress.gwt.library.ui.form.IUIComposite;

public abstract class ClientContext implements EntryPoint {

	public static ClientContext		instance	= null;
	public static CommonResources	nlsCommon	= GWT.create(CommonResources.class);
	public static EventBus			EVENT_BUS	= GWT.create(SimpleEventBus.class);

	abstract public void openHelp(IUIComposite widget);

	abstract public void showError(String windowId, AppException ae);

	abstract public void showInWindow(IUIComposite form, boolean modal);

}