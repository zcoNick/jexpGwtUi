package com.javexpress.gwt.library.ui.applet;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class AppletPanel extends SimplePanel {

	public AppletPanel(Widget parent, String id, boolean fitToParent) {
		super(DOM.createElement("applet"));
		JsUtil.ensureId(parent, this, WidgetConst.APPLETPANEL_PREFIX, id);
		getElement().setAttribute("name", id);
		getElement().setAttribute("codebase", JsUtil.getAppletsPath());
		getElement().setAttribute("MAYSCRIPT", "");
		if (fitToParent) {
			setWidth("100%");
			setHeight("100%");
		}
		Element param = DOM.createElement("param");
		param.setAttribute("name", "jsessionid");
		param.setAttribute("value", JexpGwtUser.instance.getSessionId());
		getElement().appendChild(param);
	}

	public void setArchive(String... archive) {
		String s = "";
		for (String a : archive) {
			s += (s.length() > 0 ? "," : "") + a;
		}
		getElement().setAttribute("archive", s);
	}

	public void setCode(String clazz) {
		getElement().setAttribute("code", clazz);
	}

}