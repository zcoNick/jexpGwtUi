package com.javexpress.gwt.library.ui.container.buttonbar;

import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.MenuButton;

public class ToolMenu extends MenuButton {

	public ToolMenu(Widget parent, String id, String toolTip) {
		super(parent, id, toolTip != null ? toolTip : " ");
	}

	/** Designer compatible constructor */
	public ToolMenu(Widget parent, String id, ICssIcon icon, String toolTip) {
		super(parent, id, toolTip != null ? toolTip : " ");
		setIcon(icon);
	}

}