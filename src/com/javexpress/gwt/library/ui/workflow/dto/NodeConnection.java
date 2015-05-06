package com.javexpress.gwt.library.ui.workflow.dto;

import com.google.gwt.core.client.JavaScriptObject;

public class NodeConnection {
	
	private JavaScriptObject delegate;

	public NodeConnection(final JavaScriptObject connection) {
		this.delegate = connection;
	}

}