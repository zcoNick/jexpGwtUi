package com.javexpress.gwt.library.ui.form.checkbox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.LabelControlCell;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.form.combobox.IKeyValueList;
import com.javexpress.gwt.library.ui.form.combobox.KeyValueDataLoader;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class CheckGroupBox extends FlexTable implements IUserInputWidget<String>, IKeyValueList {

	private final int			mod;
	private String				lazyValue;
	private boolean				required;
	private List<CheckBox>		items;
	private char				seperator;
	private DataBindingHandler	dataBinding;
	private boolean				valueChangeHandlerInitialized;

	public CheckGroupBox(Widget parent, String id, int mod) {
		this(parent, id, mod, true);
	}

	/** Designer compatible constructor */
	public CheckGroupBox(Widget parent, String id, int mod, boolean useborder) {
		this(parent, id, mod, ',', useborder);
	}

	public CheckGroupBox(Widget parent, String id, int mod, char seperator, boolean useborder) {
		super();
		this.mod = mod;
		this.seperator = seperator;
		JsUtil.ensureId(parent, this, WidgetConst.CHECKGROUPBOX_PREFIX, id);
		addStyleName((JsUtil.USE_BOOTSTRAP ? "" : "ui-widget ui-widget-content ") + " jexpCheckGroupBox");
		if (!useborder)
			getElement().getStyle().setBorderWidth(0, Unit.PX);
	}

	public char getSeperator() {
		return seperator;
	}

	public void setSeperator(char seperator) {
		this.seperator = seperator;
	}

	public void addItem(final Serializable label, final Serializable value) {
		addItem(label, value, null);
	}

	public void addItem(final Serializable label, final Serializable value, String data) {
		CheckBox ch = new CheckBox(label.toString());
		ch.getElement().setAttribute("v", value.toString());
		if (data != null)
			ch.getElement().setAttribute("d", data.toString());
		addItem(ch);
	}

	public void addItem(CheckBox ch) {
		if (items == null)
			items = new ArrayList<CheckBox>();
		items.add(ch);
		int row = getRowCount();
		int col = row == 0 ? 0 : getCellCount(row - 1);
		if (row == 0)
			row = 1;
		if (col == mod) {
			col = 0;
		} else
			row -= 1;
		setWidget(row, col, ch);
		ch.addStyleName("jexpCheckGroupBoxCheck");
		if (row == 1) {
			getColumnFormatter().getElement(col).getStyle().setWidth(Math.ceil(100 / mod), Unit.PCT);
		}
	}

	public CheckGroupBox setItems(final Map<? extends Serializable, ? extends Serializable> map, ConstantsWithLookup nls) {
		if (items != null)
			items.clear();
		if (map == null)
			return this;
		for (Serializable key : map.keySet()) {
			if (nls == null)
				addItem(map.get(key), key, null);
			else {
				String constant = map.get(key).toString();
				if (constant.startsWith("@")) {
					try {
						String nlsValue = nls.getString(constant.substring(1));
						addItem(nlsValue, key, null);
					} catch (Exception ex) {
						addItem(constant, key, null);
					}
				} else
					addItem(constant, key, null);
			}
		}
		return this;
	}

	public CheckGroupBox setItems(KeyValueDataLoader comboDataLoader, Enum key) {
		clear();
		comboDataLoader.add(this, key);
		return this;
	}

	@Override
	public void clear() {
		super.clear();
		if (items != null)
			for (CheckBox ch : items)
				ch.removeFromParent();
		items = null;
	}

	@Override
	public int getTabIndex() {
		return 0;
	}

	@Override
	public void setAccessKey(char key) {
	}

	@Override
	public void setFocus(boolean focused) {
		if (items != null && items.size() > 0)
			items.get(0).setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
	}

	@Override
	public void setKeyValueDataItems(JSONObject itm) {
		clear();
		if (itm != null) {
			for (String lb : itm.keySet()) {
				JSONArray arr = itm.get(lb).isArray();//0-Value,1-Data,2-Path
				addItem(lb, JsUtil.asString(arr.get(0)), JsUtil.asString(arr.get(1)));
			}
		}
		if (lazyValue != null)
			setValue(lazyValue);
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
	public boolean validate(final boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	@Override
	public String getValue() {
		StringBuilder sb = new StringBuilder();
		if (items != null)
			for (CheckBox ch : items)
				if (ch.getValue())
					sb.append(sb.length() == 0 ? "" : seperator).append(ch.getElement().getAttribute("v"));
		return sb.length() == 0 ? null : sb.toString();
	}

	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		lazyValue = null;
		if (items == null || items.isEmpty()) {
			lazyValue = value;
			return;
		}
		String t = seperator + value + seperator;
		for (CheckBox ch : items) {
			String v = seperator + ch.getElement().getAttribute("v") + seperator;
			ch.setValue(t.indexOf(v) > -1);
		}
	}

	public void addSelection(String value) {
		for (CheckBox ch : items) {
			if (ch.getElement().getAttribute("v").equals(value)) {
				ch.setValue(true);
				break;
			}
		}
	}

	public void removeSelection(String value) {
		for (CheckBox ch : items) {
			if (ch.getElement().getAttribute("v").equals(value)) {
				ch.setValue(false);
				break;
			}
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (items == null)
			return;
		for (CheckBox ch : items)
			ch.setEnabled(enabled);
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
		setTitle(validationError);
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

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;
			addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					ValueChangeEvent.fire(CheckGroupBox.this, getValue());
				}
			});
		}
		return addHandler(handler, ValueChangeEvent.getType());
	}

}