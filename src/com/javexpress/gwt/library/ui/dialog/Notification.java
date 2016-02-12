package com.javexpress.gwt.library.ui.dialog;

import com.google.gwt.core.client.GWT;
import com.javexpress.common.model.item.exception.AppException;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.facet.ProvidesModuleUtils;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class Notification {

	public static enum NotificationType {
		fatal, error, warning, info, success
	}

	public static int showNotification(final String pSummary, final String pDescription, final NotificationType type) {
		return showNotification(pSummary, pDescription, type, type == NotificationType.error || type == NotificationType.fatal, type == NotificationType.warning ? 3500 : 2000);
	}

	public static int showError(AppException appException) {
		AppException appEx = appException;
		String s = (appEx).getErrorCode();
		if (ClientContext.instance instanceof ProvidesModuleUtils)
			s = ((ProvidesModuleUtils) ClientContext.instance).getModuleNls(appEx.getModuleId(), "error_" + appEx.getErrorCode());
		else
			s = appEx.getErrorCode();
		return Notification.showNotification(ClientContext.nlsCommon.hata(), s, NotificationType.error);
	}

	public static int showNotification(final String pSummary, final String pDescription, final NotificationType type, final boolean sticky, final int time) {
		if (JsUtil.USE_BOOTSTRAP) {
			return showNotificationBS_(pSummary, pDescription, GWT.getModuleBaseURL() + "scripts/gritter/" + type.toString() + ".png", sticky, time, type == NotificationType.error || type == NotificationType.fatal, type.toString());
		} else
			return showNotification_(pSummary, pDescription, GWT.getModuleBaseURL() + "scripts/gritter/" + type.toString() + ".png", sticky, time, type == NotificationType.error || type == NotificationType.fatal);
	}

	private static native int showNotification_(final String pSummary, final String pDescription, final String pImage, final boolean sticky, final int time, boolean dark) /*-{
		var options = {
			title : pSummary
		};
		if (pDescription)
			options.text = pDescription;
		options.image = pImage;
		options.sticky = sticky;
		options.time = time;
		if (!dark)
			options.class_name = "gritter-light";
		return $wnd.$.gritter.add(options);
	}-*/;

	private static native int showNotificationBS_(final String pSummary, final String pDescription, final String pImage, final boolean sticky, final int time, boolean dark, String styleContext) /*-{
		var options = {
			title : pSummary
		};
		if (pDescription)
			options.text = pDescription;
		options.image = pImage;
		options.sticky = sticky;
		options.time = time;
		if (!dark)
			options.class_name = "gritter-" + styleContext + " gritter-light";
		else
			options.class_name = "gritter-" + styleContext;
		return $wnd.$.gritter.add(options);
	}-*/;

	public static void showWarning(String message) {
		Notification.showNotification(ClientContext.nlsCommon.uyari(), message, NotificationType.warning);
	}

	public static native void removeAll()/*-{
		$wnd.$.gritter.removeAll();
	}-*/;

	public static native void remove(int id)/*-{
		$wnd.$.gritter.remove(id, null);
	}-*/;

}