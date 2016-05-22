package com.javexpress.gwt.library.ui;

import com.google.gwt.core.client.Callback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Command;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public interface IResourceInjector {

	String getSkinName();

	void injectCore(JsonMap requireConfig, final Command onload);

	void injectUI(String applicationCss, Command onload) throws Exception;

	String getFontIconPrefixClass();

	void applyIconInputGroupStyles(Element element, Element input, Element icon, ICssIcon iconClass);

	void applyIconStyles(Element iconSpan, ICssIcon iconClass);

	void applyCheckboxStyles(Element element, InputElement check, Element label);

	void applyCheckboxColorContext(InputElement check, WContext context);

	void injectScript(String string, Callback<Void, Exception> callback);

	void injectLibrary(WidgetBundles wb, Command onload);

	void destroyUI();

}