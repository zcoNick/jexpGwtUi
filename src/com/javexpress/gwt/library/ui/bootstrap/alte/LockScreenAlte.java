package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.user.client.Command;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.bootstrap.UIComposite;

public class LockScreenAlte extends UIComposite {

	private LoginControlsAlte	loginBox;

	public LockScreenAlte() {
		super("lockscreen");
		setHeader(ClientContext.nlsCommon.oturumZamaniAsilmis());
		setXsSize(12);
		setSmSize(6);
		setMdSize(4);
		loginBox = new LoginControlsAlte();
		add(loginBox);
		addOnLoadCommand(new Command() {
			@Override
			public void execute() {
				loginBox.setFixedUserName(LoginControlsAlte.lastUserName);
				loginBox.getPass().setFocus(true);
			}
		});
	}

	public LoginControlsAlte getLoginBox() {
		return loginBox;
	}

}