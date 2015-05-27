package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;

public abstract class ApplicationMainContainer extends AbstractContainer {

	public ApplicationMainContainer(String id, String className) {
		super(DOM.createDiv());
		getElement().setId(id);
		getElement().setClassName(className);
	}

	public abstract ApplicationSideBar createSideBar(String id);

	public abstract ApplicationMainContent createMainContent();

	public abstract ApplicationFooter createFooter();

}