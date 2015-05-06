package com.javexpress.gwt.library.ui.form.filterpanel;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.application.model.item.ControlType;
import com.javexpress.application.model.item.CriteriaParameterOperator;
import com.javexpress.application.model.item.DatasetFilterDescriptor;
import com.javexpress.application.model.item.IFilterContainer;
import com.javexpress.application.model.item.ReportFilterValues;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.container.layout.GridLayout;
import com.javexpress.gwt.library.ui.form.IFormFactory;
import com.javexpress.gwt.library.ui.form.label.Label;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class AbstractFilterPanel extends AbstractContainer implements Focusable, KeyDownHandler, IFilterContainer {

	private GridLayout								filterTable;
	protected Element								filtersDiv;
	private Map<String, Boolean>					hiddenFilterNames;
	private IFilterPanelListener					listener;
	private JsonMap									values;
	private boolean									hasMandatoryFilters;
	private boolean									hasVisibleMandatoryFilters;
	private BaseFilterLabelAndControlDataResolver	filterLabelAndControlDataResolver;

	public BaseFilterLabelAndControlDataResolver getFilterLabelAndControlDataResolver() {
		return filterLabelAndControlDataResolver;
	}

	public void setFilterLabelAndControlDataResolver(BaseFilterLabelAndControlDataResolver filterLabelAndControlDataResolver) {
		this.filterLabelAndControlDataResolver = filterLabelAndControlDataResolver;
	}

	public IFilterPanelListener getListener() {
		return listener;
	}

	public void setListener(final IFilterPanelListener listener) {
		this.listener = listener;
	}

	public AbstractFilterPanel(Element el, final Widget parent, final String id) {
		super(el);
		JsUtil.ensureId(parent, this, WidgetConst.FILTERPANEL_PREFIX, id);
		filtersDiv = DOM.createDiv().cast();
		getElement().appendChild(filtersDiv);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (filterTable != null)
			add(filterTable, filtersDiv);
	}

	public void setFocus() {
		if (filterTable == null)
			return;
		Widget w = filterTable.getWidget(0, 1);
		if (w != null && w instanceof Focusable)
			((Focusable) w).setFocus(true);
	}

	public void setFilters(final List<DatasetFilterDescriptor> filters) throws Exception {
		setFilters(filters, null);
	}

	public void setFilters(final List<DatasetFilterDescriptor> filters, String group) throws Exception {
		hasMandatoryFilters = false;
		hasVisibleMandatoryFilters = false;
		hiddenFilterNames = null;
		int sz = 0;
		for (int i = 0; i < filters.size(); i++) {
			DatasetFilterDescriptor fd = filters.get(i);
			if (isFilterGroupAccepted(fd, group) && !fd.isHidden())
				sz++;
		}
		if (filterTable != null) {
			orphan(filterTable);
			filterTable = null;
		}
		filterTable = new GridLayout(sz, 3, true);
		sz = 0;
		for (int r = 0; r < filters.size(); r++) {
			DatasetFilterDescriptor fd = filters.get(r);
			if (isFilterGroupAccepted(fd, group))
				addFilter(fd.isHidden() ? -1 : sz++, fd);
		}
		filtersDiv.appendChild(filterTable.getElement());
		if (isAttached())
			adopt(filterTable);
	}

	private boolean isFilterGroupAccepted(DatasetFilterDescriptor fd, String group) {
		boolean accept = JsUtil.isEmpty(group) && JsUtil.isEmpty(fd.getGroup());
		if (!accept && JsUtil.isNotEmpty(group) && JsUtil.isNotEmpty(fd.getGroup()))
			accept = ("," + fd.getGroup() + ",").indexOf("," + group + ",") > -1;
		return accept;
	}

	@Override
	public void addFilter(DatasetFilterDescriptor fd) throws Exception {
		if (fd.isHidden()) {
			addFilter(-1, fd);
		}
		int row = 0;
		if (filterTable == null) {
			filterTable = new GridLayout(1, 3, true);
			filtersDiv.appendChild(filterTable.getElement());
			if (isAttached())
				adopt(filterTable);
		} else {
			filterTable.resizeRows((row = filterTable.getRowCount()) + 1);
		}
		addFilter(row, fd);
	}

	private Widget[] addFilter(int row, DatasetFilterDescriptor fd) throws Exception {
		if (fd.isHidden()) {
			if (hiddenFilterNames == null)
				hiddenFilterNames = new HashMap<String, Boolean>();
			hiddenFilterNames.put(fd.getField(), fd.isNullable());
			if (!fd.isNullable())
				hasMandatoryFilters = true;
			return null;
		}
		if (fd.getControlType() == null)
			throw new Exception("Filter Control Type is not defined.(Name:" + fd.getField() + ")");
		String labelStr = fd.getLabel();
		if (labelStr.startsWith("@")) {
			try {
				labelStr = filterLabelAndControlDataResolver.handleFilterLabelNls(labelStr.substring(1));
			} catch (Exception e) {
				labelStr = e.getMessage();
			}
		}
		Label l = new Label(labelStr + (fd.isNullable() ? " :" : " *:"));
		if (!fd.isNullable()) {
			l.getElement().setAttribute("mandatory", "true");
			hasMandatoryFilters = true;
			hasVisibleMandatoryFilters = true;
		}
		filterTable.setWidget(row, 0, l, true, !fd.isNullable());
		String defVal = fd.getDefaultValue();
		boolean bwDefVal = JsUtil.isNotEmpty(defVal) && fd.getCriteriaOperator() == CriteriaParameterOperator.Between && defVal.indexOf(",") > -1;
		if (bwDefVal)
			defVal = defVal.split(",")[0];
		Widget widgetFirst = JsUtil.createControl(this, fd.getControlType(), fd.getField(), fd.getControlData(), fd.isNullable(), defVal, true);
		if (widgetFirst instanceof HasAllKeyHandlers) {
			((HasAllKeyHandlers) widgetFirst).addKeyDownHandler(this);
		}
		if (filterLabelAndControlDataResolver != null && fd.getControlData() != null)
			filterLabelAndControlDataResolver.handleFilterControlData(widgetFirst, fd);
		if (fd.getControlType() != ControlType.Date)
			widgetFirst.setWidth("10em");
		if (fd.getCriteriaOperator() == CriteriaParameterOperator.Between) {
			if (bwDefVal)
				defVal = fd.getDefaultValue().split(",")[1];
			else
				defVal = null;
			Widget widgetLast = JsUtil.createControl(this, fd.getControlType(), fd.getField() + "_end", fd.getControlData(), fd.isNullable(), defVal, true);
			if (filterLabelAndControlDataResolver != null && fd.getControlData() != null)
				filterLabelAndControlDataResolver.handleFilterControlData(widgetLast, fd);
			if (fd.getControlType() != ControlType.Date)
				widgetLast.setWidth("10em");
			HorizontalPanel fp = new HorizontalPanel();
			fp.add(widgetFirst);
			fp.add(widgetLast);
			filterTable.setWidget(row, 1, fp);
			return new Widget[] { widgetFirst, widgetLast };
		} else {
			filterTable.setWidget(row, 1, widgetFirst);
			return new Widget[] { widgetFirst };
		}
	}

	public void addDateFilter(String label, String field, boolean isRange) throws Exception {
		DatasetFilterDescriptor fd = new DatasetFilterDescriptor(field);
		fd.setLabel(label);
		fd.setControlType(ControlType.Date);
		fd.setCriteriaOperator(isRange ? CriteriaParameterOperator.Between : CriteriaParameterOperator.Equal);//just to make range. query wont be affected
		addFilter(fd);
	}

	public int getRowCount() {
		return filterTable == null ? 0 : filterTable.getRowCount();
	}

	public Widget getFilterWidget(final int row, final int column) {
		Widget w = filterTable == null ? null : filterTable.getWidget(row, column > 1 ? 1 : column);
		if (w == null)
			return null;
		if (w instanceof HorizontalPanel)
			return ((HorizontalPanel) w).getWidget(column - 1);
		return w;
	}

	public void setEnabled(String filterName, boolean enabled) {
		for (int r = 0; r < getRowCount(); r++) {
			Widget s = getFilterWidget(r, 1);
			if (s != null && filterName.equals(s.getElement().getAttribute("property"))) {
				JsUtil.setWidgetEnabled(s, enabled);
			}
		}
	}

	public void setFilterValue(String filterName, Serializable value) {
		for (int r = 0; r < getRowCount(); r++) {
			Widget s = getFilterWidget(r, 1);
			if (s != null && filterName.equals(s.getElement().getAttribute("property"))) {
				JsUtil.setWidgetValue(s, value);
			}
		}
	}

	public Serializable getFilterValue(String filterName) throws ParseException {
		return getFilterValue(filterName, false);
	}

	public Serializable getFilterValue(String filterName, boolean end) throws ParseException {
		for (int r = 0; r < getRowCount(); r++) {
			Widget s = getFilterWidget(r, end ? 2 : 1);
			if (s != null && filterName.equals(s.getElement().getAttribute("property"))) {
				return JsUtil.getWidgetValue(s);
			}
		}
		return null;
	}

	private boolean fillFilterValues(final JsonMap filters) throws ParseException {
		boolean mandFilled = true;
		for (int r = 0; r < getRowCount(); r++) {
			Label l = (Label) getFilterWidget(r, 0);
			boolean required = "true".equals(l.getElement().getAttribute("mandatory"));
			Widget s = getFilterWidget(r, 1);
			Widget e = getFilterWidget(r, 2);
			if (e != null && s != e) {
				JSONArray arr = new JSONArray();
				Serializable val = JsUtil.getWidgetValue(s);
				if (val != null)
					if (val instanceof Date)
						arr.set(0, new JSONString(JsUtil.asString((Date) val)));
					else if (val instanceof List)
						arr.set(1, JsUtil.asJsonArray((List) val));
					else
						arr.set(0, new JSONString(JsUtil.asString(val)));
				val = JsUtil.getWidgetValue(e);
				if (val != null)
					if (val instanceof Date)
						arr.set(1, new JSONString(JsUtil.asString((Date) val)));
					else if (val instanceof List)
						arr.set(1, JsUtil.asJsonArray((List) val));
					else
						arr.set(1, new JSONString(JsUtil.asString(val)));
				if (arr.size() > 0)
					filters.put(s.getElement().getAttribute("property"), arr);
				else if (required)
					mandFilled = false;
			} else {
				Serializable val = JsUtil.getWidgetValue(s);
				if (val != null) {
					if (val instanceof Date)
						filters.put(s.getElement().getAttribute("property"), new JSONString(JsUtil.asString((Date) val)));
					else if (val instanceof Boolean)
						filters.put(s.getElement().getAttribute("property"), JSONBoolean.getInstance((Boolean) val));
					else if (val instanceof String) {
						if (JsUtil.isNotEmpty(((String) val)))
							filters.set(s.getElement().getAttribute("property"), val.toString());
					} else if (val instanceof List)
						filters.put(s.getElement().getAttribute("property"), JsUtil.asJsonArray((List) val));
					else
						filters.set(s.getElement().getAttribute("property"), val.toString());
				} else if (required)
					mandFilled = false;
			}
		}
		if (hiddenFilterNames != null) {
			for (String field : hiddenFilterNames.keySet()) {
				Boolean nullable = hiddenFilterNames.get(field);
				Serializable val = listener.getHiddenFilterValue(field);
				if (val != null)
					if (val instanceof Date)
						filters.put(field, new JSONString(JsUtil.asString((Date) val)));
					else if (val instanceof Boolean)
						filters.put(field, JSONBoolean.getInstance((Boolean) val));
					else
						filters.set(field, val.toString());
				else if (!nullable)
					mandFilled = false;
			}
		}
		return mandFilled;
	}

	public ReportFilterValues getFilterValuesAsMap() throws ParseException {
		ReportFilterValues filters = new ReportFilterValues();
		for (int r = 0; r < getRowCount(); r++) {
			Widget s = getFilterWidget(r, 1);
			Widget e = getFilterWidget(r, 2);
			if (e != null) {
			} else {
				Serializable val = JsUtil.getWidgetValue(s);
				if (val != null)
					filters.put(s.getElement().getAttribute("property"), val, JsUtil.getWidgetText(s));
			}
		}
		return filters;
	}

	protected void applyFilters(final boolean invokeListeners) {
		JsonMap vals = new JsonMap();
		try {
			boolean mandFilled = fillFilterValues(vals);
			if (mandFilled) {
				values = vals;
				if (listener != null && invokeListeners)
					listener.applyFilters();
			} else
				JsUtil.message(this, IFormFactory.nlsCommon.filtreleriDoldurun());
		} catch (ParseException e) {
			JsUtil.handleError(this, e);
		}
	}

	protected void clearFilters(final boolean invokeListeners) throws ParseException {
		for (int r = 0; r < getRowCount(); r++) {
			Widget s = getFilterWidget(r, 1);
			if (s != null)
				JsUtil.restoreWidgetValue(s);
			Widget e = getFilterWidget(r, 2);
			if (e != null)
				JsUtil.restoreWidgetValue(e);
		}
		applyFilters(invokeListeners);
	}

	public JsonMap getFilterValues() {
		if (values == null)
			applyFilters(false);
		return values;
	}

	public boolean hasMandatoryFilters() {
		return hasMandatoryFilters;
	}

	public boolean hasVisibleMandatoryFilters() {
		return hasVisibleMandatoryFilters;
	}

	public boolean isNeedToShow() {
		return filterTable != null && filterTable.getRowCount() > 0;
	}

	@Override
	protected void onUnload() {
		if (filterTable != null)
			remove(filterTable);
		filterTable = null;
		filtersDiv = null;
		hiddenFilterNames = null;
		listener = null;
		values = null;
		filterLabelAndControlDataResolver.onUnload();
		filterLabelAndControlDataResolver = null;
		super.onUnload();
	}

	public void setMargin(int i) {
		getElement().getStyle().setMargin(i, Unit.PX);
	}

	public Widget[] getWidgetsOfField(String field) {
		for (int r = 0; r < getRowCount(); r++) {
			Widget s = getFilterWidget(r, 1);
			if (s.getElement().getAttribute("property").equals(field))
				return new Widget[] { s, getFilterWidget(r, 2) };
		}
		return null;
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
		for (int r = 0; r < getRowCount(); r++) {
			Widget w = getFilterWidget(r, 1);
			if (w instanceof Focusable) {
				((Focusable) w).setFocus(focused);
				break;
			}
		}
	}

	@Override
	public void setTabIndex(int index) {
		getElement().setTabIndex(index);
	}

	public void applyTo(JsonMap requestData) {
		JsonMap filters = null;
		filters = getFilterValues();
		if (filters != null && filters.size() > 0)
			requestData.set("filters", filters);
		else
			requestData.clear("filters");
	}

	@Override
	public void onKeyDown(KeyDownEvent event) {
		if (event.getNativeKeyCode() == 13) {
			event.preventDefault();
			event.stopPropagation();
			applyFilters(true);
		}
	}

}