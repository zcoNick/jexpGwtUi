package com.javexpress.gwt.library.ui.data.jqplot;

import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class BarChart extends JqPlotPanel {

	private JavaScriptObject		widget;
	private IJqPlotMapDataSupplier	dataSupplier;

	public IJqPlotMapDataSupplier getDataSupplier() {
		return dataSupplier;
	}

	public void setDataSupplier(final IJqPlotMapDataSupplier dataSupplier) {
		this.dataSupplier = dataSupplier;
	}

	public BarChart(final Widget parent, final String id) {
		this(parent, id, false);
	}

	public BarChart(final Widget parent, final String id, boolean fitToParent) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.JQPLOT_BAR_PREFIX, id);
		if (fitToParent) {
			setWidth("auto");
			setHeight("100%");
		}
	}

	private native JavaScriptObject createByJs(BarChart x, String id, JavaScriptObject data, String title) /*-{
		var options = {
			seriesDefaults : {
				renderer : $wnd.$.jqplot.BarRenderer,
			},
			axes : {
				xaxis : {
					renderer : $wnd.$.jqplot.CategoryAxisRenderer
				}
			}
		};
		if (title)
			options.title = title;
		var el = $wnd.$.jqplot(id, [ data ], options);
		return el;
	}-*/;

	private JavaScriptObject createDataArray(final Map<String, Number> rd) {
		JavaScriptObject data = JsArray.createArray().cast();
		for (String k : rd.keySet())
			pushItemsAsArray(data, k, rd.get(k).doubleValue());
		return data;
	}

	protected native void pushItemsAsArray(JavaScriptObject data, String k, double v) /*-{
		var d = new Array();
		d.push(k);
		d.push(v);
		data.push(d);
	}-*/;

	@Override
	public void refresh() {
		if (widget != null)
			destroyByJs(widget);
		widget = null;
		if (dataSupplier != null) {
			Map<String, Number> map = dataSupplier.populateData();
			widget = createByJs(this, getElement().getId(), createDataArray(map), getTitle());
		}
	}

	@Override
	protected void onUnload() {
		dataSupplier = null;
		destroyByJs(widget);
		widget = null;
		super.onUnload();
	}

	private native void destroyByJs(JavaScriptObject widget) /*-{
		widget.destroy();
	}-*/;

}