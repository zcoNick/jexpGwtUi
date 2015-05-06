package com.javexpress.gwt.library.ui.bootstrap;

import java.text.ParseException;
import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IWrappedInput;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class DateBox extends JexpSimplePanel implements IWrappedInput<Date> {

	public static void fillResources(final WidgetBundles wb) {
		wb.addStyleSheet("ace/css/datepicker.css");
		wb.addStyleSheet("ace/css/bootstrap-timepicker.css");
		wb.addStyleSheet("ace/css/daterangepicker.css");
		wb.addStyleSheet("ace/css/bootstrap-datetimepicker.css");

		wb.addJavaScript("ace/js/bootstrap-datepicker.min.js");
		wb.addJavaScript("ace/js/bootstrap-timepicker.min.js");
		//wb.addJavaScript("ace/js/moment.min.js");
		wb.addJavaScript("ace/js/daterangepicker.min.js");
		wb.addJavaScript("ace/js/bootstrap-datetimepicker.min.js");
		wb.addJavaScript("ace/js/bootstrap-datepicker.tr.js");
	}

	private JsonMap				options;
	private boolean				required;
	private ChangeHandler		onChangeHandler;
	private DataBindingHandler	dataBinding;
	private Element				input;

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(final boolean required) {
		this.required = required;
	}

	public DateBox(final Widget parent, final String id) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.DATEBOX_PREFIX, id);
		getElement().setClassName("input-group jexpDateBox");

		input = DOM.createInputText();
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
			x.@com.javexpress.gwt.library.ui.bootstrap.DateBox::setValue(Ljava/lang/String;)(null);
		}).on("changeDate", function(e){
			x.@com.javexpress.gwt.library.ui.bootstrap.DateBox::fireOnDateSelect(ZLjava/lang/String;)(false,e.format());
		});
		el.inputmask("datetime",{
			mask : options.inputformat,
			separator : '.',
			placeholder : " ",
			oncomplete : function() {
				x.@com.javexpress.gwt.library.ui.bootstrap.DateBox::fireOnDateSelect(ZLjava/lang/String;)(false,dateText);
			}
		});
		el.on("blur",function(e){
			x.@com.javexpress.gwt.library.ui.bootstrap.DateBox::fireOnDateSelect(ZLjava/lang/String;)(false,el.val());
		});
		$wnd.$(".jexpHandCursor", bt).click(function(e){
			if (!el.is(":disabled"))
				el.datepicker("show");
		});
	}-*/;

	@Override
	protected void onUnload() {
		onChangeHandler = null;
		options = null;
		dataBinding = null;
		destroyByJs(getElement(), input);
		super.onUnload();
	}

	//https://github.com/RobinHerbots/jquery.inputmask/issues/648
	private native void destroyByJs(Element element, Element input) /*-{
		$wnd.$(element).datepicker('destroy').empty().off();
	}-*/;

	public void setValue(final Date cand) {
		setValueDate(cand);
	}

	public void setValueDate(final Date cand) {
		if (cand == null) {
			setValue("");
			return;
		}
		setValue(JexpGwtUser.formatDate(cand));
	}

	public Date getDate() throws ParseException {
		String s = input.getPropertyString("value");
		if (s != null && s.trim().length() > 6)
			return JexpGwtUser.parseDate(input.getPropertyString("value"));
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
			input.setPropertyString("value", value);
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		this.onChangeHandler = handler;
		/*super.addFocusHandler(new FocusHandler() {
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
		});*/
		//return super.addChangeHandler(handler);
		return null;
	}

	protected void updateLastValue() {
		this.lastValue = getText();
	}

	private String getText() {
		return input.getPropertyString("value");
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
	private Element	btDate;

	// ---------- EVENTS
	public void fireOnDateSelect(boolean mouseSelected, final String dateText) {
		if (onChangeHandler != null && !getValue().equals(lastValue)) {
			onChangeHandler.onChange(null);
		}
		lastValue = dateText;
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

	@Override
	public int getTabIndex() {
		return input.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
	}

	@Override
	public void setFocus(boolean focused) {
		if (focused)
			input.focus();
	}

	@Override
	public void setTabIndex(int index) {
		input.setTabIndex(index);
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
	public void setEnabled(boolean enabled) {
		input.setPropertyBoolean("disabled", !enabled);
	}

	@Override
	public Element getInputElement() {
		return input;
	}

}