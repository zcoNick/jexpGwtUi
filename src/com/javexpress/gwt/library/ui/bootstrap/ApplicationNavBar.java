package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.javexpress.gwt.library.ui.AbstractContainer;

public abstract class ApplicationNavBar extends AbstractContainer {

	private INavBarHandler	linkHandler;

	public INavBarHandler getLinkHandler() {
		return linkHandler;
	}

	public void setLinkHandler(INavBarHandler linkHandler) {
		this.linkHandler = linkHandler;
	}

	public ApplicationNavBar(Element el, String id) {
		super(el);
		getElement().setId(id);
	}

	public abstract void addItem(NavBarItem sbi);

	@Override
	protected void onUnload() {
		linkHandler = null;
		super.onUnload();
	}

	//--EVENTS
	protected void fireLinkClicked(String path) {
		if (linkHandler != null)
			linkHandler.navLinkClicked(path);
	}

	public abstract NavBarItem createNavBarItem(String id, String path);

}