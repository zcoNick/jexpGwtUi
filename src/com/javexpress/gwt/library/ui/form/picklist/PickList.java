package com.javexpress.gwt.library.ui.form.picklist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class PickList<V extends Serializable> extends FlexTable {

	private Element																		selected,
			nonSelected, btAdd, btRemove;
	private PickListLazyDataSupplier<? extends Serializable, ? extends Serializable>	mapSupplier;
	private boolean																		autoLoad	= true;

	//http://www.bootply.com/lFEZ9qOonX
	public PickList(final Widget parent, final String id, final String selectedLabel, final String nonSelectedLabel) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.PICKLIST_PREFIX, id);
		addStyleName("jexpPickList");
		setHeight("100px");
		if (selectedLabel != null || nonSelectedLabel != null)
			createLabelRow(selectedLabel, nonSelectedLabel);
		createListRow();
	}

	public boolean isAutoLoad() {
		return autoLoad;
	}

	public void setAutoLoad(boolean autoLoad) {
		this.autoLoad = autoLoad;
	}

	private void createLabelRow(final String selectedLabel, final String nonSelectedLabel) {
		Element left = DOM.createDiv();
		left.setClassName("col-xs-5");
		Element hl = DOM.createElement("h6");
		hl.setClassName("light-blue col-xs-10");
		hl.setInnerHTML(selectedLabel);
		left.appendChild(hl);
		Element il = DOM.createInputCheck();
		il.setClassName("all col-xs-2");
		left.appendChild(il);
		getElement().appendChild(left);

		Element right = DOM.createDiv();
		right.setClassName("col-xs-offset-2 col-xs-5");
		Element hr = DOM.createElement("h6");
		hr.setClassName("light-blue col-xs-10");
		hr.setInnerHTML(nonSelectedLabel);
		right.appendChild(hr);
		Element ir = DOM.createInputCheck();
		ir.setClassName("all col-xs-2");
		right.appendChild(ir);
		getElement().appendChild(right);
	}

	@Override
	public void setHeight(String height) {
		selected.getStyle().setProperty("height", height);
		nonSelected.getStyle().setProperty("height", height);
	}

	private void createListRow() {
		selected = DOM.createDiv();
		selected.getStyle().setOverflow(Overflow.AUTO);
		selected.setClassName("list-group col-xs-5");
		JsUtil.ensureSubId(getElement(), selected, "s");
		getElement().appendChild(selected);

		Element btDiv = DOM.createDiv();
		btDiv.setClassName("jexpPickListButtons col-xs-2");
		btAdd = DOM.createButton();
		btAdd.setClassName("btn btn-white col-xs-8");
		Element ia = DOM.createElement("i");
		ia.setClassName("fa fa-arrow-left");
		btAdd.appendChild(ia);
		btDiv.appendChild(btAdd);
		btRemove = DOM.createButton();
		btRemove.setClassName("btn btn-white col-xs-8");
		Element ir = DOM.createElement("i");
		ir.setClassName("fa fa-arrow-right");
		btRemove.appendChild(ir);
		btDiv.appendChild(btRemove);
		getElement().appendChild(btDiv);

		nonSelected = DOM.createDiv();
		nonSelected.getStyle().setOverflow(Overflow.AUTO);
		nonSelected.setClassName("list-group col-xs-5");
		JsUtil.ensureSubId(getElement(), nonSelected, "a");
		getElement().appendChild(nonSelected);
	}

	public void addItem(V value, String label, boolean isSelected) {
		addListItem(isSelected ? selected : nonSelected, value.toString(), label);
	}

	private void addListItem(Element ls, String value, String title) {
		Element a = DOM.createAnchor();
		a.setClassName("list-group-item");
		Element s = DOM.createSpan();
		s.setInnerHTML(title);
		a.appendChild(s);
		Element i = DOM.createInputCheck();
		i.setPropertyString("value", value);
		i.setClassName("pull-right");
		a.appendChild(i);
		ls.appendChild(a);
	}

	public void clearItems() {
		JsUtil.clearChilds(selected);
		JsUtil.clearChilds(nonSelected);
	}

	public PickList setItems(final PickListLazyDataSupplier<? extends Serializable, ? extends Serializable> mapSupplier) {
		this.mapSupplier = mapSupplier;
		if (isAttached())
			load();
		return this;
	}

	public void load() {
		clearItems();
		if (mapSupplier != null) {
			mapSupplier.fillItems(this);
		} else
			fireItemsChanged();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(getElement(), btAdd, btRemove, nonSelected, selected);
		if (isAutoLoad())
			load();
	}

	private native void createByJs(Element el, Element add, Element remove, Element nonSelected, Element selected) /*-{
		$wnd.$(remove).click(function(e) {
			$wnd.$(".all", $wnd.$(el)).prop("checked", false);
			var items = $wnd.$("input:checked:not('.all')", $wnd.$(selected));
			var n = items.length;
			if (n > 0) {
				items.each(function(idx, item) {
					var choice = $wnd.$(item);
					choice.prop("checked", false);
					choice.parent().appendTo($wnd.$(nonSelected));
				});
			}
			return false;
		});

		$wnd.$(add).click(
				function(e) {
					$wnd.$('.all', $wnd.$(el)).prop("checked", false);
					var items = $wnd.$("input:checked:not('.all')", $wnd
							.$(nonSelected));
					var n = items.length;
					if (n > 0) {
						items.each(function(idx, item) {
							var choice = $wnd.$(item);
							choice.prop("checked", false);
							choice.parent().appendTo($wnd.$(selected));
						});
					}
					return false;
				});
	}-*/;

	private native void _bindSelects(Element el, Element nonSelected, Element selected) /*-{
		$wnd.$(".all", $wnd.$(el)).click(
				function(e) {
					e.stopPropagation();
					var $this = $wnd.$(this);
					if ($this.is(":checked")) {
						$this.parents('.list-group').find("[type=checkbox]")
								.prop("checked", true);
					} else {
						$this.parents('.list-group').find("[type=checkbox]")
								.prop("checked", false);
						$this.prop("checked", false);
					}
				});

		$wnd.$(".list-group a", $wnd.$(el)).click(function(e) {
			e.stopPropagation();
			var $this = $wnd.$(this).find("[type=checkbox]");
			if ($this.is(":checked")) {
				$this.prop("checked", false);
			} else {
				$this.prop("checked", true);
			}
			if ($this.hasClass("all")) {
				$this.trigger("click");
			}
		});
	}-*/;

	@Override
	protected void onUnload() {
		selected = null;
		nonSelected = null;
		btAdd = null;
		btRemove = null;
		mapSupplier = null;
		destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element el) /*-{
		$wnd.$(el).empty().off();
	}-*/;

	public LinkedHashMap<String, String> getSelectionsMap() {
		LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (int i = 0; i < selected.getChildCount(); i++) {
			Element sp = selected.getChild(i).getChild(0).cast();
			InputElement in = selected.getChild(i).getChild(1).cast();
			vals.put(in.getValue(), sp.getInnerHTML());
		}
		return vals;
	}

	public List<String> getSelectionValues() {
		List<String> vals = new ArrayList<String>();
		for (int i = 0; i < selected.getChildCount(); i++) {
			InputElement in = selected.getChild(i).getChild(1).cast();
			vals.add(in.getValue());
		}
		return vals;
	}

	public void setSelectedValues(final List<String> selValues) {
		for (String sel : selValues)
			selectItem(sel);
	}

	public void selectItem(final String sel) {
		for (int i = 0; i < nonSelected.getChildCount(); i++) {
			InputElement in = nonSelected.getChild(i).getChild(1).cast();
			if (in.getValue().equals(sel)) {
				Node a = in.getParentNode();
				selected.appendChild(a);
				break;
			}
		}
	}

	public void unselectItem(final String sel) {
		for (int i = 0; i < selected.getChildCount(); i++) {
			InputElement in = selected.getChild(i).getChild(1).cast();
			if (in.getValue().equals(sel)) {
				Node a = in.getParentNode();
				nonSelected.appendChild(a);
				break;
			}
		}
	}

	public void fireItemsChanged() {
		_bindSelects(getElement(), nonSelected, selected);
	}

	public void refresh() {
		load();
	}

}