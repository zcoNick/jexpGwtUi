package com.javexpress.gwt.library.ui.event;

import com.google.gwt.event.shared.GwtEvent;
import com.javexpress.gwt.library.ui.event.handler.ApplicationReadyEventHandler;

public class ApplicationReadyEvent extends GwtEvent<ApplicationReadyEventHandler> {

	public static Type<ApplicationReadyEventHandler>	TYPE	= new Type<ApplicationReadyEventHandler>();

	public ApplicationReadyEvent() {
	}

	@Override
	public Type<ApplicationReadyEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ApplicationReadyEventHandler handler) {
		handler.onApplicationReady(this);
	}

}