package com.javexpress.gwt.library.ui.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.javexpress.gwt.library.ui.event.DashboardConfigRequest;

public interface DashboardConfigRequestHandler extends EventHandler {

	void onDashboardConfigRequested(DashboardConfigRequest request);

}