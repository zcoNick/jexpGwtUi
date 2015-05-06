package com.javexpress.gwt.library.ui.form.picklist;

import java.beans.Beans;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.fw.ui.data.control.Label;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.JqIcon;
import com.javexpress.gwt.library.ui.data.tree.Tree;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class PickTree<V extends Serializable> extends FlexTable {

	private ListBox	selected;
	private Tree	nonSelected;

	public PickTree(final Widget parent, final String id, final String selectedLabel, final String nonSelectedLabel) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.PICKTREE_PREFIX, id);
		addStyleName("jexpBorderBox");
		setWidth("100%");
		setHeight("100%");
		if (selectedLabel != null || nonSelectedLabel != null)
			createLabelRow(selectedLabel, nonSelectedLabel);
		createListRow();
	}

	private void createLabelRow(final String selectedLabel, final String nonSelectedLabel) {
		Label label = new Label(selectedLabel);
		setWidget(0, 0, label);

		label = new Label(nonSelectedLabel);
		setWidget(0, 2, label);

		getRowFormatter().getElement(0).setAttribute("height", "1em");
	}

	private void createListRow() {
		selected = new ListBox(true);
		JsUtil.ensureSubId(getElement(), selected.getElement(), "_s");
		selected.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(final DoubleClickEvent event) {
				if (selected.getSelectedIndex() == -1)
					return;
				deselectOption();
			}
		});
		nonSelected = new Tree(this, "ns", true);
		JsUtil.ensureSubId(getElement(), nonSelected.getElement(), "_a");

		selected.getElement().getStyle().setWidth(100, Unit.PCT);
		selected.getElement().getStyle().setHeight(100, Unit.PCT);
		setWidget(1, 0, selected);
		getCellFormatter().getElement(1, 0).setAttribute("width", "47%");

		FlexTable btTable = new FlexTable();
		btTable.setWidget(0, 0, createButton("l", JqIcon.triangle_1_w, "Seçime Taşı", true, false));
		btTable.setWidget(2, 0, createButton("r", JqIcon.triangle_1_e, "Seçim Dışına Taşı", false, false));
		btTable.setWidget(3, 0, createButton("rr", JqIcon.seek_next, "Tümünü Seçim Dışına Taşı", false, true));
		setWidget(1, 1, btTable);
		getCellFormatter().getElement(1, 1).setAttribute("valign", "middle");
		getCellFormatter().getElement(1, 1).setAttribute("align", "center");

		setWidget(1, 2, nonSelected);
		getCellFormatter().getElement(1, 2).setAttribute("width", "47%");

		getRowFormatter().getElement(1).setAttribute("height", "100%");
	}

	protected void deselectOption() {
	}

	private Button createButton(final String id, final ICssIcon icon, final String title, final boolean isToSelection, final boolean isAll) {
		Button b = new Button();
		JsUtil.ensureSubId(getElement(), b.getElement(), "_" + id);
		b.getElement().setAttribute("class", "pickListButton");
		b.getElement().setAttribute("icon", icon.getCssClass());
		b.setHeight("1.5em");
		b.setTitle(title);
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (isToSelection) {
					selectItem(nonSelected.getSelectedItemValue());
				} else {
					List<String> sels = new ArrayList<String>();
					for (int i = 0; i < selected.getItemCount(); i++)
						if (selected.isItemSelected(i) || isAll)
							sels.add(selected.getValue(i));
					for (String sel : sels)
						deselectItem(sel);
				}
			}
		});
		return b;
	}

	public void clearItems() {
		selected.clear();
		nonSelected.clear();
	}

	public PickTree setItems(final PickTreeLazyDataSupplier<? extends Serializable, ? extends Serializable> mapSupplier) {
		clearItems();
		mapSupplier.fillItems(this);
		return this;
	}

	public void addItem(final V value, final String label, final Boolean selected) {
		if (selected)
			this.selected.addItem(label, value.toString());
		else {
			nonSelected.addItem(label, value.toString());
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!Beans.isDesignTime()) {
			renderButtons(getElement());
		}
	}

	private native void renderButtons(Element elm) /*-{
		$wnd.$(".pickListButton", $wnd.$(elm)).each(function(index, value) {
			var el = $wnd.$(this);
			el.button({
				icons : {
					primary : el.attr("icon")
				}
			});
		});
	}-*/;

	@Override
	protected void onUnload() {
		selected = null;
		nonSelected = null;
		if (!Beans.isDesignTime())
			destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element el) /*-{
		$wnd.$(".pickListButton", $wnd.$(el)).button('destroy');
	}-*/;

	public LinkedHashMap<String, String> getSelectionsMap() {
		LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (int i = 0; i < selected.getItemCount(); i++)
			vals.put(selected.getValue(i), selected.getItemText(i));
		return vals;
	}

	public List<String> getSelectionValues() {
		List<String> vals = new ArrayList<String>();
		for (int i = 0; i < selected.getItemCount(); i++)
			vals.add(selected.getValue(i));
		return vals;
	}

	public void setSelectedValues(final List<String> selValues) {
		for (String sel : selValues)
			selectItem(sel);
	}

	public void selectItem(final String sel) {
		selected.addItem(nonSelected.getItemText(sel), sel);
		nonSelected.removeItem(sel);
	}

	public void deselectItem(final String sel) {
		List<Element> rem = new ArrayList<Element>();
		for (int i = 0; i < selected.getItemCount(); i++) {
			if (selected.getValue(i).equals(sel)) {
				nonSelected.addItem(selected.getItemText(i), selected.getValue(i));
				selected.removeItem(i);
				rem.add((Element) selected.getElement().getChild(i));
			}
		}
		for (Element el : rem)
			el.removeFromParent();
	}

}