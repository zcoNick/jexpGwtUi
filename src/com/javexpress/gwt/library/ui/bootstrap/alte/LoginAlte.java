package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.bootstrap.BootstrapClient;

public class LoginAlte extends AbstractContainer {

	private LoginControlsAlte loginControls;

	public LoginAlte(BootstrapClient pclient) {
		super(DOM.createDiv());

		getElement().setClassName("login-box");

		Element loginLogo = DOM.createDiv();
		loginLogo.setClassName("login-logo");
		Element i = DOM.createElement("i");
		i.setClassName("alte-icon " + pclient.getApplicationCssIcon() + " green");
		loginLogo.appendChild(i);
		Element s1 = DOM.createSpan();
		pclient.fillApplicationBrandLeft(s1);
		loginLogo.appendChild(s1);
		Element s2 = DOM.createSpan();
		pclient.fillApplicationBrandRight(s2);
		loginLogo.appendChild(s2);
		Element h4 = DOM.createElement("h4");
		pclient.fillApplicationBrandLink(h4);
		loginLogo.appendChild(h4);
		getElement().appendChild(loginLogo);

		Element loginBoxBody = DOM.createDiv();
		loginBoxBody.setClassName("login-box-body");
		getElement().appendChild(loginBoxBody);

		Element p = DOM.createElement("p");
		p.setClassName("login-box-msg");
		p.setInnerText(ClientContext.nlsCommon.lutfenOturumBilgileriniziGiriniz());
		loginBoxBody.appendChild(p);

		loginControls = new LoginControlsAlte();
		add(loginControls, loginBoxBody);

		Element bottom = DOM.createDiv();
		bottom.setClassName("row login-borderTop");

		Element forgot = DOM.createAnchor();
		forgot.setAttribute("href", "#");
		forgot.setInnerHTML(ClientContext.nlsCommon.sifremiUnuttum());
		bottom.appendChild(forgot);

		Element register = DOM.createAnchor();
		register.setAttribute("href", "#");
		register.setClassName("pull-right");
		register.setInnerHTML(ClientContext.nlsCommon.hesapOlusturmakIstiyorum());
		bottom.appendChild(register);

		loginBoxBody.appendChild(bottom);
	}

	public LoginControlsAlte getLoginControls() {
		return loginControls;
	}

	@Override
	protected void onLoad() {
		RootPanel.get().addStyleName("login-page");
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		remove(loginControls);
		RootPanel rp = RootPanel.get();
		rp.removeStyleName("login-page");
		loginControls = null;
		super.onUnload();
	}

}