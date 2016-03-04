package com.javexpress.gwt.library.ui.event;

import com.google.gwt.event.shared.GwtEvent;
import com.javexpress.gwt.library.shared.model.LoginResult;
import com.javexpress.gwt.library.ui.event.handler.AuthenticationEventHandler;

public class AuthenticationEvent extends GwtEvent<AuthenticationEventHandler> {

	public static Type<AuthenticationEventHandler>	TYPE	= new Type<AuthenticationEventHandler>();
	private LoginResult								result;

	public AuthenticationEvent(LoginResult result) {
		this.result = result;
	}

	public LoginResult getResult() {
		return result;
	}

	public void setResult(LoginResult result) {
		this.result = result;
	}

	@Override
	public Type<AuthenticationEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AuthenticationEventHandler handler) {
		handler.onAuthenticationChanged(this);
	}

}