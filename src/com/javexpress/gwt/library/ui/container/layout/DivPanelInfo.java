package com.javexpress.gwt.library.ui.container.layout;

import com.google.gwt.user.client.ui.Widget;

public class DivPanelInfo {

	public Widget	widget;
	public String	size;
	public boolean	scrollable;
	public boolean	panelStyle;

	public static DivPanelInfo create(Widget widget, String size, boolean scrollable) {
		DivPanelInfo d = new DivPanelInfo();
		d.widget = widget;
		d.size = size;
		d.scrollable = scrollable;
		return d;
	}

	/** @param widget
	 * @return NonScrollable Panel */
	public static DivPanelInfo create(Widget widget, String size) {
		return create(widget, size, false);
	}

	/** @param widget
	 * @return EM sized NonScrollable Panel */
	public static DivPanelInfo createEM(Widget widget, double size) {
		return create(widget, size + "em");
	}

	public static DivPanelInfo createEM(Widget widget, double size, boolean scroll) {
		return create(widget, size + "em", scroll);
	}

	/** @param widget
	 * @return NonScrollable Panel */
	public static DivPanelInfo create(Widget widget) {
		return create(widget, false);
	}

	/** @param widget
	 * @return EM sized scrollable Panel */
	public static DivPanelInfo create(Widget widget, boolean scrollable) {
		return create(widget, null, scrollable);
	}

	public DivPanelInfo applyPanelStyle() {
		this.panelStyle = true;
		return this;
	}

}