package com.javexpress.gwt.library.ui.form.autocomplete;

import com.google.gwt.dom.client.Element;
import com.javexpress.gwt.library.ui.js.JsonMap;

public interface IMultiAutoCompleteListener {

	void onAutoCompleteBeforeDataRequest(final JsonMap postData);

	boolean canSelectItem(String id, String label, JsonMap jsonMap);

	void itemSelected(String id, String label, JsonMap jsonMap, boolean userAction);

	void itemAdded(String id, JsonMap data, boolean userSelected);

	Element createListItem(String id, String label, JsonMap jsonmapdata, boolean userAction);

	void itemRemoved(String id, boolean userAction);

}