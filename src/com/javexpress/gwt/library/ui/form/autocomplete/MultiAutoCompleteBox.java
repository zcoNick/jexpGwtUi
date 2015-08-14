package com.javexpress.gwt.library.ui.form.autocomplete;

import java.beans.Beans;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.IJsonServicePoint;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.form.label.Label;
import com.javexpress.gwt.library.ui.js.JqEffect;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class MultiAutoCompleteBox extends AbstractContainer implements IUserInputWidget<ArrayList<String>> {

	private boolean						required;
	private Element						input;
	private Element						items;
	private IMultiAutoCompleteListener	listener;
	private JsonMap						options;
	private Map<String, JsonMap>		acData;
	private Element						tdOpen;
	private Label						btOpen;
	private DataBindingHandler			dataBinding;

	public IMultiAutoCompleteListener getListener() {
		return listener;
	}

	public void setListener(IMultiAutoCompleteListener listener) {
		this.listener = listener;
	}

	/** Designer compatible constructor */
	public MultiAutoCompleteBox(Widget parent, String id, String searchText) {
		this(parent, id, searchText, false, null);
	}

	public MultiAutoCompleteBox(Widget parent, String id, String searchText, boolean fitToParent, final IJsonServicePoint servicePoint) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.MULTIAUTOCOMPLETE_PREFIX, id);
		getElement().addClassName((JsUtil.USE_BOOTSTRAP ? "" : "ui-widget ui-widget-content ui-corner-all ") + "jexpMultiAutoCompleteBox");

		if (fitToParent) {
			getElement().getStyle().setWidth(100, Unit.PCT);
			getElement().getStyle().setHeight(100, Unit.PCT);
		}

		Element tr = DOM.createDiv();
		tr.addClassName("col-xs-12");
		int xs = 12;
		if (searchText != null) {
			Element td = DOM.createDiv();
			td.setClassName("col-xs-3");
			Element label = DOM.createSpan();
			label.setInnerText(searchText + " : ");
			tr.appendChild(td);
			xs -= 3;
		}
		Element inpCont = DOM.createDiv();
		inpCont.addClassName("input-group jexpAutoComplete");
		input = DOM.createInputText();
		input.addClassName("col-xs-" + xs);
		JsUtil.ensureSubId(getElement(), input, WidgetConst.AUTOCOMPLETE_PREFIX);
		inpCont.appendChild(input);
		Element span = DOM.createSpan();
		span.setClassName("input-group-addon jexpHandCursor");
		Element icon = DOM.createElement("i");
		icon.setClassName("fa fa-search");
		span.appendChild(icon);
		inpCont.appendChild(span);
		tr.appendChild(inpCont);

		getElement().appendChild(tr);

		Element itemsDiv = DOM.createDiv();
		itemsDiv.addClassName("col-xs-12");
		itemsDiv.getStyle().setHeight(100, Unit.PCT);
		itemsDiv.getStyle().setOverflow(Overflow.AUTO);
		Element itemsTable = DOM.createTable();
		itemsTable.addClassName("table table-striped");
		items = DOM.createTBody();
		itemsTable.appendChild(items);
		itemsDiv.appendChild(itemsTable);
		getElement().appendChild(itemsDiv);

		options = new JsonMap();
		options.setInt("minLength", 2);
		if (servicePoint != null)
			setListing(servicePoint);
	}

	public void setListing(IJsonServicePoint servicePoint) {
		if (servicePoint == null)
			options.clear("url");
		else
			options.set("url", JsUtil.getServiceUrl(servicePoint));
	}

	public void setOpenButton(ICssIcon icon, String caption, String hint, final Command command) {
		Label l = new Label(this, "open");
		if (icon != null)
			l.setIcon(icon);
		if (caption != null)
			l.setText(caption);
		if (hint != null)
			l.setTitle(hint);
		l.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
		add(l, tdOpen);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!Beans.isDesignTime())
			createByJs(this, input, options.getJavaScriptObject(), listener != null);
	}

	private native void createByJs(MultiAutoCompleteBox x, Element element, JavaScriptObject options, boolean hasListener) /*-{
		var el = $wnd.$(element);
		if (hasListener) {
			options.source = function(request, response) {
				x.@com.javexpress.gwt.library.ui.form.autocomplete.MultiAutoCompleteBox::fireOnSearch(Lcom/google/gwt/core/client/JavaScriptObject;)(request);
				$wnd.$.post(options.url, request, function(data) {
					response(data);
				}, "json");
			}
		} else
			options.source = options.url;
		options.id = el.attr("id") + "_menu";
		options.select = function(event, ui) {
			var r = x.@com.javexpress.gwt.library.ui.form.autocomplete.MultiAutoCompleteBox::fireOnSelect(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(ui.item.id,ui.item.label,ui.item.data);
			if (r) {
				el.val(null);
				x.@com.javexpress.gwt.library.ui.form.autocomplete.MultiAutoCompleteBox::fireAddToSelection(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;Z)(ui.item.id,ui.item.label,ui.item.data, true);
			}
			return r;
		};
		options.close = function(event, ui) {
			el.val(null);
		};
		el = el.jexpautocomplete(options).on("blur", function() {
			el.val(null);
		});
	}-*/;

	@Override
	protected void onUnload() {
		destroyByJs(input, items);
		options = null;
		input = null;
		items = null;
		acData = null;
		if (btOpen != null)
			remove(btOpen);
		tdOpen = null;
		btOpen = null;
		super.onUnload();
	}

	private native void destroyByJs(Element element, Element items) /*-{
		$wnd.$(element).jexpautocomplete('destroy');
		$wnd.$(items).empty().off();
	}-*/;

	@Override
	public ArrayList<String> getValue() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < items.getChildCount(); i++) {
			Element t = (Element) items.getChild(i);
			list.add(t.getAttribute("value"));
		}
		return list.isEmpty() ? null : list;
	}

	public void setValue(ArrayList<? extends Serializable> ids) {
		removeItems();
		if (ids == null || ids.isEmpty())
			return;
		StringBuilder values = new StringBuilder();
		boolean first = true;
		for (Serializable id : ids) {
			if (!first)
				values.append(";");
			else
				first = false;
			values.append(id.toString());
		}
		_setValueByIds(this, options.getJavaScriptObject(), values.toString());
	}

	private Element findElement(String id) {
		for (int i = 0; i < items.getChildCount(); i++) {
			Element t = (Element) items.getChild(i);
			if (id.equals(t.getAttribute("value")))
				return t;
		}
		return null;
	}

	public List<Long> getValuesLong() {
		List<Long> list = new ArrayList<Long>();
		for (int i = 0; i < items.getChildCount(); i++) {
			Element t = (Element) items.getChild(i);
			list.add(JsUtil.asLong(t.getAttribute("value")));
		}
		return list.isEmpty() ? null : list;
	}

	private native void _addDeleteEvent(MultiAutoCompleteBox x, Element span) /*-{
		$wnd
				.$(span)
				.click(
						function() {
							var item = $wnd.$(this).parent().parent();
							var val = item.attr("value");
							item.fadeOut(300, function() {
								$wnd.$(this).remove();
							});
							x.@com.javexpress.gwt.library.ui.form.autocomplete.MultiAutoCompleteBox::fireOnRemove(Ljava/lang/String;Z)(val,true);
						});
	}-*/;

	@Override
	public int getTabIndex() {
		return 0;
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
	}

	public boolean addItem(Serializable val, String label) {
		return addItem(val, label, null);
	}

	public boolean addItem(Serializable val, String label, JsonMap data) {
		Element existing = findElement(val.toString());
		if (existing != null)
			return false;
		fireAddToSelection(val.toString(), label, data != null ? data.getJavaScriptObject() : null, false);
		return true;
	}

	public boolean removeItem(String val) throws Exception {
		Element existing = findElement(val.toString());
		if (existing == null)
			return false;
		if (acData != null)
			acData.remove(val);
		existing.removeFromParent();
		fireOnRemove(val, false);
		return true;
	}

	public JsonMap getData(String val) {
		if (acData == null)
			return null;
		return acData.get(val);
	}

	private native void _setValueByIds(MultiAutoCompleteBox x, JavaScriptObject options, String values) /*-{
		$wnd.$
				.post(
						options.url,
						{
							"term" : "@" + values
						},
						function(data) {
							if (!data)
								return;
							for (var i = 0; i < data.length; i++) {
								x.@com.javexpress.gwt.library.ui.form.autocomplete.MultiAutoCompleteBox::fireAddToSelection(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;Z)(data[i].id,data[i].label,data[i].data,true);
							}
						}, "json");
	}-*/;

	// ---------- EVENTS
	private boolean fireOnSelect(final String id, final String label, JavaScriptObject data) throws Exception {
		Element existing = findElement(id);
		if (existing != null) {
			JsUtil.pulsate(existing);
			return false;
		}
		if (listener != null)
			return listener.itemSelected(id, label, data == null ? null : new JsonMap(data));
		return true;
	}

	private void fireOnRemove(final String id, boolean userAction) throws Exception {
		if (listener != null)
			listener.itemRemoved(id, userAction);
	}

	private void fireOnAdd(final String id, JsonMap data, boolean userSelected) {
		if (listener != null)
			listener.itemAdded(id, data, userSelected);
	}

	private void fireAddToSelection(final String id, final String label, JavaScriptObject data, boolean userAction) {
		TableRowElement tr = DOM.createTR().cast();
		tr.getStyle().setDisplay(Display.NONE);
		tr.addClassName("jexpMultiAutoCompleteListItem");
		tr.setAttribute("value", id);
		TableRowElement td = DOM.createTD().cast();
		td.setClassName("col-xs-11");
		Element child = null;
		JsonMap jsonmapdata = data == null ? null : new JsonMap(data);
		if (listener != null)
			child = listener.createListItem(id, label, jsonmapdata, userAction);
		if (child == null) {
			child = DOM.createSpan();
			child.setInnerText(label);
		}
		td.appendChild(child);
		tr.appendChild(td);
		td = DOM.createTD().cast();
		td.setClassName("col-xs-1");
		td.getStyle().setTextAlign(TextAlign.CENTER);
		Element span = DOM.createSpan();
		span.addClassName("fa fa-minus jexpHandCursor");
		_addDeleteEvent(this, span);
		td.appendChild(span);
		tr.appendChild(td);
		items.insertFirst(tr);
		if (data != null) {
			if (acData == null)
				acData = new HashMap<String, JsonMap>();
			acData.put(id, jsonmapdata);
		}
		fireOnAdd(id, jsonmapdata, userAction);
		JsUtil.showElement(tr, JqEffect.highlight);
	}

	private void fireOnSearch(JavaScriptObject postData) {
		if (listener != null)
			listener.onAutoCompleteBeforeDataRequest(new JsonMap(postData));
	}

	public void removeItems() {
		for (int i = 0; i < items.getChildCount(); i++) {
			Element item = (Element) items.getChild(i);
			String val = item.getAttribute("value");
			if (acData != null)
				acData.remove(val);
			item.removeFromParent();
		}
	}

	public void setValues(Map<? extends Serializable, String> values) {
		removeItems();
		if (values == null || values.isEmpty())
			return;
		for (Serializable v : values.keySet())
			addItem(v, values.get(v));
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return null;
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
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public boolean validate(boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	@Override
	public void setEnabled(boolean locked) {
	}

	@Override
	public void setValidationError(String validationError) {
	}

}