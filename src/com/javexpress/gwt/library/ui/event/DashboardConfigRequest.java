package com.javexpress.gwt.library.ui.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Command;
import com.javexpress.gwt.library.ui.event.handler.DashboardConfigRequestHandler;
import com.javexpress.gwt.library.ui.form.DashboardForm;

public class DashboardConfigRequest extends GwtEvent<DashboardConfigRequestHandler> {

	public static Type<DashboardConfigRequestHandler>	TYPE	= new Type<DashboardConfigRequestHandler>();
	private DashboardForm								dashForm;
	private Command										completeCommand;

	public DashboardConfigRequest(DashboardForm dashForm, Command completeCommand) {
		this.dashForm = dashForm;
		this.completeCommand = completeCommand;
	}

	public DashboardForm getDashForm() {
		return dashForm;
	}

	public void setDashForm(DashboardForm dashForm) {
		this.dashForm = dashForm;
	}

	public Command getCompleteCommand() {
		return completeCommand;
	}

	public void setCompleteCommand(Command completeCommand) {
		this.completeCommand = completeCommand;
	}

	@Override
	public Type<DashboardConfigRequestHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DashboardConfigRequestHandler handler) {
		handler.onDashboardConfigRequested(this);
	}

}