package com.javexpress.gwt.library.ui.form.combobox;

import java.io.Serializable;
import java.util.Map;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface IListItemBox extends IKeyValueList {

	void setItemsNls(Map<? extends Serializable, ? extends Serializable> map, ConstantsWithLookup nls);

}