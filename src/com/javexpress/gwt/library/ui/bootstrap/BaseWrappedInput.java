package com.javexpress.gwt.library.ui.bootstrap;

import java.io.Serializable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IWrappedInput;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class BaseWrappedInput<T extends Serializable> extends JexpSimplePanel implements IWrappedInput<T>, HasValueChangeHandlers<T> {

	private boolean				required;
	private DataBindingHandler	dataBinding;
	private boolean				valueChangeHandlerInitialized;
	protected Element			input;

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(final boolean required) {
		this.required = required;
	}

	public BaseWrappedInput(Widget parent, String prefix, String id, String styleName) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, prefix, id);
		getElement().setClassName("input-group " + (styleName != null ? styleName : ""));
	}

	public void setValue(T value) {
		setValue(value, false);
	}

	public void setValue(T value, boolean fireEvents) {
		T oldValue = fireEvents ? getValue() : null;
		setRawValue(value);
		if (fireEvents) {
			T newValue = getValue();
			ValueChangeEvent.fireIfNotEqual(this, oldValue, newValue);
		}
	}

	protected abstract void setRawValue(T value);

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
	public int getTabIndex() {
		return input.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		input.setPropertyString("accesskey", String.valueOf(key));
	}

	@Override
	public void setFocus(boolean focused) {
		if (focused)
			input.focus();
	}

	@Override
	public void setTabIndex(int index) {
		input.setTabIndex(index);
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (!enabled)
			input.setAttribute("disabled", "true");
		else
			input.removeAttribute("disabled");
	}

	@Override
	public Element getInputElement() {
		return input;
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

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;
			addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					ValueChangeEvent.fire(BaseWrappedInput.this, getValue());
				}
			});
		}
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	protected void onUnload() {
		dataBinding = null;
		input = null;
		super.onUnload();
	}

	@Override
	public boolean validate(final boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	public void setPlaceholder(String value) {
		input.setAttribute("placeholder", value);
	}

}