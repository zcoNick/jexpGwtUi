package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IWrappedInput;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class IconTextBox extends JexpSimplePanel implements IWrappedInput<String>, HasValueChangeHandlers<String> {

	private boolean				required;
	private ICssIcon			icon;
	private Element				input;
	private boolean				rightIcon;
	private DataBindingHandler	dataBinding;

	public ICssIcon getIcon() {
		return icon;
	}

	public void setIcon(ICssIcon icon) {
		this.icon = icon;
	}

	public boolean isRightIcon() {
		return rightIcon;
	}

	public void setRightIcon(boolean rightIcon) {
		this.rightIcon = rightIcon;
	}

	public IconTextBox(Widget parent, String id, ICssIcon icon) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.TEXTBOX_PREFIX, id);
		getElement().setClassName("input-group");

		input = createInputText();
		getElement().appendChild(input);

		this.icon = icon;
	}

	@Override
	protected void doAttachChildren() {
		if (icon != null) {
			Element span = DOM.createSpan();
			span.setClassName("input-group-addon");
			span.setInnerHTML("<i class='ace-icon " + icon.getCssClass() + "'></i>");
			if (rightIcon)
				getElement().appendChild(span);
			else
				getElement().insertFirst(span);
		}
		super.doAttachChildren();
	}

	protected Element createInputText() {
		return DOM.createInputText();
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
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public boolean validate(boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	@Override
	public String getValue() {
		return input.getPropertyString("value");
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (!enabled)
			input.setAttribute("disabled", "true");
		else
			input.removeAttribute("disabled");
	}

	public void setValue(String value) {
		setValue(value, false);
	}

	public void setValue(String value, boolean fireEvents) {
		String oldValue = fireEvents ? getValue() : null;
		setText(value);
		if (fireEvents) {
			ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
		}
	}

	public void setText(String text) {
		input.setPropertyString("value", text != null ? text : "");
	}

	public String getText() {
		return input.getPropertyString("value");
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public void setMaxLength(int maxlength) {
		getElement().setPropertyInt("maxlength", maxlength);
	}

	@Override
	public Element getInputElement() {
		return input;
	}

	@Override
	public void setValidationError(String validationError) {
		Bootstrap.setTooltip(getElement(), validationError);
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

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
	}

}