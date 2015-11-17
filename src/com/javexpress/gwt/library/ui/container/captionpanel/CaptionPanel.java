package com.javexpress.gwt.library.ui.container.captionpanel;

import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class CaptionPanel extends com.google.gwt.user.client.ui.CaptionPanel implements ISizeAwareWidget {

	public CaptionPanel(final String id, final String caption) {
		this(null, id, caption);
	}

	public CaptionPanel(final Widget parent, final String id) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.CAPTIONPANEL_PREFIX, id);
		addStyleName((!JsUtil.USE_BOOTSTRAP ? "ui-corner-all " : "") + "jexpCaptionPanel");
	}

	/** Designer compatible constructor */
	public CaptionPanel(final Widget parent, final String id, final String caption) {
		this(parent, id, caption, false);
	}

	public CaptionPanel(Widget parent, String id, String caption, boolean fillParent) {
		this(parent, id);
		if (caption != null)
			setCaptionText(caption);
		if (fillParent) {
			setWidth("auto");
			setHeight("100%");
		}
	}

	public void setChild(Widget widget) {
		add(widget);
	}

	@Override
	public void onResize() {
		Widget w = getWidget();
		if (w instanceof RequiresResize)
			((RequiresResize) w).onResize();
		else if (w instanceof SimplePanel) {
			SimplePanel sp = ((SimplePanel) w);
			if (sp.getWidget() instanceof RequiresResize)
				((RequiresResize) sp.getWidget()).onResize();
		}
	}

}