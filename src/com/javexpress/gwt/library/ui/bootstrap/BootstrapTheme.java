package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.javexpress.gwt.library.ui.BaseResourceInjector;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.container.dashboard.Dashboard;
import com.javexpress.gwt.library.ui.data.jqgrid.JqGrid;
import com.javexpress.gwt.library.ui.data.slickgrid.DataGrid;
import com.javexpress.gwt.library.ui.event.ExceptionThrownEvent;
import com.javexpress.gwt.library.ui.event.FormShowInWindowRequest;
import com.javexpress.gwt.library.ui.event.handler.ExceptionThrownEventHandler;
import com.javexpress.gwt.library.ui.event.handler.FormShowInWindowRequestHandler;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.form.decimalbox.DecimalBox;
import com.javexpress.gwt.library.ui.form.maskedit.MaskEditBox;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public abstract class BootstrapTheme extends BaseResourceInjector {

	@Override
	public void injectCore(JsonMap requireConfig, final Command onload) {
		JsUtil.USE_BOOTSTRAP = true;

		requireConfig.set("jquery", "scripts/jquery/jquery-1.11.3.min");
		requireConfig.set("moment", "scripts/moment/moment-2.10.3.min");
		_requireConfig(GWT.getModuleName(), requireConfig.getJavaScriptObject());

		WidgetBundles jq = new WidgetBundles("jQuery 1.11.4");
		jq.addJavaScript("@jquery");

		jq = new WidgetBundles("jQuery UI 1.11.4", jq);
		jq.addJavaScript("scripts/jquery/jquery-ui-1.11.4.min.js");

		WidgetBundles wb = new WidgetBundles("Bootstrap 3.3.5", jq);
		wb.addStyleSheet("scripts/bootstrap/bootstrap-3.3.5.min.css");
		wb.addStyleSheet("fonts/fontawesome/font-awesome-4.4.0.min.css");
		addStyleSheets(wb, 0);
		addJavaScripts(wb, 0);

		wb.addStyleSheet("scripts/javexpress/JexpGwtBootstrapBase-0.1.css");
		wb.addJavaScript("scripts/bootstrap/bootstrap-3.3.5.min.js");
		wb.addJavaScript("scripts/javexpress/jexpUICore-0.1.js");
		injectLibrary(wb, onload);
	}

	protected WidgetBundles createUIBundles() {
		WidgetBundles bundles = new WidgetBundles(getThemeName() != null ? getThemeName() : "Bootstrap Based UI");
		bundles.addStyleSheet("scripts/gritter/jquery.gritter-1.7.4.css");
		bundles.addStyleSheet("scripts/javexpress/JexpGwtBootstrapUI-0.1.css");
		bundles.addJavaScript("scripts/javexpress/jexpLocalizationStringHelper-0.1.js");
		addStyleSheets(bundles, 10);
		addJavaScripts(bundles, 10);

		WidgetBundles jexp = new WidgetBundles("JavExpress UI", bundles);
		addStyleSheets(jexp, 100);
		addJavaScripts(jexp, 100);

		jexp.addJavaScript("scripts/gritter/jquery.gritter-1.7.4.min.js");
		jexp.addJavaScript("scripts/numeral/numeral-1.5.3.min.js");
		jexp.addJavaScript("scripts/slickgrid/jquery.event.drag-2.2.js");
		jexp.addJavaScript("scripts/slickgrid/jquery.event.drop-2.2.js");
		jexp = DateBox.fillResources(jexp);
		jexp.addJavaScript("scripts/numeral/numeral.languages.min.js");
		Dashboard.fillResources(jexp);
		MaskEditBox.fillResources(jexp);
		DecimalBox.fillResources(jexp);

		jexp = JqGrid.fillResources(jexp);
		jexp = DataGrid.fillResources(jexp);
		addStyleSheets(jexp, 1000);
		addJavaScripts(jexp, 1000);
		return jexp;
	}

	@Override
	public void injectUI(String applicationCss, Command onload) {
		WidgetBundles jexp = createUIBundles();
		jexp.addStyleSheet(applicationCss);
		injectLibrary(jexp, onload);
	}

	protected abstract String getThemeName();

	protected abstract void addStyleSheets(WidgetBundles wb, int phase);

	protected abstract void addJavaScripts(WidgetBundles wb, int phase);

	protected void prepareCommons(ClientContext clientContext) {
		ClientContext.EVENT_BUS.addHandler(FormShowInWindowRequest.TYPE, new FormShowInWindowRequestHandler() {
			@Override
			public void onFormShowInWindowRequested(FormShowInWindowRequest formShowInWindowRequest) {
				IUIComposite form = formShowInWindowRequest.getForm();
				com.javexpress.gwt.library.ui.dialog.WindowView w = new com.javexpress.gwt.library.ui.dialog.WindowView(form.getId(), formShowInWindowRequest.isModal());
				w.setForm(form);
				w.setPosition(formShowInWindowRequest.getPosX(), formShowInWindowRequest.getPosY());
				w.show();
			}
		});
		ClientContext.EVENT_BUS.addHandler(ExceptionThrownEvent.TYPE, new ExceptionThrownEventHandler() {
			@Override
			public void onExceptionThrown(ExceptionThrownEvent exceptionThrownEvent) {
				com.javexpress.gwt.library.ui.bootstrap.ErrorDialog.showError(exceptionThrownEvent.getWindowId(), exceptionThrownEvent.getAppException());
			}
		});
	}

	public abstract void prepareUI(ClientContext clientContext);

	@Override
	public void destroyUI() {
		Dashboard.clearRegistry();
	}

	protected BootstrapClient getClient() {
		return (BootstrapClient) BootstrapClient.instance;
	}

}