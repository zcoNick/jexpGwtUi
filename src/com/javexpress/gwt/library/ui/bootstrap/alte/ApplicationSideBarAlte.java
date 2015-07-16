package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationSideBar;
import com.javexpress.gwt.library.ui.bootstrap.SideBarItem;

public class ApplicationSideBarAlte extends ApplicationSideBar {

	private Element	area;
	private Element	buttonsUl;

	public ApplicationSideBarAlte(String id) {
		super(DOM.createElement("aside"), id);
		getElement().setClassName("main-sidebar");
		getElement().getStyle().setZIndex(1);

		area = DOM.createElement("div");
		area.setClassName("sidebar");
		getElement().appendChild(area);

		buttonsUl = DOM.createElement("ul");
		buttonsUl.setClassName("sidebar-menu");
		area.appendChild(buttonsUl);//was adding to contents
	}

	@Override
	public void addItem(SideBarItem sbi) {
		add(sbi, buttonsUl);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, area);
	}

	private native void createByJs(ApplicationSideBarAlte x, Element element) /*-{
		$wnd.$.AdminLTE.tree(element);
		$wnd.$(element).slimscroll({
			height : 200 + 'px',
			alwaysVisible : false,
			size : '3px'
		}).css("width", "100%");
		$wnd.$("a.sidebar-link", $wnd.$(element)).click(
				function(e) {
					var a = $wnd.$(this);
					x.@com.javexpress.gwt.library.ui.bootstrap.alte.ApplicationSideBarAlte::fireLinkClicked(Ljava/lang/String;)(a.attr("path"));
					$wnd.$(".open,.active", $wnd.$(element)).removeClass("open active");
					a.parent().addClass("active")
					a.parents("li.hsub").addClass("open active");
				});		
	}-*/;

	@Override
	protected void onUnload() {
		area = null;
		buttonsUl = null;
		super.onUnload();
	}

	@Override
	public SideBarItem createSideBarItem(String id, String path) {
		return new SideBarItemAlte(this, id, path);
	}

}