package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationBreadcrumb;

public class ApplicationBreadcrumbAlte extends ApplicationBreadcrumb {
	/*
		    <ol class="breadcrumb">
		      <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
		      <li class="active">Here</li>
		    </ol>
	 */
	public ApplicationBreadcrumbAlte(String id) {
		super(DOM.createElement("ol"), id, "breadcrumb");
		reset();
	}

	@Override
	public void addItem(ICssIcon icon, String extraClass, String href, String label) {
		Element li = DOM.createElement("li");
		Element a = DOM.createAnchor();
		a.setAttribute("href", "#" + href);
		a.setInnerHTML(label);
		Element i = DOM.createElement("i");
		i.setClassName(icon.getCssClass() + (extraClass != null ? " " + extraClass : ""));
		a.insertFirst(i);
		li.appendChild(a);
		getElement().appendChild(li);
	}

}