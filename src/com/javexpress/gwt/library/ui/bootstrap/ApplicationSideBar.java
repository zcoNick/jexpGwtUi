package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;

public class ApplicationSideBar extends AbstractContainer {

	private Element			area;
	private Element			contents;
	private Element			collapser;
	private Element			searchDiv;
	private Element			buttonsUl;
	private ISideBarHandler	linkHandler;

	public ISideBarHandler getLinkHandler() {
		return linkHandler;
	}

	public void setLinkHandler(ISideBarHandler linkHandler) {
		this.linkHandler = linkHandler;
	}

	public ApplicationSideBar(String id) {
		super(DOM.createDiv());
		getElement().getStyle().setZIndex(1);

		area = DOM.createDiv();
		area.getStyle().setPosition(Position.RELATIVE);
		getElement().appendChild(area);

		/*Element navWrap = DOM.createDiv();
		navWrap.setClassName("nav-wrap");
		area.appendChild(navWrap);
		contents = DOM.createDiv();
		contents.setAttribute("style", "position: relative; top: 0px; transition-property: top; transition-duration: 0.2s");
		navWrap.appendChild(contents);

		searchDiv = DOM.createDiv();
		searchDiv.setClassName("nav-search");
		Element searchSpan = DOM.createSpan();
		searchSpan.setClassName("input-icon");
		Element search = DOM.createInputText();
		search.setId("nav-search-input");
		search.setAttribute("autocomplete", "off");
		search.setClassName("nav-search-input");
		search.setAttribute("placeholder", "Search...");
		searchSpan.appendChild(search);
		Element searchIcon = DOM.createElement("i");
		searchIcon.setClassName("ace-icon fa fa-search nav-search-icon");
		searchSpan.appendChild(searchIcon);
		searchDiv.appendChild(searchSpan);
		contents.appendChild(searchDiv);*/

		buttonsUl = DOM.createElement("ul");
		buttonsUl.setClassName("nav nav-list");
		getElement().appendChild(buttonsUl);//was adding to contents

		collapser = DOM.createDiv();
		collapser.setClassName("sidebar-toggle sidebar-collapse");
		collapser.setId("sidebar-collapse");
		collapser.getStyle().setZIndex(2);
		getElement().appendChild(collapser);
		Element btCollapse = DOM.createElement("i");
		btCollapse.setAttribute("data-icon2", "ace-icon fa fa-angle-double-right");
		btCollapse.setAttribute("data-icon1", "ace-icon fa fa-angle-double-left");
		btCollapse.setClassName("ace-icon fa fa-angle-double-left");
		collapser.appendChild(btCollapse);

		getElement().setClassName("sidebar responsive sidebar-fixed");
		getElement().setId(id);
		getElement().setAttribute("data-scroll-to-active", "true");
		getElement().setAttribute("data-include-shortcuts", "false");
		getElement().setAttribute("data-smooth-scroll", "150");
	}

	public void addItem(SideBarItem sbi) {
		add(sbi, buttonsUl);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement());
	}

	private native void createByJs(ApplicationSideBar x, Element element) /*-{
		//$wnd.ace.handle_side_menu($wnd.$);
		var opts = {};
		//$wnd.$(element).ace_sidebar(opts);
		$wnd.$(element).ace_sidebar_scroll(opts);
		$wnd.$("a.sidebar-link", $wnd.$(element)).click(
				function(e) {
					var a = $wnd.$(this);
					x.@com.javexpress.gwt.library.ui.bootstrap.ApplicationSideBar::fireLinkClicked(Ljava/lang/String;)(a.attr("path"));
					$wnd.$(".open,.active", $wnd.$(element)).removeClass("open active");
					a.parent().addClass("active")
					a.parents("li.hsub").addClass("open active");
				});
	}-*/;

	@Override
	protected void onUnload() {
		area = null;
		contents = null;
		collapser = null;
		searchDiv = null;
		buttonsUl = null;
		super.onUnload();
	}

	//--EVENTS
	private void fireLinkClicked(String path) {
		if (linkHandler != null)
			linkHandler.linkClicked(path);
	}

}