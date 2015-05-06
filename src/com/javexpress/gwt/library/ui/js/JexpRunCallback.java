package com.javexpress.gwt.library.ui.js;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;

public abstract class JexpRunCallback implements RunAsyncCallback {

	@Override
	public void onFailure(final Throwable reason) {
		GWT.log(reason.getMessage(), reason);
		Window.alert(reason.getMessage());
	}

}