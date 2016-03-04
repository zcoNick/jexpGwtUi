package com.javexpress.gwt.library.ui.keyboard;

import java.beans.Beans;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class Keyboard extends SimplePanel {
	
	public static final String STYLENAME = "jesKeyboard";

	public static void fillResources(final List<String> styleSheets, final List<String> javaScripts) {
		//https://github.com/Mottie/Keyboard
		styleSheets.add("scripts/keyboard/jquery.keyboard.css");
		javaScripts.add("scripts/keyboard/jquery.keyboard-1.17.7.min.js");
		javaScripts.add("scripts/keyboard/turkish.js");
	}
	
	public static Map<String,String> getLayouts(){
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("german-qwertz-1","German (qwertz-1)");
		map.put("german-qwertz-2","German (qwertz-2)");
		map.put("turkish-q","Turkish (q)");
		map.put("turkish-f","Turkish (f)");
		return map;
	}
	
	private JsonMap	options;
	private JavaScriptObject term;
	
	public Keyboard(boolean global) {
		super();
		JsUtil.ensureId(null, this, WidgetConst.KEYBOARD_PREFIX, "keyboard");
		options = createDefaultOptions();
		setGlobal(global);
	}

	private JsonMap createDefaultOptions() {
		options = new JsonMap();
		options.set("alwaysOpen", true);
		options.set("usePreview", false);
		options.set("autoAccept", true);
		options.set("tabNavigation", true);
		//options.set("enterNavigation", true);
		return options;
	}
	
	public void setGlobal(boolean value){
		if (value){
			getElement().getStyle().setPosition(Position.ABSOLUTE);
			getElement().getStyle().setTop(Window.getClientHeight()-2,Unit.PX);
			getElement().getStyle().setLeft(2,Unit.PX);
			getElement().getStyle().setRight(2,Unit.PX);
			getElement().getStyle().setBottom(2,Unit.PX);
		}
		options.set("global", value);
	}
	
	public void setStickyShift(boolean value){
		options.set("stickyShift", value);
	}
	
	public void setLayout(String value){
		options.set("layout", value);
	}
	
	public void setBigger(boolean wide){
		options.set("csscontainer", wide?"jesKeyboardWide":"jesKeyboardBig");
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
	}

	private native JavaScriptObject createByJs(Keyboard x, Element element, String style, JavaScriptObject options) /*-{
		//her bilesen icin bir keyboard olusuyor su anda.
	    options.position={
            of: options.global?$wnd.$(element):"",
            my: "center top",
            at: "center top"
	    };
	    if (options.csscontainer)
	    	options.css = { container : "ui-keyboard ui-widget-content ui-widget ui-corner-all ui-helper-clearfix "+options.csscontainer };
	    var el = $wnd.$(options.global?"input,textarea":"."+style).keyboard(options);
		return el;
	}-*/;

	@Override
	protected void onUnload() {
		options = null;
		if (!Beans.isDesignTime())
			destroyByJs(getElement());
		super.onUnload();
	}
	
	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).remove();
	}-*/;

	public void open() {
		RootPanel.get().add(this);		
	}
	
}