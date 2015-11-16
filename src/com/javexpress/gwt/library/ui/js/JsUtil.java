package com.javexpress.gwt.library.ui.js;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.javexpress.common.model.item.ControlType;
import com.javexpress.common.model.item.Result;
import com.javexpress.common.model.item.exception.AppException;
import com.javexpress.common.model.item.type.Pair;
import com.javexpress.gwt.library.shared.model.IJsonServicePoint;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.bootstrap.CheckBox;
import com.javexpress.gwt.library.ui.bootstrap.DateBox;
import com.javexpress.gwt.library.ui.bootstrap.LabelControlCell;
import com.javexpress.gwt.library.ui.container.buttonbar.ButtonBar;
import com.javexpress.gwt.library.ui.dialog.ConfirmDialog;
import com.javexpress.gwt.library.ui.dialog.ConfirmationListener;
import com.javexpress.gwt.library.ui.dialog.JexpPopupPanel;
import com.javexpress.gwt.library.ui.dialog.MessageDialog;
import com.javexpress.gwt.library.ui.dialog.Notification;
import com.javexpress.gwt.library.ui.dialog.Notification.NotificationType;
import com.javexpress.gwt.library.ui.facet.ProvidesModuleUtils;
import com.javexpress.gwt.library.ui.form.Form;
import com.javexpress.gwt.library.ui.form.IDataBindable;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.form.autocomplete.AutoCompleteBox;
import com.javexpress.gwt.library.ui.form.button.Button;
import com.javexpress.gwt.library.ui.form.checkbox.CheckBoxJq;
import com.javexpress.gwt.library.ui.form.checkbox.CheckInlineBox;
import com.javexpress.gwt.library.ui.form.combobox.ComboBox;
import com.javexpress.gwt.library.ui.form.combobox.ListBox;
import com.javexpress.gwt.library.ui.form.decimalbox.DecimalBox;
import com.javexpress.gwt.library.ui.form.numericbox.NumericBox;
import com.javexpress.gwt.library.ui.form.textbox.TextBox;
import com.javexpress.gwt.library.ui.form.upload.FileUpload;

public class JsUtil {

	public static final BigDecimal	ZERO					= new BigDecimal(0);
	public static boolean			isIE7					= false;
	public static boolean			isIE8					= false;
	public static boolean			isIE9					= false;

	static {
		if (Window.Navigator.getUserAgent().matches(".*MSIE 7.*"))
			isIE7 = true;
		if (Window.Navigator.getUserAgent().matches(".*MSIE 8.*"))
			isIE8 = true;
		if (Window.Navigator.getUserAgent().matches(".*MSIE 9.*"))
			isIE9 = true;
	}

	public static final double[]	RESPONSIVE_COL_WIDTHS	= new double[] { 0, 8.33333333, 16.6667, 25, 33.3333, 41.6667, 50, 58.3333, 66.6667, 75, 83.3333, 91.6667, 100 };

	public static boolean			testMode				= false;
	private static int				scrollbarWidth			= 0;

	public static boolean isBrowserIE() {
		String name = getUserAgent();
		return name.indexOf("msie") > -1 || name.indexOf("explorer") > -1 || name.indexOf("ie") > -1;
	}

	public static boolean isBrowserFF() {
		String name = getUserAgent();
		return name.indexOf("firefox") > -1 || name.indexOf("ff") > -1;
	}

	public static boolean isBrowserChrome() {
		String name = getUserAgent();
		return name.indexOf("chrome") > -1 || name.indexOf("chromium") > -1 || name.indexOf("safari") > -1;
	}

	public static native String getBrowserName() /*-{
		return $wnd.navigator.appName.toLowerCase();
	}-*/;

	public static native String getUserAgent() /*-{
		return $wnd.navigator.userAgent.toLowerCase();
	}-*/;

	public static int[] jsArrayIntegerToIntArray(final JsArrayInteger values) {
		int[] res = new int[values.length()];
		for (int i = 0; i < values.length(); i++)
			res[i] = values.get(i);
		return res;
	}

	public static String ensureId(final Element element) {
		if (isEmpty(element.getId()))
			element.setId(DOM.createUniqueId());
		return element.getId();
	}

	public static String ensureId(final Widget parent, final Widget widget, final String prefix, String preferredId) {
		if ((!GWT.isProdMode() || testMode) && isNotEmpty(preferredId)) {
			//Development Modu
			preferredId = prefix + "_" + preferredId.replaceAll("\\.", "_") + (parent != null && isNotEmpty(parent.getElement().getId()) ? "-" + parent.getElement().getId() : "");
			widget.getElement().setId(preferredId);
			widget.ensureDebugId(preferredId);
		} else if (isEmpty(widget.getElement().getId()))
			widget.getElement().setId(prefix + "_" + DOM.createUniqueId());
		return widget.getElement().getId();
	}

