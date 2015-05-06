package com.javexpress.gwt.library.ui.form.autocomplete;

import java.beans.Beans;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.common.model.item.type.Pair;
import com.javexpress.gwt.library.shared.model.IJsonServicePoint;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class SearchListBox extends JexpSimplePanel implements Focusable {

	private final FlexTable	table;
	private final TextBox	textBox;
	private final ListBox	listBox;
	private String			searchTitle;
	private JsonMap			options;

	/** Designer compatible constructor */
	public SearchListBox(final Widget parent, final String id) {
		this(parent, id, null);
	}

	public SearchListBox(final Widget parent, final String id, final IJsonServicePoint servicePoint) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.SEARCHLIST_PREFIX, id);
		addStyleName("jexpBorderBox");
		options = new JsonMap();
		setListing(servicePoint);
		options.setInt("minLength", 2);
		table = new FlexTable();
		table.setWidth("100%");
		table.setHeight("100%");
		textBox = new TextBox();
		JsUtil.ensureSubId(getElement(), textBox.getElement(), "t");
		listBox = new ListBox(true);
		listBox.setWidth("100%");
		listBox.setHeight("100%");
		JsUtil.ensureSubId(getElement(), listBox.getElement(), "l");
		setWidget(table);
	}

	public void setListing(IJsonServicePoint servicePoint) {
		if (servicePoint != null)
			options.set("source", JsUtil.getServiceUrl(servicePoint));
		else
			options.clear("source");
	}

	public String getSearchTitle() {
		return searchTitle;
	}

	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}

	@Override
	protected void onLoad() {
		if (searchTitle != null) {
			table.setWidget(0, 0, new Label(searchTitle));
			table.setWidget(0, 1, textBox);
			table.setWidget(1, 0, listBox);
			table.getCellFormatter().getElement(1, 0).setAttribute("colspan", "2");
		} else {
			table.setWidget(0, 0, textBox);
			table.setWidget(1, 0, listBox);
		}
		table.getRowFormatter().getElement(0).setAttribute("height", "1em");
		table.getCellFormatter().getElement(1, 0).setAttribute("height", "100%");
		super.onLoad();
		if (!Beans.isDesignTime())
			createByJs(this, textBox.getElement().getId(), listBox.getElement(), options.getJavaScriptObject());
	}

	private native void createByJs(SearchListBox x, String id, Element list, JavaScriptObject options) /*-{
																										var el = $wnd.$("#" + id);
																										var ls = $wnd.$(list);
																										el.keyup(function() {
																										clearTimeout($wnd.$.data(this, 'timer'));
																										var wait = setTimeout(function() {
																										$wnd.$.ajax({
																										url : options.source,
																										data : {
																										term : el.val()
																										},
																										dataType : "json",
																										beforeSend : function(jqXHR, settings) {
																										list.innerHTML = '';
																										},
																										success : function(data, textStatus, jqXHR) {
																										for (var i = 0; i < data.length; i++) {
																										$wnd.$(
																										"<option value='" + data[i].id + "'>"
																										+ data[i].label + "</option>")
																										.appendTo(ls);
																										}
																										}
																										});
																										}, 300);
																										$wnd.$(this).data('timer', wait);
																										});
																										}-*/;

	@Override
	protected void onUnload() {
		options = null;
		super.onUnload();
		destroyByJs(textBox.getElement());
	}

	private native void destroyByJs(Element el) /*-{
												$wnd.$(el).remove();
												}-*/;

	@Override
	public int getTabIndex() {
		return 0;
	}

	@Override
	public void setAccessKey(char key) {
		textBox.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		textBox.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		textBox.setTabIndex(index);
	}

	public List<Pair<String, String>> getSelectedOptions() {
		List<Pair<String, String>> ls = new ArrayList<Pair<String, String>>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.isItemSelected(i)) {
				Pair<String, String> p = new Pair<String, String>();
				p.setLeft(listBox.getValue(i));
				p.setRight(listBox.getItemText(i));
				ls.add(p);
			}
		}
		return ls.isEmpty() ? null : ls;
	}

}