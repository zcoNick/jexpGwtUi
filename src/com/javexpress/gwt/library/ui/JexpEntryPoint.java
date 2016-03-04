package com.javexpress.gwt.library.ui;

import java.io.Serializable;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.javexpress.common.model.item.Result;
import com.javexpress.gwt.library.ui.container.dashboard.IDashboardWidgetFactory;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class JexpEntryPoint implements EntryPoint, IDashboardWidgetFactory {

	public static void handleResult(final Result<Serializable> result) {
		if (!JsUtil.isEmpty(result.getError()))
			Window.alert(result.getError());
	}

}