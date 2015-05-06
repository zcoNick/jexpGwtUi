package com.javexpress.gwt.library.ui.form.textbox;

import com.google.gwt.user.client.ui.Widget;

public class XmlCodeEditor extends CodeEditor {

	public XmlCodeEditor(Widget parent, String id) {
		super(parent, id);
		setMode("text/xml");
	}
	
}