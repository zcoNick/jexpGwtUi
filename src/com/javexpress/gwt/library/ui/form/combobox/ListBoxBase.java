package com.javexpress.gwt.library.ui.form.combobox;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.ListBox;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class ListBoxBase extends ListBox implements IUserInputWidget<String>, IListItemBox {

	protected Map<Serializable, Serializable>	dataMap;
	private boolean								required;
	private IItemsChangeHandler					itemsChangeHandler;

	public IItemsChangeHandler getItemsChangeHandler() {
		return itemsChangeHandler;
	}

	public void setItemsChangeHandler(IItemsChangeHandler itemsChangeHandler) {
		this.itemsChangeHandler = itemsChangeHandler;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(final boolean required) {
		this.required = required;
	}

	protected ListBoxBase(final boolean isMulti) {
		super(isMulti);
	}

	public <T extends ListBoxBase> T setItems(final Map<? extends Serializable, ? extends Serializable> map) {
		return setItems(map, (ConstantsWithLookup) null);
	}

	public <T extends ListBoxBase> T setItems(final KeyValueDataLoader comboDataLoader, Enum key) {
		return setItems(comboDataLoader, key, null);
	}

	public <T extends ListBoxBase> T setItemsRemote(final KeyValueDataLoader comboDataLoader, Enum key) {
		return setItems(comboDataLoader, key, null);
	}

	private <T extends ListBoxBase> T setItems(KeyValueDataLoader comboDataLoader, Enum key, String params) {
		removeItems();
		comboDataLoader.add(this, key, params);
		return (T) this;
	}

	public <T extends ListBoxBase> T setItems(final KeyValueDataLoader comboDataLoader, Long moduleId, Enum key) {
		return (T) setItems(comboDataLoader, moduleId, key, null);
	}

	public <T extends ListBoxBase> T setItems(KeyValueDataLoader comboDataLoader, Long moduleId, Enum key, String params) {
		removeItems();
		comboDataLoader.add(this, moduleId, key, params);
		return (T) this;
	}

	public <T extends ListBoxBase> T setItems(final Map<? extends Serializable, ? extends Serializable> map, ConstantsWithLookup nls) {
		removeItems();
		if (map == null)
			return (T) this;
		for (Serializable key : map.keySet()) {
			if (nls == null)
				addItem(map.get(key), key);
			else {
				String constant = map.get(key).toString();
				if (constant.startsWith("@")) {
					try {
						String nlsValue = nls.getString(constant.substring(1));
						addItem(nlsValue, key);
					} catch (Exception ex) {
						try {
							String nlsValue = ClientContext.nlsCommon.getString(constant.substring(1));
							addItem(nlsValue, key);
						} catch (Exception ex1) {
							addItem(constant, key);
						}
					}
				} else
					addItem(constant, key);
			}
		}
		onItemListChanged();
		return (T) this;
	}

	@Override
	public void setItemsNls(Map<? extends Serializable, ? extends Serializable> map, ConstantsWithLookup nls) {
		setItems(map, nls);
	}

	public ListBoxBase setItems(final ComboDataSupplier<? extends Serializable, ? extends Serializable, ? extends Serializable> mapSupplier, boolean enableOnComplete) {
		return setItems(false, mapSupplier, enableOnComplete);
	}

	public ListBoxBase setItems(final ComboDataSupplier<? extends Serializable, ? extends Serializable, ? extends Serializable> mapSupplier) {
		return setItems(false, mapSupplier, false);
	}

	public <T extends ListBoxBase> T setItems(final boolean useEmptyItem, final ComboDataSupplier<? extends Serializable, ? extends Serializable, ? extends Serializable> mapSupplier) {
		return setItems(useEmptyItem, mapSupplier, false);
	}

	public <T extends ListBoxBase> T setItems(final boolean useEmptyItem, final ComboDataSupplier<? extends Serializable, ? extends Serializable, ? extends Serializable> mapSupplier, boolean enableOnComplete) {
		removeItems();
		mapSupplier.fillItems(enableOnComplete, this);
		return (T) this;
	}

	protected void removeItems() {
		clear();
		if (dataMap != null)
			dataMap.clear();
		dataMap = null;
		addItem("", "");
	}

	public void addItem(final Serializable label, final Serializable value) {
		addItem(label, value, null);
	}

	@Override
	public void setKeyValueDataItems(JSONObject itm) {
		if (itm != null) {
			for (String lb : itm.keySet()) {
				JSONArray arr = itm.get(lb).isArray();//0-Value,1-Data,2-Path
				String data = JsUtil.asString(arr.get(1));
				String path = JsUtil.asString(arr.get(2));
				if (path != null) {
					addItem(JsUtil.repeat("-", (path.split("\\.").length - 1) * 2) + lb, JsUtil.asString(arr.get(0)), data);
				} else
					addItem(lb, JsUtil.asString(arr.get(0)), data);
			}
		}
		onItemListChanged();
	}

	protected void onItemListChanged() {
		if (itemsChangeHandler != null)
			itemsChangeHandler.onItemsChanged();
	}

	public void addItem(final Serializable label, final Serializable value, final Serializable data) {
		addItem(label != null ? label.toString() : "", value != null ? value.toString() : "");
		if (data != null) {
			if (dataMap == null)
				dataMap = new HashMap<Serializable, Serializable>();
			dataMap.put(value, data);
		}
	}

	public OptionElement getOptionElement(Serializable value) {
		SelectElement se = getElement().cast();
		NodeList<OptionElement> items = se.getOptions();
		for (int i = 0; i < items.getLength(); i++)
			if (items.getItem(i).getAttribute("value").equals(value.toString()))
				return items.getItem(i);
		return null;
	}

	public Style getOptionStyle(Serializable value) {
		OptionElement el = getOptionElement(value);
		if (el == null)
			return null;
		return el.getStyle();
	}

	public void setItems(final boolean useEmpty, final String items, final String itemSep, final String keyValueSep) {
		removeItems();
		if (JsUtil.isNotEmpty(items)) {
			for (String s : items.split(itemSep)) {
				String[] kv = s.split(keyValueSep);
				addItem(kv[1], kv[0]);
			}
		}
		onItemListChanged();
	}

	@Override
	protected void onUnload() {
		dataMap = null;
		itemsChangeHandler = null;
		super.onUnload();
	}

	@Override
	public boolean validate(final boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	public static String getSelectedOptionValues(Element element) {
		StringBuffer sels = new StringBuffer();
		SelectElement se = element.cast();
		NodeList<OptionElement> items = se.getOptions();
		boolean first = true;
		for (int i = 0; i < items.getLength(); i++)
			if (items.getItem(i).isSelected()) {
				if (first)
					first = false;
				else
					sels.append(";");
				sels.append(items.getItem(i).getValue());
			}
		return sels.length() == 0 ? null : sels.toString();
	}

	public void setReadOnly(boolean readOnly) {
		setEnabled(readOnly);
	}

}