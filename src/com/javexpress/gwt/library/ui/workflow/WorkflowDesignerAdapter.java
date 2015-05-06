package com.javexpress.gwt.library.ui.workflow;

import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONObject;
import com.javexpress.gwt.library.ui.workflow.dto.NodeConnection;

public abstract class WorkflowDesignerAdapter {

	protected abstract void nodeCreated(Element node);

	protected abstract void nodeActivated(Element node, JSONObject data);

	protected abstract void connectionActivated(final NodeConnection nodeConnection);

	protected abstract void actionActivated(Element node, Element action);	

}