package com.javexpress.gwt.library.ui.form.label;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.application.model.item.ModuleEnumItems;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.JexpWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class Label extends JexpWidget {

	private ICssIcon								icon;
	private Serializable							data;
	private boolean									required;
	private ModuleEnumItems<? extends Serializable>	items;

	public Label() {
		this(null, null);
	}

	/** Designer compatible constructor */
	public Label(final String text) {
		this(null, null);
		setText(text);
	}

	public Label(final Widget parent, final String id) {
		setElement(DOM.createSpan());
		setStyleName("jexpLabel");
		if (JsUtil.isNotEmpty(id))
			setId(parent, id);
	}

	public void setId(final Widget parent, final String id) {
		JsUtil.ensureId(parent, this, WidgetConst.LABEL_PREFIX, id);
	}

	public void setText(final String text) {
		getElement().setInnerText(text);
	}

	public String getText() {
		return getElement().getInnerText();
	}

	public Serializable getData() {
		return data;
	}

	public void setData(final Serializable data) {
		this.data = data;
	}

	public ICssIcon getIcon() {
		return icon;
	}

	public void setIcon(final ICssIcon icon) {
		boolean has = false;
		for (String s : getElement().getClassName().split(" ")) {
			if (s.equals("ui-icon")) {
				has = true;
				break;
			}
		}
		if (!has)
			getElement().addClassName("ui-icon");
		if (this.icon != null)
			getElement().removeClassName(this.icon.getCssClass());
		getElement().addClassName(icon.getCssClass());
		this.icon = icon;
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
		JsUtil.ensureId(getElement());
		_highlight(getElement().getId(), durationInMilliseconds);
	}

	private native void _highlight(String id, int durationInMilliseconds) /*-{
		$wnd.$("#" + id).effect("highlight", {}, durationInMilliseconds);
	}-*/;

	@Override
	protected void onUnload() {
		icon = null;
		data = null;
		items = null;
		super.onUnload();
	}

	public void setBold(boolean bold) {
		getElement().getStyle().setFontWeight(bold ? FontWeight.BOLD : FontWeight.NORMAL);
	}

	public void setItalic(boolean italic) {
		getElement().getStyle().setFontStyle(italic ? FontStyle.ITALIC : FontStyle.NORMAL);
	}

	public void setTopLeftMargin(int margin, Unit unit) {
		getElement().getStyle().setMarginTop(margin, unit);
		getElement().getStyle().setMarginLeft(margin, unit);
	}

	public void setDate(Date value) {
		setText(JsUtil.asString(value));
	}

	public void setEnabled(boolean enabled) {
		if (enabled)
			getElement().removeAttribute("disabled");
		else
			getElement().setAttribute("disabled", "disabled");
	}

	public void setRequired(boolean required) {
		this.required = required;
		String t = getText();
		if (!required) {
			if (t.endsWith(" *"))
				setText(t.substring(0, t.length() - 2));
		} else {
			if (!t.endsWith(" *"))
				setText(t + " *");
		}
	}

	public Integer getValueInt() {
		return JsUtil.isEmpty(getText()) ? null : Integer.valueOf(getText());
	}

	public void append(String s) {
		String t = getText();
		if (JsUtil.isNotEmpty(t)) {
			setText(t + " :");
		}
	}

	public boolean isRequired() {
		return required;
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