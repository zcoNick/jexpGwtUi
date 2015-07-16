package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Panel;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationHeaderPanel;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationMainContainer;
import com.javexpress.gwt.library.ui.bootstrap.BootstrapTheme;
import com.javexpress.gwt.library.ui.js.JsonMap;
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
	public void injectCore(JsonMap requireConfig, final Command onload) {
		super.injectCore(requireConfig, onload);
	}

	@Override
	public void addStyleSheets(WidgetBundles wb, int phase) {
		switch (phase) {
			case 0:
				wb.addStyleSheet("themes/alte/AdminLTE-2.1.2.min.css");
				wb.addStyleSheet("themes/alte/skins/skin-blue.min.css");
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

	public Panel createRootPanel(Panel body) {
		return body;//div class=wrapper idi
	}

	public ApplicationHeaderPanel createHeaderPanel(Panel parent, String id) {
		ApplicationHeaderPanelAlte ahp = new ApplicationHeaderPanelAlte(id);
		parent.add(ahp);
		return ahp;
	}

	public ApplicationMainContainer createMainContainer(Panel parent, String id) {
		ApplicationMainContainer mc = new ApplicationMainContainerAlte(parent, id);
		//parent.add(mc);dont add!
		return mc;
	}

	@Override
	public void prepareUI(ClientContext clientContext) {
	}

}