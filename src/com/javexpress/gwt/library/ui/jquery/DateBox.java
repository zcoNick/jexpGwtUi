package com.javexpress.gwt.library.ui.jquery;

import java.text.ParseException;
import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.FormGroupCell;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.form.maskedit.TimeBox;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class DateBox extends TextBoxBase implements IUserInputWidget {

	public static enum DateType {
		BirthDate, InLastYear, InLast10Years, InNextYear, InNext10Years
	}

	private JsonMap				options;
	private boolean				required;
	private ChangeHandler		onChangeHandler;
	private DataBindingHandler	dataBinding;

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(final boolean required) {
		this.required = required;
	}

	public DateBox(Widget parent, String id, DateType type) {
		this(parent, id, null, type);
	}

	public DateBox(final Widget parent, final String id) {
		this(parent, id, null, null);
	}

	public DateBox(final Widget parent, final String id,
			final JsonMap pOptions) {
		this(parent, id, pOptions, null);
	}

	public DateBox(final Widget parent, final String id, final JsonMap pOptions, DateType type) {
		super(DOM.createInputText());
		JsUtil.ensureId(parent, this, WidgetConst.DATEBOX_PREFIX, id);
		options = pOptions == null ? createDefaultOptions() : pOptions;
		setWidth("7em");
		setDateType(type);
		addStyleName("jexpDateBox");
	}

	private void setDateType(DateType type) {
		if (type != null)
			switch (type) {
				case BirthDate:
					setYearRange(120, 0);
					break;
				case InLastYear:
					setYearRange(1, 0);
					break;
				case InLast10Years:
					setYearRange(10, 0);
					break;
				case InNextYear:
					setYearRange(0, 1);
					break;
				case InNext10Years:
					setYearRange(0, 10);
					break;
			}
	}

	protected static JsonMap createDefaultOptions() {
		JsonMap options = new JsonMap();
		if (JsUtil.isRTL())
			options.set("isRTL", true);
		options.set("changeMonth", true);
		options.set("changeYear", true);
		options.set("gotoCurrent", true);
		options.set("showOtherMonths", true);
		options.set("showButtonPanel", true);
		options.set("showWeek", true);
		options.setInt("firstDay", 1);
		options.set("duration", "fast");
		return options;
	}

	public void setChangeYear(boolean changeYear) {
		options.set("changeYear", changeYear);
	}

	public void setChangeMonth(boolean changeMonth) {
		options.set("changeMonth", changeMonth);
	}

	public void setYearRange(int back, int forward) {
		options.set("yearRange", "-" + back + ":+" + forward);
	}

	public void setNumberOfMonths(final int value) {
		options.setInt("numberOfMonths", value);
	}

	public int getNumberOfMonths() {
		Double d = options.getDouble("numberOfMonths");
		return d.intValue();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement(), options.getJavaScriptObject(), getMatchingFormat());
	}

	private String getMatchingFormat() {
		String fmt = JexpGwtUser.getDateFormat().replaceFirst("dd", "d");
		fmt = fmt.replaceFirst("mm", "m");
		fmt = fmt.replaceFirst("MM", "m");
		fmt = fmt.replaceFirst("yyyy", "y");
		fmt = fmt.replaceFirst("yy", "y");
		return fmt;
	}

	private native void createByJs(DateBox x, Element element, JavaScriptObject options, String fmt) /*-{
		options.onSelect = function(dateText, inst) {
			x.@com.javexpress.gwt.library.ui.form.datebox.DateBoxJq::fireOnDateSelect(ZLjava/lang/String;)(true,dateText);
			return false;
		};
		var el = $wnd.$(element).datepicker(options);
		if (fmt) {
			var changed = false;
			el
					.inputmask(
							"datetime",
							{
								mask : fmt,
								separator : '.',
								placeholder : " ",
								oncomplete : function() {
									x.@com.javexpress.gwt.library.ui.form.datebox.DateBoxJq::fireOnDateSelect(ZLjava/lang/String;)(false,dateText);
								}
							});
		}
	}-*/;

	public void setGotoCurrent(final String value) {
		setOption(getElement(), "gotoCurrent", String.valueOf(value));
	}

	private native void setOption(Element element, String option, String value) /*-{
		$wnd.$(element).datepicker("option", option, value);
	}-*/;

	@Override
	protected void onUnload() {
		onChangeHandler = null;
		options = null;
		dataBinding = null;
		destroyByJs(getElement());
		super.onUnload();
	}

	//https://github.com/RobinHerbots/jquery.inputmask/issues/648
	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).datepicker('destroy');
	}-*/;

	public void setValue(final Date cand) {
		setValueDate(cand);
	}

	public void setValueDate(final Date cand) {
		if (cand == null) {
			setText(null);
			return;
		}
		setText(JexpGwtUser.formatDate(cand));
	}

	public Date getDate() throws ParseException {
		String s = getText();
		if (s != null && s.trim().length() > 6)
			return JexpGwtUser.parseDate(getText());
		return null;
	}

	public Date getValueDate() throws ParseException {
		return getDate();
	}

	@Override
	public boolean validate(final boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	public Date getDateTime(final TimeBox baslama) throws ParseException {
		Date date = getDate();
		String[] hm = baslama.getText().split(":");
		date.setHours(Integer.parseInt(hm[0]));
		date.setMinutes(Integer.parseInt(hm[1]));
		if (hm.length > 2)
			date.setSeconds(Integer.parseInt(hm[2]));
		return date;
	}

	@Override
	public void setValue(String value) {
		if (value != null && value.startsWith("@")) {
			if (value.equals("@now"))
				setValue(new Date());
			else if (value.startsWith("@now-"))
				setValue(JsUtil.dateBefore(Integer.parseInt(value.substring(5))));
			else if (value.startsWith("@now+"))
				setValue(JsUtil.dateAfter(Integer.parseInt(value.substring(5))));
			else if (value.equals("@monthStart"))
				setValue(JsUtil.toMonthStart(new Date()));
		} else
			super.setValue(value);
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		this.onChangeHandler = handler;
		super.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				updateLastValue();
			}
		});
		super.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				updateLastValue();
			}
		});
		return super.addChangeHandler(handler);
	}

	protected void updateLastValue() {
		this.lastValue = getValue();
	}

	@Override
	public void setValidationError(String validationError) {
		if (JsUtil.USE_BOOTSTRAP) {
			Widget nw = getParent() instanceof FormGroupCell ? getParent() : this;
			if (validationError == null)
				nw.removeStyleName("has-error");
			else
				nw.addStyleName("has-error");
		}
		setTitle(validationError);
	}

	private String	lastValue	= null;

	// ---------- EVENTS
	public void fireOnDateSelect(boolean mouseSelected, final String dateText) {
		if (onChangeHandler != null && !getValue().equals(lastValue)) {
			onChangeHandler.onChange(null);
		}
	}

	@Override
	public void setDataBindingHandler(DataBindingHandler handler) {
		this.dataBinding = handler;
		dataBinding.setControl(this);

	}

	@Override
	public DataBindingHandler getDataBindingHandler() {
		return dataBinding;
	}

}