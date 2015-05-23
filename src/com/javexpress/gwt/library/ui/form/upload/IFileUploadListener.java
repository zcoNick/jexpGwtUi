package com.javexpress.gwt.library.ui.form.upload;

import java.io.Serializable;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javexpress.common.model.item.ControllerAction;

public interface IFileUploadListener {

	public Map<String, Serializable> beforeSubmit();

	public void onComplete(AsyncCallback callback, ControllerAction controllerAction, boolean success, String result, String errorMessage);

}