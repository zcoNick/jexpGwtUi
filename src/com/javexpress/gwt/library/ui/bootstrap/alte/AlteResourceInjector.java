package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Panel;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationHeaderPanel;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationMainContainer;
import com.javexpress.gwt.library.ui.bootstrap.ResourceInjector;
import com.javexpress.gwt.library.ui.bootstrap.ResourceInjector.IBootstrapThemeProvider;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class AlteResourceInjector extends ResourceInjector implements IBootstrapThemeProvider {

	@Override
	public String getThemeName() {
		return "AdminLTE UI 2.1.1";
	}

	@Override
	public String getSkinName() {
		return "skin-blue";
	}

	@Override
	public void injectCore(JsonMap requireConfig, final Command onload) {
		theme = this;
		super.injectCore(requireConfig, onload);
	}

	@Override
	public void addStyleSheets(WidgetBundles wb, int phase) {
		switch (phase) {
			case 0:
				wb.addStyleSheet("themes/alte/AdminLTE-2.1.1.min.css");
				wb.addStyleSheet("themes/alte/skins/skin-blue.min.css");
				break;
		}
	}

	@Override
	public void addJavaScripts(WidgetBundles wb, int phase) {
		switch (phase) {
			case 10:
				break;
			case 100:
				break;
		}
	}

	@Override
	public Panel createRootPanel(Panel body) {
		return body;//div class=wrapper idi
	}

	@Override
	public ApplicationHeaderPanel createHeaderPanel(Panel parent, String id) {
		ApplicationHeaderPanelAlte ahp = new ApplicationHeaderPanelAlte(id);
		parent.add(ahp);
		return ahp;
	}

	@Override
	public ApplicationMainContainer createMainContainer(Panel parent, String id) {
		ApplicationMainContainer mc = new ApplicationMainContainerAlte(parent, id);
		//parent.add(mc);dont add!
		return mc;
	}

}