package com.javexpress.gwt.library.ui.data.jqplot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javexpress.common.model.item.IPieChartItem;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class FlotBaseLabelValueChart extends FlotPanel
		implements AsyncCallback<ArrayList<? extends IPieChartItem>> {

	protected native void pushItemsAsArray(JavaScriptObject data, String k, double v) /*-{
		var d = new Array();
		d.push(k);
		d.push(v);
		data.push(d);
	}-*/;

	protected JavaScriptObject createDataArray(final Map<String, Number> rd) {
		JavaScriptObject data = JsArray.createArray().cast();
		for (String k : rd.keySet())
			pushItemsAsArray(data, k, rd.get(k).doubleValue());
		return data;
	}

	@Override
	public void onFailure(Throwable caught) {
		JsUtil.handleError(this, caught);
	}

	@Override
	public void onSuccess(ArrayList<? extends IPieChartItem> result) {
		setValue(result);
	}

	public void setValue(ArrayList<? extends IPieChartItem> result) {
		HashMap<String, Number> map = null;
		if (result != null && !result.isEmpty()) {
			map = new LinkedHashMap<String, Number>(result.size());
			for (IPieChartItem<? extends Number> item : result) {
				String label = null;
				if (getLabelRenderer() != null)
					label = getLabelRenderer().render(item.getL());
				else
					label = item.getL();
				map.put(label, item.getV());
			}
		}
		setValueMap(map);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		refresh();
	}

	public abstract void setValueMap(Map<String, Number> map);

}