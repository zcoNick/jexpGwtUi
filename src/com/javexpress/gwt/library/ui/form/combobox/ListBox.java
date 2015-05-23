package com.javexpress.gwt.library.ui.form.combobox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.LabelControlCell;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ListBox extends ListBoxBase {

	private List<String>		lazyValues;
	private DataBindingHandler	dataBinding;

	/** Designer compatible constructor */
	public ListBox(final Widget parent, final String id) {
		this(parent, id, "jexpListBox");
	}

	protected ListBox(final Widget parent, final String id, String className) {
		super(true);
		getElement().addClassName(className);
		JsUtil.ensureId(parent, this, WidgetConst.LISTBOX_PREFIX, id);
	}

	@Override
	public void addItem(Serializable label, Serializable value, Serializable data) {
		super.addItem(label, value, data);
		if (lazyValues != null && lazyValues.contains(value.toString())) {
			setSelectedIndex(getItemCount() - 1);
			lazyValues.remove(value.toString());
			NativeEvent event = Document.get().createChangeEvent();
			DomEvent.fireNativeEvent(event, this);
		}
	}

	@Override
	public String getValue() {
		return ListBoxBase.getSelectedOptionValues(getElement());
	}

	public boolean setValueList(ArrayList<String> values) {
		setSelectedIndex(-1);
		if (values == null || values.isEmpty()) {
			return false;
		}
		lazyValues = new ArrayList<String>(values);
		SelectElement se = getElement().cast();
		NodeList<OptionElement> items = se.getOptions();
		for (int i = 0; i < items.getLength(); i++) {
			String v = getValue(i);
			if (lazyValues.contains(v)) {
				items.getItem(i).setSelected(true);
				lazyValues.remove(v);
			}
		}
		if (lazyValues.isEmpty())
			lazyValues = null;
		return true;
	}

	public boolean setValueArray(String[] values) {
		lazyValues = new ArrayList<String>();
		setSelectedIndex(-1);
		if (values == null || values.length == 0) {
			return false;
		}
		for (String v : values)
			lazyValues.add(v);
		SelectElement se = getElement().cast();
		NodeList<OptionElement> items = se.getOptions();
		for (int i = 0; i < items.getLength(); i++) {
			String v = getValue(i);
			if (lazyValues.contains(v)) {
				items.getItem(i).setSelected(true);
				lazyValues.remove(v);
			}
		}
		if (lazyValues.isEmpty())
			lazyValues = null;
		return true;
	}

	@Override
	protected void onUnload() {
		lazyValues = null;
		dataBinding = null;
		super.onUnload();
	}

	public void setAltRows(boolean altRows) {
		if (altRows)
			getElement().addClassName("jesAltRows");
		else
			getElement().removeClassName("jesAltRows");
	}

	public List<String> getValues() {
		List<String> vals = new ArrayList<String>();
		for (int i = 0; i < getItemCount(); i++)
			if (isItemSelected(i))
				vals.add(getValue(i));
		return vals.isEmpty() ? null : vals;
	}

	public Map<String, String> getSelectedOptions() {
		Map<String, String> vals = new LinkedHashMap<String, String>();
		for (int i = 0; i < getItemCount(); i++)
			if (isItemSelected(i))
				vals.put(getValue(i), getItemText(i));
		return vals.isEmpty() ? null : vals;
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

}