package com.javexpress.gwt.library.ui.container.dashboard;

import java.io.Serializable;
import java.util.HashMap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public abstract class JqDashboardWidget extends AbstractContainer implements ISizeAwareWidget {

	protected JqDashboardWidget	that;
	private Element				contentDiv;

	public JqDashboardWidget(final String id) throws Exception {
		super(DOM.createElement("li"));
		that = this;
		JsUtil.ensureId(null, this, WidgetConst.DASHBOARDWIDGET_PREFIX, id);
		getElement().setClassName("ui-helper-reset jexpBorderBox ui-dashboard-widget ui-widget ui-widget-content ui-corner-all");
		Element header = DOM.createDiv();
		header.setClassName("widget-head ui-state-default ui-corner-top ui-helper-clearfix");
		createHeader(header);
		getElement().appendChild(header);
		contentDiv = DOM.createDiv();
		contentDiv.addClassName("jexpBorderBox");
		DOM.setStyleAttribute(contentDiv, "width", "auto");
		getElement().appendChild(contentDiv);
		createWidget();
	}

	public void setWidget(Widget widget) {
		add(widget, contentDiv);
	}

	protected void createHeader(final Element header) {
		Element h3 = DOM.createElement("h3");
		h3.setInnerText(getHeader());
		h3.getStyle().setFloat(JsUtil.isLTR() ? Float.LEFT : Float.RIGHT);
		header.appendChild(h3);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		that = null;
		contentDiv = null;
		super.onUnload();
	}

	protected void onShow() {
		onResize();
	}

	protected JsonMap getOptions() {
		JsonMap options = new JsonMap();
		options.set("movable", isMovable());
		options.set("removable", isRemovable());
		options.set("editable", isEditable());
		return options;
	}

	protected boolean isMovable() {
		return true;
	}

	protected boolean isRemovable() {
		return true;
	}

	protected boolean isEditable() {
		return false;
	}

	protected int getRefreshTimeInSeconds() {
		return 0;
	}

	/** Eger calismaya devam edecekse true dondurun */
	protected boolean doRefresh(boolean isWidgetVisibleToUser) {
		return false;
	}

	/** DESIGNER:VALUEFUNCTION{PROPERTY=PROP_TITLE,OF=this} */
	protected abstract String getHeader();

	protected abstract void createWidget() throws Exception;

	@Override
	public void onResize() {
		for (int i = 0; i < getChildren().size(); i++)
			if (getChildren().get(i) instanceof RequiresResize)
				((RequiresResize) getChildren().get(i)).onResize();
	}

	public void doSetup(HashMap<String, Serializable> parameters, Command persistCommand) {
	}

	public void parametersUpdated(HashMap<String, Serializable> parameters) {
	}

	protected void nullify() {
	}

}