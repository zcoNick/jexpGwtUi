package com.javexpress.gwt.library.ui.container.tabset;

public interface ITabSetListener {

	/** @param oldId
	 * @param newId */
	public boolean onTabChanging(String oldId, String newId);

	/** @param oldId
	 * @param newId */
	public void onTabChanged(String oldId, String newId);

	public void onTabClosed(String oldId);

}