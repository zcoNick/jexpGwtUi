package com.javexpress.gwt.library.ui.bootstrap;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.application.model.item.ControlType;
import com.javexpress.application.model.item.CriteriaParameterOperator;
import com.javexpress.application.model.item.DatasetFilterDescriptor;
import com.javexpress.application.model.item.IFilterContainer;
import com.javexpress.application.model.item.ReportFilterValues;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.form.IFormFactory;
import com.javexpress.gwt.library.ui.form.filterpanel.BaseFilterLabelAndControlDataResolver;
import com.javexpress.gwt.library.ui.form.filterpanel.IFilterPanelListener;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class FilterPanel extends WidgetBox implements Focusable, KeyDownHandler, IFilterContainer, ChangeHandler {

	private List<DatasetFilterDescriptor>			descriptors					= new ArrayList<DatasetFilterDescriptor>();
	private String									group;
	private int										level						= 0;
	private Map<String, Boolean>					hiddenFilterNames			= null;
	private IFilterPanelListener					filterListener;
	private JsonMap									values;
	private boolean									hasMandatoryFilters			= false;
	private boolean									hasVisibleMandatoryFilters	= false;
	private BaseFilterLabelAndControlDataResolver	filterLabelAndControlDataResolver;
	private boolean									listenersEnabled			= false;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public BaseFilterLabelAndControlDataResolver getFilterLabelAndControlDataResolver() {
		return filterLabelAndControlDataResolver;
	}

	public void setFilterLabelAndControlDataResolver(BaseFilterLabelAndControlDataResolver filterLabelAndControlDataResolver) {
		this.filterLabelAndControlDataResolver = filterLabelAndControlDataResolver;
	}

	public IFilterPanelListener getFilterListener() {
		return filterListener;
	}

	public void setFilterListener(final IFilterPanelListener listener) {
		this.filterListener = listener;
	}

	public void setHasMandatoryFilters(boolean hasMandatoryFilters) {
		this.hasMandatoryFilters = hasMandatoryFilters;
	}

	public void setHasVisibleMandatoryFilters(boolean hasVisibleMandatoryFilters) {
		this.hasVisibleMandatoryFilters = hasVisibleMandatoryFilters;
	}

	public FilterPanel(final Widget parent, final String id) {
		super(parent, id, true, true, true);
		JsUtil.ensureId(parent, this, WidgetConst.FILTERPANEL_PREFIX, id);
		getElement().addClassName("jexpFilterPanel");
		//setIcon(FaIcon.filter);
		setHeader(IFormFactory.nlsCommon.filtreler());
		headerEl.addClassName("grey");
		addToolItem(FaIcon.eraser, IFormFactory.nlsCommon.temizle(), new Command() {
			@Override
			public void execute() {
				try {
					clearFilters(true);
				} catch (Exception e) {
					JsUtil.handleError(getParent(), e);
				}
			}
		});
		addToolItem(FaIcon.plus, IFormFactory.nlsCommon.fazlasi(), new Command() {
			@Override
			public void execute() {
				level++;
				boolean changed = false;
				for (int r = 0; r < getWidgetCount(); r++)
					if (getWidget(r) instanceof FilterPanelItem) {
						FilterPanelItem fpi = (FilterPanelItem) getWidget(r);
						if (fpi.getDescriptor().getLevel() == level) {
							changed = true;
							fpi.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
							fpi.setLevelShowing(true);
						}
					}
				if (!changed)
					level--;
			}
		});
		addToolItem(FaIcon.minus, IFormFactory.nlsCommon.azi(), new Command() {
			@Override
			public void execute() {
				if (level == 0)
					return;
				boolean changed = false;
				for (int r = 0; r < getWidgetCount(); r++)
					if (getWidget(r) instanceof FilterPanelItem) {
						FilterPanelItem fpi = (FilterPanelItem) getWidget(r);
						if (fpi.getDescriptor().getLevel() == level) {
							changed = true;
							fpi.getElement().getStyle().setDisplay(Display.NONE);
							fpi.setLevelShowing(false);
						}
					}
				if (changed)
					level--;
			}
		});
		contentDiv.getStyle().setOverflow(Overflow.AUTO);
	}

	@Override
	protected void onLoad() {
		int xsRemain = 12;
		int smRemain = 12;
		int mdRemain = 12;
		for (DatasetFilterDescriptor dfd : descriptors)
			try {
				if (!isFilterGroupAccepted(dfd))
					continue;
				if (dfd.isHidden()) {
					if (hiddenFilterNames == null)
						hiddenFilterNames = new HashMap<String, Boolean>();
					hiddenFilterNames.put(dfd.getField(), dfd.isNullable());
					if (!dfd.isNullable())
						hasMandatoryFilters = true;
					continue;
				}
				FilterPanelItem fpi = new FilterPanelItem(this, dfd);
				if (dfd.getCriteriaOperator() == CriteriaParameterOperator.Between) {
				}
				fpi.getElement().addClassName("col-xs-12 col-sm-6 col-md-4");
				if (!isFilterLevelAccepted(dfd))
					fpi.getElement().getStyle().setDisplay(Display.NONE);
				else
					fpi.setLevelShowing(true);
				add(fpi);
				xsRemain -= 12;
				smRemain -= 6;
				mdRemain -= 4;
				if (xsRemain == 0 || smRemain == 0 || mdRemain == 0) {
					Element afterOf = fpi.getElement();
					Element div = DOM.createDiv();
					String visibleClazz = "";
					if (xsRemain == 0) {
						visibleClazz += " visible-xs-block";
						xsRemain = 12;
					}
					if (smRemain == 0) {
						visibleClazz += " visible-sm-block";
						smRemain = 12;
					}
					if (mdRemain == 0) {
						visibleClazz += " visible-md-block visible-lg-block";
						mdRemain = 12;
					}
					div.setClassName("clearfix" + visibleClazz);
					contentDiv.insertAfter(div, afterOf);
				}
			} catch (Exception e) {
				JsUtil.handleError(getParent(), e);
			}
		super.onLoad();
		listenersEnabled = true;
	}

	private boolean isFilterGroupAccepted(DatasetFilterDescriptor fd) {
		boolean accept = JsUtil.isEmpty(group) && JsUtil.isEmpty(fd.getGroup());
		if (!accept && JsUtil.isNotEmpty(group) && JsUtil.isNotEmpty(fd.getGroup())) {
			for (String g : group.split(",")) {
				if (("," + fd.getGroup() + ",").indexOf("," + g + ",") > -1)
					return true;
			}
		}
		return accept;
	}

	private boolean isFilterLevelAccepted(DatasetFilterDescriptor fd) {
		return fd.getLevel() <= level;
	}

	@Override
	public void addFilter(DatasetFilterDescriptor fd) throws Exception {
		descriptors.add(fd);
	}

	public void addDateFilter(String label, String field, boolean isRange) throws Exception {
		DatasetFilterDescriptor fd = new DatasetFilterDescriptor(field);
		fd.setLabel(label);
		fd.setControlType(ControlType.Date);
		fd.setCriteriaOperator(isRange ? CriteriaParameterOperator.Between : CriteriaParameterOperator.Equal);//just to make range. query wont be affected
		addFilter(fd);
	}

	private FilterPanelItem getFilterPanelItem(String filterName) {
		for (int r = 0; r < getWidgetCount(); r++)
			if (getWidget(r) instanceof FilterPanelItem) {
				FilterPanelItem fpi = (FilterPanelItem) getWidget(r);
				if (fpi.getFilterName().equals(filterName))
					return fpi;
			}
		return null;
	}

	public void setEnabled(String filterName, boolean enabled) {
		FilterPanelItem fpi = getFilterPanelItem(filterName);
		if (fpi != null)
			fpi.setEnabled(enabled);
	}

	public void setFilterValue(String filterName, Serializable value, Serializable valueEnd) {
		FilterPanelItem fpi = getFilterPanelItem(filterName);
		if (fpi == null)
			return;
		fpi.setValue(value);
		fpi.setValueEnd(valueEnd);
	}

	public Serializable[] getFilterValue(String filterName) throws ParseException {
		FilterPanelItem fpi = getFilterPanelItem(filterName);
		if (fpi == null)
			return null;
		return fpi.getValues();
	}

	private boolean fillFilterValues(final JsonMap filters) throws ParseException {
		boolean mandFilled = true;
		for (int r = 0; r < getWidgetCount(); r++)
			if (getWidget(r) instanceof FilterPanelItem) {
				FilterPanelItem fpi = (FilterPanelItem) getWidget(r);
				boolean required = !fpi.isNullable();
				if (!required && !fpi.isLevelShowing())
					continue;
				Serializable[] values = fpi.getValues();
				if (values[1] != null) {
					JSONArray arr = new JSONArray();
					Serializable val = values[0];
					if (val != null)
						if (val instanceof Date)
							arr.set(0, new JSONString(JsUtil.asString((Date) val)));
						else if (val instanceof Long)
							arr.set(0, new JSONNumber((Long) val));
						else if (val instanceof List)
							arr.set(1, JsUtil.asJsonArray((List) val));
						else
							arr.set(0, new JSONString(JsUtil.asString(val)));
					val = values[1];
					if (val != null)
						if (val instanceof Date)
							arr.set(1, new JSONString(JsUtil.asString((Date) val)));
						else if (val instanceof Long)
							arr.set(1, new JSONNumber((Long) val));
						else if (val instanceof List)
							arr.set(1, JsUtil.asJsonArray((List) val));
						else
							arr.set(1, new JSONString(JsUtil.asString(val)));
					if (arr.size() > 0)
						filters.put(fpi.getFilterName(), arr);
					else if (required)
						mandFilled = false;
				} else {
					Serializable val = values[0];
					if (val != null) {
						if (val instanceof Date)
							filters.put(fpi.getFilterName(), new JSONString(JsUtil.asString((Date) val)));
						else if (val instanceof Boolean)
							filters.put(fpi.getFilterName(), JSONBoolean.getInstance((Boolean) val));
						else if (val instanceof Long)
							filters.put(fpi.getFilterName(), new JSONNumber((Long) val));
						else if (val instanceof String) {
							if (JsUtil.isNotEmpty(((String) val)))
								filters.set(fpi.getFilterName(), val.toString());
						} else if (val instanceof List)
							filters.put(fpi.getFilterName(), JsUtil.asJsonArray((List) val));
						else
							filters.set(fpi.getFilterName(), val.toString());
					} else if (required)
						mandFilled = false;
				}
			}
		if (hiddenFilterNames != null) {
			for (String field : hiddenFilterNames.keySet()) {
				Boolean nullable = hiddenFilterNames.get(field);
				Serializable val = filterListener.getHiddenFilterValue(field);
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
		for (int r = 0; r < getWidgetCount(); r++)
			if (getWidget(r) instanceof FilterPanelItem) {
				FilterPanelItem fpi = (FilterPanelItem) getWidget(r);
				Serializable[] values = fpi.getValues();
				if (values[1] != null) {
				} else {
					if (values[0] != null)
						filters.put(fpi.getFilterName(), values[0], fpi.getFilterLabel());
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
				if (filterListener != null && invokeListeners)
					filterListener.applyFilters();
			} else
				JsUtil.message(this, IFormFactory.nlsCommon.filtreleriDoldurun());
		} catch (ParseException e) {
			JsUtil.handleError(this, e);
		}
	}

	protected void clearFilters(final boolean invokeListeners) throws ParseException {
		for (int r = 0; r < getWidgetCount(); r++)
			if (getWidget(r) instanceof FilterPanelItem) {
				FilterPanelItem fpi = (FilterPanelItem) getWidget(r);
				fpi.resetValues();
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

	@Override
	public int getTabIndex() {
		return getElement().getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
	}

	@Override
	public void setFocus(boolean focused) {
		for (int r = 0; r < getWidgetCount(); r++)
			if (getWidget(r) instanceof FilterPanelItem) {
				FilterPanelItem fpi = (FilterPanelItem) getWidget(r);
				fpi.setFocus(focused);
				break;
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
		/*if (listenersEnabled){
		if (event.getNativeKeyCode() == 13) {
				applyFilters(true);
			}
			event.preventDefault();
			event.stopPropagation();
		}*/
	}

	@Override
	protected void onUnload() {
		hiddenFilterNames = null;
		filterListener = null;
		values = null;
		filterLabelAndControlDataResolver.onUnload();
		filterLabelAndControlDataResolver = null;
		descriptors = null;
		super.onUnload();
	}

	public void setMinHeight(int h, Unit unit) {
		contentDiv.getStyle().setProperty("minHeight", h, unit);
	}

	public void setMaxHeight(int h, Unit unit) {
		contentDiv.getStyle().setProperty("maxHeight", h, unit);
	}

	@Override
	public void onChange(ChangeEvent event) {
		if (listenersEnabled)
			applyFilters(true);
	}

}