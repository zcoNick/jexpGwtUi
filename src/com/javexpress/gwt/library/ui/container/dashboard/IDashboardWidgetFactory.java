package com.javexpress.gwt.library.ui.container.dashboard;

import java.io.Serializable;
import java.util.HashMap;

public interface IDashboardWidgetFactory {

	public DashboardWidget createWidget(Dashboard dashboard, String code, HashMap<String, Serializable> parameters) throws Exception;

}