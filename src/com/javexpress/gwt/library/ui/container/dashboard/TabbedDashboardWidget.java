package com.javexpress.gwt.library.ui.container.dashboard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.container.tabset.TabItem;
import com.javexpress.gwt.library.ui.form.IUIComposite;

public abstract class TabbedDashboardWidget extends DashboardWidget {

	private Element				navTabs;
	private Element				tabsDiv;
	private Map<String, Widget>	widgets;
	private List<String>		sizeWaitingWidgets	= new ArrayList<String>();

	public TabbedDashboardWidget(Widget parent, String id) throws Exception {
		super(parent, id, false);

		navTabs = DOM.createElement("ul");
		navTabs.setClassName("nav nav-tabs");
		toolDiv.appendChild(navTabs);

		tabsDiv = DOM.createDiv();
		tabsDiv.setClassName("tab-content padding-2");
		contentDiv.appendChild(tabsDiv);

		createWidget();
	}

	protected void addTab(TabItem tab) throws Exception {
		String id = getElement().getId() + "_" + (tab.getId() != null ? tab.getId() : tab.hashCode());
		if (widgets == null)
			widgets = new LinkedHashMap<String, Widget>();
		else if (widgets.containsKey(id))
			throw new Exception("There is already a widget in tab with id : " + id);

		Element li = DOM.createElement("li");
		Element a = DOM.createAnchor();
		a.setAttribute("href", "#" + id);
		a.setAttribute("tabid", tab.getId());
		a.setAttribute("data-toggle", "tab");
		a.setAttribute("aria-expanded", "false");
		if (tab.getIcon() != null)
			a.setInnerHTML("<i class='ace-icon " + tab.getIcon().getCssClass() + "'></i> " + tab.getTitle());
		else
			a.setInnerHTML(tab.getTitle());
		li.appendChild(a);
		navTabs.appendChild(li);

		Element tabDiv = DOM.createDiv();
		tabDiv.setClassName("tab-pane");
		tabDiv.setId(id);
		tabsDiv.appendChild(tabDiv);
		add(tab.getWidget(), tabDiv);
		widgets.put(tab.getId(), tab.getWidget());
		sizeWaitingWidgets.add(tab.getId());

		if (tabsDiv.getChildCount() == 1)
			setActiveTab(0);
	}

	private void setActiveTab(int index) {
		for (int i = 0; i < navTabs.getChildCount(); i++) {
			((Element) navTabs.getChild(i)).removeClassName("active");
		}
		Element navLi = (Element) navTabs.getChild(index);
		navLi.addClassName("active");
		for (int i = 0; i < tabsDiv.getChildCount(); i++) {
			((Element) tabsDiv.getChild(i)).removeClassName("active");
		}
		Element tabDiv = (Element) tabsDiv.getChild(index);
		tabDiv.addClassName("active");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement());
	}

	private native JavaScriptObject createByJs(TabbedDashboardWidget x, Element el) /*-{
		var jso = $wnd.$("a[data-toggle='tab']",el).on('shown.bs.tab', function(e) {
			x.@com.javexpress.gwt.library.ui.container.dashboard.TabbedDashboardWidget::fireOnTabChanged(Ljava/lang/String;Ljava/lang/String;)($wnd.$(e.relatedTarget).attr("tabid"), $wnd.$(e.target).attr("tabid"));
		});
		return jso;
	}-*/;

	@Override
	protected void onUnload() {
		navTabs = null;
		tabsDiv = null;
		sizeWaitingWidgets = null;
		widgets = null;
		destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).off().empty();
	}-*/;

	//EVENTS
	private void fireOnTabChanged(final String oldId, final String newId) {
		performOnShow(newId);
	}

	private void performOnShow(String id) {
		Widget widget = widgets.get(id);
		if (sizeWaitingWidgets.contains(id) && widget instanceof RequiresResize) {
			sizeWaitingWidgets.remove(id);
			((RequiresResize) widget).onResize();
		}
		if (widget instanceof IUIComposite) {
			((IUIComposite) widget).onShow();
		}
	}

}