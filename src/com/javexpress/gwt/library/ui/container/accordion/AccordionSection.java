package com.javexpress.gwt.library.ui.container.accordion;

import com.google.gwt.user.client.ui.Widget;

public class AccordionSection {

	private String title;
	private Widget widget;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Widget getWidget() {
		return widget;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}

}