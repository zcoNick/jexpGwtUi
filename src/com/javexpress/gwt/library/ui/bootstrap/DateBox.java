package com.javexpress.gwt.library.ui.bootstrap;

import java.text.ParseException;
import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class DateBox extends BaseWrappedInput<Date> {

	public static WidgetBundles fillResources(final WidgetBundles parent) {
		WidgetBundles wb = new WidgetBundles("Date/Time Pickers", parent);

		wb.addStyleSheet("scripts/datetimepicker/datepicker.css");
		wb.addStyleSheet("scripts/datetimepicker/bootstrap-timepicker.css");
		wb.addStyleSheet("scripts/datetimepicker/daterangepicker.css");
		wb.addStyleSheet("scripts/datetimepicker/bootstrap-datetimepicker.css");

		wb.addJavaScript("scripts/datetimepicker/bootstrap-datepicker.min.js");
		wb.addJavaScript("scripts/datetimepicker/bootstrap-timepicker.min.js");
		wb.addJavaScript("scripts/datetimepicker/daterangepicker.min.js");
		wb.addJavaScript("scripts/datetimepicker/bootstrap-datetimepicker.min.js");

		WidgetBundles lclz = new WidgetBundles("Date/Time Localizations", wb);
		lclz.addJavaScript("scripts/datetimepicker/bootstrap-datepicker.tr.js");
		return lclz;
	}

	private JsonMap	options;
	private Element	btDate;

	public DateBox(final Widget parent, final String id) {
		super(parent, WidgetConst.DATEBOX_PREFIX, id, "jexpDateBox");

		input = DOM.createInputText().cast();
		JsUtil.ensureSubId(getElement(), input, "inp");
		input.addClassName("form-control");
		getElement().appendChild(input);

		btDate = DOM.createSpan();
		btDate.setClassName("input-group-addon jexpHandCursor");
		btDate.setInnerHTML("<i class='jexpHandCursor ace-icon " + FaIcon.calendar.getCssClass() + "'></i>");
		getElement().appendChild(btDate);

		createDefaultOptions();
	}

	protected void createDefaultOptions() {
		//https://bootstrap-datepicker.readthedocs.org/en/release/
		options = new JsonMap();
		if (JsUtil.isRTL())
			options.set("rtl", true);
		options.set("autoclose", true);
		options.set("clearBtn", true);
		options.set("showOnFocus", false);
		options.set("todayHighlight", true);
		options.setInt("weekStart", 0);
		options.set("calendarWeeks", false);
		options.set("forceParse", false);
		options.set("orientation", "top auto");
		options.set("language", LocaleInfo.getCurrentLocale().getLocaleName());
		String fmt = JexpGwtUser.getDateFormat();
		options.set("format", fmt.replaceFirst("MM", "mm"));
		fmt = fmt.replaceFirst("dd", "d");
		fmt = fmt.replaceFirst("mm", "m");
		fmt = fmt.replaceFirst("MM", "m");
		fmt = fmt.replaceFirst("yyyy", "y");
		fmt = fmt.replaceFirst("yy", "y");
		options.set("inputformat", fmt);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, input, getElement(), options.getJavaScriptObject());
	}

	private native void createByJs(DateBox x, Element element, Element bt, JavaScriptObject options) /*-{
																										var el = $wnd.$(element).datepicker(options).on("clearDate", function(e){
																										x.@com.javexpress.gwt.library.ui.bootstrap.DateBox::setValue(Ljava/lang/String;Z)(null,true);
																										});
																										el.on("focus", function(e){
																										x.@com.javexpress.gwt.library.ui.bootstrap.DateBox::onTogglePopup(Z)(true);
																										});
																										el.on("changeDate", function(e){
																										if (el.val().indexOf(' ')==-1)
																										x.@com.javexpress.gwt.library.ui.bootstrap.DateBox::setValue(Ljava/lang/String;Z)(e.format(),true);
																										});
																										el.inputmask("datetime",{
																										mask : options.inputformat,
																										separator : '.',
																										placeholder : " ",
																										oncomplete : function() {
																										x.@com.javexpress.gwt.library.ui.bootstrap.DateBox::setValue(Ljava/lang/String;Z)(el.val(),true);
																										}
																										});
																										$wnd.$(".jexpHandCursor", bt).click(function(e){
																										if (!el.is(":disabled"))
																										el.datepicker("show");
																										});
																										}-*/;

	@Override
	protected void onUnload() {
		btDate = null;
		options = null;
		destroyByJs(getElement(), input);
		super.onUnload();
	}

	private void onTogglePopup(boolean shown) {
		input.setAttribute("c", ((InputElement) input).getValue());
	}

	//https://github.com/RobinHerbots/jquery.inputmask/issues/648
	private native void destroyByJs(Element element, Element input) /*-{
																	$wnd.$(element).datepicker('destroy').empty().off();
																	}-*/;

	public void setRawValue(final String cand) {
		((InputElement) input).setValue(cand);
	}

	@Override
	public void setValue(final Date cand) {
		setValue(cand, false);
	}

	public Date getDate() throws ParseException {
		String s = input.hasAttribute("c") ? input.getAttribute("c") : ((InputElement) input).getValue();
		if (s != null && s.trim().length() > 6)
			return JexpGwtUser.parseDate(s);
		return null;
	}

	public Date getValueDate() throws ParseException {
		return getDate();
	}

	@Override
	public boolean validate(final boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	public void setValue(String value) {
		setValue(value, false);
	}

	public void setValue(String value, boolean fireEvents) {
		if (value != null && value.startsWith("@")) {
			if (value.equals("@now"))
				setValue(new Date(), fireEvents);
			else if (value.startsWith("@now-"))
				setValue(JsUtil.daysBefore(Integer.parseInt(value.substring(5))), fireEvents);
			else if (value.startsWith("@now+"))
				setValue(JsUtil.daysAfter(Integer.parseInt(value.substring(5))), fireEvents);
			else if (value.equals("@monthStart"))
				setValue(JsUtil.toMonthStart(new Date()), fireEvents);
		} else
			setValue(JexpGwtUser.parseDate(value), fireEvents);
	}

	@Override
	public Date getValue() {
		try {
			return getDate();
		} catch (ParseException e) {
			JsUtil.handleError(this, e);
		}
		return null;
	}

	@Override
	protected void setRawValue(Date value) {
		((InputElement) input).setValue(JexpGwtUser.formatDate(value));
		input.setAttribute("c", ((InputElement) input).getValue());
	}

	public void setValueDate(Date value) {
		setValue(value);
	}

}