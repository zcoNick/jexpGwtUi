package com.javexpress.gwt.library.ui.map;

import com.google.gwt.core.client.JavaScriptObject;

public abstract class MapsListener {

	public void onNewShapeAdded(JavaScriptObject event, JavaScriptObject shape) {
	}

	public String getApiKey() {
		return null;
	}

}