package com.javexpress.gwt.library.ui.menu;

import java.beans.Beans;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Focusable;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class MenuBar extends JexpSimplePanel implements Focusable {

	public static void fillResources(final WidgetBundles wb) {
		wb.addStyleSheet("scripts/menubar/jesMenuBar.css");
		wb.addJavaScript("scripts/menubar/jesMenuBar.css");
	}

	private JsonMap						options;
	private Map<String, Command>		commands;
	private Map<String, IMenuHandler>	handlers;
	private List<MenuItem>				widgets	= new ArrayList<MenuItem>();

	public MenuBar(final String id) {
		this(id, null);
	}

	public MenuBar(final String id, final JsonMap pOptions) {
		super(DOM.createElement("ul"));
		JsUtil.ensureId(null, this, WidgetConst.MENUBAR_PREFIX, id);
		options = pOptions == null ? createDefaultOptions() : pOptions;
	}

	public void add(MenuItem mi) {
		getElement().appendChild(mi.getElement());
		widgets.add(mi);
	}

	private JsonMap createDefaultOptions() {
		options = new JsonMap();
		options.set("autoExpand", false);
		options.set("useHeaderStyle", true);
		options.set("direction", JsUtil.isLTR() ? "ltr" : "rtl");
		return options;
	}

	public boolean isUseHeaderStyle() {
		return options.getBoolean("useHeaderStyle");
	}

	public void setUseHeaderStyle(boolean useHeaderStyle) {
		options.set("useHeaderStyle", useHeaderStyle);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!Beans.isDesignTime()) {
			prepareHandlers();
			createByJs(this, getElement(), options.getJavaScriptObject());
		}
	}

	private void prepareHandlers() {
		handlers = new HashMap<String, IMenuHandler>();
		commands = new HashMap<String, Command>();
		Iterator<MenuItem> iter = widgets.iterator();
		while (iter.hasNext()) {
			MenuItem mi = iter.next();
			mi.fillHandlers(handlers, commands);
		}
	}

	private native void createByJs(MenuBar x, Element element, JavaScriptObject options) /*-{
		options.select=function(event, ui){
			var el = event.currentTarget;
			x.@com.javexpress.gwt.library.ui.menu.MenuBar::fireOnClick(Ljava/lang/String;Ljava/lang/String;)(el.id,el.getAttribute("code"));
		};
		options.position = { within: $wnd.$("#demo-frame").add($wnd.window).first() };		
		$wnd.$(element).menubar(options);
	}-*/;

	@Override
	protected void onUnload() {
		options = null;
		commands = null;
		handlers = null;
		if (widgets != null)
			for (MenuItem mi : widgets)
				mi.removeFromParent();
		widgets = null;
		if (!Beans.isDesignTime())
			destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element el) /*-{
		$wnd.$(el).menubar('destroy');
	}-*/;

	//---------- EVENTS
	public void fireOnClick(final String id, final String code) {
		IMenuHandler handler = handlers.get(id);
		if (handler != null) {
			handler.itemClicked(code);
		}
		Command command = commands.get(id);
		if (command != null) {
			command.execute();
		}
	}

	@Override
	public int getTabIndex() {
		return getElement().getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
	}

	@Override
	public void setFocus(boolean focused) {
	}

	@Override
	public void setTabIndex(int index) {
		getElement().setTabIndex(index);
	}

}