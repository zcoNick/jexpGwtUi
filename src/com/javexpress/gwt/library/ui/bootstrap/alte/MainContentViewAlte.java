package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.MainContentView;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class MainContentViewAlte extends MainContentView {

	private Element	header, h1, content;

	public MainContentViewAlte(String id) {
		super(id);

		header = DOM.createElement("section");
		header.setClassName("content-header");
		getElement().appendChild(header);

		content = DOM.createElement("section");
		content.setClassName("content");
		getElement().appendChild(content);
	}

	@Override
	public void setContents(IUIComposite form) {
		setHeader(form.getIcon(), form.getHeader());
		Widget w = (Widget) form;
		w.addStyleName("container-fluid no-padding");
		add((Widget) form, content);
	}

	@Override
	public void setHeader(ICssIcon icon, String title) {
		if (h1 != null)
			JsUtil.clearChilds(h1);
		else {
			h1 = DOM.createElement("h1");
			header.appendChild(h1);
		}
		if (icon != null)
			title = "<i class=\"" + icon.getCssClass() + " smaller-90\"></i>&nbsp;" + title;
		h1.setInnerHTML(title + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	}

	@Override
	protected void addToolItem(FaIcon icon, String title, boolean right, Command command) {
		Element i = DOM.createElement("i");
		i.setClassName("ub_" + getId() + " " + icon.getCssClass() + " " + (right ? "pull-right" : "") + " jexpMainViewToolItem");
		i.setTitle(title);
		bindOnClick(i, command);
		h1.appendChild(i);
	}

}