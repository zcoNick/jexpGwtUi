package com.javexpress.gwt.library.ui.form.textbox;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.ui.Widget;

public class SqlCodeEditor extends CodeEditor {
	
	private ISqlCodeEditorListener listener;
	
	public ISqlCodeEditorListener getListener() {
		return listener;
	}

	public void setListener(ISqlCodeEditorListener listener) {
		this.listener = listener;
	}

	public SqlCodeEditor(Widget parent, String id) {
		super(parent, id);
		addExtraKey("Ctrl-Space", "autocomplete");
		setMode("text/x-sql");
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		attachLazyColumnProvider(jsObject, this);
	}

	private native void attachLazyColumnProvider(JavaScriptObject cm, SqlCodeEditor x) /*-{
		cm.setOption("sqlHintLazyColumnProvider", function(table){
			return x.@com.javexpress.gwt.library.ui.form.textbox.SqlCodeEditor::fireOnFindColumnsOfTable(Ljava/lang/String;)(table);
		});
	}-*/;
	
	private JsArrayString fireOnFindColumnsOfTable(String table){
		if (listener!=null){
			List<String> columnsList = listener.findColumnsOfTable(table);
			if (columnsList!=null&&!columnsList.isEmpty()){
				JsArrayString columns = JsArrayString.createArray().cast();
				for (int i=0;i<columnsList.size();i++)
					columns.set(i, columnsList.get(i));
				return columns;
			}
		}
		return null;
	}
	
}