package com.javexpress.gwt.library.ui.container.panel;

import com.google.gwt.user.client.ui.Widget;

public class FlowPanel extends com.google.gwt.user.client.ui.FlowPanel {

	/** Designer compatible constructor */
	public FlowPanel() {
		super();
	}

	public void add(int index, Widget widget) {
		if (getWidgetCount() <= index)
			add(widget);
		else
			insert(widget, index);
	}

}