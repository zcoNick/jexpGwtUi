package com.javexpress.gwt.library.ui.form.autocomplete;

import com.google.gwt.dom.client.Element;
import com.javexpress.gwt.library.ui.js.JsonMap;

public interface IMultiAutoCompleteListener extends IAutoCompleteListener {

	public Element createListItem(final String id, final String label, final JsonMap data, final boolean userAction);

	public void itemAdded(final String id, final JsonMap data, final boolean userAction);

	public void itemRemoved(final String id, final boolean userAction);

}