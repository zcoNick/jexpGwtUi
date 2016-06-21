package com.javexpress.gwt.library.ui.data.datatable;

import java.io.Serializable;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.javexpress.common.model.item.PagedData;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class DataTable<T extends Serializable> extends SimpleLayoutPanel implements AsyncCallback<PagedData<T>> {

	private ProvidesKey<T> keyProvider;
	private Integer pageSize;
	private DataGrid<T> grid;

	private boolean multiSelect;

	public DataTable(Widget parent, String id, Integer rowsPerPage, ProvidesKey<T> keyProvider) {
		super();
		this.keyProvider = keyProvider;
		JsUtil.ensureId(parent, getElement(), WidgetConst.DATATABLE, id);
		grid = rowsPerPage != null ? new DataGrid<T>(pageSize = rowsPerPage.intValue(), keyProvider)
				: new DataGrid<T>(keyProvider);
		setWidget(grid);
	}

	public boolean isMultiSelect() {
		return multiSelect;
	}

	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}

	public void setEmptyTableWidget(Widget w) {
		grid.setEmptyTableWidget(w);
	}

	public Widget getEmptyTableWidget() {
		return grid.getEmptyTableWidget();
	}

	public void addColumn(Column<T, ?> column, String header) {
		grid.addColumn(column, header);
	}

	@Override
	public void onFailure(Throwable caught) {
		JsUtil.handleError(this, caught);
	}

	@Override
	public void onSuccess(PagedData<T> result) {
		grid.setRowCount(result.getCount());
		grid.setRowData(0, result.getData());
	}

	public void addColumn(Column<T, ?> col) {
		grid.addColumn(col);
	}

	@Override
	protected void onAttach() {
		if (!multiSelect) {
			final SelectionModel<T> selectionModel = new SingleSelectionModel<T>(keyProvider);
			grid.setSelectionModel(selectionModel);
			grid.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		}
		super.onAttach();
	}

	@Override
	protected void onUnload() {
		grid = null;
		keyProvider = null;
		super.onUnload();
	}

	public void setColumnWidth(Column<T, ?> col, String width) {
		if (width == null)
			return;
		if (width.endsWith("px"))
			grid.setColumnWidth(col, Double.valueOf(width.replaceFirst("px", "")), Unit.PX);
		if (width.endsWith("em"))
			grid.setColumnWidth(col, Double.valueOf(width.replaceFirst("em", "")), Unit.EM);
		if (width.endsWith("%"))
			grid.setColumnWidth(col, Double.valueOf(width.replaceFirst("%", "")), Unit.PCT);

	}

}