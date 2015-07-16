package com.javexpress.gwt.library.ui.facet;

import java.util.List;

import com.javexpress.gwt.library.ui.js.JexpCallback;

public interface ProvidesAuthorization {

	void formYetkiListesi(Long moduleId, String authKey, JexpCallback<List<String>> callback);

}