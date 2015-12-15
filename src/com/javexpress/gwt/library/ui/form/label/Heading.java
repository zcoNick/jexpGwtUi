package com.javexpress.gwt.library.ui.form.label;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.JexpWidget;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.HeadingSize;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class Heading extends JexpWidget {

	public Heading(final Widget parent, final String id, HeadingSize size) {
		setElement(Bootstrap.createHeading(size));
		JsUtil.ensureId(parent, this, WidgetConst.HEADING_PREFIX, id);
		setStyleName("jexpHeading");
	}

	public void setText(final String text) {
		getElement().setInnerText(text);
	}

	public String getText() {
		return getElement().getInnerText();
	}

	@Override
	public void setTitle(final String title) {
		getElement().setAttribute("title", title);
	}

	public HandlerRegistration addClickHandler(final ClickHandler handler) {
		getElement().addClassName("ui-cursor-hand");
		HandlerRegistration hr = addDomHandler(handler, ClickEvent.getType());
		return hr;
	}

	public void setValueInt(final Integer val) {
		setText(val == null ? null : JsUtil.asString(val));
	}

	public void setValueLong(final Long val) {
		setText(JsUtil.asString(val));
	}

	public void setValueDate(final Date val) {
		setText(JsUtil.asString(val));
	}

	public void setValueDecimal(final BigDecimal val) {
		setText(JsUtil.asString(val));
	}

	public void highlight(final int durationInMilliseconds) {
		_highlight(getElement(), durationInMilliseconds);
	}

	private native void _highlight(Element el, int durationInMilliseconds) /*-{
		$wnd.$(el).effect("highlight", {}, durationInMilliseconds);
	}-*/;

	public void setDate(Date value) {
		setText(JsUtil.asString(value));
	}

	public String getValue() {
		return getText();
	}

	public Integer getValueInt() {
		String v = getValue();
		return JsUtil.isEmpty(v) ? null : Integer.valueOf(v);
	}

	public void append(String s) {
		String t = getText();
		if (JsUtil.isNotEmpty(t)) {
			setText(t + " :");
		}
	}

	public void setValue(Short value) {
		setText(JsUtil.asString(value));
	}

	public void setValue(Integer value) {
		setText(JsUtil.asString(value));
	}

	public void setValue(String value) {
		setText(value);
	}

	public void setValue(Date value) {
		setText(JsUtil.asString(value));
	}

}