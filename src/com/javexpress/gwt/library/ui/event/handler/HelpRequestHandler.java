package com.javexpress.gwt.library.ui.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.javexpress.gwt.library.ui.event.HelpRequest;

public interface HelpRequestHandler extends EventHandler {

	void onHelpRequested(HelpRequest helpRequest);

}