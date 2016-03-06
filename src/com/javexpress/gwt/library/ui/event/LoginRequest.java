package com.javexpress.gwt.library.ui.event;

import com.google.gwt.event.shared.GwtEvent;
import com.javexpress.gwt.library.shared.model.LoginResult;
import com.javexpress.gwt.library.ui.event.handler.LoginRequestHandler;
import com.javexpress.gwt.library.ui.js.JexpCallback;

public class LoginRequest extends GwtEvent<LoginRequestHandler> {

	public static Type<LoginRequestHandler>	TYPE	= new Type<LoginRequestHandler>();
	private String							user;
	private String							pass;
	private String							cultureCode;
	private JexpCallback<LoginResult>		callback;

	public LoginRequest(String user, String pass, String cultureCode, JexpCallback<LoginResult> callback) {
		this.user = user;
		this.pass = pass;
		this.cultureCode = cultureCode;
		this.callback = callback;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getCultureCode() {
		return cultureCode;
	}

	public void setCultureCode(String cultureCode) {
		this.cultureCode = cultureCode;
	}

	public JexpCallback<LoginResult> getCallback() {
		return callback;
	}

	public void setCallback(JexpCallback<LoginResult> callback) {
		this.callback = callback;
	}

	@Override
	public Type<LoginRequestHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LoginRequestHandler handler) {
		handler.onLoginRequested(this);
	}

}