package com.javexpress.gwt.library.ui.form.numericbox;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.BaseWrappedInput;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class SpinnerBox extends BaseWrappedInput<Long, InputElement> {

	private ButtonElement	btMinus;
	private ButtonElement	btPlus;
	private Integer			minValue, maxValue;
	private int				step	= 1;

	public SpinnerBox(final Widget parent, final String id) {
		super(parent, WidgetConst.SPINNERBOX_PREFIX, id, "ace-spinner middle touch-spinner jexpSpinner");

		Element main = DOM.createDiv();
		main.setClassName("input-group");

		Element divMinus = DOM.createDiv();
		divMinus.setClassName("spinbox-buttons input-group-btn");
		btMinus = DOM.createButton().cast();
		btMinus.setClassName("btn spinbox-down btn-sm btn-default");
		btMinus.setInnerHTML("<i class=\"icon-only ace-icon ace-icon fa fa-minus\"></i>");
		divMinus.appendChild(btMinus);
		main.appendChild(divMinus);

		input = DOM.createInputText().cast();
		input.setClassName("spinbox-input form-control text-center");
		main.appendChild(input);

		Element divPlus = DOM.createDiv();
		divPlus.setClassName("spinbox-buttons input-group-btn");
		btPlus = DOM.createButton().cast();
		btPlus.setClassName("btn spinbox-down btn-sm btn-default");
		btPlus.setInnerHTML("<i class=\"icon-only ace-icon ace-icon fa fa-plus\"></i>");
		divPlus.appendChild(btPlus);
		main.appendChild(divPlus);

		getElement().appendChild(main);
	}

	@Override
	public String getText() {
		return input.getValue();
	}

	@Override
	public void setText(String text) {
		input.setValue(text);
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

	public void setMaxLength(final int length) {
		input.setAttribute("maxlength", String.valueOf(length));
		setWidth(JsUtil.calcSizeForMaxLength(length));
	}

	@Override
	public Long getValue() {
		return JsUtil.asLong(getText());
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		btMinus.setDisabled(!enabled);
		btPlus.setDisabled(!enabled);
	}

	protected Long applyMinMaxCheck(Long v) {
		if (v != null) {
			if (maxValue != null && v.longValue() > maxValue.longValue()) {
				input.setValue(maxValue.toString());
				v = maxValue.longValue();
				JsUtil.highlight(input);
			} else if (minValue != null && v.longValue() < minValue.longValue()) {
				input.setValue(minValue.toString());
				v = minValue.longValue();
				JsUtil.highlight(input);
			}
		}
		return v;
	}

	@Override
	protected void onUnload() {
		btMinus = null;
		btPlus = null;
		destroyByJs(btMinus, btPlus);
		super.onUnload();
	}

	private native void destroyByJs(Element btMinus, Element btPlus) /*-{
		$wnd.$(btMinus).off();
		$wnd.$(btPlus).off();
	}-*/;

	@Override
	protected void doAttachChildren() {
		super.doAttachChildren();
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

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (minValue != null)
			setText(minValue.toString());
		createByJs(this, btMinus, btPlus);
	}

	private native void createByJs(SpinnerBox x, Element btMinus, Element btPlus) /*-{
		$wnd
				.$(btMinus)
				.click(
						function(e) {
							e.preventDefault();
							e.stopPropagation();
							x.@com.javexpress.gwt.library.ui.form.numericbox.SpinnerBox::fireMinusPlus(I)(-1);
						});
		$wnd
				.$(btPlus)
				.click(
						function(e) {
							e.preventDefault();
							e.stopPropagation();
							x.@com.javexpress.gwt.library.ui.form.numericbox.SpinnerBox::fireMinusPlus(I)(1);
						});
	}-*/;

	public void increment() {
		fireMinusPlus(1);
	}

	public void decrement() {
		fireMinusPlus(-1);
	}

	private void fireMinusPlus(int direction) {
		Long l = getValue();
		if (l == null)
			l = Long.valueOf(direction * step);
		else
			l = l + (direction * step);
		l = applyMinMaxCheck(l);
		input.setValue(l.toString());
		ValueChangeEvent.fire(SpinnerBox.this, l);
	}

}