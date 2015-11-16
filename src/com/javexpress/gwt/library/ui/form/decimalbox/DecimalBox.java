package com.javexpress.gwt.library.ui.form.decimalbox;

import java.math.BigDecimal;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.JexpWidget;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap;
import com.javexpress.gwt.library.ui.bootstrap.FormGroupCell;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.ISingleValueWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class DecimalBox extends JexpWidget implements ISingleValueWidget<BigDecimal>, HasAllKeyHandlers {

	public static void fillResources(WidgetBundles wb) {
		//https://github.com/BobKnothe/autoNumeric
		wb.addJavaScript("scripts/autonumeric/autoNumeric-1.9.37.js");
	}

	private JsonMap				options;
	private boolean				zeroDefault	= false;
	private boolean				required;
	private DataBindingHandler	dataBinding;
	private boolean				valueChangeHandlerInitialized;
	private InputElement		input;

	/** Designer compatible constructor */
	public DecimalBox(final Widget parent, final String id) {
		super();
		setElement(input = DOM.createInputText().cast());
		JsUtil.ensureId(parent, this, WidgetConst.DECIMALBOX_PREFIX, id);
		addStyleName("jexpRightAlign");
		options = new JsonMap();
		options.set("lZero", "deny");
		setGroupSeparator(String.valueOf(JexpGwtUser.getCurrencyGroupChar()));
		setDecimalSeparator(String.valueOf(JexpGwtUser.getCurrencyDecimalChar()));
		if (!JsUtil.USE_BOOTSTRAP)
			setWidth("6em");
	}

	public void setGroupSeparator(String groupSeparator) {
		options.set("aSep", groupSeparator);
	}

	public void setDecimalSeparator(String decimalSeparator) {
		options.set("aDec", decimalSeparator);
	}

	public void setGroupSize(int groupSize) {
		options.setInt("dGroup", groupSize);
	}

	public void setDecimals(int digits) {
		options.setInt("mDec", digits);
	}

	public int getDecimals() {
		return options.getInt("mDec", 2);
	}

	public void setEmptyDecimals(boolean emptyDecimals) {
		options.set("aPad", emptyDecimals);
	}

	public boolean isZeroDefault() {
		return zeroDefault;
	}

	public void setZeroDefault(boolean zeroDefault) {
		this.zeroDefault = zeroDefault;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(boolean required) {
		this.setRequired(required);
		if (required && !options.containsKey("wEmpty") && zeroDefault)
			options.set("wEmpty", "zero");
	}

	public void setMinValue(BigDecimal min) {
		options.set("vMin", min);
	}

	public void setMaxValue(BigDecimal max) {
		options.set("vMax", max);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement(), options.getJavaScriptObject(), getText());
	}

	private native void createByJs(DecimalBox x, Element element, JavaScriptObject options, String value) /*-{
		var el = $wnd.$(element).autoNumeric('init', options);
		if (value && value != '')
			el.autoNumeric('set', parseFloat(value));
	}-*/;

	@Override
	protected void onUnload() {
		options = null;
		destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).autoNumeric('destroy').off();
	}-*/;

	public BigDecimal getValueDecimal() {
		if (JsUtil.isEmpty(getText()))
			return null;
		double d = _getValue(getElement());
		return BigDecimal.valueOf(d);
	}

	private native double _getValue(Element element) /*-{
		return parseFloat($wnd.$(element).autoNumeric('get'));
	}-*/;

	private native void _setValue(Element element, double d) /*-{
		$wnd.$(element).autoNumeric('set', d);
	}-*/;

	@Override
	public void setValue(final BigDecimal val) {
		setValue(val, false);
	}

	@Override
	public void setValue(final BigDecimal value, boolean fireEvents) {
		BigDecimal oldValue = fireEvents ? getValue() : null;
		if (isAttached())
			_setValue(getElement(), value.doubleValue());
		else
			setText(JsUtil.asString(value));
		if (fireEvents)
			fireValueChanged(oldValue, value);
	}

	protected void fireValueChanged(BigDecimal oldValue, BigDecimal newValue) {
		if (valueChangeHandlerInitialized)
			ValueChangeEvent.fireIfNotEqual(this, oldValue, newValue);
		else if (oldValue != newValue && (oldValue == null || !oldValue.equals(newValue))) {
			NativeEvent event = Document.get().createChangeEvent();
			ChangeEvent.fireNativeEvent(event, this);
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<BigDecimal> handler) {
		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;
			addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					ValueChangeEvent.fire(DecimalBox.this, getValue());
				}
			});
		}
		return addHandler(handler, ValueChangeEvent.getType());
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
	public int getTabIndex() {
		return input.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		input.setAccessKey(String.valueOf(key));
	}

	@Override
	public void setFocus(boolean focused) {
		if (focused)
			input.focus();
		else
			input.blur();
	}

	@Override
	public void setTabIndex(int index) {
		input.setTabIndex(index);
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
	}

	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addDomHandler(handler, FocusEvent.getType());
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addDomHandler(handler, BlurEvent.getType());
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return addDomHandler(handler, KeyDownEvent.getType());
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return addDomHandler(handler, KeyUpEvent.getType());
	}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return addDomHandler(handler, KeyPressEvent.getType());
	}

	@Override
	public boolean validate(boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	@Override
	public void setEnabled(boolean enabled) {
		input.setDisabled(!enabled);
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
		Bootstrap.setTooltip(getElement(), validationError);
	}

	@Override
	public BigDecimal getValue() {
		return JsUtil.asDecimal(getText());
	}

	@Override
	public String getText() {
		return input.getValue();
	}

	@Override
	public void setText(String text) {
		input.setValue(text);
	}

	public void setMaxLength(final int length) {
		getElement().setAttribute("maxlength", String.valueOf(length));
		setWidth(JsUtil.calcSizeForMaxLength(length));
	}

}