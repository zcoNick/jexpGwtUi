package com.javexpress.gwt.library.ui.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.javexpress.gwt.library.ui.event.ApplicationReadyEvent;

public interface ApplicationReadyEventHandler extends EventHandler {

	void onApplicationReady(ApplicationReadyEvent event);

}