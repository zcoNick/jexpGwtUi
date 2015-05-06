package com.javexpress.gwt.library.ui.form.textbox;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.fw.ui.library.form.IFormFactory;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.FormGroupCell;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.keyboard.Keyboard;

public class TextBox extends com.google.gwt.user.client.ui.TextBox implements IUserInputWidget<String>, BlurHandler {

	private boolean						titleCaseConvert;
	private boolean						upperCaseConvert;
	private boolean						required;
	private DataBindingHandler<TextBox>	dataBinding;

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(final boolean required) {
		this.required = required;
	}

	public boolean isTitleCaseConvert() {
		return titleCaseConvert;
	}

	public void setTitleCaseConvert(boolean titleCaseConvert) {
		this.titleCaseConvert = titleCaseConvert;
		this.upperCaseConvert = false;
	}

	public boolean isUpperCaseConvert() {
		return upperCaseConvert;
	}

	public void setUpperCaseConvert(boolean upperCaseConvert) {
		this.upperCaseConvert = upperCaseConvert;
		this.titleCaseConvert = false;
	}

	@Deprecated
	public TextBox() {
		super();
	}

	/** Designer compatible constructor */
	public TextBox(final Widget parent, final String id) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.TEXTBOX_PREFIX, id);
		addStyleName("jexpTextBox");
	}

	public HandlerRegistration addEnterKeyHandler(final KeyDownHandler handler) {
		return addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13) {
					handler.onKeyDown(event);
					event.preventDefault();
					event.stopPropagation();
				}
			}
		});
	}

	@Override
	public boolean validate(final boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	@Override
	public String getValue() {
		String s = super.getValue();
		return JsUtil.isEmpty(s) ? null : s;
	}

	public void enableVirtualKeyboard() {
		addStyleName(Keyboard.STYLENAME);
	}

	@Override
	protected void onLoad() {
		if (titleCaseConvert || upperCaseConvert) {
			addBlurHandler(this);
		}
		super.onLoad();
	}

	@Override
	public void onBlur(BlurEvent event) {
		String s = getText();
		if (JsUtil.isNotEmpty(s)) {
			if (titleCaseConvert)
				setText(_toTitleCase(s, ClientContext.instance.getModuleNls("lang()));
			else if (upperCaseConvert)
				setText(_toUpperCase(s, ClientContext.instance.getModuleNls("lang()));
		}
	}

	private static native String _toTitleCase(String str, String locale) /*-{
		return $wnd.toLocaleTitleCase(str, locale);
	}-*/;

	private static native String _toUpperCase(String str, String locale) /*-{
		$wnd.toLocaleUpperCase(str, locale);
	}-*/;

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
	public void setDataBindingHandler(DataBindingHandler handler) {
		this.dataBinding = handler;
		dataBinding.setControl(this);
	}

	@Override
	public DataBindingHandler getDataBindingHandler() {
		return dataBinding;
	}

	@Override
	protected void onUnload() {
		dataBinding = null;
		super.onUnload();
	}

}