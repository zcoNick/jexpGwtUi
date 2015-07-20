package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.IJexpWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class IconEmbeddedInput extends SimplePanel implements IJexpWidget, Focusable, HasKeyDownHandlers, HasKeyUpHandlers, HasKeyPressHandlers {

	private Element	input;
	private Element	icon;

	public IconEmbeddedInput(Widget parent, String id, ICssIcon iconClass) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, getIdPrefix(), "c_" + id);
		input = createInput();
		JsUtil.ensureId(parent, input, getIdPrefix(), id);
		getElement().appendChild(input);
		icon = DOM.createSpan();
		getElement().appendChild(icon);
		ClientContext.resourceInjector.applyIconInputGroupStyles(getElement(), input, icon, iconClass);
	}

	protected String getIdPrefix() {
		return WidgetConst.TEXTBOX_PREFIX;
	}

	protected Element createInput() {
		return DOM.createInputText();
	}

	public void setIconRight(boolean right) {
		if (right)
			getElement().addClassName("input-icon-right");
		else
			getElement().removeClassName("input-icon-right");
	}

	public void setPlaceHolder(String placeHolder) {
		if (placeHolder != null)
			input.setAttribute("placeholder", placeHolder);
	}

	@Override
	public String getId() {
		return getElement().getId();
	}

	public String getValue() {
		return input.getPropertyString("value");
	}

	@Override
	public int getTabIndex() {
		return getElement().getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		input.setAttribute("accessKey", String.valueOf(key));
	}

	@Override
	public void setFocus(boolean focused) {
		if (focused)
			input.focus();
	}

	@Override
	public void setTabIndex(int index) {
		getElement().setTabIndex(index);
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return addDomHandler(handler, KeyDownEvent.getType());
	}

	@Override
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return addDomHandler(handler, KeyPressEvent.getType());
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return addDomHandler(handler, KeyUpEvent.getType());
	}

	public void setEnabled(boolean locked) {
		input.setAttribute("disabled", locked ? "disabled" : "false");
	}

	public void addInputStyleName(String style) {
		input.addClassName(style);
	}

	public void setValue(String value) {
		input.setPropertyString("value", value);
	}

}