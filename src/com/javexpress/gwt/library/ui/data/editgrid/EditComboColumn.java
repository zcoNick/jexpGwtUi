package com.javexpress.gwt.library.ui.data.editgrid;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.javexpress.application.model.item.IModuleConstantsEnum;
import com.javexpress.application.model.item.ModuleEnumItems;
import com.javexpress.application.model.item.type.ListItem;
import com.javexpress.gwt.fw.client.GwtBootstrapApplication;
import com.javexpress.gwt.library.ui.data.slickgrid.EditGrid;
import com.javexpress.gwt.library.ui.form.combobox.IKeyValueList;
import com.javexpress.gwt.library.ui.form.combobox.KeyValueDataLoader;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class EditComboColumn extends EditColumn {

	private Map<? extends Serializable, String>	map;
	private Map<? extends Serializable, String>	mapData;
	private ConstantsWithLookup					mapNls;
	private List<ListItem<String, String>>		list;
	private EditGrid							grid;
	private IEditComboItemListener				listener;

	public void setListener(IEditComboItemListener listener) {
		this.listener = listener;
	}

	public void setGrid(EditGrid grid) {
		this.grid = grid;
	}

	/** Designer compatible constructor */
	public EditComboColumn(final String title, final String valueField) {
		this(title, null, valueField, null);
	}

	public EditComboColumn(final String title, final String valueField, final String width) {
		this(title, null, valueField, width);
	}

	public EditComboColumn(final String title, final String hint, final String valueField, final String width) {
		super(title, hint, valueField, ColumnAlign.left, width, EditorType.combo, null);
	}

	public EditComboColumn setItemsEnum(ModuleEnumItems<? extends Serializable> moduleEnumDescriber) {
		ConstantsWithLookup nls = GwtBootstrapApplication.getModuleNls(moduleEnumDescriber.getModuleId());
		return setMap(moduleEnumDescriber, nls);
	}

	public EditComboColumn setMap(final Map<? extends Serializable, String> map) {
		return setMap(map, null);
	}

	public EditComboColumn setMap(final Map<? extends Serializable, String> map, ConstantsWithLookup nls) {
		if (!isRequired())
			map.put(null, "");
		this.map = map;
		this.mapNls = nls;
		return this;
	}

	public EditComboColumn setList(final List<ListItem<String, String>> list) {
		this.list = list;
		return this;
	}

	public EditComboColumn setItemsRemote(KeyValueDataLoader comboDataLoader, Enum key) {
		return setItems(comboDataLoader, null, key);
	}

	public EditComboColumn setItemsConstant(final KeyValueDataLoader comboDataLoader, IModuleConstantsEnum key) {
		this.mapNls = GwtBootstrapApplication.getModuleNls(key.getModuleId());
		comboDataLoader.addConstant(new IKeyValueList() {
			@Override
			public void setKeyValueDataItems(JSONObject itm) {
				map = new LinkedHashMap<String, String>();
				mapData = new HashMap<String, String>();
				LinkedHashMap<String, String> hm = (LinkedHashMap<String, String>) map;
				HashMap<String, String> hmData = (HashMap<String, String>) mapData;
				if (!isRequired())
					hm.put("", "");
				if (itm == null)
					return;
				for (String lb : itm.keySet()) {
					JSONArray arr = itm.get(lb).isArray();//0-Value,1-Data,2-Path
					String data = JsUtil.asString(arr.get(1));
					hmData.put(JsUtil.asString(arr.get(0)), data != null ? data : "");
					String path = JsUtil.asString(arr.get(2));
					if (path != null)
						hm.put(JsUtil.asString(arr.get(0)), JsUtil.repeat("-", (path.split("\\.").length - 1) * 2) + lb);
					else
						hm.put(JsUtil.asString(arr.get(0)), lb);
				}
				grid.redraw();
			}
		}, key);
		return this;
	}

	public EditComboColumn setItems(KeyValueDataLoader comboDataLoader, Long moduleId, Enum key) {
		this.mapNls = GwtBootstrapApplication.getModuleNls(moduleId != null ? moduleId : comboDataLoader.getModuleId());
		comboDataLoader.add(new IKeyValueList() {
			@Override
			public void setKeyValueDataItems(JSONObject itm) {
				map = new LinkedHashMap<String, String>();
				mapData = new HashMap<String, String>();
				LinkedHashMap<String, String> hm = (LinkedHashMap<String, String>) map;
				HashMap<String, String> hmData = (HashMap<String, String>) mapData;
				if (!isRequired())
					hm.put("", "");
				if (itm == null)
					return;
				for (String lb : itm.keySet()) {
					JSONArray arr = itm.get(lb).isArray();//0-Value,1-Data,2-Path
					String data = JsUtil.asString(arr.get(1));
					hmData.put(JsUtil.asString(arr.get(0)), data != null ? data : "");
					String path = JsUtil.asString(arr.get(2));
					if (path != null)
						hm.put(JsUtil.asString(arr.get(0)), JsUtil.repeat("-", (path.split("\\.").length - 1) * 2) + lb);
					else
						hm.put(JsUtil.asString(arr.get(0)), lb);
				}
				grid.redraw();
			}
		}, moduleId, key);
		return this;
	}

	public boolean fillItems(JsonMap model, JavaScriptObject rowData, String labelProperty, String valueProperty, String dataProperty) {
		StringBuffer labels = new StringBuffer();
		StringBuffer values = new StringBuffer();
		StringBuffer datas = new StringBuffer();
		boolean first = true;
		if (map != null) {
			for (Serializable key : map.keySet()) {
				if (mapNls == null) {
					labels.append(first ? "" : "é").append(map.get(key));
					values.append(first ? "" : "é").append(key.toString());
					datas.append(first ? "" : "é").append(key.toString());
				} else {
					String constant = map.get(key).toString();
					if (constant.startsWith("@")) {
						String nlsValue = null;
						try {
							nlsValue = mapNls.getString(constant.substring(1));
						} catch (Exception ex) {
							nlsValue = constant;
						}
						labels.append(first ? "" : "é").append(nlsValue);
						values.append(first ? "" : "é").append(key != null ? key.toString() : "");
					} else {
						labels.append(first ? "" : "é").append(constant);
						values.append(first ? "" : "é").append(key != null ? key.toString() : "");
					}
					String s = mapData == null || key == null ? null : mapData.get(key.toString());
					datas.append(first ? "" : "é").append(s != null ? s : "");
				}
				first = false;
			}
			map = null;//GWT hint
			mapData = null;//GWT hint
			list = null;//GWT hint
			model.set("lazyinited", true);
			model.set(valueProperty, values.toString());
			model.set(labelProperty, labels.toString());
			model.set(dataProperty, datas.toString());
		} else if (list != null) {
			for (ListItem<String, String> li : list) {
				labels.append(first ? "" : "é").append(li.getLabel());
				values.append(first ? "" : "é").append(li.getValue());
				datas.append(first ? "" : "é").append("");
				first = false;
			}
			map = null;//GWT hint
			mapData = null;//GWT hint
			list = null;//GWT hint
			model.set("lazyinited", true);
			model.set(valueProperty, values.toString());
			model.set(labelProperty, labels.toString());
			model.set(dataProperty, datas.toString());
		}
		return !first;
	}

	public void fireComboItemSelected(JavaScriptObject rowData, String value, String selectedItemData) {
		if (listener != null)
			listener.itemSelected(new JsonMap(rowData), value, selectedItemData);
	}

	public boolean fireIsComboItemShowing(JavaScriptObject rowData, String value, String label, String data) {
		if (listener != null)
			return listener.isItemShowing(new JsonMap(rowData), value, label, data);
		return true;
	}

	public boolean hasListener() {
		return listener != null;
	}

}
