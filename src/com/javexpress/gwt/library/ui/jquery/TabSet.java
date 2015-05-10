package com.javexpress.gwt.library.ui.jquery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainerFocusable;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.JqIcon;
import com.javexpress.gwt.library.ui.container.tabset.ITabSetListener;
import com.javexpress.gwt.library.ui.container.tabset.TabItem;
import com.javexpress.gwt.library.ui.form.Form;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class TabSet extends AbstractContainerFocusable implements ISizeAwareWidget {

	private JavaScriptObject	jsObject;
	private JsonMap				options				= new JsonMap();
	private Map<String, Widget>	widgets;
	private List<String>		sizeWaitingWidgets	= new ArrayList<String>();
	private Element				navBar;
	private ITabSetListener		listener;
	private int					tabcounter			= 0;
	private boolean				useSmallerTitles;
	private String				activeWidgetId;

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

	@Deprecated
	public TabSet() {
		this(null, null);
	}

	/** Designer compatible constructor */
	public TabSet(final Widget parent, final String id) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.TABSET_PREFIX, id);
		getElement().addClassName("jexpBorderBox");
		navBar = DOM.createElement("ul");
		getElement().appendChild(navBar);
		options.set("heightStyle", "fill");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		jsObject = createByJs(this, getElement().getId(), options.getJavaScriptObject());
		if (widgets != null)
			performOnShow(widgets.keySet().iterator().next());
	}

	private native void bindCloseButtons(TabSet x, JavaScriptObject obj, Element liElement, Element closeSpan) /*-{
																													$wnd
																													.$(closeSpan)
																													.click(
																													function() {
																													var li = $wnd.$(liElement);
																													var tabId = li.attr("tabid");
																													x.@com.javexpress.gwt.library.ui.jquery.TabSet::fireOnTabClosed(Ljava/lang/String;)(tabId);
																													var panelId = li.remove().attr("aria-controls");
																													$wnd.$("#" + panelId).remove();
																													obj.tabs("refresh");
																													});
																													}-*/;

	private native void unbindCloseButtons(TabSet x, Element navBar) /*-{
																		$wnd.$("li > span.ui-icon-close", $wnd.$(navBar)).off();
																		}-*/;

	@Override
	protected void onUnload() {
		unbindCloseButtons(this, navBar);
		jsObject = null;
		options = null;
		navBar = null;
		listener = null;
		for (Widget w : widgets.values())
			remove(w);
		widgets = null;
		sizeWaitingWidgets = null;
		destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
														$wnd.$(element).tabs('destroy');
														}-*/;

	private native JavaScriptObject createByJs(TabSet x, String id, JavaScriptObject options) /*-{
																								options.activate=function(event, ui ) {
																								x.@com.javexpress.gwt.library.ui.jquery.TabSet::fireOnAfterActivate(Ljava/lang/String;Ljava/lang/String;)(ui.oldTab.attr("tabid"), ui.newTab.attr("tabid"));
																								}
																								return $wnd.$("#" + id).tabs(options).removeClass("ui-widget-content");
																								}-*/;

	public void addTab(final Form form) {
		addTab(form.getHeader(), form, form.getId());
	}

	public void addTab(final IUIComposite form, boolean closable) {
		addTab(form.getHeader(), (Widget) form, form.getId(), closable);
	}

	@Deprecated
	public void addTab(final String title, final Widget widget, final String id) {
		addTab(title, widget, id, false);
	}

	public void addTab(final String title, final Widget widget, final String id, boolean closable) {
		addTab(null, title, widget, id, closable);
	}

	public void addTab(final JqIcon icon, final String title, final Widget widget, final String id) {
		addTab(icon, title, widget, id, false);
	}

	/** Designer compatible method */
	public void addTab(TabItem tabItem) {
		addTab(tabItem.getIcon(), tabItem.getTitle(), tabItem.getWidget(), tabItem.getId(), tabItem.isClosable());
	}

	public void addTab(final ICssIcon icon, final String title, final Widget widget, final String id, boolean closable) {
		if (widgets == null)
			widgets = new LinkedHashMap<String, Widget>();
		Element a = DOM.createAnchor();
		String tabId = getElement().getId() + "_" + tabcounter++;
		a.setAttribute("href", "#" + tabId);
		if (title != null)
			a.setInnerHTML(useSmallerTitles ? "<small>" + title + "</small>" : title);
		if (icon != null) {
			Element span = DOM.createSpan();
			span.setClassName("ui-icon " + icon.getCssClass());
			span.setAttribute("role", "presentation");
			a.appendChild(span);
		}
		Element li = DOM.createElement("li");
		li.appendChild(a);
		li.setAttribute("tabid", id);
		Element closeSpan = null;
		if (closable) {
			closeSpan = DOM.createSpan();
			closeSpan.setClassName("ui-icon " + JqIcon.close.getCssClass());
			closeSpan.setAttribute("role", "presentation");
			closeSpan.setInnerText(ClientContext.nlsCommon.kapat());
			li.appendChild(closeSpan);
		}

		navBar.appendChild(li);

		widgets.put(id, widget);
		sizeWaitingWidgets.add(id);
		Element div = DOM.createDiv();
		div.setId(tabId);
		div.addClassName("jesBorderFix");
		getElement().appendChild(div);
		add(widget, div);
		if (isAttached()) {
			if (closeSpan != null)
				bindCloseButtons(this, jsObject, li, closeSpan);
			_refresh(jsObject);
			select(widgets.size() - 1);
			performOnShow(id);
		}
	}

	private void performOnShow(String id) {
		if (!sizeWaitingWidgets.contains(id))
			return;
		Widget widget = widgets.get(id);
		if (widget instanceof Form) {
			((Form) widget).onShow();
			sizeWaitingWidgets.remove(id);
		} else if (widget instanceof RequiresResize) {
			((RequiresResize) widget).onResize();
			sizeWaitingWidgets.remove(id);
		}
	}

	private native void _refresh(JavaScriptObject obj) /*-{
														obj.tabs("refresh");
														}-*/;

	public void select(int index) {
		_select(jsObject, index);
	}

	private native void _select(JavaScriptObject obj, int index) /*-{
																	obj.tabs("option", "active", index);
																	}-*/;

	//EVENTS
	private void fireOnAfterActivate(final String oldId, final String newId) {
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

	@Override
	public void onResize() {
		_refresh(jsObject);
		super.onResize();
	}

	public Widget getActiveWidget() {
		return widgets.get(activeWidgetId);
	}

}