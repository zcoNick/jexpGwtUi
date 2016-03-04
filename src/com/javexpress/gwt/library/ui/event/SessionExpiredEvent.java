package com.javexpress.gwt.library.ui.event;

import com.google.gwt.event.shared.GwtEvent;
import com.javexpress.gwt.library.ui.event.handler.SessionExpiredEventHandler;

public class SessionExpiredEvent extends GwtEvent<SessionExpiredEventHandler> {

	public static Type<SessionExpiredEventHandler>	TYPE	= new Type<SessionExpiredEventHandler>();

	public SessionExpiredEvent() {
	}

	@Override
	public Type<SessionExpiredEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SessionExpiredEventHandler handler) {
		handler.onSessionExpired(this);
	}

}