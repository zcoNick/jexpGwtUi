package com.javexpress.gwt.library.ui.form.datebox;

import java.beans.Beans;
import java.text.ParseException;
import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.FormGroupCell;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IFormFactory;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class DateTimeBox extends TextBoxBase implements IUserInputWidget {

	private JsonMap				options;
	private boolean				required;
	private DataBindingHandler	dataBinding;

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(final boolean required) {
		this.required = required;
	}

	public DateTimeBox(final Widget parent, final String id) {
		this(parent, id, null);
	}

	public DateTimeBox(final Widget parent, final String id, final JsonMap pOptions) {
		super(DOM.createInputText());
		JsUtil.ensureId(parent, this, WidgetConst.DATETIMEBOX_PREFIX, id);
		options = pOptions == null ? createDefaultOptions() : pOptions;
		setWidth("9em");
	}

	private JsonMap createDefaultOptions() {
		options = DateBoxJq.createDefaultOptions();
		options.set("timeText", IFormFactory.nlsCommon.zaman());
		options.set("hourText", IFormFactory.nlsCommon.saat());
		options.set("minuteText", IFormFactory.nlsCommon.dakika());
		return options;
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
		if (!Beans.isDesignTime())
			createByJs(this, getElement(), JsUtil.isRTL(), options.getJavaScriptObject());
	}

	private native void createByJs(DateTimeBox x, Element element, boolean isRTL, JavaScriptObject options) /*-{
		var myControl = {
			create : function(tp_inst, obj, unit, val, min, max, step) {
				$wnd.$(
						'<input class="ui-timepicker-input" value="' + val
								+ '" style="width:50%">').appendTo(obj)
						.spinner(
								{
									min : min,
									max : max,
									step : step,
									change : function(e, ui) { // key events
										// don't call if api was used and not key press
										if (e.originalEvent !== undefined)
											tp_inst._onTimeChange();
										tp_inst._onSelectHandler();
									},
									spin : function(e, ui) { // spin events
										tp_inst.control.value(tp_inst, obj,
												unit, ui.value);
										tp_inst._onTimeChange();
										tp_inst._onSelectHandler();
									}
								});
				return obj;
			},
			options : function(tp_inst, obj, unit, opts, val) {
				if (typeof (opts) == 'string' && val !== undefined)
					return obj.find('.ui-timepicker-input').spinner(opts, val);
				return obj.find('.ui-timepicker-input').spinner(opts);
			},
			value : function(tp_inst, obj, unit, val) {
				if (val !== undefined)
					return obj.find('.ui-timepicker-input').spinner('value',
							val);
				return obj.find('.ui-timepicker-input').spinner('value');
			}
		};
		options.onSelect = function(dateText, inst) {
			x.@com.javexpress.gwt.library.ui.form.datebox.DateTimeBox::fireOnDateSelect(Ljava/lang/String;)(dateText);
			return false;
		};
		options.controlType = myControl;
		$wnd.$(element).datetimepicker(options);
	}-*/;

	public void setGotoCurrent(final String value) {
		setOption(getElement(), "gotoCurrent", String.valueOf(value));
	}

	private native void setOption(Element element, String option, String value) /*-{
		$wnd.$(element).datepicker("option", option, value);
	}-*/;

	@Override
	protected void onUnload() {
		options = null;
		dataBinding = null;
		if (!Beans.isDesignTime())
			destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).datetimepicker('destroy');
	}-*/;

	@Override
	public boolean validate(final boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	public void setValue(final Date cand) {
		setValueDate(cand);
	}

	public void setValueDate(final Date cand) {
		if (cand == null) {
			setText(null);
			return;
		}
		setText(JexpGwtUser.formatTimestamp(cand));
	}

	public Date getDate() throws ParseException {
		String s = getText();
		if (s != null && s.trim().length() > 6)
			return JexpGwtUser.parseTimestamp(getText());
		return null;
	}

	public Date getValueDate() throws ParseException {
		return getDate();
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

	@Override
	public void setDataBindingHandler(DataBindingHandler handler) {
		this.dataBinding = handler;
		dataBinding.setControl(this);

	}

	@Override
	public DataBindingHandler getDataBindingHandler() {
		return dataBinding;
	}

	//---------- EVENTS
	public void fireOnDateSelect(final String dateText) {
	}

}