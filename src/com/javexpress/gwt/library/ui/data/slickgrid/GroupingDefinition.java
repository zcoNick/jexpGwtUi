package com.javexpress.gwt.library.ui.data.slickgrid;

public class GroupingDefinition {

	private String	field;
	private boolean	collapsed;
	private boolean	aggregateCollapsed;
	private boolean	lazyCalculation;

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

}