package com.javexpress.gwt.library.ui.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.javexpress.gwt.library.ui.event.SessionExpiredEvent;

public interface SessionExpiredEventHandler extends EventHandler {

	void onSessionExpired(SessionExpiredEvent sessionExpired);

}