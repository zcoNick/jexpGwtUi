package com.javexpress.gwt.library.ui.bootstrap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IDataBindable;
import com.javexpress.gwt.library.ui.form.combobox.IKeyValueList;
import com.javexpress.gwt.library.ui.form.label.Label;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class TextField extends Label implements IDataBindable<String>, IKeyValueList {

	private DataBindingHandler<TextField>	dataBinding;
	private Map<String, String>				optionsMap;

	public TextField(Widget parent, String id) {
		super(parent, id);
	}

	@Override
	public void setValue(Integer value) {
		setValueInt(value);
	}

	@Override
	public void setValue(Short value) {
		setValueShort(value);
	}

	public void setValue(Long value) {
		setValueLong(value);
	}

	public void setValueShort(Short val) {
		setValue(JsUtil.asString(val));
	}

	@Override
	public void setValueInt(Integer val) {
		setValue(JsUtil.asString(val));
	}

	@Override
	public void setValueLong(Long val) {
		setValue(JsUtil.asString(val));
	}

	@Override
	public String getValue() {
		String v = getElement().getAttribute("v");
		return JsUtil.isEmpty(v) ? null : v;
	}

	@Override
	public void setText(String text) {
		getElement().removeAttribute("v");
		super.setText(text);
	}

	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		String t = optionsMap != null ? optionsMap.get(value) : value;
		if (value == null)
			getElement().removeAttribute("v");
		else
			getElement().setAttribute("v", value);
		setText(t);
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
	public void setKeyValueDataItems(JSONObject itm) {
		optionsMap = new HashMap<String, String>();
		if (itm == null)
			return;
		for (String lb : itm.keySet()) {
			JSONArray arr = itm.get(lb).isArray();//0-Value,1-Data,2-Path
			String data = JsUtil.asString(arr.get(1));
			addItem(lb, JsUtil.asString(arr.get(0)), data);
		}
		onItemListChanged();
	}

	private void addItem(String value, String label, String data) {
		optionsMap.put(value, label);
	}

	protected void onItemListChanged() {
	}

	public void setItems(final Map<? extends Serializable, ? extends Serializable> map, ConstantsWithLookup nls) {
		optionsMap = new HashMap<String, String>();
		if (map == null)
			return;
		for (Serializable key : map.keySet()) {
			if (nls == null)
				addItem(key.toString(), map.get(key).toString(), null);
			else {
				String constant = map.get(key).toString();
				if (constant.startsWith("@")) {
					try {
						String nlsValue = nls.getString(constant.substring(1));
						addItem(key.toString(), nlsValue, null);
					} catch (Exception ex) {
						addItem(key.toString(), constant, null);
					}
				} else
					addItem(key.toString(), constant, null);
			}
		}
		onItemListChanged();
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler handler) {
		return null;
	}

}