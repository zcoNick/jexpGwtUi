package com.javexpress.gwt.library.ui;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.javexpress.common.model.item.Result;
import com.javexpress.common.model.item.exception.AppException;
import com.javexpress.gwt.library.shared.nls.CommonResources;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JexpCallback;

public abstract class ClientContext implements EntryPoint {

	public static ClientContext		instance	= null;
	public static CommonResources	nlsCommon	= GWT.create(CommonResources.class);
	public static EventBus			EVENT_BUS	= GWT.create(SimpleEventBus.class);

	abstract public void formYetkiListesi(Long moduleId, String authKey, JexpCallback<List<String>> callback);

	abstract public String getModuleNls(Long moduleId, String key);

	abstract public void createJiraIssue(long moduleId, String summary, String description, String userAgent, byte issueType, AsyncCallback<Result<String>> callback);

	abstract public void openHelp(IUIComposite widget);

	abstract public void showError(String windowId, AppException ae);

	abstract public ServiceDefTarget getModuleServiceTarget(long moduleId);

	abstract public void showInWindow(IUIComposite form, boolean modal);

}