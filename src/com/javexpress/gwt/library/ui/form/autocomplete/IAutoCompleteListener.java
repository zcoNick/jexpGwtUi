package com.javexpress.gwt.library.ui.form.autocomplete;

import com.javexpress.gwt.library.ui.js.JsonMap;

public interface IAutoCompleteListener {

	public void onAutoCompleteBeforeDataRequest(final JsonMap postData);

	public boolean canSelectItem(String id, String label, JsonMap data);

	public void itemSelected(final String id, final String label, final JsonMap data) throws Exception;

	public void buttonClicked();

}