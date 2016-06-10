package com.javexpress.gwt.library.ui.bootstrap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainerFocusable;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.container.tabset.ITabSetListener;
import com.javexpress.gwt.library.ui.container.tabset.TabItem;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class TabSet extends AbstractContainerFocusable implements ISizeAwareWidget {

	public static enum TabsPosition {
		top, left, right, bottom
	}

	private JavaScriptObject	jsObject;
	private Map<String, Widget>	widgets;
	private List<String>		sizeWaitingWidgets	= new ArrayList<String>();
	private Element				navBar, tabContents;
	private ITabSetListener		listener;
	private int					tabcounter			= 0;
	private boolean				useSmallerTitles;
	private String				activeWidgetId;
	private TabsPosition		tabsPosition;
	private boolean				useAnimation;
	private boolean				contentBorders		= false;
	private boolean				inverseTabs			= false;

	public boolean isInverseTabs() {
		return inverseTabs;
	}

	public void setInverseTabs(boolean inverseTabs) {
		this.inverseTabs = inverseTabs;
	}

	public boolean isContentBorders() {
		return contentBorders;
	}

	public void setContentBorders(boolean contentBorders) {
		this.contentBorders = contentBorders;
	}

	public boolean isUseAnimation() {
		return useAnimation;
	}

	public void setUseAnimation(boolean useAnimation) {
		this.useAnimation = useAnimation;
	}

	public TabsPosition getTabsPosition() {
		return tabsPosition;
	}

	public void setTabsPosition(TabsPosition tabsPosition) {
		this.tabsPosition = tabsPosition;
	}

	public boolean isUseSmallerTitles() {
		return useSmallerTitles;
	}

	public void setUseSmallerTitles(boolean useSmallerTitles) {
		this.useSmallerTitles = useSmallerTitles;
	}

	public ITabSetListener getListener() {
		return listener;
	}

	public void setListener(final ITabSetListener listener) {
		this.listener = listener;
	}

	@Override
	public void setHeight(String height) {
		tabContents.getStyle().setProperty("height", height);
	}

	@Deprecated
	public TabSet() {
		this(null, null);
	}

	/** Designer compatible constructor */
	public TabSet(final Widget parent, final String id) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.TABSET_PREFIX, id);
		getElement().addClassName("jexpTabSet");
		navBar = DOM.createElement("ul");
		navBar.setClassName("nav nav-tabs");
		getElement().appendChild(navBar);
		tabContents = DOM.createDiv();
		tabContents.setClassName("tab-content");
		getElement().appendChild(tabContents);
	}

	private TabSet(Element element) {
		super(element);
		getElement().addClassName("jexpTabSet");
		navBar = getElement().getFirstChildElement();
		tabContents = navBar.getNextSiblingElement();
		jsObject = createByJs(this, getElement());
	}

	public static TabSet wrap(Element element) {
		// Assert that the element is attached.
		assert Document.get().getBody().isOrHasChild(element);

		TabSet tabset = new TabSet(element);

		// Mark it attached and remember it for cleanup.
		tabset.onAttach();
		RootPanel.detachOnWindowClose(tabset);

		return tabset;
	}

	@Override
	protected void onLoad() {
		if (contentBorders || inverseTabs)
			tabContents.addClassName("hasBorders");
		if (inverseTabs)
			navBar.addClassName("padding-2 tab-color-blue background-blue");
		if (tabsPosition == TabsPosition.left)
			getElement().addClassName("tabs-left");
		else if (tabsPosition == TabsPosition.bottom)
			getElement().addClassName("tabs-below");
		else if (tabsPosition == TabsPosition.right)
			getElement().addClassName("tabs-right");
		super.onLoad();
		jsObject = createByJs(this, getElement());
		if (widgets != null) {
			activeWidgetId = null;
			int index = -1;
			for (int i = 0; i < tabContents.getChildCount(); i++) {
				Element div = ((Element) tabContents.getChild(i));
				if (!div.hasClassName("hidden")) {
					activeWidgetId = div.getAttribute("tabid");
					index = i;
					break;
				}
			}
			if (index > -1) {
				if (index == 0)
					performOnShow(activeWidgetId);
				else
					select(index);
			}
		}
	}

	private native JavaScriptObject createByJs(TabSet x, Element el) /*-{
																		var jso = $wnd.$("a[data-toggle='tab']",el);
																		jso.on('show.bs.tab', function(e) {
																		x.@com.javexpress.gwt.library.ui.bootstrap.TabSet::fireOnTabChanging(Ljava/lang/String;Ljava/lang/String;)($wnd.$(e.relatedTarget).attr("tabid"), $wnd.$(e.target).attr("tabid"));
																		});
																		jso.on('shown.bs.tab', function(e) {
																		x.@com.javexpress.gwt.library.ui.bootstrap.TabSet::fireOnTabChanged(Ljava/lang/String;Ljava/lang/String;)($wnd.$(e.relatedTarget).attr("tabid"), $wnd.$(e.target).attr("tabid"));
																		});
																		return jso;
																		}-*/;

	@Override
	protected void onUnload() {
		jsObject = null;
		navBar = null;
		listener = null;
		if (widgets != null)
			for (Widget w : widgets.values())
				remove(w);
		widgets = null;
		sizeWaitingWidgets = null;
		activeWidgetId = null;
		clear();
		destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).off().empty();
	}-*/;

	public void addTab(final IUIComposite form, boolean closable) throws Exception {
		addTab(form.getHeader(), (Widget) form, form.getId(), closable);
	}

	@Deprecated
	public void addTab(final String title, final Widget widget, final String id) throws Exception {
		addTab(title, widget, id, false);
	}

	public void addTab(final String title, final Widget widget, final String id, boolean closable) throws Exception {
		addTab(null, title, widget, id, closable);
	}

	public void addTab(final ICssIcon icon, final String title, final Widget widget, final String id) throws Exception {
		addTab(icon, title, widget, id, false);
	}

	public void addTab(TabItem tabItem) throws Exception {
		addTab(tabItem.getIcon(), tabItem.getTitle(), tabItem.getWidget(), tabItem.getId(), tabItem.isClosable());
	}

	public void addTab(String title, String id, boolean closable) throws Exception {
		addTab(null, title, null, id, closable);
	}

	public void addTab(final ICssIcon icon, final String title, final Widget widget, final String id, boolean closable) throws Exception {
		if (JsUtil.isEmpty(id))
			throw new Exception("Id is required for tabItem");
		if (widgets == null)
			widgets = new LinkedHashMap<String, Widget>();
		else if (widgets.containsKey(id))
			throw new Exception("There is already a widget in tab with id : " + id);
		Element a = DOM.createAnchor();
		String tabId = getElement().getId() + "_" + tabcounter++;
		a.setAttribute("href", "#" + tabId);
		a.setAttribute("tabid", id);
		a.setAttribute("data-toggle", "tab");
		a.setAttribute("aria-expanded", "false");
		if (title != null)
			a.setInnerHTML(useSmallerTitles ? "<small>" + title + "</small>" : title);
		if (icon != null) {
			Element span = DOM.createElement("i");
			ClientContext.resourceInjector.applyIconStyles(span, icon);
			a.insertFirst(span);
		}
		Element li = DOM.createElement("li");
		li.appendChild(a);
		Element closeSpan = null;
		if (closable) {
			closeSpan = DOM.createSpan();
			ClientContext.resourceInjector.applyIconStyles(closeSpan, FaIcon.close);
			closeSpan.setInnerText(ClientContext.nlsCommon.kapat());
			li.appendChild(closeSpan);
		}
		navBar.appendChild(li);

		widgets.put(id, widget);
		sizeWaitingWidgets.add(id);
		Element div = DOM.createDiv();
		div.setId(tabId);
		div.setAttribute("tabid", id);
		div.addClassName("tab-pane" + (useAnimation ? " fade" : ""));
		tabContents.appendChild(div);
		if (widget != null)
			add(widget, div);
		if (isAttached()) {
			/*if (closeSpan != null)
				bindCloseButtons(this, jsObject, li, closeSpan);*/
			attachShowEvents(this, a, closeSpan);
			//select(widgets.size() - 1);
			//performOnShow(id);
		} else {
			if (tabcounter == 1) {
				li.addClassName("active");
				div.addClassName((useAnimation ? "in " : "") + "active");
			} else
				sizeWaitingWidgets.add(id);
		}
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

	public void select(int index) {
		String wid = (((Element) navBar.getChild(index).getChild(0)).getAttribute("tabid"));
		if (listener != null)
			listener.onTabChanging(activeWidgetId, wid);
		_select(navBar, index);
		if (listener != null)
			listener.onTabChanged(activeWidgetId, wid);
		activeWidgetId = wid;
	}

	private native void attachShowEvents(TabSet x, Element alink, Element clspan) /*-{
																					$wnd.$(alink).tab('show').on('show.bs.tab', function(e) {
																					x.@com.javexpress.gwt.library.ui.bootstrap.TabSet::fireOnTabChanging(Ljava/lang/String;Ljava/lang/String;)($wnd.$(e.relatedTarget).attr("tabid"), $wnd.$(e.target).attr("tabid"));
																					}).on('shown.bs.tab', function(e) {
																					x.@com.javexpress.gwt.library.ui.bootstrap.TabSet::fireOnTabChanged(Ljava/lang/String;Ljava/lang/String;)($wnd.$(e.relatedTarget).attr("tabid"), $wnd.$(e.target).attr("tabid"));
																					});
																					}-*/;

	private native void _select(Element navbar, int index) /*-{
		$wnd.$("li:eq(" + index + ") a", navbar).tab('show');
	}-*/;

	public void hideItem(String id) {
		toggleItem(id, false);
	}

	public void showItem(String id) {
		toggleItem(id, true);
	}

	private void toggleItem(String id, boolean show) {
		for (int i = 0; i < navBar.getChildCount(); i++) {
			Element a = ((Element) navBar.getChild(i)).getFirstChildElement();
			if (a.getAttribute("tabid").equals(id))
				a.getStyle().setDisplay(!show ? Display.NONE : Display.BLOCK);
		}
		for (int i = 0; i < tabContents.getChildCount(); i++) {
			Element div = ((Element) tabContents.getChild(i));
			if (div.getAttribute("tabid").equals(id)) {
				if (!show)
					div.addClassName("hidden");
				else
					div.removeClassName("hidden");
			}
		}
	}

	//EVENTS
	private void fireOnTabChanging(final String oldId, final String newId) {
		if (listener != null)
			listener.onTabChanging(oldId, newId);
	}

	private void fireOnTabChanged(final String oldId, final String newId) {
		performOnShow(newId);
		activeWidgetId = newId;
		if (listener != null)
			listener.onTabChanged(oldId, newId);
	}

	private void fireOnTabClosed(String oldId) {
		sizeWaitingWidgets.remove(oldId);
		Widget w = widgets.get(oldId);
		widgets.remove(oldId);
		if (w != null)
			remove(w);
		if (listener != null)
			listener.onTabClosed(oldId);
	}

	public Widget getActiveWidget() {
		return widgets.get(activeWidgetId);
	}

	public String getActiveTabItemId() {
		return activeWidgetId;
	}

	public void setTabWidget(String id, Widget widget) {
		sizeWaitingWidgets.remove(id);
		Widget old = widgets.get(id);
		widgets.remove(id);
		if (old != null)
			remove(old);
		if (widget != null) {
			for (int i = 0; i < tabContents.getChildCount(); i++) {
				Element div = (Element) tabContents.getChild(i);
				if (div.getAttribute("tabid").equals(id)) {
					add(widget, div);
					sizeWaitingWidgets.add(id);
					widgets.put(id, widget);
					break;
				}
			}
		}
	}

	public void setMaxHeight(int maxHeight) {
		getElement().getStyle().setProperty("maxHeight", maxHeight + "px");
	}

}