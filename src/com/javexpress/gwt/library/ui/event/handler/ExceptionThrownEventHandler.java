package com.javexpress.gwt.library.ui.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.javexpress.gwt.library.ui.event.ExceptionThrownEvent;

public interface ExceptionThrownEventHandler extends EventHandler {

	void onExceptionThrown(ExceptionThrownEvent exceptionThrownEvent);

}