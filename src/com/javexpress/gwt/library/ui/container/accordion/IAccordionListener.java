package com.javexpress.gwt.library.ui.container.accordion;

public interface IAccordionListener {

	/** @param oldId
	 * @param newId */
	public boolean onAccordionChanging(String oldId, String newId);

	/** @param oldId
	 * @param newId */
	public void onAccordionChanged(String oldId, String newId);

}