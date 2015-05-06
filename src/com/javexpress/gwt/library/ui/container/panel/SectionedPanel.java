package com.javexpress.gwt.library.ui.container.panel;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.container.captionpanel.CaptionPanel;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class SectionedPanel extends FlexTable implements ISizeAwareWidget {

	public SectionedPanel(Widget parent, String id) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.SECTIONEDPANEL_PREFIX, id);
		setCellPadding(0);
		setCellSpacing(0);
	}

	public CaptionPanel addPanel(String title) {
		CaptionPanel cp = new CaptionPanel(this, "section_" + getRowCount(), title);
		cp.setWidth("auto");
		setWidget(getRowCount(), 0, cp);
		return cp;
	}

	@Override
	public void onResize() {
		for (int i = 0; i < getRowCount(); i++) {
			Widget w = getWidget(i, 0);
			if (w instanceof RequiresResize)
				((RequiresResize) w).onResize();
		}
	}

}