package com.javexpress.gwt.library.ui.form.autocomplete;

import java.io.Serializable;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.IJsonServicePoint;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.bootstrap.BaseWrappedInput;
import com.javexpress.gwt.library.ui.facet.ProvidesModuleUtils;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class AutoCompleteBox<V extends Serializable> extends BaseWrappedInput<String> {

	private JsonMap					options;
	private IAutoCompleteListener	listener;
	private Command					newItemRequestCommand;
	private String					newItemTitle;
	private Element					indicator;

	public IAutoCompleteListener getListener() {
		return listener;
	}

	public void setListener(final IAutoCompleteListener listener) {
		this.listener = listener;
	}

	/** Designer compatible constructor */
	public AutoCompleteBox(final Widget parent, final String id) {
		this(parent, id, null);
	}

	@Deprecated
	public AutoCompleteBox(final Widget parent, final String id, final IJsonServicePoint servicePoint) {
		super(parent, WidgetConst.AUTOCOMPLETE_PREFIX, id, "jexpAutoComplete");

		input = DOM.createInputText().cast();
		JsUtil.ensureSubId(getElement(), input, "inp");
		getElement().appendChild(input);

		indicator = DOM.createSpan();
		indicator.setClassName("input-group-addon jexpHandCursor");
		indicator.setInnerHTML("<i class='fa fa-search'></i>");
		getElement().appendChild(indicator);

		options = new JsonMap();
		setMinLength(2);
		if (servicePoint != null)
			setListing(servicePoint);
	}

	public void setMinLength(int minLength) {
		options.setInt("minLength", minLength);
	}

	public void setListing(IJsonServicePoint servicePoint) {
		if (servicePoint == null)
			options.clear("url");
		else
			options.set("url", JsUtil.getServiceUrl(servicePoint));
	}

	public void setListingAlias(String controlData) throws Exception {
		if (ClientContext.instance instanceof ProvidesModuleUtils) {
			String[] cds = controlData.split("/");
			if (cds.length >= 2) {
				String url = ((ProvidesModuleUtils) ClientContext.instance).getModuleServiceTarget(Long.valueOf(cds[0])).getServiceEntryPoint() + "." + controlData.substring(controlData.indexOf("/") + 1);
				options.set("url", url);
				return;
			}
		}
		throw new Exception("ControlData is not resolvable");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, input, indicator, options.getJavaScriptObject(), newItemTitle, listener != null);
	}

	private native void createByJs(AutoCompleteBox<V> x, Element input, Element indicator, JavaScriptObject options, String newItemTitle, boolean hasListener) /*-{
		var el = $wnd.$(input);
		el.attr("v", "");
		if (hasListener) {
			options.source = function(request, response) {
				x.@com.javexpress.gwt.library.ui.form.autocomplete.AutoCompleteBox::fireOnSearch(Lcom/google/gwt/core/client/JavaScriptObject;)(request);
				$wnd.$.post(options.url, request, function(data) {
					response(data);
				}, "json");
			}
		} else
			options.source = options.url;
		options.id = el.attr("id") + "_menu";
		options.select = function(event, ui) {
			var r = x.@com.javexpress.gwt.library.ui.form.autocomplete.AutoCompleteBox::fireOnSelect(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(ui.item.id,ui.item.label,ui.item.data);
			if (r) {
				el.attr("v", ui.item.id);
				el.val(ui.item.label);
				$wnd.$.data(el, "acdata", ui.item.data);
			}
			return r;
		};
		options.search = function(event, ui) {
			el.attr("v", "");
			$wnd.$(".fa", $wnd.$(indicator)).removeClass("fa-search").addClass(
					"fa-spin").addClass("fa-spinner");
		};
		options.response = function(event, ui) {
			$wnd.$(".fa", $wnd.$(indicator)).removeClass("fa-spin")
					.removeClass("fa-spinner").addClass("fa-search");
		};
		el = el
				.jexpautocomplete(options)
				.on(
						"blur",
						function() {
							if (el.attr("v") != "" && el.val().trim() == "") {
								el.attr("v", "");
								$wnd.$.data(el, "acdata", null);
								x.@com.javexpress.gwt.library.ui.form.autocomplete.AutoCompleteBox::fireOnSelect(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(null,null,null);
							}
						});
		if (newItemTitle) {
			var btn = $wnd
					.$("<span class='ui-icon ui-icon-plus ui-cursor-hand' style='display:inline-block' tabindex='500' title='"
							+ newItemTitle + "'></span>");
			parent.append(btn);
			btn
					.click(function() {
						x.@com.javexpress.gwt.library.ui.form.autocomplete.AutoCompleteBox::fireOnNewItemRequest()();
					});
		}
		if (indicator) {
			$wnd
					.$(indicator)
					.click(
							function() {
								x.@com.javexpress.gwt.library.ui.form.autocomplete.AutoCompleteBox::fireOnButtonClick()();
							});
		}
	}-*/;

	@Override
	protected void onUnload() {
		listener = null;
		newItemRequestCommand = null;
		options = null;
		destroyByJs(input, indicator);
		indicator = null;
		super.onUnload();
	}

	private native void destroyByJs(Element elm, Element btn) /*-{
		$wnd.$(btn).off();
		var el = $wnd.$(elm);
		if (el)
			el.jexpautocomplete('destroy');
	}-*/;

	public void setValue(final V value, final String label) {
		((InputElement) input).setValue(label);
		input.setAttribute("v", value.toString());
	}

	@Override
	public String getValue() {
		String v = input.getAttribute("v");
		if (v == null || JsUtil.isEmpty(v.trim()))
			return null;
		return v;
	}

	public Long getValueLong() {
		return JsUtil.asLong(getValue());
	}

	public String getText() {
		return ((InputElement) input).getValue();
	}

	public void onNewItemRequest(String newItemTitle, final Command newItemRequestCommand) {
		this.newItemTitle = newItemTitle;
		this.newItemRequestCommand = newItemRequestCommand;
	}

	@Override
	public void clear() {
		((InputElement) input).setValue("");
		String old = input.getAttribute("v");
		input.setAttribute("v", "");
		try {
			if (JsUtil.isNotEmpty(old))
				fireOnSelect(null, null, null);
		} catch (Exception e) {
			JsUtil.handleError(getParent(), e);
		}
	}

	public void setValue(final Long value) {
		setValueLong(value);
	}

	public void setValueLong(final Long value) {
		setValue(value == null ? (String) null : value.toString());
	}

	@Override
	public boolean setValue(final String value) {
		if (value == null)
			clear();
		else {
			_setValueById(this, input, options.getJavaScriptObject(), value);
		}
		return true;
	}

	private native void _setValueById(AutoCompleteBox<V> x, Element input, JavaScriptObject options, String value) /*-{
		var lb = $wnd.$(input).val("...");
		$wnd.$
				.post(
						options.url,
						{
							"term" : "@" + value
						},
						function(data) {
							if (!data)
								return;
							data = data[0];
							var r = x.@com.javexpress.gwt.library.ui.form.autocomplete.AutoCompleteBox::fireOnSelect(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(data.id,data.label,data.data);
							if (r) {
								lb.val(data.label);
								$wnd.$.data(lb, "acdata", data.data);
								lb.attr("v", data.id);
							}
						}, "json");
	}-*/;

	public JsonMap getData() {
		JavaScriptObject o = JsUtil.getElementData(getElement(), "acdata");
		return o == null ? null : new JsonMap(o);
	}

	// ---------- EVENTS
	private void fireOnSearch(JavaScriptObject postData) {
		if (listener != null)
			listener.onAutoCompleteBeforeDataRequest(new JsonMap(postData));
	}

	private boolean fireOnSelect(final String id, final String label, JavaScriptObject data) throws Exception {
		if (listener != null)
			return listener.itemSelected(id, label, data != null ? new JsonMap(data) : null);
		return true;
	}

	private void fireOnButtonClick() throws Exception {
		if (listener != null)
			listener.buttonClicked();
	}

	private void fireOnNewItemRequest() throws Exception {
		if (newItemRequestCommand != null)
			newItemRequestCommand.execute();
	}

	public void setMaxLength(int maxLength) {
		((InputElement) input).setMaxLength(maxLength);
	}

	@Override
	protected boolean setRawValue(String value) {
		((InputElement) input).setValue(value);
		return true;
	}

}