package com.javexpress.gwt.library.ui.form.combobox;

import java.util.Set;

public interface IKeyValueDataLoaderListener {

	void onStartLoadingKeys(Set<IKeyValueList> keySet);

	void onLoadedKey(String k);

	void onCompleted(Set<IKeyValueList> keySet);

}