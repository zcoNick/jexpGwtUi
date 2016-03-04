package com.javexpress.gwt.library.ui.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.javexpress.gwt.library.ui.event.FormOpenRequest;

public interface FormOpenRequestHandler extends EventHandler {

	void onFormOpenRequested(FormOpenRequest formOpenRequest);

}