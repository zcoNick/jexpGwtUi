package com.javexpress.gwt.library.ui.form.combobox;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.common.model.item.IComboItem;
import com.javexpress.common.model.item.IComboItemWithHint;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap;
import com.javexpress.gwt.library.ui.bootstrap.LabelControlCell;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ComboBox extends ListBoxBase implements AsyncCallback<List<? extends IComboItem<Serializable>>> {

	protected String			lazyValue;
	protected boolean			lazyFire;
	private DataBindingHandler	dataBinding;

	/** Designer compatible constructor */
	public ComboBox(final Widget parent, final String id) {
		super(false);
		getElement().addClassName("jexpComboBox");
		JsUtil.ensureId(parent, this, WidgetConst.COMBOBOX_PREFIX, id);
	}

	public String getLabel() {
		int sel = getSelectedIndex();
		if (sel == -1)
			return null;
		String val = getItemText(sel);
		if (val == null || val.trim().equals(""))
			return null;
		return val;
	}

	public Serializable getData() {
		int sel = getSelectedIndex();
		if (sel == -1)
			return null;
		String val = getValue(sel);
		return dataMap.get(val);
	}

	@Override
	public String getValue() {
		int sel = getSelectedIndex();
		if (sel == -1)
			return null;
		String val = getValue(sel);
		if (JsUtil.isEmpty(val))
			return null;
		return val;
	}

	public Integer getValueInt() {
		String val = getValue();
		if (val == null)
			return null;
		return Integer.valueOf(val);
	}

	public Byte getValueByte() {
		String val = getValue();
		if (val == null)
			return null;
		return Byte.valueOf(val);
	}

	public Short getValueShort() {
		String val = getValue();
		if (val == null)
			return null;
		return Short.valueOf(val);
	}

	public Long getValueLong() {
		String val = getValue();
		if (val == null)
			return null;
		return Long.parseLong(val);
	}

	public void setValueInt(final Integer selectedValue) {
		setValue(selectedValue == null ? null : String.valueOf(selectedValue));
	}

	public void setValueInt(final Integer selectedValue, boolean fireChanged) {
		setValue(selectedValue == null ? null : String.valueOf(selectedValue), fireChanged);
	}

	public void setValueByte(final Byte selectedValue) {
		setValue(selectedValue == null ? null : String.valueOf(selectedValue));
	}

	public void setValueByte(final Byte selectedValue, boolean fireChanged) {
		setValue(selectedValue == null ? null : String.valueOf(selectedValue), fireChanged);
	}

	public void setValueLong(final Long selectedValue) {
		setValue(selectedValue == null ? null : String.valueOf(selectedValue));
	}

	public void setValueLong(final Long selectedValue, boolean fireChanged) {
		setValue(selectedValue == null ? null : String.valueOf(selectedValue), fireChanged);
	}

	public void setValueShort(final Short selectedValue) {
		setValue(selectedValue == null ? null : String.valueOf(selectedValue));
	}

	public void setValueShort(final Short selectedValue, boolean fireChanged) {
		setValue(selectedValue == null ? null : String.valueOf(selectedValue), fireChanged);
	}

	@Override
	public void setValue(final String selectedValue) {
		setValue(selectedValue, false);
	}

	@Override
	public void setValue(final String value, boolean fireEvents) {
		lazyValue = value;
		lazyFire = fireEvents;
		String oldValue = fireEvents ? getValue() : null;
		setSelectedIndex(-1);
		for (int i = 0; i < getItemCount(); i++) {
			if (getValue(i).equals(value)) {
				setSelectedIndex(i);
				lazyValue = null;
				lazyFire = false;
				GWT.log(getElement().getId() + " lazy cleared " + i + " : " + value);
				break;
			}
		}
		if (fireEvents) {
			String newValue = getValue();
			ValueChangeEvent.fireIfNotEqual(this, oldValue, newValue);
		}
	}

	public String getSelectedText() {
		return getSelectedIndex() == -1 ? null : getItemText(getSelectedIndex());
	}

	@Override
	public void addItem(Serializable label, Serializable value, Serializable data, String hint) {
		super.addItem(label, value, data, hint);
		if (lazyValue != null && lazyValue.equals(value.toString()))
			setValue(lazyValue, lazyFire);
	}

	@Override
	protected void onUnload() {
		dataBinding = null;
		super.onUnload();
	}

	@Override
	public void setValidationError(String validationError) {
		if (JsUtil.USE_BOOTSTRAP) {
			Widget nw = getParent() instanceof LabelControlCell ? getParent() : this;
			if (validationError == null)
				nw.removeStyleName("has-error");
			else
				nw.addStyleName("has-error");
		}
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
	public void onFailure(Throwable caught) {
		JsUtil.handleError(this, caught);
	}

	@Override
	public void onSuccess(List<? extends IComboItem<Serializable>> result) {
		clear();
		if (result == null)
			return;
		for (IComboItem item : result)
			addItem(item.getL(), item.getV(), item.getD(), item instanceof IComboItemWithHint ? ((IComboItemWithHint) item).getH() : null);
		onItemListChanged();
	}

}