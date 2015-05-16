package com.javexpress.gwt.library.ui;

import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public abstract class BaseResourceInjector implements IResourceInjector {

	protected static void injectLibrary(final WidgetBundles wb, final Command onload) {
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

	protected static void injectLibrary(final String name, List<String> styleSheets, List<String> javaScripts, final Command onload) {
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

	protected static native void _requireConfig(String module, JavaScriptObject pathConfig) /*-{
																							$wnd.requirejs.config({
																							baseUrl : module,
																							paths : pathConfig
																							});
																							}-*/;

	public static void inject(WidgetBundles wb, Command onload) {
		injectLibrary(wb, onload);
	}

}