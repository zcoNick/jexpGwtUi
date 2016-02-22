package com.javexpress.gwt.library.ui.dialog;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class StatusDialog extends SimplePanel {
	
	public static final String DEFAULT_LINE = "jexpLoading_default";

	private Element textContainer;
	
	public StatusDialog() {
		super(DOM.createDiv());
		addStyleName("jexpLoading");
		Element inner = DOM.createDiv();
		inner.addClassName("jexpLoadingInner jexpShadow");
		getElement().appendChild(inner);
		Element img = DOM.createImg();
		img.getStyle().setPaddingRight(10, Unit.PX);
		img.setAttribute("src", JsUtil.getImagesPath()+"circle-loading.gif");
		inner.appendChild(img);
		
		textContainer = DOM.createDiv();
		textContainer.getStyle().setFloat(Float.RIGHT);
		inner.appendChild(textContainer);
	}
	
	public void addLine(String id,String text){
		Element line = DOM.createDiv();
		line.setClassName("jexpLoadingText");
		line.setInnerHTML(text);
		line.setId(id);
		textContainer.appendChild(line);
	}
	
	public void removeLine(String id){
		Element line = DOM.getElementById(id);
		if (line.hasClassName("jexpLoadingText")){
			line.removeFromParent();
		}
		if (!textContainer.hasChildNodes())
			removeFromParent();
	}
	
	public void show(){
		RootPanel.get().add(this);
	}

}