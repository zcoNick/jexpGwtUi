package com.javexpress.gwt.library.ui.bootstrap.bootswatch;

import com.google.gwt.dom.client.Element;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.BootstrapTheme;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public abstract class BootswatchTheme extends BootstrapTheme {

	@Override
	public void applyIconInputGroupStyles(Element element, Element input, Element icon, ICssIcon iconClass) {
	}

	@Override
	public void applyIconStyles(Element iconSpan, ICssIcon iconClass) {
	}

	@Override
	protected void addStyleSheets(WidgetBundles wb, int phase) {
	}

	@Override
	protected void addJavaScripts(WidgetBundles wb, int phase) {
	}

	@Override
	public void prepareUI(ClientContext clientContext) {
	}

	@Override
	public void handleHistoryValueChanged(String value) {
	}

}