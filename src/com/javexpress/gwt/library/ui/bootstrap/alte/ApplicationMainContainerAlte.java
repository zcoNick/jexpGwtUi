package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.user.client.ui.Panel;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationFooter;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationMainContainer;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationMainContent;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationSideBar;

public class ApplicationMainContainerAlte extends ApplicationMainContainer {

	private Panel	parent;

	public ApplicationMainContainerAlte(Panel parent, String id) {
		super(id, null);
		this.parent = parent;
	}

	@Override
	public ApplicationSideBar createSideBar(String id) {
		ApplicationSideBar asb = new ApplicationSideBarAlte(id);
		parent.add(asb);
		return asb;
	}

	@Override
	public ApplicationMainContent createMainContent() {
		ApplicationMainContent cnt = new ApplicationMainContentAlte();
		parent.add(cnt);
		return cnt;
	}

	@Override
	public ApplicationFooter createFooter() {
		ApplicationFooter footer = new ApplicationFooter("footer");
		parent.add(footer);
		return footer;
	}

	@Override
	protected void onUnload() {
		parent = null;
		super.onUnload();
	}

}