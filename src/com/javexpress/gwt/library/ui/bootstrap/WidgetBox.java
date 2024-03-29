package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainerFocusable;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class WidgetBox extends AbstractContainerFocusable implements ISizeAwareWidget {

	protected Element contentDiv;
	protected Element toolDiv;
	private Element iconSpan;
	private Element headerSpan;
	protected Element headerEl;
	private Element elCollapse;
	private boolean collapsed;
	private Element bodyDiv;
	private IWidgetBoxListener listener;
	private ICssIcon icon;

	public IWidgetBoxListener getListener() {
		return listener;
	}

	public void setListener(IWidgetBoxListener listener) {
		this.listener = listener;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}

	public void toogleIconStyle(String style) {
		iconSpan.toggleClassName(style);
	}

	public WidgetBox(Widget parent, final String id, boolean smallTitle, boolean transparentTitle,
			boolean collapsible) {
		this(parent, DOM.createDiv(), id, smallTitle, transparentTitle, collapsible);
	}

	public WidgetBox(Widget parent, Element el, final String id, boolean smallTitle, boolean transparentTitle,
			boolean collapsible) {
		super(el);
		JsUtil.ensureId(parent, this, WidgetConst.DASHBOARDWIDGET_PREFIX, id);
		getElement().setClassName("jexpGroupBox widget-box" + (transparentTitle ? " transparent" : ""));
		if (!transparentTitle)
			getElement().getStyle().setPadding(0, Unit.PX);
		Element headerDiv = DOM.createDiv();
		headerDiv.setClassName("widget-header");
		headerEl = DOM.createElement(smallTitle ? "h5" : "h4");
		headerEl.setClassName("widget-title lighter smaller");
		iconSpan = DOM.createElement("i");
		headerEl.appendChild(iconSpan);
		headerSpan = DOM.createSpan();
		headerEl.appendChild(headerSpan);
		headerDiv.appendChild(headerEl);

		contentDiv = DOM.createDiv();
		contentDiv.addClassName("widget-main padding-2");
		JsUtil.ensureSubId(getElement(), contentDiv, "content");

		toolDiv = DOM.createDiv();
		toolDiv.setClassName("widget-toolbar" + (transparentTitle ? " no-border" : ""));
		if (collapsible) {
			elCollapse = DOM.createAnchor();
			elCollapse.setAttribute("data-action", "collapse");
			elCollapse.setAttribute("data-parent", "#" + getElement().getId());
			// elCollapse.setAttribute("data-target", "#" + contentDiv.getId());
			elCollapse.setClassName("jexpHandCursor");
			toolDiv.appendChild(elCollapse);
		}
		headerDiv.appendChild(toolDiv);

		getElement().appendChild(headerDiv);
		bodyDiv = DOM.createDiv();
		bodyDiv.addClassName("widget-body");
		getElement().appendChild(bodyDiv);

		bodyDiv.appendChild(contentDiv);
	}

	public void setIcon(ICssIcon icon) {
		this.icon = icon;
		ClientContext.resourceInjector.applyIconStyles(iconSpan, icon);
	}

	public ICssIcon getIcon() {
		return icon;
	}

	public void setHeader(String header) {
		headerSpan.setInnerHTML(header);
	}

	public void setWidget(Widget widget) {
		add(widget);
	}

	@Override
	public void add(Widget widget) {
		add(widget, contentDiv);
	}

	@Override
	public void add(Widget widget, int index) {
		add(widget, contentDiv);
	}

	public void addToolItem(ICssIcon icon, String hint, Command command) {
		Element a = DOM.createAnchor();
		a.setTitle(hint);
		a.addClassName("ub_" + getId());
		a.setInnerHTML("<i class='jexpHandCursor ace-icon jexpWidgetToolItem " + icon.getCssClass() + "'></i>");
		if (elCollapse != null)
			toolDiv.insertBefore(a, elCollapse);
		else
			toolDiv.appendChild(a);
		bindOnClick(a, command);
	}

	private native void bindOnClick(Element el, Command command) /*-{
		$wnd.$(el).click(function() {
			command.@com.google.gwt.user.client.Command::execute()();
		});
	}-*/;

	@Override
	protected void onLoad() {
		if (collapsed) {
			elCollapse.setInnerHTML("<i class='ace-icon fa fa-chevron-up'></i>");
			bodyDiv.getStyle().setDisplay(Display.NONE);
		} else if (elCollapse != null) {
			elCollapse.setInnerHTML("<i class='ace-icon fa fa-chevron-down'></i>");
		}
		super.onLoad();
		_createByJs(this, getElement(), elCollapse, isCollapsed());
	}

	private native void _createByJs(WidgetBox x, Element el, Element btcollapse, boolean collapsed) /*-{
		if (btcollapse) {
			$wnd
					.$(el)
					.widget_box(collapsed ? 'hide' : 'show')
					.on(
							'show.ace.widget',
							function(event) {
								if (!x.@com.javexpress.gwt.library.ui.bootstrap.WidgetBox::fireOnShowing()())
									event.preventDefault();
							})
					.on(
							'hide.ace.widget',
							function(event) {
								if (!x.@com.javexpress.gwt.library.ui.bootstrap.WidgetBox::fireOnHiding()())
									event.preventDefault();
							});
		}
	}-*/;

	@Override
	protected void onUnload() {
		toolDiv = null;
		contentDiv = null;
		bodyDiv = null;
		iconSpan = null;
		headerSpan = null;
		headerEl = null;
		elCollapse = null;
		listener = null;
		clear();
		_destroyByJs(getElement(), ".ub_" + getId());
		super.onUnload();
	}

	private native void _destroyByJs(Element el, String ubSel) /*-{
		var root = $wnd.$(el);
		$wnd.$(root).empty().off();
	}-*/;

	// --EVENTS
	private boolean fireOnShowing() {
		if (listener != null)
			return listener.onExpanding();
		return true;
	}

	private boolean fireOnHiding() {
		if (listener != null)
			return listener.onCollapsing();
		return true;
	}

	public void expand() {
		if (isAttached())
			collapsed = false;
		else if (elCollapse != null)
			_perform(getElement(), "show");
	}

	public void collapse() {
		if (isAttached())
			collapsed = true;
		else if (elCollapse != null)
			_perform(getElement(), "hide");
	}

	private native void _perform(Element el, String func) /*-{
		$wnd.$(el).widget_box(func);
	}-*/;

	public void setMinHeight(String value) {
		bodyDiv.getStyle().setProperty("minHeight", value);
	}

	@Override
	public void setHeight(String value) {
		bodyDiv.getStyle().setProperty("height", value);
	}

	public void setMaxHeight(String value) {
		bodyDiv.getStyle().setProperty("maxHeight", value);
	}

	public void setOverflow(Overflow value) {
		bodyDiv.getStyle().setOverflow(value);
	}

	public void addContentStyle(String style) {
		contentDiv.addClassName(style);
	}

}