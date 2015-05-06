package com.javexpress.gwt.library.ui.workflow;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.SimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.workflow.dto.NodeConnection;

public class WorkflowDrawing extends SimplePanel {
	
	private WorkflowDesigner designer;
	private int lastItem = 0;
	
	public WorkflowDrawing(final WorkflowDesigner designer) {
		this.designer = designer;
		JsUtil.ensureSubId(designer.getElement(), getElement(), "_dwg");
		getElement().addClassName("ui-widget-content ui-corner-right wf-canvas");
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		initialize(this,getElement(),designer.getElement().getId());
	}

	private native void initialize(WorkflowDrawing x, Element cnvs, String designerId) /*-{
		var jqcnvs = $wnd.$(cnvs);
		jsPlumb.Defaults.Container = jqcnvs.attr("id");
		jqcnvs.droppable({
			accept: ".wf-palette-item",
			drop: function( event, ui ) {

				function activateNode(el){
					$wnd.$(".wf-selected",el.parent()).removeClass("wf-selected");
					var nd = el.addClass("wf-selected");
					var d = nd.data("data");
					x.@com.javexpress.gwt.library.ui.workflow.WorkflowDrawing::nodeActivated(Lcom/google/gwt/dom/client/Element;Lcom/google/gwt/core/client/JavaScriptObject;)(this,d);
				}

				if (!ui.draggable.hasClass("wf-palette-item"))
					return false;
				var el = ui.helper.clone().appendTo(cnvs).removeClass().addClass("ui-helper-reset ui-helper-clear wf-node ui-corner-all").css("z-index","").css("position","relative").width("100px");
				el.attr("id",jqcnvs.attr("id")+"_"+el.index());
				var pp = jqcnvs.parent().parent().parent().parent().parent().position();
				var ep = el.position();
				el.css("top",ep.top-pp.top);
				el.css("left",ep.left-pp.left);
				x.@com.javexpress.gwt.library.ui.workflow.WorkflowDrawing::nodeCreated(Lcom/google/gwt/dom/client/Element;)(el[0]);
				el.data("data",{id:el.attr("nid")});
				$wnd.jsPlumb.draggable(el, { containment:jqcnvs.attr("id") });
				el.bind("click",function(){
					activateNode(el);
				});
				$wnd.$(".wf-node-action",el).each(function(){
					if (!$wnd.jesWorkFlowSourceOptions)
						$wnd.jesWorkFlowSourceOptions={endpoint:"Rectangle",paintStyle:{ width:15, height:15, fillStyle:'#666' },isSource:true,
							connectorStyle : { strokeStyle:"orange", lineWidth:5 }};
					var act = $wnd.$(this);
					act.attr("id",el.attr("id")+"_"+act.index());
					$wnd.jsPlumb.addEndpoint(act.attr("id"), { anchor:"RightMiddle" }, $wnd.jesWorkFlowSourceOptions);
					x.@com.javexpress.gwt.library.ui.workflow.WorkflowDrawing::actionActivated(Lcom/google/gwt/dom/client/Element;Lcom/google/gwt/dom/client/Element;)(el[0],this);
				});
				if (ui.draggable.attr("targetable")=="true"){
					$wnd.jsPlumb.makeTarget(el.attr("id"), {
						anchor:"Continuous",
						paintStyle:{ fillStyle:"red" }
					});
					if (!$wnd.jesWorkFlowTargetOptions)
						$wnd.jesWorkFlowTargetOptions={endpoint:"Rectangle",paintStyle:{ width:15, height:15, fillStyle:'#666' },isTarget:true};
					$wnd.jsPlumb.addEndpoint(el.attr("id"), { anchor:"TopLeft" }, $wnd.jesWorkFlowTargetOptions);
				}
				activateNode(el);
				return false;
			}
		});
		$wnd.jsPlumb.bind("jsPlumbConnection", function(info) {
			info.connection.bind("click", function(conn) {
				x.@com.javexpress.gwt.library.ui.workflow.WorkflowDrawing::connectionActivated(Lcom/google/gwt/core/client/JavaScriptObject;)(conn);
			});
		});
	}-*/;
	
	private void nodeCreated(final Element node){
		JsUtil.ensureSubId(getElement(), node, "_"+lastItem);
		node.setAttribute("nid",String.valueOf(lastItem++));
		designer.nodeCreated(node);
	}

	private void nodeActivated(final Element node, final JavaScriptObject data){
		designer.nodeActivated(node,new JSONObject(data));
	}

	private void actionActivated(final Element node, final Element action){
		designer.actionActivated(node,action);
	}
	
	private void connectionActivated(final JavaScriptObject connection){
		designer.connectionActivated(new NodeConnection(connection));
	}
	
}