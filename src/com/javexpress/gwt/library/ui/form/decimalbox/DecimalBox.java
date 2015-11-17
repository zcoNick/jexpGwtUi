package com.javexpress.gwt.library.ui.form.decimalbox;

import java.math.BigDecimal;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.form.textbox.JexpValueBox;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class DecimalBox extends JexpValueBox<BigDecimal> {

	public static void fillResources(WidgetBundles wb) {
		//https://github.com/BobKnothe/autoNumeric
		wb.addJavaScript("scripts/autonumeric/autoNumeric-1.9.37.js");
	}

	private JsonMap	options;
	private boolean	zeroDefault	= false;

	/** Designer compatible constructor */
	public DecimalBox(final Widget parent, final String id) {
		super(parent, id, WidgetConst.DECIMALBOX_PREFIX);
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
	public void setRequired(boolean required) {
		super.setRequired(required);
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
		if (isAttached() && value != null)
			_setValue(getElement(), value.doubleValue());
		else
			setText(JsUtil.asString(value));
		if (fireEvents)
			fireValueChanged(oldValue, value);
	}

	@Override
	public BigDecimal getValue() {
		return JsUtil.asDecimal(getText());
	}

}