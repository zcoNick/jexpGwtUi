package com.javexpress.gwt.library.ui.form.textbox;

import com.google.gwt.user.client.ui.Widget;

public class HtmlCodeEditor extends CodeEditor {

	public HtmlCodeEditor(Widget parent, String id) {
		super(parent, id);
		setMode("text/html");
	}
	
}