package com.javexpress.gwt.library.ui;

import com.google.gwt.user.client.Command;
import com.javexpress.gwt.library.ui.js.JsonMap;

public interface IResourceInjector {

	public String getSkinName();

	public void injectCore(JsonMap requireConfig, final Command onload);

	public void injectUI(String applicationCss, Command onload) throws Exception;

}