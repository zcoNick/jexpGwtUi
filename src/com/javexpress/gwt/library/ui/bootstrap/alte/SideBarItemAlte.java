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

		if (ul != null)
			getElement().addClassName("treeview");
		Element a = DOM.createAnchor();
		a.setAttribute("href", "#");
		if (ul == null) {
			a.setAttribute("path", path);
			a.setClassName("sidebar-link");
		}
		Element i = DOM.createElement("i");
		i.setClassName(iconClass != null ? iconClass : "fa fa-caret-right");
		a.appendChild(i);
		Element s = DOM.createSpan();
		s.setInnerText(text);
		a.appendChild(s);
		getElement().appendChild(a);
		if (ul != null) {
			Element b1 = DOM.createElement("i");
			b1.setClassName("fa fa-angle-left pull-right");
			a.appendChild(b1);
		}

		if (ul != null)
			getElement().appendChild(ul);

		super.onLoad();
	}

	@Override
	public SideBarItem createSubItem(String id, String path) {
		SideBarItem sbi = new SideBarItemAlte(this, id, path);
		if (ul == null) {
			ul = DOM.createElement("ul");
			ul.setClassName("treeview-menu");
		}
		add(sbi, ul);
		return sbi;
	}

}