package com.javexpress.gwt.library.ui.form.numericbox;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap;
import com.javexpress.gwt.library.ui.bootstrap.DateBox;
import com.javexpress.gwt.library.ui.bootstrap.FormGroupCell;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class SpinnerBox extends JexpSimplePanel implements IUserInputWidget<Long>, HasValueChangeHandlers<Long> {

	/*	<div class="ace-spinner middle touch-spinner" style="width: 125px;">
			<div class="input-group">
				<div class="spinbox-buttons input-group-btn">					
					<button class="btn spinbox-down btn-sm btn-danger" type="button">						
						<i class="icon-only  ace-icon ace-icon fa fa-minus bigger-110"></i>					
					</button>				
				</div>
				<input type="text" id="spinner3" class="spinbox-input form-control text-center">
				<div class="spinbox-buttons input-group-btn">					
					<button class="btn spinbox-up btn-sm btn-success" type="button">						
						<i class="icon-only  ace-icon ace-icon fa fa-plus bigger-110"></i>					
					</button>
				</div>
			</div>
		</div>*/

	private ButtonElement		btMinus;
	private ButtonElement		btPlus;
	private InputElement		input;
	private boolean				required;
	private DataBindingHandler	dataBinding;
	private boolean				valueChangeHandlerInitialized;
	private Integer				minValue, maxValue;
	private int step=1;

	public SpinnerBox(final Widget parent, final String id) {
		super();
		getElement().addClassName("ace-spinner middle touch-spinner jexpSpinner");
		JsUtil.ensureId(parent, getElement(), WidgetConst.SPINNERBOX_PREFIX, id);

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
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(boolean required) {
		this.required = required;
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

	public Long getValueLong() {
		return JsUtil.asLong(getText());
	}

	public String getText() {
		return input.getValue();
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
		input.setAttribute("maxlength", String.valueOf(length));
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

	@Override
	public void setEnabled(boolean enabled) {
		input.setDisabled(!enabled);
		btMinus.setDisabled(!enabled);
		btPlus.setDisabled(!enabled);
	}

	@Override
	protected Element getSinkElement() {
		return input;
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

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return addDomHandler(handler, KeyDownEvent.getType());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Long> handler) {
		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;
			addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					Long v = applyMinMaxCheck();
					ValueChangeEvent.fire(SpinnerBox.this, v);
				}
			});
		}
		return addHandler(handler, ValueChangeEvent.getType());
	}

	protected Long applyMinMaxCheck() {
		Long v = getValue();
		if (v != null) {
			if (maxValue != null && v.longValue() > maxValue.longValue()) {
				input.setValue(maxValue.toString());
				v = maxValue.longValue();
			} else if (minValue != null && v.longValue() < minValue.longValue()) {
				input.setValue(minValue.toString());
				v = minValue.longValue();
			}
		}
		return v;
	}

	@Override
	protected void onUnload() {
		dataBinding = null;
		input = null;
		btMinus = null;
		btPlus = null;
		super.onUnload();
	}

	@Override
	public boolean validate(final boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	public void setPlaceholder(String value) {
		input.setAttribute("placeholder", value);
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
		//setTitle(validationError);
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

	public void setText(String value) {
		input.setValue(value);
	}

	@Override
	public Long getValue() {
		return getValueLong();
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
		createByJs(this, btMinus, btPlus);
	}

	private native void createByJs(SpinnerBox x, Element btMinus, Element btPlus) /*-{
		$wnd.$(btMinus).click(function(e){
			x.@com.javexpress.gwt.library.ui.form.numericbox.SpinnerBox::fireMinusPlus(I)(-1);
		});
		$wnd.$(btPlus).click(function(e){
			x.@com.javexpress.gwt.library.ui.form.numericbox.SpinnerBox::fireMinusPlus(I)(1);
		});
	}-*/;
	
	public void increment() {
		fireMinusPlus(1);
	}

	public void decrement() {
		fireMinusPlus(-1);
	}
	
	private void fireMinusPlus(int direction){
		Long l = getValue();
		if (l==null)
			l = Long.valueOf(direction*step);
		else
			l = l+(direction*step);
		l = applyMinMaxCheck();
		input.setValue(l.toString());
		ValueChangeEvent.fire(SpinnerBox.this, l);
	}

}