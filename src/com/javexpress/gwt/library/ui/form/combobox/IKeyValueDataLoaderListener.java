package com.javexpress.gwt.library.ui.form.combobox;

import java.util.Set;

import com.google.gwt.json.client.JSONObject;

public interface IKeyValueDataLoaderListener {

	void onStartLoadingKeys(Set<IKeyValueList> keySet);

	void onLoadedKeyValues(String k, JSONObject data);

	void onKeysCompleted(Set<IKeyValueList> keySet);
}