	public static String ensureId(final Widget parent, final Element we, final String prefix, String preferredId) {
		if ((!GWT.isProdMode() || testMode) && isNotEmpty(preferredId)) {
			//Development Modu
			preferredId = prefix + "_" + preferredId.replaceAll("\\.", "_") + (parent != null && isNotEmpty(parent.getElement().getId()) ? "-" + parent.getElement().getId() : "");
			we.setId(preferredId);
		} else if (isEmpty(we.getId()))
			we.setId(prefix + "_" + DOM.createUniqueId());
		return we.getId();
	}

	public static String ensureSubId(final Element parent, final Element el, final String suffix) {
		if (isEmpty(el.getId())) {
			String pid = JsUtil.ensureId(parent) + "_" + suffix;
			el.setId(pid);
		}
		return el.getId();
	}

	public static boolean isRTL() {
		return LocaleInfo.getCurrentLocale().isRTL();
	}

	public static boolean isLTR() {
		return !isRTL();
	}

	public static List<Pair<String, String>> getAvailableLocales() {
		List<Pair<String, String>> ls = new ArrayList<Pair<String, String>>();
		for (String localeName : LocaleInfo.getAvailableLocaleNames())
			ls.add(new Pair<String, String>(localeName, LocaleInfo.getLocaleNativeDisplayName(localeName)));
		return ls;
	}

	public static boolean isEmpty(final String val) {
		return val == null || val.trim().equals("");
	}

	public static boolean isNotEmpty(final String val) {
		return !isEmpty(val);
	}

	public static Long asLong(final String text) {
		return isEmpty(text) ? null : Long.valueOf(text);
	}

	public static Date asDate(final String text) {
		return isEmpty(text) ? null : JexpGwtUser.parseDate(text);
	}

	public static Long asLong(final Serializable value) {
		if (value instanceof Long)
			return (Long) value;
		if (value == null)
			return null;
		return Long.valueOf(value.toString());
	}

	public static Short asShort(final String text) {
		return isEmpty(text) ? null : Short.valueOf(text);
	}

	public static Integer asInteger(final String text) {
		return isEmpty(text) ? null : Integer.valueOf(text);
	}

	public static Long asLong(final JSONValue v) {
		if (v == null)
			return null;
		JSONNumber n = v.isNumber();
		if (n != null)
			return (long) n.doubleValue();
		JSONString s = v.isString();
		if (s != null && isNotEmpty(s.stringValue()))
			return Long.valueOf(s.stringValue());
		return null;
	}

	public static Integer asInteger(final JSONValue v) {
		if (v == null)
			return null;
		JSONNumber n = v.isNumber();
		if (n != null)
			return (int) n.doubleValue();
		JSONString s = v.isString();
		if (s != null && isNotEmpty(s.stringValue()))
			return Integer.valueOf(s.stringValue());
		return null;
	}

	public static Short asShort(final JSONValue v) {
		if (v == null)
			return null;
		JSONNumber n = v.isNumber();
		if (n != null)
			return (short) n.doubleValue();
		JSONString s = v.isString();
		if (s != null && isNotEmpty(s.stringValue()))
			return Short.valueOf(s.stringValue());
		return null;
	}

	public static Byte asByte(final JSONValue v) {
		if (v == null)
			return null;
		JSONNumber n = v.isNumber();
		if (n != null)
			return (byte) n.doubleValue();
		JSONString s = v.isString();
		if (s != null && isNotEmpty(s.stringValue()))
			return Byte.valueOf(s.stringValue());
		return null;
	}

	public static BigDecimal asDecimal(final JSONValue v) {
		if (v == null)
			return null;
		JSONNumber n = v.isNumber();
		if (n != null)
			return new BigDecimal(n.doubleValue());
		JSONString s = v.isString();
		if (s != null && isNotEmpty(s.stringValue()))
			return JexpGwtUser.parseDecimal(s.stringValue());
		return null;
	}

	public static BigDecimal asDecimal(final String text) {
		if (isEmpty(text))
			return null;
		return JexpGwtUser.parseDecimal(text);
	}

	public static Byte asByte(final String text) {
		return isEmpty(text) ? null : Byte.parseByte(text);
	}

	public static String asString(final JSONValue v) {
		JSONString s = v.isString();
		if (s != null)
			return s.stringValue();
		JSONNumber n = v.isNumber();
		if (n != null) {
			return String.valueOf(n.doubleValue());
		}
		return null;
	}

	public static String repeatStr(final int length, final String str) {
		String s = "";
		for (int i = 0; i < length; i++)
			s += str;
		return s;
	}

