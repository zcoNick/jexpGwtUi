package com.javexpress.gwt.library.ui.js;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.javexpress.gwt.library.ui.bootstrap.DateBox;
import com.javexpress.gwt.library.ui.container.dashboard.Dashboard;
import com.javexpress.gwt.library.ui.container.panel.SplitPanel;
import com.javexpress.gwt.library.ui.data.jqgrid.JqGrid;
import com.javexpress.gwt.library.ui.data.jqplot.BarChart;
import com.javexpress.gwt.library.ui.data.jqplot.PieChart;
import com.javexpress.gwt.library.ui.data.schedule.Schedule;
import com.javexpress.gwt.library.ui.form.decimalbox.DecimalBox;
import com.javexpress.gwt.library.ui.form.maskedit.MaskEditBox;
import com.javexpress.gwt.library.ui.form.textbox.CKEditor;
import com.javexpress.gwt.library.ui.form.textbox.CodeEditor;
import com.javexpress.gwt.library.ui.menu.MenuBar;
import com.javexpress.gwt.library.ui.webcam.WebCam;

public class ResourceInjector {
	public static enum JqTheme {
		aristo(false), cupertino(true), sunny(false), flick(false), start(false),
		south_street(false), ui_lightness(false), ui_darkness(false), humanity(
				false),
		eggplant(false), smoothness(false), vader(false), redmond(false),
		trontastic(false), absolution(true), afternoon(true);

		boolean	invert;

		JqTheme(final boolean invert) {
			this.invert = invert;
		}

		public static JqTheme parse(final String theme) {
			if (JsUtil.isEmpty(theme))
				return JqTheme.afternoon;
			return JqTheme.valueOf(theme.replaceAll("-", "_"));
		}

		public boolean isInverted() {
			return invert;
		}
	}

	public static JqTheme	theme;

	@Deprecated
	public static void injectCore(final Command onload) {
		//injectStyleSheet("http://fonts.googleapis.com/css?family=Noto+Sans");
		List<String> styleSheets = new ArrayList<String>();
		String ptheme = Window.Location.getParameter("theme");
		ResourceInjector.theme = JqTheme.parse(ptheme);
		styleSheets.add("themes/" + theme.toString() + "/javexpress-gwt-library.ui.css");
		styleSheets.add("scripts/gritter/jquery.gritter.css");
		styleSheets.add("JexpGwtLibraryBase.css");

		List<String> javaScripts = new ArrayList<String>();
		javaScripts.add("scripts/jquery-1.11.1.min.js");
		javaScripts.add("scripts/jquery-ui-1.11.2.min.js");
		javaScripts.add("scripts/gritter/jquery.gritter.min.js");
		javaScripts.add("scripts/jexpUICore-0.1.js");
		javaScripts.add("scripts/jexpLocalizationStringHelper-0.1.js");
		injectLibrary("JexpUI Core", styleSheets, javaScripts, onload);
	}

	@Deprecated
	public static void injectUI(final Command onload, final String applicationCss) {
		WidgetBundles wb = new WidgetBundles("JexpUI Extra");

		wb.addStyleSheet("themes/common/jquery-silk-icons.css");
		wb.addJavaScript("scripts/jquery.ui.datepicker-" + LocaleInfo.getCurrentLocale().getLocaleName() + ".min.js");
		wb.addJavaScript("scripts/jquery-ui-timepicker-1.3.js");
		MenuBar.fillResources(wb);
		Dashboard.fillResources(wb);
		JqGrid.fillResources(wb);
		MaskEditBox.fillResources(wb);
		DecimalBox.fillResources(wb);
		//-END User Interface

		//User Interface Extra
		BarChart.fillResources(wb);
		PieChart.fillResources(wb);
		Schedule.fillResources(wb);
		//-END User Interface Extra

		//AWESOME
		SplitPanel.fillResources(wb);
		CKEditor.fillResources(wb);
		CodeEditor.fillResources(wb);
		//Keyboard.fillResources(styleSheets, javaScripts);
		//TerminalConsole.fillResources(styleSheets, javaScripts);
		WebCam.fillResources(wb);
		//-END AWESOME

		//APPLICATION
		wb.addStyleSheet("JexpGwtLibraryUI.css");
		wb.addJavaScript(applicationCss);
		//-END APPLICATION

		injectLibrary(wb, onload);
	}

	private static void injectLibrary(final WidgetBundles wb, final Command onload) {
		if (wb.getParent() != null)
			injectLibrary(wb.getParent(), new Command() {
				@Override
				public void execute() {
					injectLibrary(wb.getName(), wb.getStyleSheets(), wb.getJavaScripts(), onload);
				}
			});
		else
			injectLibrary(wb.getName(), wb.getStyleSheets(), wb.getJavaScripts(), onload);
	}

	public static native void loadCss(String url) /*-{
													var fileref = document.createElement("link");
													fileref.setAttribute("rel", "stylesheet");
													fileref.setAttribute("type", "text/css");
													fileref.setAttribute("href", url);
													$doc.getElementsByTagName("head")[0].appendChild(fileref);
													}-*/;

	private static void injectLibrary(final String name, List<String> styleSheets, List<String> javaScripts, final Command onload) {
		RootPanel root = RootPanel.get("progress");
		final Element progress = root != null ? root.getElement() : null;
		for (String css : styleSheets) {
			if (progress != null)
				progress.setInnerHTML("Injecting " + css);
			else
				Window.setStatus("Injecting " + css);
			loadCss((!css.startsWith("http") ? GWT.getModuleBaseURL() : "") + css);
		}
		JsArrayString files = JsArrayString.createArray(javaScripts.size()).cast();
		int i = 0;
		for (String js : javaScripts)
			if (js.startsWith("@"))
				files.set(i++, js.substring(1));
			else
				files.set(i++, (!js.startsWith("http") ? GWT.getModuleBaseURL() : "") + js);
		if (progress != null)
			progress.setInnerHTML("Injecting " + name);
		else
			Window.setStatus("Injecting " + name);
		Command command = new Command() {
			@Override
			public void execute() {
				GWT.log(name + " injected.");
				if (progress != null)
					progress.setInnerHTML(name + " injected.");
				else
					Window.setStatus("");
				if (onload != null)
					onload.execute();
			}
		};
		_injectFiles(files, command);
	}

	private static native void _injectFiles(JsArrayString files, Command command) /*-{
																					$wnd.require(files, function() {
																					command.@com.google.gwt.user.client.Command::execute()();
																					});
																					}-*/;

	public static void injectScript(final String path, final Callback<Void, Exception> callback) {
		ScriptInjector.fromUrl(path.startsWith("http") ? path : GWT.getModuleBaseURL() + path).setWindow(ScriptInjector.TOP_WINDOW).setCallback(callback).inject();
	}

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

	private static native void _requireConfig(String module, JavaScriptObject pathConfig) /*-{
																							$wnd.requirejs.config({
																							baseUrl : module,
																							paths : pathConfig
																							});
																							}-*/;

	public static void injectAceUI(final Command onload) {
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
		injectLibrary(jexp, onload);
	}

	public static void inject(WidgetBundles wb, Command onload) {
		injectLibrary(wb, onload);
	}

}