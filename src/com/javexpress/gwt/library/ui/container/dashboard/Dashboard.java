package com.javexpress.gwt.library.ui.container.dashboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class Dashboard extends AbstractContainer implements ISizeAwareWidget {

	public static void fillResources(WidgetBundles wb) {
		wb.addStyleSheet("scripts/dashboard/jexpDashboard.css");
		wb.addJavaScript("scripts/dashboard/jexpDashboard-0.1.js");
	}

	private static HashMap<String, DashboardRegistryEntry>	registry	= new HashMap<String, DashboardRegistryEntry>();

	public static void clearRegistry() {
		registry = null;
	}

	public static class DashboardRegistryEntry {
		private String					code, label, catalog;
		private boolean					used, added;
		private IDashboardWidgetFactory	factory;

		public String getCode() {
			return code;
		}

		public void setCode(final String code) {
			this.code = code;
		}

		public boolean isAdded() {
			return added;
		}

		public void setAdded(boolean added) {
			this.added = added;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(final String label) {
			this.label = label;
		}

		public String getCatalog() {
			return catalog;
		}

		public void setCatalog(final String catalog) {
			this.catalog = catalog;
		}

		public IDashboardWidgetFactory getFactory() {
			return factory;
		}

		public void setFactory(final IDashboardWidgetFactory factory) {
			this.factory = factory;
		}

		public boolean isUsed() {
			return used;
		}

		public void setUsed(final boolean used) {
			this.used = used;
		}

		public DashboardRegistryEntry(final String code, final String label, final String catalog, final IDashboardWidgetFactory factory) {
			super();
			this.code = code;
			this.label = label;
			this.catalog = catalog;
			this.factory = factory;
		}
	}

	private JavaScriptObject				widget;
	private List<Element>					columns	= new ArrayList<Element>();
	private IDashboardItemReplaceListener	listener;
	private Map<String, DashboardWidget>	currentWidgets;
	private boolean							showing;

	public IDashboardItemReplaceListener getListener() {
		return listener;
	}

	public void setListener(final IDashboardItemReplaceListener listener) {
		this.listener = listener;
	}

	public Dashboard(final String id) {
		super(DOM.createDiv());
		getElement().setId(id == null ? DOM.createUniqueId() : id);
		getElement().setClassName("ui-helper-reset ui-widget jexpBorderBox ui-dashboard");
	}

	public Dashboard(final String id, final int[] colWidths) {
		this(id);
		if (JsUtil.USE_BOOTSTRAP) {
			for (int i = 0; i < colWidths.length; i++) {
				Element ul = addColumn(null, i == 0);
				ul.addClassName("col-xs-" + colWidths[i]);
			}
		} else {
			double remaining = 100;
			for (int i = 0; i < colWidths.length; i++) {
				int w = (int) (colWidths[i] - (0.125 * (colWidths.length - 1)));
				addColumn(w, i == 0);
				if (i < colWidths.length - 1)
					remaining -= w + 0.3;
			}
			columns.get(columns.size() - 1).getStyle().setWidth(remaining, Unit.PCT);
		}
	}

	public static void registerWidget(final String code, final String label, final String catalog, final IDashboardWidgetFactory dbwf) throws Exception {
		if (registry.containsKey(code))
			throw new Exception("Dashboard widget (" + code + ") is already registered");
		registry.put(code, new DashboardRegistryEntry(code, label, catalog, dbwf));
	}

	public void addWidget(Byte kolon, Byte order, final String code, long reference, HashMap<String, Serializable> parameters) throws Exception {
		if (kolon.intValue() >= columns.size()) {
			kolon = (byte) (columns.size() - 1);
			order = null;
		}
		Element ul = columns.get(kolon.intValue());
		DashboardRegistryEntry entry = registry.get(code);
		if (entry == null)
			throw new Exception("Dashboard widget " + code + " not registered");
		IDashboardWidgetFactory factory = entry.getFactory();
		DashboardWidget panel = factory.createWidget(this, code, parameters);
		if (panel == null)
			throw new Exception("Unresolved widget code " + code);
		if (currentWidgets == null)
			currentWidgets = new HashMap<String, DashboardWidget>();
		currentWidgets.put(code, panel);
		panel.getElement().setAttribute("code", code);
		panel.getElement().setAttribute("refr", String.valueOf(reference));
		if (order == null || order > ul.getChildCount())
			add(panel, ul);
		else
			insert(panel, ul, order, true);
		if (isAttached()) {
			panel.onShow();
		}
	}

	private Element addColumn(final Integer colWidth, boolean first) {
		Element ul = DOM.createElement("ul");
		ul.setClassName("ui-dashboard-column" + (first ? " ui-dashboard-column-first" : ""));
		if (colWidth != null)
			ul.getStyle().setWidth(colWidth, Unit.PCT);
		columns.add(ul);
		ul.setAttribute("col", String.valueOf(columns.size() - 1));
		getElement().appendChild(ul);
		return ul;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		init();
	}

	private native JavaScriptObject createByJs(Dashboard x, String id, JavaScriptObject options) /*-{
		options.onupdate = function(code, refr, col, ind) {
			x.@com.javexpress.gwt.library.ui.container.dashboard.Dashboard::fireOnUpdate(Ljava/lang/String;Ljava/lang/String;II)(code,refr,col,ind);
			return false;
		}
		options.onsettings = function(code, refr) {
			x.@com.javexpress.gwt.library.ui.container.dashboard.Dashboard::fireOnSettings(Ljava/lang/String;Ljava/lang/String;)(code,refr);
			return false;
		}
		var el = new $wnd.JexpUI.Dashboard(id, options);
		return el;
	}-*/;

	@Override
	protected void onUnload() {
		widget = null;
		listener = null;
		currentWidgets = null;
		clear();
		columns = null;
		destroyByJs(getElement());
		super.onUnload();
	}

	@Override
	public void clear() {
		for (Widget w : getChildren())
			remove(w);
	}

	private native void destroyByJs(Element elem) /*-{
		$wnd.$(elem).empty().off();
	}-*/;

	public HashMap<String, DashboardRegistryEntry> getRegistry() {
		return registry;
	}

	public void init() {
		JsonMap options = new JsonMap();
		for (Widget w : getChildren()) {
			DashboardWidget dbw = (DashboardWidget) w;
			options.set(dbw.getElement().getId(), dbw.getOptions());
			//dbw.onShow();
		}
		if (JsUtil.isRTL())
			options.set("direction", "rtl");
		widget = createByJs(this, getElement().getId(), options.getJavaScriptObject());
		for (Widget w : getChildren()) {
			final DashboardWidget dbw = (DashboardWidget) w;
			if (dbw.getRefreshTimeInSeconds() > 0) {
				Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
					@Override
					public boolean execute() {
						return dbw.isAttached() && dbw.doRefresh(showing);
					}
				}, dbw.getRefreshTimeInSeconds() * 1000);
			}
		}
	}

	//---EVENTS
	public void fireOnUpdate(final String code, String reference, final int column, final int order) {
		if (listener != null) {
			if (column > -1)
				listener.itemReplaced(code, Long.valueOf(reference), Integer.valueOf(column), Integer.valueOf(order));
			else {
				if (currentWidgets != null)
					currentWidgets.remove(code);
				listener.itemDeactivated(code, Long.valueOf(reference));
			}
			onResize();
		}
	}

	public void fireOnSettings(final String code, String reference) {
		if (currentWidgets == null)
			return;
		if (listener != null) {
			DashboardWidget dbw = currentWidgets.get(code);
			listener.doItemSetup(dbw, code, Long.valueOf(reference));
		}
	}

	public void onShow() {
		showing = true;
	}

	public void onHide() {
		showing = false;
	}

}