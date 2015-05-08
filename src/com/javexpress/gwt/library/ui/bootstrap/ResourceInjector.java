package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.javexpress.gwt.library.ui.BaseResourceInjector;
import com.javexpress.gwt.library.ui.container.dashboard.Dashboard;
import com.javexpress.gwt.library.ui.data.jqgrid.JqGrid;
import com.javexpress.gwt.library.ui.form.decimalbox.DecimalBox;
import com.javexpress.gwt.library.ui.form.maskedit.MaskEditBox;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class ResourceInjector extends BaseResourceInjector {

	public static void injectBootstrapCore(JsonMap requireConfig, final Command onload) {
		JsUtil.USE_BOOTSTRAP = true;

		requireConfig.set("jquery", "ace/js/jquery-1.11.1.min");
		requireConfig.set("moment", "ace/js/moment.min");
		_requireConfig(GWT.getModuleName(), requireConfig.getJavaScriptObject());

		WidgetBundles jq = new WidgetBundles("jQuery 1.11.1");
		jq.addJavaScript("@jquery");

		jq = new WidgetBundles("jQuery UI 1.11.2", jq);
		jq.addJavaScript("ace/js/jquery-ui-1.11.2.min.js");

		WidgetBundles wb = new WidgetBundles("Bootstrap 3.3.1", jq);
		wb.addStyleSheet("ace/css/bootstrap-3.3.1.min.css");
		wb.addStyleSheet("ace/css/font-awesome-4.2.0.min.css");
		wb.addStyleSheet("ace/css/ace-fonts-1.3.3.min.css");
		wb.addStyleSheet("ace/css/ace-1.3.3.min.css");
		wb.addStyleSheet("ace/css/ace-skins-1.3.3.min.css");
		if (JsUtil.isRTL())
			wb.addStyleSheet("ace/css/ace-rtl-1.3.3.min.css");
		wb.addStyleSheet("JexpGwtBootstrapBase-0.1.css");
		wb.addJavaScript("ace/js/bootstrap-3.3.1.min.js");
		wb.addJavaScript("scripts/jexpUICore-0.1.js");
		injectLibrary(wb, onload);
	}

	public static void injectAceUI(WidgetBundles injectAtEnd, final Command onload) throws Exception {
		WidgetBundles ace = new WidgetBundles("Ace UI 1.3.3");
		ace.addStyleSheet("ace/css/jquery.gritter-1.7.4.css");
		ace.addStyleSheet("JexpGwtBootstrapUI-0.1.css");
		ace.addJavaScript("scripts/jexpLocalizationStringHelper-0.1.js");
		ace.addJavaScript("ace/js/ace-1.3.3.min.js");

		WidgetBundles jexp = new WidgetBundles("JavExpress UI", ace);
		jexp.addJavaScript("ace/js/ace-elements-1.3.3.min.js");
		jexp.addJavaScript("ace/js/ace-extra-1.3.3.min.js");
		jexp.addJavaScript("ace/js/jquery.gritter-1.7.4.min.js");
		jexp.addJavaScript("scripts/numeral/numeral-1.5.3.min.js");
		jexp.addJavaScript("scripts/numeral/numeral.languages.min.js");
		jexp.addJavaScript("scripts/slickgrid/jquery.event.drag-2.2.js");
		jexp.addJavaScript("scripts/slickgrid/jquery.event.drop-2.2.js");
		DateBox.fillResources(jexp);
		Dashboard.fillResources(jexp);
		MaskEditBox.fillResources(jexp);
		DecimalBox.fillResources(jexp);

		jexp = JqGrid.fillResources(jexp);
		if (injectAtEnd != null) {
			WidgetBundles root = injectAtEnd;
			int max = 999;
			while (max-- > 0 && root.getParent() != null)
				root = root.getParent();
			if (root.getParent() != null)
				throw new Exception("Cant find root dependency");
			root.setParent(jexp);
			jexp = injectAtEnd;
		}
		injectLibrary(jexp, onload);
	}

}