package com.javexpress.gwt.library.ui.data.jqplot;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javexpress.common.model.item.ILabelValueSerieItem;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class FlotBaseLabelValueChart extends FlotPanel
		implements AsyncCallback<ArrayList<? extends ILabelValueSerieItem>> {

	@Override
	public void onFailure(Throwable caught) {
		JsUtil.handleError(this, caught);
	}

	@Override
	public void onSuccess(ArrayList<? extends ILabelValueSerieItem> result) {
		setValue(result);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		refresh();
	}

	public abstract void setValue(java.util.ArrayList<? extends ILabelValueSerieItem> result);

	/*
	 * public void setValue(java.util.ArrayList<? extends
	 * com.javexpress.common.model.item.ILabelValueSerieItem> result) { if
	 * (widget != null) { JsArray data = createDataArray(result); if (data ==
	 * null || data.length() == 0) { destroyByJs(getElement(), widget); widget =
	 * null; } else _refresh(this, widget, data); } }
	 */

	private native JavaScriptObject _refresh(FlotBaseLabelValueChart x, JavaScriptObject plot,
			JavaScriptObject data) /*-{
		plot.setData(data);
		plot.setupGrid();
		plot.draw();
		return plot;
	}-*/;

	protected abstract JsArray createDataArray(final ArrayList<? extends ILabelValueSerieItem> result);

}