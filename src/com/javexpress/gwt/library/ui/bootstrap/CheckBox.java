package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.IJexpWidget;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class CheckBox extends SimplePanel implements IJexpWidget, HasKeyDownHandlers, IUserInputWidget<Boolean> {

	private Element				label;
	private InputElement		check;
	private DataBindingHandler	dataBinding;
	private boolean				valueChangeHandlerInitialized;

	public CheckBox(Widget parent, String id, String text) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.CHECKBOX_PREFIX, id);
		getElement().setClassName("jexpCheckBox");

		check = DOM.createInputCheck().cast();
		check.setClassName("ace");
		JsUtil.ensureSubId(getElement(), check, "ch");
		getElement().appendChild(check);

		label = DOM.createLabel();
		label.setClassName("lbl");
		label.setAttribute("for", check.getId());
		setText(text);
		getElement().appendChild(label);
	}

	public void setText(String text) {
		label.setInnerText(text != null ? " " + text : null);
	}

	@Override
	protected void onUnload() {
		check = null;
		label = null;
		dataBinding = null;
		super.onUnload();
	}

	@Override
	public String getId() {
		return getElement().getId();
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
	}

	@Override
	public Boolean getValue() {
		return check.isChecked();
	}

	@Override
	public void setValue(Boolean checked) {
		setValue(checked, false);
	}

	@Override
	public void setValue(Boolean value, boolean fireEvents) {
		Boolean oldValue = fireEvents ? getValue() : null;
		getElement().removeClassName("jexpCheckSelected");
		check.setChecked(value != null && value.booleanValue());
		if (check.isChecked())
			getElement().addClassName("jexpCheckSelected");
		if (fireEvents) {
			Boolean newValue = getValue();
			ValueChangeEvent.fireIfNotEqual(this, oldValue, newValue);
		}
	}

	@Override
	public int getTabIndex() {
		return check.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		check.setAccessKey(String.valueOf(key));
	}

	@Override
	public void setFocus(boolean focused) {
		if (focused)
			check.focus();
		else
			check.blur();
	}

	@Override
	public void setTabIndex(int index) {
		check.setTabIndex(index);
	}

	@Override
	public boolean isRequired() {
		return false;
	}

	@Override
	public void setRequired(boolean required) {
	}

	@Override
	public boolean validate(boolean focusedBefore) {
		return true;
	}

	@Override
	public void setEnabled(boolean enabled) {
		check.setDisabled(!enabled);
	}

	@Override
	public void setValidationError(String validationError) {
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
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return addDomHandler(handler, KeyDownEvent.getType());
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;
			addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					ValueChangeEvent.fire(CheckBox.this, getValue());
				}
			});
		}
		return addHandler(handler, ValueChangeEvent.getType());
	}

}