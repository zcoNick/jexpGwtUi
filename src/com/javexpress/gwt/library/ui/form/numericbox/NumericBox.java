package com.javexpress.gwt.library.ui.form.numericbox;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.FormGroupCell;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class NumericBox extends TextBox implements IUserInputWidget {

	protected JsonMap			options;
	private boolean				required;
	protected JavaScriptObject	widget;
	private DataBindingHandler	dataBinding;

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(final boolean required) {
		this.required = required;
	}

	/** Designer compatible constructor */
	public NumericBox(final Widget parent, final String id) {
		this(parent, id, true);
	}

	@Deprecated
	public NumericBox(final Widget parent, final String id, final boolean alignRight) {
		super(DOM.createInputText());
		JsUtil.ensureId(parent, this, WidgetConst.NUMERICBOX_PREFIX, id);
		if (!JsUtil.USE_BOOTSTRAP)
			setWidth("4em");
		createDefaultOptions();
		setStyleName("gwt-TextBox jexpNumericBox");
		if (alignRight)
			setAlignment(TextAlignment.RIGHT);
		addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				int kc = event.getNativeKeyCode();
				boolean allowed = ((kc >= 48 && kc <= 57)
						|| (kc >= 96 && kc <= 105)
						|| kc == 8 || kc == 9
						|| kc == 37 || kc == 39
						|| kc == 13 || kc == 46);
				if (!allowed)
					event.getNativeEvent().preventDefault();
			}
		});
	}

	protected JsonMap createDefaultOptions() {
		options = new JsonMap();
		return options;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		widget = createByJs(this, getElement(), options.getJavaScriptObject());
	}

	private native JavaScriptObject createByJs(NumericBox x, Element element, JavaScriptObject options) /*-{
		var el = $wnd.$(element);
		if (options.spinnerOptions)
			el.spinner(options.spinnerOptions);
		return el;
	}-*/;

	@Override
	protected void onUnload() {
		widget = null;
		dataBinding = null;
		destroyByJs(getElement(), options.getJavaScriptObject());
		options = null;
		super.onUnload();
	}

	private native void destroyByJs(Element element, JavaScriptObject options) /*-{
		if (options.spinnerOptions)
			$wnd.$(element).spinner("destroy");
	}-*/;

	public Long getValueLong() {
		return JsUtil.asLong(getText());
	}

	public Short getValueShort() {
		return JsUtil.asShort(getText());
	}

	public Integer getValueInt() {
		return JsUtil.asInteger(getText());
	}

	public Byte getValueByte() {
		return JsUtil.asByte(getText());
	}

	public void setValueLong(final Long val) {
		setText(val == null ? null : val.toString());
	}

	public void setValueInt(final Integer val) {
		setText(val == null ? null : val.toString());
	}

	public void setMaxLength(final int length) {
		getElement().setAttribute("maxlength", String.valueOf(length));
		setWidth(JsUtil.calcSizeForMaxLength(length));
	}

	public void setValue(final Byte val) {
		setText(val == null ? null : String.valueOf(val));
	}

	public void setValue(final Integer val) {
		setText(val == null ? null : String.valueOf(val));
	}

	public void setValue(final Long val) {
		setText(val == null ? null : String.valueOf(val));
	}

	public void setValueShort(final Short val) {
		setText(val == null ? null : String.valueOf(val));
	}

	public void setValueByte(final Byte val) {
		setText(val == null ? null : val.toString());
	}

	public int getValueIntDef(final int i) {
		return JsUtil.isEmpty(getText()) ? i : getValueInt();
	}

	@Override
	public boolean validate(final boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
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
	public void setDataBindingHandler(DataBindingHandler handler) {
		this.dataBinding = handler;
		dataBinding.setControl(this);

	}

	@Override
	public DataBindingHandler getDataBindingHandler() {
		return dataBinding;
	}

}