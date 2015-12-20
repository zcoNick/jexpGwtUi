package com.javexpress.gwt.library.ui.bootstrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.combobox.ListBoxBase;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class MultiSelectBox extends ListBoxBase {

	public static WidgetBundles fillResources(WidgetBundles parent) {
		WidgetBundles jexp = new WidgetBundles("Bootstrap MultiSelect", parent);
		jexp.addStyleSheet("scripts/multiselect/bootstrap-multiselect.min.css");
		jexp.addJavaScript("scripts/multiselect/bootstrap-multiselect.min.js");
		return jexp;
	}

	private List<String>		lazyValues;
	private JsonMap				options;
	private DataBindingHandler	dataBinding;

	public MultiSelectBox(Widget parent, String id) {
		super(true);
		setUseEmptyItem(false);
		JsUtil.ensureId(parent, this, WidgetConst.CHECKCOMBOBOX_PREFIX, id);
		options = createDefaultOptions();
	}

	protected static JsonMap createDefaultOptions() {
		JsonMap options = new JsonMap();
		if (JsUtil.isRTL())
			options.set("dir", "rtl");
		options.set("enableClickableOptGroups", true);
		options.setInt("maxHeight", 200);
		options.set("nonSelectedText", " ");
		options.set("allSelectedText", ClientContext.nlsCommon.tumu());
		options.setInt("numberDisplayed", 3);
		options.set("includeSelectAllOption", true);
		options.set("selectAllText", ClientContext.nlsCommon.tumunuSec());
		options.set("enableFiltering", false);
		options.set("enableCaseInsensitiveFiltering", false);
		return options;
	}

	public void setMaxHeight(int maxHeight) {
		options.setInt("maxHeight", 200);
	}

	public void setEmptyText(String emptyText) {
		options.set("nonSelectedText", emptyText);
	}

	public void setSelectAllText(String allText) {
		options.set("selectAllText", allText);
	}

	public void setIncludeSelectAll(boolean value) {
		options.set("includeSelectAllOption", value);
	}

	public void setEnableFiltering(boolean value) {
		options.set("enableFiltering", value);
	}

	public void setNumberDisplayed(int value) {
		options.setInt("numberDisplayed", value);
	}

	public JsonMap getOptions() {
		return options;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement(), options.getJavaScriptObject(), ClientContext.nlsCommon.secili());
	}

	private native void createByJs(MultiSelectBox x, Element el, JavaScriptObject options, String selText) /*-{
		options.onChange=function(option, checked, select) {
			x.@com.javexpress.gwt.library.ui.bootstrap.MultiSelectBox::fireOnChanged(Ljava/lang/String;Z)($wnd.$(option).val(),checked);
		};
		options.onDropdownShown=function(event) {
			x.@com.javexpress.gwt.library.ui.bootstrap.MultiSelectBox::fireOnOpened()();
		};
		options.onDropdownHidden=function(event) {
			x.@com.javexpress.gwt.library.ui.bootstrap.MultiSelectBox::fireOnClosed()();
		};
		options.buttonText=function(seloptions, select) {
			if (seloptions.length === 0) {
				return options.nonSelectedText;
			} else if (seloptions.length > 3) {
				return seloptions.length+' '+selText;
			} else {
				var labels = [];
				seloptions.each(function() {
					if ($wnd.$(this).attr('label') !== undefined)
						labels.push($wnd.$(this).attr('label'));
					else
						labels.push($wnd.$(this).html());
				});
				return labels.join(', ') + '';
			}
		}
		$wnd.$(el).multiselect(options);
	}-*/;

	@Override
	public void addItem(Serializable label, Serializable value, Serializable data, String hint) {
		super.addItem(label, value, data, hint);
		if (lazyValues != null && lazyValues.contains(value.toString())) {
			setSelectedIndex(getItemCount() - 1);
			lazyValues.remove(value.toString());
			ValueChangeEvent.fireIfNotEqual(this, null, value.toString());
		}
	}

	@Override
	protected void onItemListChanged() {
		super.onItemListChanged();
		rebuild(getElement());
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
		syncSelections(getElement());
		return true;
	}

	private native void rebuild(Element el) /*-{
		$wnd.$(el).multiselect('rebuild');
	}-*/;

	private native void syncSelections(Element el) /*-{
		$wnd.$(el).multiselect('refresh');
	}-*/;

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

	public void setAllowClear(boolean allowClear) {
		getOptions().set("allowClear", allowClear);
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
	public void setValidationError(String validationError) {
		if (JsUtil.USE_BOOTSTRAP) {
			Widget nw = getParent() instanceof FormGroupCell ? getParent() : this;
			if (validationError == null)
				nw.removeStyleName("has-error");
			else
				nw.addStyleName("has-error");
		}
		setTitle(validationError);
	}

	@Override
	protected void onUnload() {
		lazyValues = null;
		options = null;
		dataBinding = null;
		super.onUnload();
	}

	//*EVENTS
	private void fireOnOpened() {
	}

	private void fireOnChanged(String val, boolean checked) {
		ValueChangeEvent.fireIfNotEqual(this, null, val);
	}

	private void fireOnClosed() {
	}

	@Override
	public void setValue(String value) {
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
	}

}