package com.javexpress.gwt.library.ui.form.textbox;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.bootstrap.LabelControlCell;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class CKEditor extends SimplePanel implements IUserInputWidget<String> {

	public static void fillResources(final WidgetBundles wb) {
		wb.addJavaScript("scripts/ckeditor/ckeditor.js");
	}

	private JavaScriptObject	jsObject;
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

	public CKEditor(Widget parent, String id) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.RICHTEXTEDITOR_PREFIX, id);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		jsObject = createByJs(this, getElement().getId(), LocaleInfo.getCurrentLocale().getLocaleName());
	}

	@Override
	protected void onUnload() {
		jsObject = null;
		dataBinding = null;
		super.onUnload();
	}

	private native JavaScriptObject createByJs(CKEditor x, String id, String lang) /*-{
																					var config = {
																					language : lang,
																					maximize : false,
																					tabSpaces : 8,
																					uiColor : '#A1CFF3',
																					height : $wnd.$("#" + id).parent().height() - 45,
																					startupFocus : true,
																					enterMode : $wnd.CKEDITOR.ENTER_BR,
																					removePlugins : 'elementspath',
																					resize_enabled : false
																					//scayt_autoStartup:true,
																					//scayt_sLang:'tr_TR'
																					}
																					return $wnd.CKEDITOR.replace(id, config);
																					}-*/;

	@Override
	public String getValue() {
		String s = _getValue(jsObject);
		return JsUtil.isEmpty(s) ? null : s;
	}

	public void setValue(String value) {
		_setValue(jsObject, value);
	}

	private native String _getValue(JavaScriptObject cke) /*-{
															return cke.getData();
															}-*/;

	private native void _setValue(JavaScriptObject cke, String data) /*-{
																		cke.setData(data);
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

	private native void _setFocus(JavaScriptObject cke) /*-{
														cke.focus();
														}-*/;

	@Override
	public void setTabIndex(int index) {
	}

	@Override
	public void setEnabled(boolean locked) {
		_setReadOnly(jsObject, locked);
	}

	private native void _setReadOnly(JavaScriptObject cke, boolean readOnly) /*-{
																				cke.setReadOnly(readOnly);
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
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
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