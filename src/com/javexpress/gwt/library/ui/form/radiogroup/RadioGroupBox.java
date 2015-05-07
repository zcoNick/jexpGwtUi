package com.javexpress.gwt.library.ui.form.radiogroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class RadioGroupBox extends FlexTable implements Focusable {

	private final int			mod;
	private List<RadioButton>	radios	= new ArrayList<RadioButton>();

	public RadioGroupBox(Widget parent, final String id, final int mod) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.RADIOGROUP_PREFIX, id);
		setStyleName("ui-widget ui-widget-content ui-corner-all jexpBorderBox");
		this.mod = mod;
	}

	public void addItem(final Serializable value, final Serializable label) {
		RadioButton rb = new RadioButton(getElement().getId(), label.toString());
		rb.setFormValue(JsUtil.asString(value));
		addItem(rb);
	}

	public void addItem(RadioButton rb) {
		if (radios == null)
			radios = new ArrayList<RadioButton>();
		int row = getRowCount();
		int col = row == 0 ? 0 : getCellCount(row - 1);
		if (row == 0)
			row = 1;
		if (col == mod) {
			col = 0;
		} else
			row -= 1;
		setWidget(row, col, rb);
		radios.add(rb);
	}

	private RadioButton getSelectedRadio() {
		for (RadioButton rb : radios)
			if (rb.getValue())
				return rb;
		return null;
	}

	public String getValue() {
		RadioButton sel = getSelectedRadio();
		if (sel == null)
			return null;
		if (JsUtil.isEmpty(sel.getFormValue()))
			return null;
		return sel.getFormValue();
	}

	public Integer getValueInt() {
		String val = getValue();
		if (val == null)
			return null;
		return Integer.parseInt(val);
	}

	public Long getValueLong() {
		String val = getValue();
		if (val == null)
			return null;
		return Long.parseLong(val);
	}

	public void setValueInt(final Integer selectedValue) {
		if (selectedValue == null) {
			setValue(null);
			return;
		}
		setValue(String.valueOf(selectedValue));
	}

	public void setValueLong(final Long selectedValue) {
		if (selectedValue == null) {
			setValue(null);
			return;
		}
		setValue(String.valueOf(selectedValue));
	}

	public void setValue(final String selectedValue) {
		for (RadioButton rb : radios)
			rb.setValue(selectedValue != null && selectedValue.equals(rb.getFormValue()));
	}

	@Override
	protected void onUnload() {
		radios = null;
		super.onUnload();
	}

	public RadioGroupBox setItems(final Map<? extends Serializable, ? extends Serializable> map) {
		return setItems(map, null);
	}

	public RadioGroupBox setItems(final Map<? extends Serializable, ? extends Serializable> map, ConstantsWithLookup nls) {
		clear();
		for (Serializable key : map.keySet()) {
			if (nls == null)
				addItem(key, map.get(key));
			else {
				String constant = map.get(key).toString();
				if (constant.startsWith("@")) {
					try {
						String nlsValue = nls.getString(constant.substring(1));
						addItem(key, nlsValue);
					} catch (Exception ex) {
						addItem(key, constant);
					}
				} else
					addItem(key, constant);
			}
		}
		return this;
	}

	@Override
	public int getTabIndex() {
		return getElement().getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
	}

	@Override
	public void setFocus(boolean focused) {
		Widget w = getWidget(0, 0);
		if (w != null && w instanceof Focusable)
			((Focusable) w).setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		getElement().setTabIndex(index);
	}

}