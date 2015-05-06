package com.javexpress.gwt.library.ui.form.autocomplete;

import java.beans.Beans;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.IJsonServicePoint;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.form.label.Label;
import com.javexpress.gwt.library.ui.js.JqEffect;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class MultiAutoCompleteBox<V extends Serializable> extends AbstractContainer implements Focusable {

	private TableElement				table;
	private InputElement				input;
	private DivElement					div;
	private IMultiAutoCompleteListener	listener;
	private JsonMap						options;
	private Map<String, JsonMap>		acData;
	private Element						tdOpen;
	private Label						btOpen;

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
		super(DOM.createTable());
		JsUtil.ensureId(parent, this, WidgetConst.MULTIAUTOCOMPLETE_PREFIX, id);

		addStyleName("jexpBorderBox");

		if (fitToParent) {
			getElement().getStyle().setWidth(100, Unit.PCT);
			getElement().getStyle().setHeight(100, Unit.PCT);
		}

		table = getElement().cast();
		table.addClassName("ui-widget ui-widget-content ui-corner-all jesMultiAutoCompleteBox");
		table.setCellPadding(2);
		table.setCellSpacing(0);

		Element tr = DOM.createTR();
		tr.setAttribute("height", "1%");
		tr.addClassName("ui-widget-header");
		tr.getStyle().setFontWeight(FontWeight.NORMAL);
		Element td = DOM.createTD();
		td.setAttribute("align", "right");
		Element label = DOM.createSpan();
		label.setInnerText(searchText + " : ");
		td.appendChild(label);
		tr.appendChild(td);
		td = DOM.createTD();
		input = DOM.createInputText().cast();
		input.getStyle().setWidth(97, Unit.PCT);
		JsUtil.ensureSubId(getElement(), input, WidgetConst.AUTOCOMPLETE_PREFIX);
		td.appendChild(input);
		tr.appendChild(td);
		tdOpen = DOM.createTD();
		tr.appendChild(tdOpen);
		table.appendChild(tr);

		tr = DOM.createTR();
		tr.setAttribute("height", "99%");
		td = DOM.createTD();
		td.setAttribute("colspan", "3");
		td.setAttribute("height", "100%");
		div = DOM.createDiv().cast();
		div.getStyle().setWidth(100, Unit.PCT);
		div.getStyle().setHeight(100, Unit.PCT);
		div.getStyle().setOverflow(Overflow.AUTO);
		td.appendChild(div);
		tr.appendChild(td);
		table.appendChild(tr);

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

	private native void createByJs(MultiAutoCompleteBox x, InputElement element, JavaScriptObject options, boolean hasListener) /*-{
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
		options = null;
		input = null;
		div = null;
		table = null;
		acData = null;
		if (btOpen != null)
			remove(btOpen);
		tdOpen = null;
		btOpen = null;
		if (!Beans.isDesignTime())
			destroyByJs(input);
		super.onUnload();
	}

	private native void destroyByJs(InputElement element) /*-{
		$wnd.$(element).jexpautocomplete('destroy');
	}-*/;

	public List<String> getValues() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < div.getChildCount(); i++) {
			Element t = (Element) div.getChild(i);
			list.add(t.getAttribute("value"));
		}
		return list.isEmpty() ? null : list;
	}

	private Element findElement(String id) {
		for (int i = 0; i < div.getChildCount(); i++) {
			Element t = (Element) div.getChild(i);
			if (id.equals(t.getAttribute("value")))
				return t;
		}
		return null;
	}

	public List<Long> getValuesLong() {
		List<Long> list = new ArrayList<Long>();
		for (int i = 0; i < div.getChildCount(); i++) {
			Element t = (Element) div.getChild(i);
			list.add(JsUtil.asLong(t.getAttribute("value")));
		}
		return list.isEmpty() ? null : list;
	}

	private native void _addCloseEvent(MultiAutoCompleteBox<V> x, Element span) /*-{
		$wnd
				.$(span)
				.click(
						function() {
							var item = $wnd.$(this).parent().parent().parent();
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
		TableElement t = DOM.createTable().cast();
		t.setAttribute("align", "center");
		t.addClassName("jesMultiAutoCompleteListItem");
		t.setCellPadding(2);
		t.setCellSpacing(0);
		t.getStyle().setDisplay(Display.NONE);
		t.getStyle().setWidth(99, Unit.PCT);
		t.setAttribute("value", id);
		TableRowElement tr = DOM.createTR().cast();
		TableRowElement td = DOM.createTD().cast();
		td.setAttribute("width", "98%");
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
		td.setAttribute("width", "1%");
		Element span = DOM.createSpan();
		span.addClassName("ui-icon ui-icon-only ui-icon-trash ui-cursor-hand");
		_addCloseEvent(this, span);
		td.appendChild(span);
		tr.appendChild(td);
		t.appendChild(tr);
		div.insertFirst(t);
		if (data != null) {
			if (acData == null)
				acData = new HashMap<String, JsonMap>();
			acData.put(id, jsonmapdata);
		}
		fireOnAdd(id, jsonmapdata, userAction);
		JsUtil.showElement(t, JqEffect.slide);
	}

	private void fireOnSearch(JavaScriptObject postData) {
		if (listener != null)
			listener.onAutoCompleteBeforeDataRequest(new JsonMap(postData));
	}

	public void removeItems() {
		for (int i = 0; i < div.getChildCount(); i++) {
			Element item = (Element) div.getChild(i);
			String val = item.getAttribute("value");
			if (acData != null)
				acData.remove(val);
			item.removeFromParent();
		}
	}

	public void setValues(Map<V, String> values) {
		removeItems();
		if (values == null || values.isEmpty())
			return;
		for (Serializable v : values.keySet()) {
			addItem(v, values.get(v));
		}
	}

}