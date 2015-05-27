package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.javexpress.gwt.library.ui.AbstractContainer;

public abstract class ApplicationSideBar extends AbstractContainer {

	private ISideBarHandler	linkHandler;

	public ISideBarHandler getLinkHandler() {
		return linkHandler;
	}

	public void setLinkHandler(ISideBarHandler linkHandler) {
		this.linkHandler = linkHandler;
	}

	public ApplicationSideBar(Element el, String id) {
		super(el);
		getElement().setId(id);
	}

	public abstract void addItem(SideBarItem sbi);

	@Override
	protected void onUnload() {
		linkHandler = null;
		super.onUnload();
	}

	//--EVENTS
	protected void fireLinkClicked(String path) {
		if (linkHandler != null)
			linkHandler.linkClicked(path);
	}

}