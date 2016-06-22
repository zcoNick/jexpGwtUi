package com.javexpress.gwt.library.ui.container.dashboard;

import java.io.Serializable;
import java.util.HashMap;

import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.bootstrap.WidgetBox;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsonMap;

public abstract class DashboardWidget extends WidgetBox implements ISizeAwareWidget {

	protected DashboardWidget that;
	private RepeatingCommand repeatingCommand;

	public RepeatingCommand getRepeatingCommand() {
		return repeatingCommand;
	}

	public void setRepeatingCommand(RepeatingCommand repeatingCommand) {
		this.repeatingCommand = repeatingCommand;
	}

	public DashboardWidget(Widget parent, final String id) throws Exception {
		this(parent, id, true);
	}

	public DashboardWidget(Widget parent, final String id, boolean createGui) throws Exception {
		super(parent, DOM.createElement("li"), id, false, true, false);
		getElement().removeClassName("jexpGroupBox");
		getElement().addClassName("jexpDashboardWidget");
		that = this;
		setHeader(getHeader());
		if (createGui)
			createGUI();
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

	protected void createGUI() {
	}

	public void doSetup(HashMap<String, Serializable> parameters, Command persistCommand) {
	}

	public void parametersUpdated(HashMap<String, Serializable> parameters) {
	}

	protected void nullify() {
	}

	public void onShow() {
	}

	@Override
	protected void onUnload() {
		that = null;
		repeatingCommand = null;
		super.onUnload();
	}

	public void loadingStarted() {
		addStyleName("loading");
		toogleIconStyle(getIcon().getSpinStyle());
	}

	public void loadingFinished() {
		removeStyleName("loading");
		toogleIconStyle(getIcon().getSpinStyle());
	}

}