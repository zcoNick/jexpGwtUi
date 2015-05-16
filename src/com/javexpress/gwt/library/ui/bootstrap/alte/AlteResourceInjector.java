package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.user.client.Command;
import com.javexpress.gwt.library.ui.bootstrap.ResourceInjector;
import com.javexpress.gwt.library.ui.bootstrap.ResourceInjector.IBootstrapThemeBundlesProvider;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class AlteResourceInjector extends ResourceInjector implements IBootstrapThemeBundlesProvider {

	@Override
	public String getThemeName() {
		return "ALTE UI 2.1.1";
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

}