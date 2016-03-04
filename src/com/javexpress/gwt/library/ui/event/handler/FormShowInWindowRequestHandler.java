package com.javexpress.gwt.library.ui.event.handler;

import com.google.gwt.event.shared.EventHandler;
import com.javexpress.gwt.library.ui.event.FormShowInWindowRequest;

public interface FormShowInWindowRequestHandler extends EventHandler {

	void onFormShowInWindowRequested(FormShowInWindowRequest formShowInWindowRequest);

}