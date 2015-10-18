package com.javexpress.gwt.library.ui.listgroup;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.HeadingSize;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ListGroupItem extends AbstractContainer {

	private String		title;
	private HeadingSize	titleSize;
	private String		text;
	private boolean		active;

	public ListGroupItem(Widget parent, String id) {
		super(DOM.createAnchor());
		JsUtil.ensureId(parent, getElement(), WidgetConst.LISTGROUPITEM_PREFIX, id);
		addStyleName("list-group-item jexpLink");
		getElement().setAttribute("href", "#");
		getElement().setAttribute("v", id);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	public HeadingSize getTitleSize() {
		return titleSize;
	}

	public void setTitleSize(HeadingSize titleSize) {
		this.titleSize = titleSize;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	protected void doAttachChildren() {
		if (isActive())
			addStyleName("active");
		if (title != null) {
			Element h = Bootstrap.createHeading(titleSize != null ? titleSize : HeadingSize.h4);
			h.setClassName("list-group-item-heading");
			h.setInnerHTML(title);
			getElement().appendChild(h);
			Element p = DOM.createElement("p");
			p.setClassName("list-group-item-text");
			p.setInnerHTML(text);
			getElement().appendChild(p);
		} else
			getElement().setInnerHTML(text);
		super.doAttachChildren();
	}

	@Override
	protected void onUnload() {
		this.text = null;
		this.title = null;
		this.titleSize = null;
		super.onUnload();
	}

}