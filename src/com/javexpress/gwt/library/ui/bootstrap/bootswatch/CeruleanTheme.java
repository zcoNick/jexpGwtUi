package com.javexpress.gwt.library.ui.bootstrap.bootswatch;

import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class CeruleanTheme extends BootswatchTheme {

	@Override
	protected void addBootstrapStyleSheet(WidgetBundles wb) {
		wb.addStyleSheet("themes/bootswatch/cerulean/bootstrap-cerulean-3.3.6.min.css");
	}

	@Override
	protected String getThemeName() {
		return "Bootswatch Cerulean 3.3.6";
	}

	@Override
	public String getSkinName() {
		return null;
	}

}