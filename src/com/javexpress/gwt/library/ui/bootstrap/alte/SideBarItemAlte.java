package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.bootstrap.SideBarItem;

public class SideBarItemAlte extends SideBarItem {

	public SideBarItemAlte(Widget sideBar, String id, String path) {
		super(sideBar, id, path);
	}

	@Override
	protected void onLoad() {
		/*<li class="treeview">
		<a href="#"><span>Multilevel</span> <i class="fa fa-angle-left pull-right"></i></a>
		<ul class="treeview-menu">
		  <li><a href="#">Link in level 2</a></li>
		  <li><a href="#">Link in level 2</a></li>
		</ul>
		</li>*/
		super.onLoad();
	}

	@Override
	public void setIconClass(String iconClass) {
		iconSpan.setClassName(iconClass != null ? iconClass : "fa fa-caret-right");
	}

	@Override
	public SideBarItem createAndAddSubItem(String id, String path) {
		SideBarItem sbi = new SideBarItemAlte(this, id, path);
		if (ul == null) {
			ul = DOM.createElement("ul");
			ul.setClassName("treeview-menu");
			getElement().appendChild(ul);
			getElement().addClassName("treeview");
			Element b1 = DOM.createElement("i");
			b1.setClassName("fa fa-angle-left pull-right");
			anchor.appendChild(b1);
			anchor.removeClassName("jexpSidebarLink");
		}
		add(sbi, ul);
		return sbi;
	}

}