	public static String asString(final Serializable value) {
		return value != null ? ((value instanceof String) ? (String) value : value.toString()) : null;
	}

	public static String asString(final Date value) {
		return value != null ? JexpGwtUser.formatDate(value) : null;
	}

	public static String asString(final Timestamp value) {
		return value != null ? JexpGwtUser.formatTimestamp(value) : null;
	}

	public static String asString(final BigDecimal value) {
		return value != null ? JexpGwtUser.formatDecimal(value) : null;
	}

	public static String asString(final BigDecimal value, double precisionSize) {
		return value != null ? JexpGwtUser.formatDecimal(value, precisionSize) : null;
	}

	public static String substringAfterLast(final String text, final String ch) {
		int i = text.lastIndexOf(ch);
		if (i == -1)
			return text;
		return text.substring(i + 1);
	}

	public static void downloadFile(final ServiceDefTarget defTarget, final Enum method, final Map<String, Serializable> elementsToPost, final Command command) {
		downloadFile(defTarget.getServiceEntryPoint() + "." + method, elementsToPost, command);
	}

	public static void downloadFile(final String url, final Map<String, Serializable> elementsToPost, final Command command) {
		final FormPanel form = new FormPanel();
		form.setAction(url);
		form.setMethod(FormPanel.METHOD_GET);
		if (elementsToPost != null)
			for (String key : elementsToPost.keySet()) {
				Hidden h = new Hidden(key);
				h.setValue(elementsToPost.get(key).toString());
				form.getElement().appendChild(h.getElement());
			}
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(final SubmitCompleteEvent event) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						form.removeFromParent();
						if (command != null)
							command.execute();
					}
				});
			}
		});
		RootPanel.get().add(form);
		form.submit();
	}

	public static void openWindow(final String path, final Map<String, Serializable> elementsToPost) {
		String url = path;
		if (elementsToPost != null)
			for (String key : elementsToPost.keySet()) {
				url += (url.indexOf("?") == -1 ? "?" : "&") + key + "=" + URL.encode(elementsToPost.get(key).toString());
			}
		Window.open(url, null, "location=no");
	}

	public static native void clearChilds(final Element el) /*-{
		$wnd.$(el).empty();
	}-*/;

	public static native void removeJQEvents(final Element el) /*-{
		$wnd.$(el).off();
	}-*/;

	public static void alert(final String msg) {
		Window.alert(resolveMessage(msg));
	}

	private static String resolveMessage(String msg) {
		if ((ClientContext.instance instanceof ProvidesModuleUtils) && isNotEmpty(msg) && msg.startsWith("@")) {
			String[] ml = msg.substring(1).split("@");
			return ((ProvidesModuleUtils) ClientContext.instance).getModuleNls(Long.valueOf(ml[0]), ml[1]);
		}
		return msg;
	}

	public static String prompt(String message, String value) {
		return Window.prompt(message, value);
	}

	public static boolean confirm(final String msg) {
		return Window.confirm(resolveMessage(msg));
	}

	public static void message(final String id, final String message) {
		MessageDialog.showAlert(id, ClientContext.nlsCommon.uyari(), message);
	}

	public static void message(final Widget parent, final String message) {
		message(parent.getElement().getId(), message);
	}

	public static void confirm(final String id, final String title, final String msg, final ConfirmationListener listener) {
		new ConfirmDialog(null, id, title, msg, listener).show();
	}

	public static void confirm(final Widget parent, final String title, final String msg, final ConfirmationListener listener) {
		confirm(parent.getElement().getId(), title, msg, listener);
	}

	public static void confirm(final IsWidget parent, final String title, final String msg, final ConfirmationListener listener) {
		confirm((Widget) parent, title, msg, listener);
	}

	public static void handleError(final String windowId, final Throwable e) {
		if (!GWT.isProdMode())
			e.printStackTrace();
		GWT.log(e.getMessage(), e);
		if (e instanceof UmbrellaException) {
			StringBuffer buf = new StringBuffer();
			UmbrellaException ue = (UmbrellaException) e;
			for (Throwable t : ue.getCauses()) {
				buf.append(t.getMessage());
				if (!GWT.isProdMode()) {
					StackTraceElement ste = t.getStackTrace()[0];
					buf.append(ste.getClassName() + "." + ste.getMethodName() + " " + ste.getFileName() + ":" + ste.getLineNumber()).append("<br/>");
				} else
					buf.append("<br/>");
			}
			MessageDialog.showAlert(windowId, ClientContext.nlsCommon.hata(), buf.toString());
		} else if (e instanceof AppException) {
			AppException ae = (AppException) e;
			ClientContext.instance.showError(windowId, ae);
		} else {
			MessageDialog.showAlert(windowId, ClientContext.nlsCommon.taninmayanHata(), e.getMessage());
		}
	}

	public static void handleError(final Widget parent, final Throwable e) {
		handleError(parent.getElement().getId(), e);
	}

	public static void handleError(final IsWidget parent, final Throwable e) {
		handleError((Widget) parent, e);
	}

	public static void handleResult(final Widget parent, final Result<? extends Serializable> result) {
		handleResult(parent.getElement().getId(), result);
	}

	public static void handleResult(final String id, final Result<? extends Serializable> result) {
		if (isNotEmpty(result.getError()))
			message(id, result.getError());
	}

	public static void showUploader(final FileUpload fu) {
		final JexpPopupPanel db = new JexpPopupPanel(true, true);
		db.setWidth("30em");
		db.setHeight(fu.isMulti() ? (fu.isAskName() ? "10em" : "8em") : (fu.isAskName() ? "4em" : "2.5m"));
		fu.addCompleteCommand(new Command() {
			@Override
			public void execute() {
				db.hide(true);
			}
		});
		db.setWidget(fu);
		db.center();
		db.show();
	}

	public static Widget createControl(final Widget parent, final ControlType type, final String property, final String controlData, final boolean useEmpty, String value, boolean searchMode) throws Exception {
		Widget w = null;
		switch (type) {
			case Text:
				w = new TextBox(parent, property + "_");
				if (value != null)
					((TextBox) w).setText(value);
				break;
			case Number:
				w = new NumericBox(parent, property + "_");
				if (value != null)
					((NumericBox) w).setValue(Long.valueOf(value));
				break;
			case Decimal:
				w = new DecimalBox(parent, property + "_");
				if (value != null)
					((DecimalBox) w).setValue(JsUtil.asDecimal(value));
				break;
			case Date:
				w = new DateBox(parent, property + "_");
				if (value != null)
					((DateBox) w).setValueString(value);
				break;
			case Check:
				w = new CheckBoxJq(parent, property + "_");
				if (value != null)
					((CheckBoxJq) w).setValue(value.equals("1") || value.equals("checked") || value.equals("true") || value.equals("on"));
				break;
			case ComboNumber:
			case ComboText:
				w = new ComboBox(parent, property + "_");
				if (JsUtil.isNotEmpty(controlData)) {
					if (!controlData.startsWith("@"))
						((ComboBox) w).setItems(useEmpty, controlData, ";", ":");
					else if (useEmpty)
						((ComboBox) w).addItem("", "");
				}
				if (value != null) {
					ComboBox c = ((ComboBox) w);
					c.setValue(value);
				}
				break;
			case AutoCompleteNumber:
				w = new AutoCompleteBox<Long>(parent, property + "_");
				if (JsUtil.isNotEmpty(controlData)) {
					if (!controlData.startsWith("@"))
						((AutoCompleteBox<Long>) w).setListingAlias(controlData);
				}
				if (value != null) {
					AutoCompleteBox<Long> c = (AutoCompleteBox<Long>) w;
					c.setValue(value);
				}
				break;
			case AutoCompleteText:
				w = new AutoCompleteBox<String>(parent, property + "_");
				if (JsUtil.isNotEmpty(controlData)) {
					if (!controlData.startsWith("@"))
						((AutoCompleteBox<String>) w).setListingAlias(controlData);
				}
				if (value != null) {
					AutoCompleteBox<String> c = (AutoCompleteBox<String>) w;
					c.setValue(value);
				}
				break;
			case ListNumber:
			case ListText:
				w = searchMode ? new CheckInlineBox(parent, property + "_") : new ListBox(parent, property + "_");
				if (w instanceof CheckInlineBox)
					((CheckInlineBox) w).setEmptyText(ClientContext.nlsCommon.filtreTumu());
				if (JsUtil.isNotEmpty(controlData)) {
					if (!controlData.startsWith("@")) {
						if (searchMode)
							((CheckInlineBox) w).setItems(controlData, ";", ":");
						else
							((ListBox) w).setItems(useEmpty, controlData, ";", ":");
					} else if (useEmpty && !searchMode) {
						((ListBox) w).addItem("", "");
					}
				}
				if (value != null) {
					if (searchMode) {
						CheckInlineBox c = ((CheckInlineBox) w);
						c.setValueArray(value.split(","));
					} else {
						ListBox c = ((ListBox) w);
						c.setValueArray(value.split(","));
					}
				}
				break;
		}
		if (w != null) {
			w.getElement().setAttribute("property", property);
			if (isNotEmpty(value))
				w.getElement().setAttribute("defaultValue", value);
		}
		return w;
	}

	public static Serializable getWidgetValue(final Widget w) throws ParseException {
		return getWidgetValue(w, false);
	}

	public static Serializable getWidgetValue(Widget w, boolean forceNumeric) throws ParseException {
		Serializable val = null;
		if (w instanceof LabelControlCell)
			w = ((LabelControlCell) w).getWidget(0);
		else if (w instanceof DateBox)
			val = ((DateBox) w).getValue();
		else if (w instanceof NumericBox)
			val = ((NumericBox) w).getValueLong();
		else if (w instanceof DecimalBox)
			val = ((DecimalBox) w).getValueDecimal();
		else if (w instanceof ComboBox) {
			ComboBox cmb = (ComboBox) w;
			val = forceNumeric ? cmb.getValueLong() : cmb.getValue();
		} else if (w instanceof CheckInlineBox) {
			CheckInlineBox cilb = (CheckInlineBox) w;
			val = forceNumeric ? cilb.getValueListLong() : cilb.getValueList();
		} else if (w instanceof ListBox)
			val = ((ListBox) w).getValue();
		else if (w instanceof TextBox)
			val = ((TextBox) w).getValue();
		else if (w instanceof CheckBoxJq)
			val = ((CheckBoxJq) w).getValue();
		else if (w instanceof CheckBox)
			val = ((CheckBox) w).getValue();
		else if (w instanceof AutoCompleteBox)
			val = ((AutoCompleteBox) w).getValue();
		return val;
	}

	public static void setWidgetValue(final Widget w, Serializable value) {
		if (w instanceof DateBox)
			((DateBox) w).setValue((Date) value);
		else if (w instanceof NumericBox)
			((NumericBox) w).setValue(value != null ? (value instanceof Number ? ((Number) value).longValue() : Long.valueOf(value.toString())) : null);
		else if (w instanceof DecimalBox)
			((DecimalBox) w).setValue((BigDecimal) value);
		else if (w instanceof ComboBox) {
			if (value == null)
				((ComboBox) w).setSelectedIndex(-1);
			else if (value instanceof Long)
				((ComboBox) w).setValueLong(((Long) value).longValue());
			else
				((ComboBox) w).setValue((String) value);
		} else if (w instanceof CheckInlineBox) {
			if (value == null)
				((CheckInlineBox) w).clearSelection();
			else if (value instanceof String[])
				((CheckInlineBox) w).setValueArray((String[]) value);
			else if (value instanceof ArrayList)
				((CheckInlineBox) w).setValueList((ArrayList<String>) value);
			else
				((CheckInlineBox) w).setValue(value.toString());
		} else if (w instanceof ListBox) {
			if (value == null)
				((ListBox) w).setSelectedIndex(-1);
			else if (value instanceof String[])
				((ListBox) w).setValueArray((String[]) value);
			else if (value instanceof ArrayList)
				((ListBox) w).setValueList((ArrayList<String>) value);
		} else if (w instanceof TextBox)
			((TextBox) w).setValue((String) value);
		else if (w instanceof CheckBox)
			((CheckBox) w).setValue((Boolean) value);
		else if (w instanceof AutoCompleteBox) {
			if (value instanceof String)
				((AutoCompleteBox) w).setValue((String) value);
			else if (value instanceof Long)
				((AutoCompleteBox) w).setValue((Long) value);
		}
	}

	public static void setWidgetEnabled(final Widget w, boolean enabled) {
		if (w instanceof DateBox)
			((DateBox) w).setEnabled(enabled);
		else if (w instanceof NumericBox)
			((NumericBox) w).setEnabled(enabled);
		else if (w instanceof DecimalBox)
			((DecimalBox) w).setEnabled(enabled);
		else if (w instanceof ComboBox)
			((ComboBox) w).setEnabled(enabled);
		else if (w instanceof CheckInlineBox)
			((CheckInlineBox) w).setEnabled(enabled);
		else if (w instanceof ListBox)
			((ListBox) w).setEnabled(enabled);
		else if (w instanceof TextBox)
			((TextBox) w).setEnabled(enabled);
		else if (w instanceof CheckBox)
			((CheckBox) w).setEnabled(enabled);
		else if (w instanceof AutoCompleteBox)
			((AutoCompleteBox) w).setEnabled(enabled);
	}

	public static String getWidgetText(final Widget w) throws ParseException {
		String val = null;
		if (w instanceof TextBox)
			val = ((TextBox) w).getText();
		else if (w instanceof ComboBox)
			val = ((ComboBox) w).getSelectedText();
		return val;
	}

	public static Serializable clearWidgetValue(final Widget w, boolean fireEvents) throws ParseException {
		Serializable val = null;
		if (w instanceof DateBox)
			((DateBox) w).setValue((Date) null, fireEvents);
		else if (w instanceof NumericBox)
			((NumericBox) w).setValue(null, fireEvents);
		else if (w instanceof DecimalBox)
			((DecimalBox) w).setValue((BigDecimal) null, fireEvents);
		else if (w instanceof ComboBox)
			((ComboBox) w).setValue(null, fireEvents);
		else if (w instanceof TextBox)
			((TextBox) w).setValue(null, fireEvents);
		else if (w instanceof CheckBoxJq)
			((CheckBoxJq) w).setValue(false, fireEvents);
		return val;
	}

	public static Serializable restoreWidgetValue(final Widget w) throws ParseException {
		String val = w.getElement().getAttribute("defaultValue");
		if (isEmpty(val))
			val = null;
		if (w instanceof DateBox)
			((DateBox) w).setValueString(val);
		else if (w instanceof NumericBox)
			((NumericBox) w).setValue(JsUtil.asLong(val));
		else if (w instanceof DecimalBox)
			((DecimalBox) w).setValue(JsUtil.asDecimal(val));
		else if (w instanceof ComboBox)
			((ComboBox) w).setValue(val);
		else if (w instanceof TextBox)
			((TextBox) w).setValue(val);
		else if (w instanceof CheckInlineBox)
			((CheckInlineBox) w).setValue(val);
		else if (w instanceof CheckBoxJq)
			((CheckBoxJq) w).setValue(val != null && "true".equals(val));
		return val;
	}

	public static Double asDouble(final String v) {
		return isEmpty(v) ? null : Double.parseDouble(v);
	}

	public static String join(final List<String> rights, final String seperator) {
		String s = "";
		for (String str : rights)
			s += (s.length() == 0 ? "" : ",") + str;
		return s;
	}

	public static boolean validateWidget(final IUserInputWidget<? extends Serializable> vw, final boolean focusedBefore) {
		if (!vw.isRequired())
			return true;
		Serializable val = vw.getValue();
		if (val == null || ((val instanceof String) && JsUtil.isEmpty((String) val))) {
			flagInvalid(vw, ClientContext.nlsCommon.alanZorunlu(), focusedBefore);
			return false;
		} else {
			clearInvalid(vw);
			return true;
		}
	}

	public static void clearInvalid(final IUserInputWidget vw) {
		if (!USE_BOOTSTRAP)
			vw.getElement().removeClassName("ui-state-highlight");
		vw.setValidationError(null);
	}

	public static void flagInvalid(final IUserInputWidget vw, final String validationError, final boolean focusedBefore) {
		if (!USE_BOOTSTRAP)
			vw.getElement().addClassName("ui-state-highlight");
		vw.setValidationError(validationError);
		if (!focusedBefore)
			((Focusable) vw).setFocus(true);
	}

	public static int calcPercent(final int full, final String percent) {
		return full * (Integer.parseInt(percent.substring(0, percent.length() - 1))) / 100;
	}

	public static volatile int	lastDialogZIndex	= 3;

	public static int calcDialogZIndex() {
		lastDialogZIndex += 3;
		if (lastDialogZIndex > 703)
			lastDialogZIndex = calcZIndex(".ui-dialog,.jexpErrorDialog,.jexp-ui-window,.modal-backdrop");
		return lastDialogZIndex;
	}

	public static void resetZIndex() {
		lastDialogZIndex = 0;
	}

	public static native int calcZIndex(String selector) /*-{
		var zmax = 0;
		$wnd.$(selector).each(function() {
			var cur = parseInt($wnd.$(this).css('z-index'));
			if (cur != 9999)//jesmaxzindex for alerts
				zmax = cur > zmax ? cur : zmax;
		});
		return parseInt(zmax) + 3;
	}-*/;

	public static native void centerInWindow(Element el) /*-{
		$wnd.$(el).centerInWindow();
	}-*/;

	public static void bindClick(final Element el, final Command execute) {
		bindClick(JsUtil.ensureId(el), execute);
	}

	public static native void bindClick(String id, Command execute) /*-{
		$wnd.$("#" + id).click(function() {
			execute.@com.google.gwt.user.client.Command::execute()();
		});
	}-*/;

	public static void disableInputs(final HasWidgets hasWidgets) {
		toggleState((Widget) hasWidgets, false);
	}

	public static void enableInputs(final HasWidgets hasWidgets) {
		toggleState((Widget) hasWidgets, true);
	}

	public static void toggleState(final Widget widget, final boolean enabled) {
		if (widget == null)
			return;
		if (widget instanceof HasWidgets) {
			HasWidgets hw = (HasWidgets) widget;
			Iterator<Widget> iter = hw.iterator();
			while (iter.hasNext())
				toggleState(iter.next(), enabled);
		}
		if (widget instanceof IUserInputWidget)
			((IUserInputWidget) widget).setEnabled(enabled);
	}

	public static boolean runDataBindingReads(final HasWidgets hasWidgets) {
		boolean result = true;
		Iterator<Widget> iter = hasWidgets.iterator();
		while (iter.hasNext()) {
			if (!_runDataBindingReads(iter.next()))
				result = false;
		}
		return result;
	}

	private static boolean _runDataBindingReads(Widget widget) {
		boolean result = true;
		if (widget instanceof HasWidgets) {
			HasWidgets hw = (HasWidgets) widget;
			Iterator<Widget> iter = hw.iterator();
			while (iter.hasNext()) {
				Widget w = iter.next();
				if (!_runDataBindingReads(w)) {
					result = false;
				}
			}
		}
		if (widget instanceof IDataBindable) {
			IDataBindable vw = (IDataBindable) widget;
			if (vw.getDataBindingHandler() != null)
				try {
					vw.getDataBindingHandler().execute(true);
				} catch (Exception e) {
					JsUtil.handleError(vw, e);
				}
		}
		return result;
	}

	public static boolean validateInputs(final HasWidgets hasWidgets) {
		boolean result = true;
		Iterator<Widget> iter = hasWidgets.iterator();
		while (iter.hasNext()) {
			if (!validateInputs(iter.next(), false))
				result = false;
		}
		if (!result) {
			shakeWidget((Widget) hasWidgets);
			Notification.showNotification(ClientContext.nlsCommon.bilgiEksik(), ClientContext.nlsCommon.isaretliAlanlariKontrolEdin(), NotificationType.warning);
		}
		return result;
	}

	private static boolean validateInputs(final Widget widget, boolean focusedBefore) {
		boolean result = true;
		if (widget instanceof HasWidgets) {
			HasWidgets hw = (HasWidgets) widget;
			Iterator<Widget> iter = hw.iterator();
			while (iter.hasNext()) {
				Widget w = iter.next();
				if (!validateInputs(w, focusedBefore)) {
					result = false;
					focusedBefore = true;
				}
			}
		}
		if (widget instanceof IUserInputWidget) {
			IUserInputWidget vw = (IUserInputWidget) widget;
			if (!vw.validate(focusedBefore))
				result = false;
		}
		if (widget instanceof IDataBindable) {
			IDataBindable db = (IDataBindable) widget;
			if (db.getDataBindingHandler() != null)
				try {
					db.getDataBindingHandler().execute(false);
				} catch (Exception e) {
					JsUtil.handleError(db, e);
				}
		}
		return result;
	}

	public static void addCloseButton(final Form that, ButtonBar bb) {
		Button b = new Button(that, "kapat", ClientContext.nlsCommon.kapat());
		b.setTitle(ClientContext.nlsCommon.kapat() + " (Esc)");
		b.setIcon(FaIcon.close);
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				that.closeWindow();
			}
		});
		bb.add(b);
	}

	public static void addCancelButton(final Form that, ButtonBar bb) {
		Button b = new Button(that, "vazgec", ClientContext.nlsCommon.vazgec());
		b.setTitle(ClientContext.nlsCommon.vazgec() + " (Esc)");
		b.setIcon(FaIcon.close);
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				that.closeWindow();
			}
		});
		bb.add(b);
	}

	public static String getUrlForImage(String imagePath) {
		return getImagesPath() + imagePath;
	}

	public static String getImagesPath() {
		return GWT.getModuleBaseForStaticFiles() + "images/";
	}

	public static String getAppletsPath() {
		return GWT.getModuleBaseForStaticFiles() + "applets/";
	}

	public static String calcSizeForMaxLength(int length) {
		return length >= 50 ? WidgetConst.WIDTH_WIDE : (length >= 35 ? WidgetConst.WIDTH_BIG : (length >= 20 ? WidgetConst.WIDTH_MIDDLE : Math.max(length - 1, 5) + "em"));
	}

	public static Date toMonthStart(Date date) {
		CalendarUtil.setToFirstDayOfMonth(date);
		return date;
	}

	public static Date toMonthEnd(Date date) {
		CalendarUtil.setToFirstDayOfMonth(date);
		CalendarUtil.addMonthsToDate(date, 1);
		CalendarUtil.addDaysToDate(date, -1);
		return date;
	}

	public static Date monthsAfter(int months, Date date) {
		CalendarUtil.addMonthsToDate(date, months);
		return date;
	}

	public static Date daysAfter(int amount) {
		return daysAfter(amount, new Date());
	}

	public static Date daysAfter(int amount, Date date) {
		Date r = new Date();
		CalendarUtil.addDaysToDate(r, amount);
		return r;
	}

	public static Date daysBefore(int amount) {
		return daysBefore(amount, new Date());
	}

	public static Date daysBefore(int amount, Date date) {
		return daysAfter(-1 * amount, date);
	}

	public static native JsArrayInteger getParentWidthHeight(Element element) /*-{
		var el = $wnd.$(element).parent();
		return [ el.width(), el.height() ];
	}-*/;

	public static native void hoverStyle(Element element, String hoverState, String normalState) /*-{
		$wnd.$(element).hover(function() {
			$wnd.$(this).removeClass(normalState).addClass(hoverState);
		}, function() {
			$wnd.$(this).removeClass(hoverState).addClass(normalState);
		});
	}-*/;

	public static void shakeWidget(Widget widget) {
		applyEffect(widget.getElement(), JqEffect.shake);
	}

	public static void pulsateWidget(Widget widget) {
		pulsate(widget.getElement());
	}

	public static void pulsate(Element elm) {
		applyEffect(elm, JqEffect.pulsate);
	}

	public static void slide(Element elm) {
		applyEffect(elm, JqEffect.slide);
	}

	public static void highlight(Element elm) {
		applyEffect(elm, JqEffect.highlight);
	}

	public static native void transfer(JavaScriptObject fromEl, JavaScriptObject toEl) /*-{
		$wnd.$(fromEl).effect("transfer", {
			to : toEl
		}, 500);
	}-*/;

	public static native void showElement(Element element, JqEffect effect) /*-{
		$wnd.$(element).show(effect.toString());
	}-*/;

	public static native void applyEffect(Element element, JqEffect effect) /*-{
		$wnd.$(element).effect(effect.toString());
	}-*/;

	public static native void setElementData(Element element, String key, JavaScriptObject data) /*-{
		$wnd.$.data(element, key, data);
	}-*/;

	public static native JavaScriptObject getElementData(Element element, String key) /*-{
		return $wnd.$.data(element, key);
	}-*/;

	public static String repeat(String s, int times) {
		return times == 0 ? "" : new String(new char[times]).replace("\0", s);
	}

	public static JSONValue asJsonArray(List<? extends Serializable> vals) {
		if (vals == null || vals.isEmpty())
			return null;
		JSONArray arr = new JSONArray();
		int i = 0;
		for (Serializable s : vals) {
			if (s instanceof String)
				arr.set(i++, new JSONString((String) s));
			else if (s instanceof Number)
				arr.set(i++, new JSONNumber(((Number) s).doubleValue()));
			else if (s instanceof Date)
				arr.set(i++, new JSONString(asString((Date) s)));
			else
				arr.set(i++, new JSONString(s.toString()));
		}
		return arr;
	}

	public static String strLeft(String str, int left) {
		if (str != null && str.length() > left)
			return str.substring(0, left);
		return str;
	}

	public static String getServiceUrl(IJsonServicePoint servicePoint) {
		if (ClientContext.instance instanceof ProvidesModuleUtils)
			return ((ProvidesModuleUtils) ClientContext.instance).getModuleServiceTarget(servicePoint.getModuleId()).getServiceEntryPoint() + "." + ((Enum) servicePoint).toString();
		return null;
	}

	public static boolean	USE_BOOTSTRAP	= false;

	public static Integer nvl(Integer val, int i) {
		if (val == null)
			return i;
		return val;
	}

	public static native void draggable(Element el, JavaScriptObject opts) /*-{
		$wnd.$(el).draggable(opts);
	}-*/;

	public static native void setNumeralLibLanguage(String cultureCode) /*-{
		$wnd.numeral.language(cultureCode);
	}-*/;

	public static String createNumeralFormat(int decimals, boolean emptyDecimal, String currSymbol) {
		String f = "0,0";
		if (decimals == 0)
			return f;
		if (emptyDecimal)
			f += "[.][";
		else
			f += ".";
		for (int i = 0; i < decimals; i++)
			f += "0";
		if (emptyDecimal)
			f += "]";
		if (currSymbol != null)
			f += " " + currSymbol;
		return f;
	}

	public static String createNumeralFormat(int decimals, boolean emptyDecimal) {
		return createNumeralFormat(decimals, emptyDecimal, null);
	}

	public static ArrayList<Long> asArrayListLong(JsArrayNumber jsarray) {
		ArrayList<Long> list = new ArrayList<Long>();
		for (int i = 0; i < jsarray.length(); i++)
			list.add(Long.valueOf((long) jsarray.get(i)));
		return list.isEmpty() ? null : list;
	}

	public static ArrayList<Long> asArrayListLong(JsArrayString jsarray) {
		ArrayList<Long> list = new ArrayList<Long>();
		for (int i = 0; i < jsarray.length(); i++)
			list.add(Long.valueOf(jsarray.get(i)));
		return list.isEmpty() ? null : list;
	}

}