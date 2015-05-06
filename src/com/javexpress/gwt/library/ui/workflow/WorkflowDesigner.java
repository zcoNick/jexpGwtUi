package com.javexpress.gwt.library.ui.workflow;

import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.container.layout.TableBorderLayout;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.workflow.WorkflowPalette.PaletteItemType;
import com.javexpress.gwt.library.ui.workflow.dto.NodeConnection;

public class WorkflowDesigner extends TableBorderLayout {
	
	private WorkflowPalette palette;
	private WorkflowDrawing drawing;
	private WorkflowDesignerAdapter adapter;
	
	public WorkflowDesignerAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(final WorkflowDesignerAdapter adapter) {
		this.adapter = adapter;
	}

	public void addPaletteItem(final PaletteItemType type, final String title, final String code, final String action){
		palette.addItem(type,title,code,action);
	}
	
	public WorkflowDesigner(final boolean fitToParent) {
		super(fitToParent);
	}
	
	public WorkflowDesigner(final Widget parent, final String id) {
		super(true);
		JsUtil.ensureId(parent, this, WidgetConst.WORKFLOWDESIGNER_PREFIX, id);
		getElement().addClassName("ui-helper-reset ui-helper-clear ui-widget");
		drawing = new WorkflowDrawing(this);
		setCenterWidget(drawing);
		palette = new WorkflowPalette(this, "Araçlar");
		setLeftWidget(palette,"12%");
	}

	public void nodeCreated(final Element node) {
		if (adapter!=null)
			adapter.nodeCreated(node);
	}
	
	public void nodeActivated(final Element node, final JSONObject data) {
		if (adapter!=null)
			adapter.nodeActivated(node, data);
	}

	public void connectionActivated(final NodeConnection nodeConnection) {
		if (adapter!=null)
			adapter.connectionActivated(nodeConnection);
	}

	public void actionActivated(final Element node, final Element action) {
		if (adapter!=null)
			adapter.actionActivated(node,action);
	}

}