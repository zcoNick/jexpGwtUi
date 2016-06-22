package com.javexpress.gwt.library.ui.data.jqplot;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.common.model.item.ILabelValueSerieItem;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class FlotPieChart extends FlotBaseLabelValueChart {

	public FlotPieChart(final Widget parent, final String id) {
		this(parent, id, false);
	}

	public FlotPieChart(final Widget parent, final String id, boolean fitToParent) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.FLOT_PIE_PREFIX, id);
		if (fitToParent) {
			setWidth("auto");
			setHeight("100%");
		}
	}

	@Override
	protected JsArray createDataArray(final ArrayList<? extends ILabelValueSerieItem> result) {
		JsArray data = JsArray.createArray().cast();
		if (result != null && !result.isEmpty()) {
			for (ILabelValueSerieItem<? extends Number> item : result)
				if (item.getV() != null && item.getL() != null) {
					JSONObject o = new JSONObject();
					o.put("data", new JSONNumber(item.getV().doubleValue()));
					if (getLabelRenderer() != null)
						o.put("label", new JSONString(getLabelRenderer().render(item.getL())));
					else
						o.put("label", new JSONString(item.getL()));
					data.push(o.getJavaScriptObject());
				}
		}
		return data;
	}

	@Override
	public void setValue(ArrayList<? extends ILabelValueSerieItem> result) {
		if (widget == null)
			widget = createByJs(this, getElement().getId(), createDataArray(result), getTitle());
		else
			super.setValue(result);
	}

	protected native JavaScriptObject createByJs(FlotPieChart x, String id, JavaScriptObject data, String title) /*-{
		if (!data || data.length == 0)
			return null;
		var options = {
			series : {
				pie : {
					show : true,
					label : {
						show : true,
						formatter : function(label, series) {
							var element = '<div style="font-size:9pt;text-align:center;padding:2px;color:'
									+ 'black'//series.color
									+ ';">'
									+ label
									+ '<br/>'
									+ series.data[0][1] + '</div>';
							return element;
						}
					}
				}
			},
			legend : {
				show : true
			},
			grid : {
				hoverable : true,
				clickable : true
			}
		};
		if (title)
			options.title = title;
		options.colors = $wnd.JexpUI.Colorizer(data);
		var el = $wnd.$.plot("#" + id, data, options);
		$wnd.$("#" + id).bind("plotclick", function(event, pos, item) {
			// axis coordinates for other axes, if present, are in pos.x2, pos.x3, ...
			// if you need global screen coordinates, they are pos.pageX, pos.pageY
			if (item) {
				$wnd.console.debug(item.series, item.datapoint);
			}
		});
		return el;
	}-*/;

}