package com.javexpress.gwt.library.ui.form.checkbox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.common.model.item.type.Pair;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.dialog.JexpPopupPanel;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.form.combobox.IListItemBox;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class CheckInlineBox extends TextArea implements IUserInputWidget<String>, IListItemBox {

	private Map<String, Pair<String, Boolean>>	items;
	private boolean								required;
	private HashMap<Serializable, Serializable>	dataMap;
	private String								emptyText	= "";
	private String								popWidth	= WidgetConst.WIDTH_MIDDLE;
	private String								popHeight	= "22em";
	private List<String>						lazyValues;
	private DataBindingHandler					dataBinding;

	public String getEmptyText() {
		return emptyText;
	}

	public void setEmptyText(String emptyText) {
		this.emptyText = emptyText;
	}

	public void setPopWidth(String width) {
		this.popWidth = width;
	}

	public void setPopHeight(String height) {
		this.popHeight = height;
	}

	public CheckInlineBox(final Widget parent, final String id) {
		super();
		addStyleName("jexpCheckComboBox");
		JsUtil.ensureId(parent, this, WidgetConst.CHECKCOMBOBOX_PREFIX, id);
		setReadOnly(true);
		setHeight("4em");
		getElement().getStyle().setFontSize(0.85, Unit.EM);
		items = new LinkedHashMap<String, Pair<String, Boolean>>();
		addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (items.isEmpty())
					return;
				final JexpPopupPanel pop = new JexpPopupPanel(true);
				if (!JsUtil.USE_BOOTSTRAP)
					pop.addStyleName("ui-widget ui-widget-content ui-corner-all jexpShadow jexpCheckComboPanel");
				else
					pop.addStyleName("jexpShadow jexpCheckComboPanel");
				pop.setWidth(popWidth);
				pop.setHeight(popHeight);
				pop.addCloseHandler(new CloseHandler<PopupPanel>() {
					@Override
					public void onClose(CloseEvent<PopupPanel> event) {
						pop.removeFromParent();
						fireOnChange();
					}
				});
				SimplePanel panel = new SimplePanel();
				panel.getElement().getStyle().setOverflow(Overflow.AUTO);
				panel.getElement().getStyle().setWidth(100, Unit.PCT);
				panel.getElement().getStyle().setHeight(100, Unit.PCT);

				final VerticalPanel table = new VerticalPanel();
				table.setWidth("100%");
				HorizontalPanel tools = new HorizontalPanel();
				tools.addStyleName("jexpCheckComboTools");
				tools.setWidth("100%");
				Label lbTumu = new Label(ClientContext.nlsCommon.selectAll());
				lbTumu.addStyleName("jexpCheckComboAll");
				lbTumu.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						evictSelection(table, true);
					}
				});
				tools.add(lbTumu);
				Label lbTemizle = new Label(ClientContext.nlsCommon.clear());
				lbTemizle.addStyleName("jexpCheckComboReset");
				lbTemizle.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						evictSelection(table, false);
					}
				});
				tools.add(lbTemizle);
				table.add(tools);

				ClickHandler chHandler = new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						CheckBox cb = (CheckBox) event.getSource();
						if (cb != null) {
							Pair<String, Boolean> pair = items.get(cb.getText());
							pair.setRight(cb.getValue());
						}
					}
				};

				for (String label : items.keySet()) {
					Pair<String, Boolean> pair = items.get(label);
					CheckBox check = new CheckBox(label);
					check.setValue(pair.getRight());
					check.addClickHandler(chHandler);
					table.add(check);
				}

				panel.setWidget(table);
				pop.setWidget(panel);
				pop.showRelativeTo(CheckInlineBox.this);
			}
		});
	}

	@Override
	protected void onLoad() {
		if (JsUtil.isEmpty(getText()))
			setText(required ? "" : emptyText);
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		items = null;
		dataMap = null;
		popWidth = null;
		popHeight = null;
		dataBinding = null;
		super.onUnload();
	}

	@Override
	public void setKeyValueDataItems(JSONObject itm) {
		if (itm == null)
			return;
		for (String lb : itm.keySet()) {
			JSONArray arr = itm.get(lb).isArray();//0-Value,1-Data,2-Path
			addItem(lb, JsUtil.asString(arr.get(0)), false, JsUtil.asString(arr.get(1)));//Value,Data,Path
		}
		fireItemListChanged();
	}

	public void clear() {
		items.clear();
		fireItemListChanged();
	}

	public void addItem(final Serializable label, final Serializable value, boolean selected, final Serializable data) {
		if (lazyValues != null && lazyValues.contains(value.toString()))
			selected = true;
		_addItem(label != null ? label.toString() : "", value != null ? value.toString() : "", selected);
		if (lazyValues != null)
			lazyValues.remove(value);
		if (data != null) {
			if (dataMap == null)
				dataMap = new HashMap<Serializable, Serializable>();
			dataMap.put(value, data);
		}
	}

	private void _addItem(String label, String value, boolean selected) {
		if (selected)
			setText(getText() + (JsUtil.isEmpty(getText()) ? "" : ", ") + label);
		items.put(label, new Pair<String, Boolean>(value, selected));
	}

	public CheckInlineBox setItems(final Map<? extends Serializable, ? extends Serializable> map, ConstantsWithLookup nls) {
		items.clear();
		if (map == null)
			return this;
		for (Serializable key : map.keySet()) {
			if (nls == null)
				addItem(map.get(key), key, false, null);
			else {
				String constant = map.get(key).toString();
				if (constant.startsWith("@")) {
					try {
						String nlsValue = nls.getString(constant.substring(1));
						addItem(nlsValue, key, false, null);
					} catch (Exception ex) {
						try {
							String nlsValue = ClientContext.nlsCommon.getString(constant.substring(1));
							addItem(nlsValue, key, false, null);
						} catch (Exception ex1) {
							addItem(constant, key, false, null);
						}
					}
				} else
					addItem(constant, key, false, null);
			}
		}
		fireItemListChanged();
		return this;
	}

	@Override
	public void setItemsNls(Map<? extends Serializable, ? extends Serializable> map, ConstantsWithLookup nls) {
		setItems(map, nls);
	}

	private void fireItemListChanged() {
	}

	@Override
	public String getValue() {
		if (items == null || items.isEmpty())
			return null;
		StringBuilder sels = new StringBuilder();
		for (String l : items.keySet()) {
			Pair<String, Boolean> pair = items.get(l);
			if (pair.getRight())
				sels.append(pair.getLeft()).append(",");
		}
		if (sels.length() > 0)
			sels.setLength(sels.length() - 1);
		return sels.length() == 0 ? null : sels.toString();
	}

	public ArrayList<String> getValueList() {
		if (items == null || items.isEmpty())
			return null;
		ArrayList<String> sels = new ArrayList<String>();
		for (String l : items.keySet()) {
			Pair<String, Boolean> pair = items.get(l);
			if (pair.getRight())
				sels.add(pair.getLeft());
		}
		return sels.isEmpty() ? null : sels;
	}

	public void clearSelection() {
		updateAllValues(false);
		lazyValues = null;
		setText(null);
	}

	private void updateAllValues(boolean selected) {
		if (items == null || items.isEmpty())
			return;
		for (String l : items.keySet()) {
			Pair<String, Boolean> pair = items.get(l);
			pair.setRight(selected);
		}
	}

	@Override
	public void setValue(String value) {
		if (JsUtil.isEmpty(value))
			clearSelection();
		else
			setValueArray(value.split(","));
	}

	public boolean setValueList(ArrayList<String> values) {
		clearSelection();
		if (values == null || values.isEmpty()) {
			return false;
		}
		if (items.isEmpty()) {
			if (lazyValues == null)
				lazyValues = new ArrayList<String>();
			lazyValues.addAll(values);
		} else
			for (String l : items.keySet()) {
				Pair<String, Boolean> pair = items.get(l);
				pair.setRight(values.contains(pair.getLeft()));
			}
		fireOnChange();
		return true;
	}

	public boolean setValueArray(String[] values) {
		clearSelection();
		if (values == null || values.length == 0) {
			return false;
		}
		if (items.isEmpty()) {
			if (lazyValues == null)
				lazyValues = new ArrayList<String>();
			for (String v : values)
				lazyValues.add(v);
		} else {
			for (String l : items.keySet()) {
				Pair<String, Boolean> pair = items.get(l);
				for (String v : values) {
					if (pair.getLeft().equals(v)) {
						pair.setRight(true);
						break;
					}
				}
			}
			fireOnChange();
		}
		return true;
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

	public void setItems(final String items, final String itemSep, final String keyValueSep) {
		clearSelection();
		if (JsUtil.isEmpty(items))
			return;
		for (String s : items.split(itemSep)) {
			String[] kv = s.split(keyValueSep);
			_addItem(kv[1], kv[0], false);
		}
		fireItemListChanged();
	}

	private void fireOnChange() {
		StringBuilder t = new StringBuilder();
		for (String l : items.keySet()) {
			Pair<String, Boolean> pair = items.get(l);
			if (pair.getRight())
				t.append(l).append(", ");
		}
		if (t.length() > 0)
			t.setLength(t.length() - 2);
		else
			t.append(emptyText);
		String old = getText();
		setText(t.toString());
		if (!getText().equals(old)) {
			NativeEvent event = Document.get().createChangeEvent();
			DomEvent.fireNativeEvent(event, this);
		}
	}

	private void evictSelection(VerticalPanel table, boolean selected) {
		Iterator<Widget> iter = table.iterator();
		while (iter.hasNext()) {
			Widget w = iter.next();
			if (w instanceof CheckBox) {
				((CheckBox) w).setValue(selected);
			}
		}
		updateAllValues(selected);
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

	public ArrayList<Long> getValueListLong() {
		if (items == null || items.isEmpty())
			return null;
		ArrayList<Long> sels = new ArrayList<Long>();
		for (String l : items.keySet()) {
			Pair<String, Boolean> pair = items.get(l);
			if (pair.getRight())
				sels.add(Long.valueOf(pair.getLeft()));
		}
		return sels.isEmpty() ? null : sels;
	}

}