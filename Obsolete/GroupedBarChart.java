package com.javexpress.gwt.library.ui.data.jqplot;

import java.beans.Beans;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class GroupedBarChart extends JqPlotPanel {

	private JavaScriptObject				widget;
	private IJqPlotGroupSeriesDataSupplier	dataSupplier;

	public IJqPlotGroupSeriesDataSupplier getDataSupplier() {
		return dataSupplier;
	}

	public void setDataSupplier(final IJqPlotGroupSeriesDataSupplier dataSupplier) {
		this.dataSupplier = dataSupplier;
	}

	public GroupedBarChart(final Widget parent, final String id) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.JQPLOT_BAR_PREFIX, id);
	}

	private native JavaScriptObject createByJs(GroupedBarChart x, String id, JavaScriptObject groups, JavaScriptObject serieTitles, JavaScriptObject data, String title) /*-{
		var options = {
			seriesDefaults : {
				renderer : $wnd.$.jqplot.BarRenderer,
			},
			series : serieTitles,
			axes : {
				xaxis : {
					renderer : $wnd.$.jqplot.CategoryAxisRenderer,
					ticks : groups
				}
			}
		};
		if (title)
			options.title = title;
		var el = $wnd.$.jqplot(id, [ data ], options);
		return el;
	}-*/;

	@Override
	public void onResize() {
		if (!Beans.isDesignTime()) {
			if (dataSupplier != null) {
				refresh();
			}
		}
	}

	private JavaScriptObject createTickArray(final List<String> ticks) {
		JavaScriptObject data = JsArray.createArray().cast();
		for (String k : ticks)
			pushItems(data, k);
		return data;
	}

	protected native void pushItems(JavaScriptObject data, String k) /*-{
		data.push(k);
	}-*/;

	protected native void pushItems(JavaScriptObject data, Number k) /*-{
		data.push(k);
	}-*/;

	protected native void pushItems(JavaScriptObject data, JavaScriptObject k) /*-{
		data.push(k);
	}-*/;

	private JavaScriptObject createGroupedDataArray(final Map<String, List<Number>> map) {
		JavaScriptObject data = JsArray.createArray().cast();
		for (String k : map.keySet()) {
			List<Number> ls = map.get(k);
			JavaScriptObject sd = JsArray.createArray().cast();
			for (Number n : ls)
				pushItems(sd, n);
			pushItems(data, sd);
		}
		return data;
	}

	private JavaScriptObject createSerieTitleArray(final Map<String, List<Number>> map) {
		JavaScriptObject data = JsArray.createArray().cast();
		for (String k : map.keySet()) {
			JSONObject json = new JSONObject();
			json.put("label", new JSONString(k));
			pushItems(data, json.getJavaScriptObject());
		}
		return data;
	}

	public void refresh() {
		//http://www.jqplot.com/tests/bar-charts.php
		List<String> groups = dataSupplier.getGroups();
		Map<String, List<Number>> map = dataSupplier.populateData();
		widget = createByJs(this, getElement().getId(), createTickArray(groups), createSerieTitleArray(map), createGroupedDataArray(map), getTitle());
	}

	@Override
	protected void onUnload() {
		dataSupplier = null;
		destroyByJs(widget);
		widget = null;
		super.onUnload();
	}

}