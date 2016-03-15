package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WPull;
import com.javexpress.gwt.library.ui.bootstrap.Button;
import com.javexpress.gwt.library.ui.bootstrap.CheckBox;
import com.javexpress.gwt.library.ui.bootstrap.IconEmbeddedInput;
import com.javexpress.gwt.library.ui.bootstrap.IconEmbeddedPassword;

public class LoginControlsAlte extends AbstractContainer {

	public static String			lastUserName;
	private IconEmbeddedInput		user;
	private IconEmbeddedPassword	pass;
	private CheckBox				remember;
	private Button					button;

	public LoginControlsAlte() {
		super(DOM.createForm());

		KeyDownHandler kdh = new KeyDownHandler() {
			@Override
			public void onKeyDown(final KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					button.click();
			}
		};

		user = new IconEmbeddedInput(this, "user", FaIcon.user);
		user.getElement().getStyle().setMarginBottom(1, Unit.EM);
		user.getInputStyle().setHeight(2.2, Unit.EM);
		user.getInputStyle().setPaddingLeft(6, Unit.PX);
		user.setPlaceHolder(ClientContext.nlsCommon.username());
		String cuser = Cookies.getCookie(GWT.getModuleName() + ".login");
		if (cuser != null)
			user.setValue(cuser);
		add(user);

		pass = new IconEmbeddedPassword(this, "pass");
		pass.getElement().getStyle().setMarginBottom(1, Unit.EM);
		pass.getInputStyle().setHeight(2.2, Unit.EM);
		pass.getInputStyle().setPaddingLeft(6, Unit.PX);
		pass.setPlaceHolder(ClientContext.nlsCommon.password());
		pass.addKeyDownHandler(kdh);
		add(pass);

		Element row = DOM.createDiv();
		row.addClassName("form-group");
		row.getStyle().setHeight(3, Unit.EM);
		Element colLeft = DOM.createDiv();
		colLeft.setClassName("col-xs-8");
		remember = new CheckBox(this, "remember", ClientContext.nlsCommon.rememberMe());
		remember.addKeyDownHandler(kdh);
		add(remember, colLeft);
		row.appendChild(colLeft);
		Element colRight = DOM.createDiv();
		colRight.setClassName("col-xs-4");
		button = new Button(this, "login", ClientContext.nlsCommon.login());
		button.setWpull(WPull.Right);
		button.setWcontext(WContext.Primary);
		button.setIcon(FaIcon.key);
		button.setTextClass("bigger-110");
		button.addKeyDownHandler(kdh);
		add(button, colRight);
		row.appendChild(colRight);
		getElement().appendChild(row);
	}

	public void addLoginHandler(ClickHandler loginHandler) {
		button.addClickHandler(loginHandler);
	}

	public IconEmbeddedInput getUser() {
		return user;
	}

	public IconEmbeddedPassword getPass() {
		return pass;
	}

	public CheckBox getRemember() {
		return remember;
	}

	@Override
	protected void onUnload() {
		if (user != null)
			lastUserName = user.getValue();
		user = null;
		pass = null;
		remember = null;
		button = null;
		super.onUnload();
	}

	public void setFixedUserName(String name) {
		user.setValue(name);
		user.setEnabled(false);
	}

}