package com.javexpress.gwt.library.ui.bootstrap;

import java.io.Serializable;
import java.text.ParseException;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.application.model.item.ControlType;
import com.javexpress.application.model.item.CriteriaParameterOperator;
import com.javexpress.application.model.item.DatasetFilterDescriptor;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class FilterPanelItem extends AbstractContainer {

	private DatasetFilterDescriptor	descriptor;
	private FilterPanel				filterPanel;
	private Widget					widgetFirst;
	private Widget					widgetLast;
	private Element					label;
	private boolean					levelShowing;

	public boolean isLevelShowing() {
		return levelShowing;
	}

	public void setLevelShowing(boolean levelShowing) {
		this.levelShowing = levelShowing;
	}

	public FilterPanelItem(FilterPanel filterPanel, DatasetFilterDescriptor descriptor) throws Exception {
		super(DOM.createDiv());
		if (descriptor.getControlType() == null)
			throw new Exception("Filter Control Type is not defined.(Name:" + descriptor.getField() + ")");
		JsUtil.ensureSubId(filterPanel.getElement(), getElement(), WidgetConst.FILTERPANELITEM_PREFIX);
		this.descriptor = descriptor;
		this.filterPanel = filterPanel;
		setStyleName("jexpFilterPanelItem");
	}

	@Override
	protected void onLoad() {
		String labelStr = descriptor.getLabel();
		if (labelStr.startsWith("@")) {
			try {
				labelStr = filterPanel.getFilterLabelAndControlDataResolver().handleFilterLabelNls(labelStr.substring(1));
			} catch (Exception e) {
				labelStr = e.getMessage();
			}
		}
		label = DOM.createSpan();
		label.setInnerHTML(labelStr);
		label.setTitle(labelStr);
		if (descriptor.isNullable())
			label.setClassName("jexpFilterLabel jexp-ui-field-required");
		else {
			label.setClassName("jexpFilterLabel");
			label.setAttribute("mandatory", "true");
			filterPanel.setHasMandatoryFilters(true);
			filterPanel.setHasVisibleMandatoryFilters(true);
		}
		getElement().appendChild(label);

		Element div = DOM.createDiv();
		div.setClassName("jexpControlContainer form-inline");
		getElement().appendChild(div);

		String defVal = descriptor.getDefaultValue();
		boolean bwDefVal = JsUtil.isNotEmpty(defVal) && descriptor.getCriteriaOperator() == CriteriaParameterOperator.Between && defVal.indexOf(",") > -1;
		if (bwDefVal)
			defVal = defVal.split(",")[0];
		widgetFirst = JsUtil.createControl(this, descriptor.getControlType(), descriptor.getField(), descriptor.getControlData(), descriptor.isNullable(), defVal, true);
		if (widgetFirst instanceof HasAllKeyHandlers)
			((HasAllKeyHandlers) widgetFirst).addKeyDownHandler(filterPanel);
		if (widgetFirst instanceof HasChangeHandlers)
			((HasChangeHandlers) widgetFirst).addChangeHandler(filterPanel);
		if (filterPanel.getFilterLabelAndControlDataResolver() != null && descriptor.getControlData() != null)
			filterPanel.getFilterLabelAndControlDataResolver().handleFilterControlData(widgetFirst, descriptor);
		add(widgetFirst, div);
		if (descriptor.getCriteriaOperator() == CriteriaParameterOperator.Between) {
			if (bwDefVal)
				defVal = descriptor.getDefaultValue().split(",")[1];
			else
				defVal = null;
			widgetLast = JsUtil.createControl(this, descriptor.getControlType(), descriptor.getField() + "_end", descriptor.getControlData(), descriptor.isNullable(), defVal, true);
			if (widgetLast instanceof HasAllKeyHandlers)
				((HasAllKeyHandlers) widgetLast).addKeyDownHandler(filterPanel);
			if (widgetLast instanceof HasChangeHandlers)
				((HasChangeHandlers) widgetLast).addChangeHandler(filterPanel);
			if (filterPanel.getFilterLabelAndControlDataResolver() != null && descriptor.getControlData() != null)
				filterPanel.getFilterLabelAndControlDataResolver().handleFilterControlData(widgetLast, descriptor);
			if (descriptor.getControlType() != ControlType.Date)
				widgetLast.setWidth("10em");
			label.addClassName("col-xs-5");
			div.addClassName("col-xs-7");
			widgetFirst.addStyleName("col-xs-6");
			widgetLast.addStyleName("col-xs-6");
			add(widgetLast, div);
		} else {
			label.addClassName("col-xs-5");
			div.addClassName("col-xs-7");
			widgetFirst.addStyleName("col-xs-12");
		}
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		descriptor = null;
		filterPanel = null;
		widgetFirst = null;
		widgetLast = null;
		super.onUnload();
	}

	public String getFilterName() {
		return descriptor.getField();
	}

	public void setEnabled(boolean enabled) {
		if (!enabled) {
			widgetFirst.getElement().setPropertyBoolean("disabled", !enabled);
			if (widgetLast != null)
				widgetLast.getElement().setPropertyBoolean("disabled", !enabled);
		} else {
			widgetFirst.getElement().removeAttribute("disabled");
			if (widgetLast != null)
				widgetLast.getElement().removeAttribute("disabled");
		}
	}

	public void setValue(Serializable value) {
		JsUtil.setWidgetValue(widgetFirst, value);
	}

	public void setValueEnd(Serializable value) {
		if (widgetLast != null)
			JsUtil.setWidgetValue(widgetLast, value);
	}

	public Serializable[] getValues() throws ParseException {
		return new Serializable[] { levelShowing ? JsUtil.getWidgetValue(widgetFirst, descriptor.getControlType() == ControlType.ComboNumber) : null,
				levelShowing ? (widgetLast != null ? JsUtil.getWidgetValue(widgetLast, descriptor.getControlType() == ControlType.ComboNumber) : null) : null };
	}

	public boolean isNullable() {
		return descriptor.isNullable();
	}

	public void setFocus(boolean focused) {
		((Focusable) widgetFirst).setFocus(focused);
	}

	public String getFilterLabel() {
		return label.getInnerHTML();
	}

	public void resetValues() throws ParseException {
		JsUtil.restoreWidgetValue(widgetFirst);
		if (widgetLast != null)
			JsUtil.restoreWidgetValue(widgetLast);
	}

	public DatasetFilterDescriptor getDescriptor() {
		return descriptor;
	}

}