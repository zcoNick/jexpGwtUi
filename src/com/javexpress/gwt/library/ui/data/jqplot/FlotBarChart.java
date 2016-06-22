package com.javexpress.gwt.library.ui.data.jqplot;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.common.model.item.ILabelValueSerieItem;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class FlotBarChart extends FlotBaseLabelValueChart {

	public FlotBarChart(final Widget parent, final String id) {
		this(parent, id, false);
	}

	public FlotBarChart(final Widget parent, final String id, boolean fitToParent) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.FLOT_PIE_PREFIX, id);
		if (fitToParent) {
			setWidth("auto");
			setHeight("100%");
		}
	}

	private native JavaScriptObject createByJs(FlotBarChart x, String id, JavaScriptObject data, String title) /*-{
		if (!data || data.length == 0)
			return null;
		var options = {
			series : {
				bars : {
					show : true,
					barWidth : 0.5,
					align : "center",
					label : {
						formatter : function(label, series) {
							var element = '<div style="font-size:9pt;text-align:center;padding:2px;color:'
									+ series.color
									+ ';">'
									+ label
									+ '<br/>'
									+ series.data[0][1] + '</div>';
							return element;
						}
					}
				}
			},
			xaxis : {
				mode : "categories",
				tickLength : 0
			},
			legend : {
				show : true
			},
			grid : {
				hoverable : true,
				clickable : true
			}
		};
		options.colors = $wnd.JexpUI.Colorizer;
		if (title)
			options.title = title;
		var bar_data = {
			data : data,
		};
		var el = $wnd.$.plot("#" + id, [ bar_data ], options);
		$wnd.$("#" + id).bind("plotclick", function(event, pos, item) {
			// axis coordinates for other axes, if present, are in pos.x2, pos.x3, ...
			// if you need global screen coordinates, they are pos.pageX, pos.pageY
			if (item) {
				$wnd.console.debug(item.series, item.datapoint);
			}
		});
		return el;
	}-*/;

	@Override
	public void setValue(ArrayList<? extends ILabelValueSerieItem> result) {
		if (widget == null)
			widget = createByJs(this, getElement().getId(), createDataArray(result), getTitle());
		else
			super.setValue(result);
	}

	protected native void pushItemsAsArray(JavaScriptObject data, String k, double v) /*-{
		var d = new Array();
		d.push(k);
		d.push(v);
		data.push(d);
	}-*/;

	@Override
	protected JsArray createDataArray(ArrayList<? extends ILabelValueSerieItem> result) {
		JsArray data = JsArray.createArray().cast();
		if (result != null && !result.isEmpty()) {
			for (ILabelValueSerieItem<? extends Number> item : result)
				if (item.getV() != null && item.getL() != null) {
					String label = getLabelRenderer() != null ? getLabelRenderer().render(item.getL()) : item.getL();
					pushItemsAsArray(data, label, item.getV().doubleValue());
				}
		}
		return data;
	}

}