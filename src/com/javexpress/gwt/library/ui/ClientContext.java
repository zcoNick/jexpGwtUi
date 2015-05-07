package com.javexpress.gwt.library.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.javexpress.common.model.item.Result;
import com.javexpress.common.model.item.exception.AppException;
import com.javexpress.gwt.library.shared.nls.CommonResources;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JexpCallback;

public interface ClientContext {

	public static ClientContext		instance	= null;
	public static CommonResources	nlsCommon	= GWT.create(CommonResources.class);

	void formYetkiListesi(Long moduleId, String authKey, JexpCallback<List<String>> callback);

	String getModuleNls(Long moduleId, String key);

	void createJiraIssue(long moduleId, String summary, String description, String userAgent, byte issueType, AsyncCallback<Result<String>> callback);

	void openHelp(IUIComposite widget);

	void goLockScreen();

	void showError(String windowId, AppException ae);

	ServiceDefTarget getModuleServiceTarget(long moduleId);

}