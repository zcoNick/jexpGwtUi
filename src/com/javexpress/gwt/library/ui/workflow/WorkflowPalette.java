package com.javexpress.gwt.library.ui.workflow;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class WorkflowPalette extends SimplePanel {
	
	private Element header;
	private Element container;
	
	public static enum PaletteItemType {
		start,user,finish
	}
	
	private final WorkflowDesigner designer;
	
	protected WorkflowPalette(final WorkflowDesigner designer, final String title) {
		this.designer = designer;
		JsUtil.ensureSubId(designer.getElement(), getElement(), "_plt");
		getElement().addClassName("ui-widget-content ui-corner-top wf-palette "+designer.getElement().getId());
		header = DOM.createElement("h5");
		header.addClassName("ui-widget-header ui-corner-left");
		header.setInnerHTML(title);
		getElement().appendChild(header);
		container = DOM.createDiv();
		container.addClassName("ui-widget wf-palette-container");
		JsUtil.ensureSubId(getElement(),container,"_pc");
		getElement().appendChild(container);
		
	}

	protected void addItem(final PaletteItemType type, final String title, final String code,final String action) {
		Element el = DOM.createDiv();
		el.addClassName("ui-helper-reset ui-helper-clear wf-palette-item ui-corner-left");
		el.setAttribute("type", type.toString());
		el.setAttribute("code", code);
		el.setAttribute("targetable", type!=PaletteItemType.start?"true":"false");
		el.setTitle(title);
		Element h6 = DOM.createElement("h6");
		h6.setInnerHTML(title);
		el.appendChild(h6);
		if (type!=PaletteItemType.finish){
			Element ul = DOM.createElement("ul");
			ul.addClassName("wf-node-actions");
			Element li = DOM.createElement("li");
			li.addClassName("wf-node-action");
			li.setInnerHTML(action);
			ul.appendChild(li);
			el.appendChild(ul);
		}
		container.appendChild(el);
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		for (int i=0;i<container.getChildCount();i++)
			initializeItem((Element) container.getChild(i));
	}

	private native void initializeItem(final Element item) /*-{
		$wnd.$(item).css("z-index","9999").draggable({
			appendTo: "body",
			helper: "clone"
		});
	}-*/;

}