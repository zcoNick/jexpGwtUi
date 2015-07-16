package com.javexpress.gwt.library.ui.event;

import com.google.gwt.event.shared.GwtEvent;
import com.javexpress.common.model.item.exception.AppException;
import com.javexpress.gwt.library.ui.event.handler.ExceptionThrownEventHandler;

public class ExceptionThrownEvent extends GwtEvent<ExceptionThrownEventHandler> {

	public static Type<ExceptionThrownEventHandler>	TYPE	= new Type<ExceptionThrownEventHandler>();
	private String									windowId;
	private AppException							appException;

	public ExceptionThrownEvent(String windowId, AppException appException) {
		this.windowId = windowId;
		this.appException = appException;
	}

	public String getWindowId() {
		return windowId;
	}

	public void setWindowId(String windowId) {
		this.windowId = windowId;
	}

	public AppException getAppException() {
		return appException;
	}

	public void setAppException(AppException appException) {
		this.appException = appException;
	}

	@Override
	public Type<ExceptionThrownEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ExceptionThrownEventHandler handler) {
		handler.onExceptionThrown(this);
	}

}