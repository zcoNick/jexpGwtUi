package com.javexpress.gwt.library.ui.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.javexpress.gwt.library.ui.event.LoginRequest;

public interface LoginRequestHandler extends EventHandler {

	void onLoginRequested(LoginRequest request);

}