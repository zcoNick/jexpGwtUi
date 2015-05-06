package com.javexpress.gwt.library.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javexpress.common.model.item.Result;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JexpCallback;

public interface ClientContext {

	public static ClientContext	instance	= null;

	void formYetkiListesi(Long moduleId, String authKey, JexpCallback<List<String>> callback);

	String getCommonNls(String key);

	String getModuleNls(Long moduleId, String key);

	void createJiraIssue(long moduleId, String summary, String description, String userAgent, byte issueType, AsyncCallback<Result<String>> callback);

	void openHelp(IUIComposite widget);

}