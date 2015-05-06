package com.javexpress.gwt.library.ui.js;

public interface SyncRequestCallback {

	public void onError(String textStatus, String errorThrown);

	public void onSuccess(String textStatus, String response);

}
