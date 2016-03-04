package com.javexpress.gwt.library.ui.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.javexpress.gwt.library.ui.event.AuthenticationEvent;

public interface AuthenticationEventHandler extends EventHandler {

	void onAuthenticationChanged(AuthenticationEvent authenticationEvent);

}