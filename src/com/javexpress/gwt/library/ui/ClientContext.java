package com.javexpress.gwt.library.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.javexpress.common.model.item.exception.AppException;
import com.javexpress.gwt.library.shared.nls.CommonResources;
import com.javexpress.gwt.library.ui.dialog.WindowView;
import com.javexpress.gwt.library.ui.event.ExceptionThrownEvent;
import com.javexpress.gwt.library.ui.event.FormShowInWindowRequest;
import com.javexpress.gwt.library.ui.event.HelpRequest;
import com.javexpress.gwt.library.ui.form.IJiraEnabledForm;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JexpCallback;

public abstract class ClientContext implements EntryPoint {

	public final static String				REPORT_NODE_PREFIX	= "rnrep_";
	public final static String				PROCESS_NODE_PREFIX	= "rnprc_";

	public static ClientContext				instance			= null;
	public static final CommonResources		nlsCommon			= GWT.create(CommonResources.class);
	public static final EventBus			EVENT_BUS			= GWT.create(SimpleEventBus.class);
	public static final IResourceInjector	resourceInjector	= GWT.create(IResourceInjector.class);
	public static final int					HELP_KEYCODE		= 170;

	@Override
	public void onModuleLoad() {
		ClientContext.instance = this;
	}

	public void openHelp(IUIComposite form) {
		EVENT_BUS.fireEvent(new HelpRequest(form));
	}

	public void showError(String windowId, AppException ae) {
		EVENT_BUS.fireEvent(new ExceptionThrownEvent(windowId, ae));
	}

	public void showInWindow(IUIComposite form, boolean modal, JexpCallback<WindowView> callback) {
		showInWindow(form, modal, null, null, callback);
	}

	public void showInWindow(IUIComposite form, boolean modal) {
		showInWindow(form, modal, null, null, null);
	}

	public void showInWindow(IUIComposite form, boolean modal, Integer x, Integer y, JexpCallback<WindowView> callback) {
		EVENT_BUS.fireEvent(new FormShowInWindowRequest(form, modal, x, y, callback));
	}

	public void openJiraForm(IJiraEnabledForm iJiraEnabledForm) {
	}

}