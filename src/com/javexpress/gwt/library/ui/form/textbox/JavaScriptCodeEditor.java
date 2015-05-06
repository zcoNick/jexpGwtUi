package com.javexpress.gwt.library.ui.form.textbox;

import com.google.gwt.user.client.ui.Widget;

public class JavaScriptCodeEditor extends CodeEditor {

	public JavaScriptCodeEditor(Widget parent, String id) {
		super(parent, id);
		setMode("text/javascript");
	}
	
}