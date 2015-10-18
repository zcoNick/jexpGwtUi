package com.javexpress.gwt.library.ui.listgroup;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ListGroup extends AbstractContainer {

	public ListGroup(Widget parent, String id) {
		super(DOM.createElement("div"));
		JsUtil.ensureId(parent, getElement(), WidgetConst.LISTGROUP_PREFIX, id);
		addStyleName("list-group");
	}

}
