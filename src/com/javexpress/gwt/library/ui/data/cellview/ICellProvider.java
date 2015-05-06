package com.javexpress.gwt.library.ui.data.cellview;

import com.google.gwt.user.client.ui.Widget;

public interface ICellProvider<T> {

	public Widget createCellWidget(final CellView<T> cellView, T dto);

}