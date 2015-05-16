package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.javexpress.gwt.library.ui.BaseResourceInjector;
import com.javexpress.gwt.library.ui.container.dashboard.Dashboard;
import com.javexpress.gwt.library.ui.data.jqgrid.JqGrid;
import com.javexpress.gwt.library.ui.data.slickgrid.DataGrid;
import com.javexpress.gwt.library.ui.form.decimalbox.DecimalBox;
import com.javexpress.gwt.library.ui.form.maskedit.MaskEditBox;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public abstract class ResourceInjector extends BaseResourceInjector {

	public static interface IBootstrapThemeBundlesProvider {
		String getThemeName();

		void addStyleSheets(WidgetBundles wb, int phase);

		void addJavaScripts(WidgetBundles wb, int phase);
	}

	protected IBootstrapThemeBundlesProvider	theme;

	@Override
	public void injectCore(JsonMap requireConfig, final Command onload) {
		JsUtil.USE_BOOTSTRAP = true;

		requireConfig.set("jquery", "scripts/jquery/jquery-1.11.1.min");
		requireConfig.set("moment", "scripts/moment/moment.min");
		_requireConfig(GWT.getModuleName(), requireConfig.getJavaScriptObject());

		WidgetBundles jq = new WidgetBundles("jQuery 1.11.1");
		jq.addJavaScript("@jquery");

		jq = new WidgetBundles("jQuery UI 1.11.2", jq);
		jq.addJavaScript("scripts/jquery/jquery-ui-1.11.2.min.js");

		WidgetBundles wb = new WidgetBundles("Bootstrap 3.3.1", jq);
		wb.addStyleSheet("scripts/bootstrap/bootstrap-3.3.1.min.css");
		wb.addStyleSheet("fonts/fontawesome/font-awesome-4.2.0.min.css");
		if (theme != null)
			theme.addStyleSheets(wb, 0);
		wb.addStyleSheet("JexpGwtBootstrapBase-0.1.css");
		wb.addJavaScript("scripts/bootstrap/bootstrap-3.3.1.min.js");
		wb.addJavaScript("scripts/jexpUICore-0.1.js");
		injectLibrary(wb, onload);
	}

	protected WidgetBundles createUIBundles() {
		WidgetBundles ace = new WidgetBundles(theme != null ? theme.getThemeName() : "Bootstrap Based UI");
		ace.addStyleSheet("scripts/gritter/jquery.gritter-1.7.4.css");
		ace.addStyleSheet("JexpGwtBootstrapUI-0.1.css");
		ace.addJavaScript("scripts/jexpLocalizationStringHelper-0.1.js");
		if (theme != null) {
			theme.addStyleSheets(ace, 10);
			theme.addJavaScripts(ace, 10);
		}

		WidgetBundles jexp = new WidgetBundles("JavExpress UI", ace);
		if (theme != null) {
			theme.addStyleSheets(jexp, 100);
			theme.addJavaScripts(jexp, 100);
		}
		jexp.addJavaScript("scripts/gritter/jquery.gritter-1.7.4.min.js");
		jexp.addJavaScript("scripts/numeral/numeral-1.5.3.min.js");
		jexp.addJavaScript("scripts/numeral/numeral.languages.min.js");
		jexp.addJavaScript("scripts/slickgrid/jquery.event.drag-2.2.js");
		jexp.addJavaScript("scripts/slickgrid/jquery.event.drop-2.2.js");
		DateBox.fillResources(jexp);
		Dashboard.fillResources(jexp);
		MaskEditBox.fillResources(jexp);
		DecimalBox.fillResources(jexp);

		jexp = JqGrid.fillResources(jexp);
		jexp = DataGrid.fillResources(jexp);
		return jexp;
	}

	@Override
	public void injectUI(String applicationCss, Command onload) throws Exception {
		WidgetBundles jexp = createUIBundles();
		jexp.addStyleSheet(applicationCss);
		injectLibrary(jexp, onload);
	}

}