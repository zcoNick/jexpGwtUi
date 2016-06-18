package com.javexpress.gwt.library.ui.data.slickgrid;

import java.util.LinkedHashMap;
import java.util.Map;

import com.javexpress.gwt.library.ui.data.ListColumn.SummaryType;

public class GroupingDefinition {

	private String						field;
	private String						orderField;
	private boolean						collapsed;
	private boolean						aggregateCollapsed;
	private boolean						lazyCalculation;
	private Map<String, SummaryType>	aggregators;

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}

	public boolean isAggregateCollapsed() {
		return aggregateCollapsed;
	}

	public void setAggregateCollapsed(boolean aggregateCollapsed) {
		this.aggregateCollapsed = aggregateCollapsed;
	}

	public boolean isLazyCalculation() {
		return lazyCalculation;
	}

	public void setLazyCalculation(boolean lazyCalculation) {
		this.lazyCalculation = lazyCalculation;
	}

	public GroupingDefinition(String field, boolean collapsed) {
		this.field = field;
		this.collapsed = collapsed;
	}

	public Map<String, SummaryType> getAggregators() {
		return aggregators;
	}

	public void addAggregator(String field, SummaryType sum) {
		if (aggregators == null)
			aggregators = new LinkedHashMap<String, SummaryType>();
		aggregators.put(field, sum);
	}

}