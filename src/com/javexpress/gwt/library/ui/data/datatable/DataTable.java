package com.javexpress.gwt.library.ui.data.datatable;

import java.io.Serializable;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.javexpress.common.model.item.PagedData;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class DataTable<T extends Serializable> extends JexpSimplePanel implements AsyncCallback<PagedData<T>> {

	private ProvidesKey<T>	keyProvider;
	private int				pageSize	= 50;
	private DataGrid<T>		grid;

	public DataTable(Widget parent, String id) {
		this(parent, id, null);
	}

	public DataTable(Widget parent, String id, ProvidesKey<T> keyProvider) {
		super(DOM.createDiv());
		this.keyProvider = keyProvider;
		JsUtil.ensureId(parent, getElement(), WidgetConst.DATATABLE, id);
		grid = new DataGrid<T>(keyProvider);
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

}