package com.javexpress.gwt.library.ui.form.numericbox;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.form.textbox.JexpValueBox;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class NumericBox extends JexpValueBox<Long> {

	private Integer	minValue, maxValue;

	/** Designer compatible constructor */
	public NumericBox(final Widget parent, final String id) {
		super(parent, id, WidgetConst.NUMERICBOX_PREFIX);
		if (!JsUtil.USE_BOOTSTRAP)
			setWidth("4em");
		addStyleName("jexpNumericBox jexpRightAlign");
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
		addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				applyMinMaxCheck();
			}
		});
	}

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

	public void setValueInt(final Integer val) {
		setValue(val == null ? null : val.longValue());
	}

	public void setValueShort(final Short val) {
		setValue(val == null ? null : val.longValue());
	}

	public void setValueByte(final Byte val) {
		setValue(val == null ? null : val.longValue());
	}

	public int getValueIntDef(final int i) {
		return JsUtil.isEmpty(getText()) ? i : getValueInt();
	}

	public Integer getMinValue() {
		return minValue;
	}

	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

	protected Long applyMinMaxCheck() {
		Long v = getValueLong();
		if (v != null) {
			if (maxValue != null && v.longValue() > maxValue.longValue()) {
				setText(maxValue.toString());
				v = maxValue.longValue();
				JsUtil.highlight(getElement());
			} else if (minValue != null && v.longValue() < minValue.longValue()) {
				setText(minValue.toString());
				v = minValue.longValue();
				JsUtil.highlight(getElement());
			}
		}
		return v;
	}

	@Override
	public void setValue(final Long val) {
		setValue(val, false);
	}

	@Override
	public void setValue(final Long value, boolean fireEvents) {
		Long oldValue = fireEvents ? getValue() : null;
		setText(JsUtil.asString(value));
		if (fireEvents)
			fireValueChanged(oldValue, value);
	}

	@Override
	public Long getValue() {
		return JsUtil.asLong(getText());
	}

	public void setValueString(String value) {
		setValue(Long.valueOf(value));
	}

}