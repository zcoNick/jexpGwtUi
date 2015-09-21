package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.BootstrapTheme;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class AlteStdTheme extends BootstrapTheme {

	@Override
	public String getThemeName() {
		return "AdminLTE UI 2.1.1";
	}

	@Override
	public String getSkinName() {
		return "skin-blue fixed";
	}

	@Override
	public void addStyleSheets(WidgetBundles wb, int phase) {
		switch (phase) {
			case 0:
				wb.addStyleSheet("themes/alte/AdminLTE-2.1.2.min.css");
				wb.addStyleSheet("themes/alte/skins/skin-blue.min.css");
				break;
			case 1000:
				wb.addStyleSheet("themes/alte/javexpress-gwt-library.ui.css");
				break;
		}
	}

	@Override
	public void addJavaScripts(WidgetBundles wb, int phase) {
		switch (phase) {
			case 10:
				wb.addJavaScript("scripts/slimscroll/jquery.slimscroll-1.3.3.min.js");
				break;
			case 100:
				wb.addJavaScript("scripts/alte/app.js");
				break;
		}
	}

	@Override
	public void prepareUI(ClientContext clientContext) {
		prepareCommons(clientContext);
	}

	@Override
	public void applyIconInputGroupStyles(Element element, Element input, Element icon, ICssIcon iconClass) {
		element.setClassName("form-group has-feedback");
		input.setClassName("form-control");
		icon.setClassName("alte-icon " + (iconClass != null ? iconClass.getCssClass() : "") + " form-control-feedback");
	}

	@Override
	public void applyIconStyles(Element iconSpan, ICssIcon iconClass) {
		iconSpan.addClassName("alte-icon " + (iconClass != null ? iconClass.getCssClass() : ""));
	}

}