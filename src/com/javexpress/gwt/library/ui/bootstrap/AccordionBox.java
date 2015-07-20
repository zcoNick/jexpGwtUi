package com.javexpress.gwt.library.ui.bootstrap;

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
import com.javexpress.gwt.library.ui.container.accordion.AccordionItem;
import com.javexpress.gwt.library.ui.container.accordion.IAccordionListener;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class AccordionBox extends AbstractContainerFocusable implements ISizeAwareWidget {

	private JavaScriptObject	jsObject;
	private Map<String, Widget>	widgets;
	private int					tabCounter			= 0;
	private boolean				useSmallerTitles;
	private boolean				useAnimation		= true;
	private List<String>		sizeWaitingWidgets	= new ArrayList<String>();
	private String				activeWidgetId;
	private IAccordionListener	listener;

	public IAccordionListener getListener() {
		return listener;
	}

	public void setListener(IAccordionListener listener) {
		this.listener = listener;
	}

	/** Designer compatible constructor */
	public AccordionBox(final Widget parent, final String id) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.ACCORDIONBOX_PREFIX, id);
		getElement().addClassName("panel-group jexpAccordion");
		getElement().setAttribute("role", "tablist");
		getElement().setAttribute("aria-multiselectable", "true");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		jsObject = createByJs(this, getElement());
		if (widgets != null) {
			performOnShow(widgets.keySet().iterator().next());
		}
	}

	private native JavaScriptObject createByJs(AccordionBox x, Element el) /*-{
		var jso = $wnd.$("a[data-toggle='collapse']",el);
		jso.on('show.bs.collapse', function(e) {
			x.@com.javexpress.gwt.library.ui.bootstrap.AccordionBox::fireOnAccordionChanging(Ljava/lang/String;Ljava/lang/String;)($wnd.$(e.relatedTarget).attr("tabid"), $wnd.$(e.target).attr("tabid"));
		});
		jso.on('shown.bs.collapse', function(e) {
			x.@com.javexpress.gwt.library.ui.bootstrap.AccordionBox::fireOnAccordionChanged(Ljava/lang/String;Ljava/lang/String;)($wnd.$(e.relatedTarget).attr("tabid"), $wnd.$(e.target).attr("tabid"));
		});
		return jso;
	}-*/;

	@Override
	protected void onUnload() {
		jsObject = null;
		listener = null;
		for (Widget w : widgets.values())
			remove(w);
		widgets = null;
		clear();
		destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).off().empty();
	}-*/;

	public void addPanel(AccordionItem item) throws Exception {
		addPanel(item.getIcon(), item.getTitle(), item.getWidget(), item.getId());
	}

	public void addPanel(final ICssIcon icon, final String title, final Widget widget, final String id) throws Exception {
		if (JsUtil.isEmpty(id))
			throw new Exception("Id is required for accordionItem");
		if (widgets == null)
			widgets = new LinkedHashMap<String, Widget>();
		else if (widgets.containsKey(id))
			throw new Exception("There is already a widget in accordion with id : " + id);

		Element panel = DOM.createDiv();
		panel.setClassName("panel panel-default");

		Element heading = DOM.createDiv();
		heading.setClassName("panel-heading");
		heading.setAttribute("role", "tab");
		JsUtil.ensureSubId(getElement(), heading, "heading");
		String tabId = getElement().getId() + "_" + tabCounter++;
		Element h4 = DOM.createElement("h4");
		h4.setClassName("panel-title");
		Element a = DOM.createAnchor();
		a.setAttribute("data-toggle", "collapse");
		a.setAttribute("data-parent", "#" + getElement().getId());
		a.setAttribute("href", "#" + tabId);
		a.setAttribute("aria-expanded", String.valueOf(tabCounter == 1));
		a.setAttribute("aria-controls", tabId);
		a.setAttribute("tabid", id);
		if (title != null)
			a.setInnerHTML(useSmallerTitles ? "<small>" + title + "</small>" : title);
		if (icon != null) {
			Element span = DOM.createElement("i");
			ClientContext.resourceInjector.applyIconStyles(span, icon);
			a.insertFirst(span);
		}
		h4.appendChild(a);
		heading.appendChild(h4);
		panel.appendChild(heading);

		Element collapsing = DOM.createDiv();
		collapsing.setId(tabId);
		collapsing.setClassName("panel-collapse collapse");
		collapsing.setAttribute("role", "tabpanel");
		collapsing.setAttribute("aria-labelledby", heading.getId());
		Element body = DOM.createDiv();
		collapsing.appendChild(body);
		panel.appendChild(collapsing);

		widgets.put(id, widget);
		sizeWaitingWidgets.add(id);

		if (widget != null)
			add(widget, body);
		if (isAttached()) {
			attachShowEvents(this, a);
		} else {
			if (tabCounter == 1) {
				collapsing.addClassName((useAnimation ? "in " : "") + "active");
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
		_select(jsObject, index);
	}

	private native void attachShowEvents(AccordionBox x, Element alink) /*-{
		$wnd.$(alink).tab('show').on('shown.bs.collapse', function(e) {
			x.@com.javexpress.gwt.library.ui.bootstrap.AccordionBox::fireOnAccordionChanged(Ljava/lang/String;Ljava/lang/String;)($wnd.$(e.relatedTarget).attr("tabid"), $wnd.$(e.target).attr("tabid"));
		});
	}-*/;

	private native void _select(JavaScriptObject obj, int index) /*-{
		$wnd.$("li:eq(" + index + ")", obj).tab('show');
	}-*/;

	public void hideItem(String id) {
		toggleItem(id, false);
	}

	public void showItem(String id) {
		toggleItem(id, true);
	}

	private void toggleItem(String id, boolean show) {
		for (int i = 0; i < getElement().getChildCount(); i++) {
			Element div = ((Element) getElement().getChild(i));
			if (div.getAttribute("tabid").equals(id) && !show)
				div.addClassName("hidden");
			else
				div.removeClassName("hidden");
		}
	}

	//EVENTS
	private void fireOnAccordionChanging(final String oldId, final String newId) {
		if (listener != null)
			listener.onAccordionChanging(oldId, newId);
	}

	private void fireOnAccordionChanged(final String oldId, final String newId) {
		performOnShow(newId);
		activeWidgetId = newId;
		if (listener != null)
			listener.onAccordionChanged(oldId, newId);
	}

	public Widget getActiveWidget() {
		return widgets.get(activeWidgetId);
	}

	public void setTabWidget(String id, Widget widget) {
		sizeWaitingWidgets.remove(id);
		Widget old = widgets.get(id);
		widgets.remove(id);
		if (old != null)
			remove(old);
		if (widget != null) {
			for (int i = 0; i < getElement().getChildCount(); i++) {
				Element div = (Element) getElement().getChild(i);
				if (div.getAttribute("tabid").equals(id)) {
					add(widget, div);
					sizeWaitingWidgets.add(id);
					widgets.put(id, widget);
					break;
				}
			}
		}
	}
}