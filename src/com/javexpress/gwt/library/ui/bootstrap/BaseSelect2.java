package com.javexpress.gwt.library.ui.bootstrap;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.combobox.ListBoxBase;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

abstract class BaseSelect2 extends ListBoxBase {

	public static void fillResources(final List<String> styleSheets, final List<String> javaScripts) {
		styleSheets.add("ace/css/select2-3.5.0.css");
		javaScripts.add("ace/js/select2-3.5.0.min.js");
	}

	private List<String>		lazyValues;
	private JsonMap				options;
	private DataBindingHandler	dataBinding;

	public BaseSelect2(Widget parent, String id, boolean multiple) {
		super(multiple);
		JsUtil.ensureId(parent, this, multiple ? WidgetConst.LISTBOX_PREFIX : WidgetConst.COMBOBOX_PREFIX, id);
		options = createDefaultOptions();
	}

	protected static JsonMap createDefaultOptions() {
		JsonMap options = new JsonMap();
		if (JsUtil.isRTL())
			options.set("dir", "rtl");
		//options.set("theme", "classic");
		return options;
	}

	public JsonMap getOptions() {
		return options;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement(), options.getJavaScriptObject());
	}

	private native void createByJs(BaseSelect2 x, Element el, JavaScriptObject options) /*-{
		$wnd.$(el).select2(options);
	}-*/;

	public void setPlaceHolder(String placeHolder) {
		options.set("placeholder", placeHolder);
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

	@Override
	public void setValidationError(String validationError) {
		if (JsUtil.USE_BOOTSTRAP) {
			Widget nw = getParent() instanceof FormGroupCell ? getParent() : this;
			if (validationError == null)
				nw.removeStyleName("has-error");
			else
				nw.addStyleName("has-error");
		}
		setTitle(validationError);
	}

	@Override
	protected void onUnload() {
		lazyValues = null;
		options = null;
		dataBinding = null;
		super.onUnload();
	}

}