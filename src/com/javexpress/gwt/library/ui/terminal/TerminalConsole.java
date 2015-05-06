package com.javexpress.gwt.library.ui.terminal;

import java.beans.Beans;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class TerminalConsole extends JexpSimplePanel {
	
	public static void fillResources(final List<String> styleSheets, final List<String> javaScripts) {
		//http://terminal.jcubic.pl/rpc-demo.html
		styleSheets.add("scripts/terminal/jquery.terminal.css");
		javaScripts.add("scripts/terminal/jquery.terminal-0.6.3.min.js");
	}
	
	private JsonMap	options;
	private JavaScriptObject term;
	
	public TerminalConsole(final Widget parent, final String id, ServiceDefTarget service, Enum method) {
		this(parent, id, null,service, method);
	}

	public TerminalConsole(final Widget parent, final String id, final JsonMap pOptions, ServiceDefTarget service, Enum method) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.TERMINALCONSOLE_PREFIX, id);
		options = pOptions == null ? createDefaultOptions() : pOptions;
		options.set("url", service.getServiceEntryPoint() + "." + method.toString());
		setWidth("7em");
	}

	private JsonMap createDefaultOptions() {
		options = new JsonMap();
		options.set("greetings", "Welcome!");
		options.set("width", "500");
		options.set("height", "250");
		options.set("history", "true");
		options.set("tabcompletion", "true");
		return options;
	}
	
	public void setGreetings(String greetings){
		options.set("greetings", greetings);
	}

	public void setPrompt(String prompt){
		options.set("prompt", prompt+"> ");
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		if (!Beans.isDesignTime())
			term = createByJs(this, getElement(), options.getJavaScriptObject());
	}

	private native JavaScriptObject createByJs(TerminalConsole x, Element element, JavaScriptObject options) /*-{
		options.login=function(login,pass,callback){
			callback.call(this,true);
		};
		options.completion=function(terminal, cand, callback){
			var cmds = ["info","whoami"];//login_name()http://terminal.jcubic.pl/api_reference.php
			callback.call(this,cmds);
		};
		return $wnd.$(element).terminal(options);
	}-*/;

	public void echo(String cmd){
		_echo(term, cmd);
	}

	private native void _echo(JavaScriptObject t, String cmd) /*-{
		t.echo(cmd);
	}-*/;
	
	@Override
	protected void onUnload() {
		options = null;
		if (!Beans.isDesignTime())
			destroyByJs(getElement());
		super.onUnload();
	}
	
	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).terminal('destroy');
	}-*/;
	
}