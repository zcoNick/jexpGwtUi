package com.javexpress.gwt.library.ui.data.cellview;

import java.io.Serializable;
import java.util.Collection;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.menu.IHasPopupMenu;
import com.javexpress.gwt.library.ui.menu.JqPopupMenu;

public class CellView<T> extends AbstractContainer implements IHasPopupMenu {

	private TableElement		grid;
	private byte				mod;
	private int					last	= -1;
	private ICellProvider<T>	cellProvider;
	private Serializable		widgetData;

	public Serializable getWidgetData() {
		return widgetData;
	}

	public void setWidgetData(Serializable widgetData) {
		this.widgetData = widgetData;
	}

	public CellView(final Widget parent, final String id, final Integer mod) {
		this(parent, id, mod, false);
	}

	public CellView(final Widget parent, final String id, final Integer mod, final boolean fitToParent) {
		super(DOM.createDiv());
		getElement().addClassName("jexpCellView");
		this.mod = mod.byteValue();
		JsUtil.ensureId(parent, this, WidgetConst.CELLVIEW_PREFIX, id);
		if (fitToParent) {
			setWidth("auto");
			getElement().getStyle().setHeight(100, Unit.PCT);
		}
		grid = DOM.createTable().cast();
		grid.getStyle().setWidth(100, Unit.PCT);
		getElement().appendChild(grid);
	}

	public void setPadding(final int value) {
		grid.setCellPadding(value);
	}

	public void setSpacing(final int value) {
		grid.setCellSpacing(value);
	}

	public ICellProvider<T> getCellProvider() {
		return cellProvider;
	}

	public void setCellProvider(final ICellProvider<T> cellProvider) {
		this.cellProvider = cellProvider;
	}

	@Override
	public void add(final Widget child) {
		if (child == null)
			return;
		int col = ++last % mod;
		Element row = (Element) grid.getLastChild();
		if (col == 0) {
			row = DOM.createTR();
			row.setAttribute("valign", "top");
			for (byte b = 0; b < mod; b++) {
				Element td = DOM.createTD();
				int percell = 100 / mod;
				td.getStyle().setWidth(percell, Unit.PCT);
				td.setId(getElement().getId() + "_" + grid.getChildCount() + "_" + b);
				row.appendChild(td);
			}
			grid.appendChild(row);
		}
		Element td = (Element) row.getChild(col);
		td.appendChild(child.getElement());
		super.add(child, td);
	}

	@Override
	public void clear() {
		super.clear();
		last = -1;
	}

	public void setData(final Collection<T> data) throws Exception {
		clear();
		if (cellProvider == null)
			throw new Exception("Please set a cellProvider");
		for (T dto : data) {
			Widget w = cellProvider.createCellWidget(this, dto);
			add(w);
		}
	}

	public Widget getSelectedWidget() {
		for (Widget widget : getChildren())
			if (widget.getElement().getClassName().indexOf("ui-state-highlight") > -1)
				return widget;
		return null;
	}

	public void setSelection(final Serializable value) {
		for (Widget widget : getChildren()) {
			if (widget instanceof CellPanel) {
				if (((CellPanel) widget).getData().equals(value)) {
					((CellPanel) widget).setSelected(true);
					break;
				}
			}
		}
	}

	public void setSelectedIndex(final int index) {
		((CellPanel) getChildren().get(index)).setSelected(true);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		grid = null;
		cellProvider = null;
		super.onUnload();
	}

	@Override
	public HandlerRegistration addContextMenuHandler(ContextMenuHandler handler) {
		return addDomHandler(handler, ContextMenuEvent.getType());
	}

	@Override
	public void setPopupMenu(JqPopupMenu menu) {
		addContextMenuHandler(menu);
	}

	@Override
	public boolean canOpenContextMenu(JqPopupMenu menu) throws Exception {
		return true;
	}

}