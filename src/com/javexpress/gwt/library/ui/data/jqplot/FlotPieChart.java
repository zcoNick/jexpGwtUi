package com.javexpress.gwt.library.ui.data.jqplot;

import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
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
	protected JavaScriptObject createDataArray(final Map<String, Number> rd) {
		JsArray data = JsArray.createArray().cast();
		for (String k : rd.keySet()) {
			JSONObject o = new JSONObject();
			o.put("label", new JSONString(k));
			o.put("data", new JSONNumber(rd.get(k).doubleValue()));
			data.push(o.getJavaScriptObject());
		}
		return data;
	}

	protected native JavaScriptObject createByJs(FlotPieChart x, String id, JavaScriptObject data, String title) /*-{
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
		options.colors = [ '#3c8dbc', 'yellow', 'green', 'red', 'orange',
				'green', 'blue', 'purple' ];
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

	@Override
	public void setValueMap(Map<String, Number> map) {
		if (widget != null)
			destroyByJs(getElement(), widget);
		widget = null;
		if (map != null)
			widget = createByJs(this, getElement().getId(), createDataArray(map), getTitle());
	}

}