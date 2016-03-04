package com.javexpress.gwt.library.ui.listgroup;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.menu.IMenuHandler;

public class ListGroup extends AbstractContainer {

	private IMenuHandler	handler;

	public IMenuHandler getHandler() {
		return handler;
	}

	public void setHandler(IMenuHandler handler) {
		this.handler = handler;
	}

	public ListGroup(Widget parent, String id) {
		super(DOM.createElement("div"));
		JsUtil.ensureId(parent, getElement(), WidgetConst.LISTGROUP_PREFIX, id);
		addStyleName("list-group");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement());
	}

	private native void createByJs(ListGroup x, Element element) /*-{
		var el = $wnd.$("a.jexpLink", element);
		el.click(function(e) {
			var sel = $wnd.$(this);
			el.each(function(){
				var a = $wnd.$(this);
				if (a.attr("v")==sel.attr("v")){
					a.addClass("active");
					x.@com.javexpress.gwt.library.ui.listgroup.ListGroup::executeHandler(Ljava/lang/String;Lcom/google/gwt/user/client/Event;)($wnd.$(this).attr("v"), e);
				} else
					a.removeClass("active");
			});
		});
	}-*/;

	private void executeHandler(String id, Event event) {
		if (handler != null)
			handler.menuItemClicked(id, event);
	}

}