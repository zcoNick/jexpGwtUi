package com.javexpress.gwt.library.ui.bootstrap;

import java.io.Serializable;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class Select2Single extends BaseSelect2 {

	protected String	lazyValue;

	public Select2Single(Widget parent, String id) {
		super(parent, id, false);
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

	public void setValueByte(final Byte selectedValue) {
		setValue(selectedValue == null ? null : String.valueOf(selectedValue));
	}

	public void setValueLong(final Long selectedValue) {
		setValue(selectedValue == null ? null : String.valueOf(selectedValue));
	}

	public void setValueShort(final Short selectedValue) {
		setValue(selectedValue == null ? null : String.valueOf(selectedValue));
	}

	public boolean setValue(final String selectedValue) {
		lazyValue = null;
		setSelectedIndex(-1);
		if (JsUtil.isEmpty(selectedValue)) {
			return false;
		}
		for (int i = 0; i < getItemCount(); i++) {
			if (getValue(i).equals(selectedValue)) {
				setSelectedIndex(i);
				return true;
			}
		}
		lazyValue = selectedValue;
		return false;
	}

	public String getSelectedText() {
		return getSelectedIndex() == -1 ? null : getItemText(getSelectedIndex());
	}

	@Override
	public void addItem(Serializable label, Serializable value, Serializable data) {
		super.addItem(label, value, data);
		if (lazyValue != null && lazyValue.equals(value.toString())) {
			setSelectedIndex(getItemCount() - 1);
			lazyValue = null;
			NativeEvent event = Document.get().createChangeEvent();
			DomEvent.fireNativeEvent(event, this);
		}
	}

	@Override
	protected void onUnload() {
		lazyValue = null;
		super.onUnload();
	}

	public void setValueLong(Long value, boolean fireChanged) {
		setValueLong(value);
		if (fireChanged) {
			NativeEvent event = Document.get().createChangeEvent();
			DomEvent.fireNativeEvent(event, this);
		}
	}

}