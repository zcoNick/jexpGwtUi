package com.javexpress.gwt.library.ui.container.dashboard;

public interface IDashboardItemReplaceListener {

	public void itemReplaced(String code, Long reference, Integer toColumn, Integer toOrder);

	void itemDeactivated(String code, Long reference);

	public void doItemSetup(DashboardWidget dbw, String code, Long reference);

}