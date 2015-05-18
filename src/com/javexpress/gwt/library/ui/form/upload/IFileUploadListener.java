package com.javexpress.gwt.library.ui.form.upload;

import java.io.Serializable;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IFileUploadListener {

	Map<String, Serializable> beforeSubmit();

	void onComplete(AsyncCallback callback, boolean success, String result, String errorMessage);

}