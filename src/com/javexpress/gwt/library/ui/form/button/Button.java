package com.javexpress.gwt.library.ui.form.button;

import java.beans.Beans;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.form.ICallbackAware;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class Button extends com.google.gwt.user.client.ui.Button implements ICallbackAware {

	private JavaScriptObject	jsobject;
	private JsonMap				options;

	public void setIcon(final ICssIcon icon) {
		JsonMap icn = (JsonMap) (options.get("icons") != null ? options.get("icons") : new JsonMap());
		icn.set("primary", icon.getCssClass());
		options.set("icons", icn);
	}

	public void setSecondaryIcon(final ICssIcon icon) {
		JsonMap icn = (JsonMap) (options.get("icons") != null ? options.get("icons") : new JsonMap());
		icn.set("secondary", icon.getCssClass());
		options.set("icons", icn);
	}

	public void setHighlighted(boolean highlighted) {
		if (!highlighted)
			getElement().removeClassName("ui-state-highlight");
		else
			getElement().addClassName("ui-state-highlight");
	}

	public void setFocused(boolean highlighted) {
		if (!highlighted)
			getElement().removeClassName("ui-state-focus");
		else
			getElement().addClassName("ui-state-focus");
	}

	public Button(final Widget parent, final String id) {
		this(parent, id, null);
	}

	/** Designer compatible constructor */
	public Button(final Widget parent, final String id, final String text) {
		this(parent, id, text, null);
	}

	public Button(final Widget parent, final String id, final String text, final JsonMap pOptions) {
		super(text);
		JsUtil.ensureId(parent, this, WidgetConst.BUTTON_PREFIX, id);
		options = pOptions == null ? createDefaultOptions() : pOptions;
		getElement().setClassName("");
	}

	protected JsonMap createDefaultOptions() {
		options = new JsonMap();
		return options;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!Beans.isDesignTime()) {
			jsobject = createByJs(getElement(), options.getJavaScriptObject());
		}
	}

	private native JavaScriptObject createByJs(Element element, JavaScriptObject options) /*-{
		return $wnd.$(element).button(options);
	}-*/;

	@Override
	protected void onUnload() {
		options = null;
		if (!Beans.isDesignTime() && isAttached())
			destroyByJs(jsobject);
		jsobject = null;
		super.onUnload();
	}

	private native void destroyByJs(JavaScriptObject jso) /*-{
		jso.button('destroy');
	}-*/;

	@Override
	public String getId() {
		return getElement().getId();
	}

	@Override
	public void callbackStarted() {
		setEnabled(false);
	}

	@Override
	public void callbackCompleted() {
		setEnabled(true);
	}

}