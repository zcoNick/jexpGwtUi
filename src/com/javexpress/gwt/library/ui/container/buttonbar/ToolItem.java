package com.javexpress.gwt.library.ui.container.buttonbar;

import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.form.button.Button;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class ToolItem extends Button {

	/** Designer compatible constructor */
	public ToolItem(Widget parent, String id, String toolTip) {		
		super(parent, id, toolTip != null ? toolTip : " ");
	}

	public ToolItem(Widget parent, String id, ICssIcon icon, String toolTip) {
		super(parent, id, toolTip != null ? toolTip : " ");
		setIcon(icon);
	}

	@Override
	protected JsonMap createDefaultOptions() {
		JsonMap options = super.createDefaultOptions();
		options.set("text", false);
		return options;
	}

}