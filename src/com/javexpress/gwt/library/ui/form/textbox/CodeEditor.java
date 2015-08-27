package com.javexpress.gwt.library.ui.form.textbox;

import java.beans.Beans;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.bootstrap.LabelControlCell;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class CodeEditor extends TextArea implements IUserInputWidget<String> {

	public static void fillResources(final WidgetBundles wb) {
		wb.addStyleSheet("scripts/codemirror/lib/codemirror.css");
		wb.addStyleSheet("scripts/codemirror/addon/hint/jexp.show-hint.css");
		wb.addJavaScript("scripts/codemirror/lib/codemirror-5.6.js");
		wb.addJavaScript("scripts/codemirror/addon/hint/show-hint.js");
		//wb.addJavaScript("scripts/codemirror/addon/hint/jexp.sql-hint.js");
		wb.addJavaScript("scripts/codemirror/addon/hint/sql-hint.js");
		wb.addJavaScript("scripts/codemirror/addon/fold/xml-fold.js");
		wb.addJavaScript("scripts/codemirror/addon/edit/matchbrackets.js");
		wb.addJavaScript("scripts/codemirror/addon/edit/matchtags.js");
		wb.addJavaScript("scripts/codemirror/mode/sql/sql.js");
		//wb.addJavaScript("scripts/codemirror/mode/xml/xml.js");
		//wb.addJavaScript("scripts/codemirror/mode/css/css.js");
		//wb.addJavaScript("scripts/codemirror/mode/javascript/javascript.js");
		wb.addJavaScript("scripts/codemirror/mode/htmlmixed/htmlmixed.js");
	}

	protected JavaScriptObject	jsObject;
	private final JsonMap		options;
	private boolean				required;
	private Integer				maxLength;
	private DataBindingHandler	dataBinding;

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(final boolean required) {
		this.required = required;
	}

	public CodeEditor(Widget parent, String id) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.CODEEDITOR_PREFIX, id);
		options = createDefaultOptions();
	}

	protected JsonMap createDefaultOptions() {
		JsonMap o = new JsonMap();
		o.set("lineNumbers", true);
		o.set("styleActiveLine", true);
		o.set("matchBrackets", true);
		o.set("styleActiveLine", true);
		JsonMap o2 = new JsonMap();
		o2.set("bothTags", true);
		o.set("matchTags", o2);
		return o;
	}

	public void setMode(String mode) {
		options.set("mode", mode);
	}

	public void addExtraKey(String key, String codeMirrorFunc) {
		JsonMap ek = (JsonMap) options.get(key);
		if (ek == null) {
			ek = new JsonMap();
			options.put("extraKeys", ek);
		}
		ek.set(key, codeMirrorFunc);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!Beans.isDesignTime())
			jsObject = createByJs(this, getElement(), options.getJavaScriptObject(), getElement().getStyle().getWidth(), getElement().getStyle().getHeight());

	}

	private native JavaScriptObject createByJs(CodeEditor x, Element el, JavaScriptObject options, String width, String height) /*-{
		var cm = $wnd.CodeMirror.fromTextArea(el, options);
		cm.setSize(width, height);
		return cm;
	}-*/;

	@Override
	protected void onUnload() {
		jsObject = null;
		dataBinding = null;
		destroyByJs(getElement().getNextSiblingElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).empty().off();
	}-*/;

	@Override
	public String getValue() {
		String s = _getValue(jsObject);
		return JsUtil.isEmpty(s) ? null : s;
	}

	@Override
	public void setValue(String value) {
		_setValue(jsObject, value);
	}

	private native String _getValue(JavaScriptObject cm) /*-{
		return cm ? cm.getValue() : null;
	}-*/;

	private native void _setValue(JavaScriptObject cm, String data) /*-{
		if (cm)
			cm.setValue(data ? data : "");
	}-*/;

	@Override
	public boolean validate(final boolean focusedBefore) {
		boolean validated = JsUtil.validateWidget(this, focusedBefore);
		if (validated) {
			String v = getValue();
			if (maxLength != null && (v == null || v.length() > maxLength)) {
				JsUtil.flagInvalid(this, ClientContext.nlsCommon.alanDegeriUzun(), focusedBefore);
				validated = false;
			}
		}
		return validated;
	}

	@Override
	public int getTabIndex() {
		return 0;
	}

	@Override
	public void setAccessKey(char key) {
	}

	@Override
	public void setFocus(boolean focused) {
		_setFocus(jsObject);
	}

	private native void _setFocus(JavaScriptObject cm) /*-{
		cm.focus();
	}-*/;

	@Override
	public void setTabIndex(int index) {
		getElement().getNextSiblingElement().setTabIndex(index);
	}

	@Override
	public void setEnabled(boolean locked) {
		_setReadOnly(jsObject, locked);
	}

	private native void _setReadOnly(JavaScriptObject cm, boolean readOnly) /*-{
		cm.setReadOnly(readOnly);
	}-*/;

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public void setValidationError(String validationError) {
		if (JsUtil.USE_BOOTSTRAP) {
			Widget nw = getParent() instanceof LabelControlCell ? getParent() : this;
			if (validationError == null)
				nw.removeStyleName("has-error");
			else
				nw.addStyleName("has-error");
		}
		setTitle(validationError);
	}

	@Override
	public void setDataBindingHandler(DataBindingHandler handler) {
		this.dataBinding = handler;
		dataBinding.setControl(this);

	}

	@Override
	public DataBindingHandler getDataBindingHandler() {
		return dataBinding;
	}

}