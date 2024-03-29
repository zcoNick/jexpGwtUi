package com.javexpress.gwt.library.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.javexpress.common.model.item.exception.AppException;
import com.javexpress.gwt.library.shared.nls.CommonResources;
import com.javexpress.gwt.library.ui.dialog.StatusDialog;
import com.javexpress.gwt.library.ui.dialog.WindowView;
import com.javexpress.gwt.library.ui.event.ExceptionThrownEvent;
import com.javexpress.gwt.library.ui.event.FormShowInWindowRequest;
import com.javexpress.gwt.library.ui.event.HelpRequest;
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

	private StatusDialog					currentStatusDialog;

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

	public void showInWindow(IUIComposite form, boolean modal, Element originator) {
		showInWindow(form, modal, null, null, originator, null);
	}

	public void showInWindow(IUIComposite form, boolean modal, Element originator, JexpCallback<WindowView> callback) {
		showInWindow(form, modal, null, null, originator, callback);
	}

	public void showInWindow(IUIComposite form, boolean modal, JexpCallback<WindowView> callback) {
		showInWindow(form, modal, null, null, null, callback);
	}

	public void showInWindow(IUIComposite form, boolean modal) {
		showInWindow(form, modal, null, null, null, null);
	}

	public void showInWindow(IUIComposite form, boolean modal, Integer x, Integer y, JexpCallback<WindowView> callback) {
		showInWindow(form, modal, x, y, null, callback);
	}

	public void showInWindow(IUIComposite form, boolean modal, Integer x, Integer y, Element originator, JexpCallback<WindowView> callback) {
		EVENT_BUS.fireEvent(new FormShowInWindowRequest(form, modal, x, y, callback));
	}

	public void showBusy() {
		showBusy(StatusDialog.DEFAULT_LINE, nlsCommon.yukleniyor());
	}

	public void showBusy(String id, String message) {
		if (currentStatusDialog == null || !currentStatusDialog.isAttached()) {
			currentStatusDialog = new StatusDialog();
			currentStatusDialog.show();
		}
		currentStatusDialog.addLine(id, message);
	}

	public void removeBusy(String id) {
		if (currentStatusDialog != null && currentStatusDialog.isAttached())
			currentStatusDialog.removeLine(id);
	}

	public void clearBusy() {
		if (currentStatusDialog != null && currentStatusDialog.isAttached())
			currentStatusDialog.removeFromParent();
		currentStatusDialog = null;
	}

}