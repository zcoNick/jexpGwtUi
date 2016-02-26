package com.javexpress.gwt.library.ui.data.jqplot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.javexpress.common.model.item.IPieChartItem;

public abstract class BaseLabelValueChart extends JqPlotPanel {

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

	public void setValue(List<? extends IPieChartItem<? extends Number>> result) {
		HashMap<String, Number> map = null;
		if (result!=null&&!result.isEmpty()){
			map = new HashMap<String,Number>(result.size());
			for (IPieChartItem<? extends Number> item:result){
				String label = null;
				if (getLabelRenderer()!=null)
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