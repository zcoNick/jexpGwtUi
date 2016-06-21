package com.javexpress.gwt.library.ui.data.jqplot;

import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Widget;
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
		if (true)
			options.colors = [ '#3c8dbc', 'yellow', 'green', 'red', 'orange',
					'green', 'blue', 'purple' ];
		else
			options.colors = $wnd.$.map(data, function(o, i) {
				return $wnd.$.Color({
					hue : (i * 360 / data.length),
					saturation : 0.95,
					lightness : 0.35,
					alpha : 1
				}).toHexString();
			});
		if (title)
			options.title = title;
		var bar_data = {
			data : data,
		};
		$wnd.console.debug(bar_data);
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
	public void setValueMap(Map<String, Number> map) {
		if (widget != null)
			destroyByJs(getElement(), widget);
		widget = null;
		if (map != null)
			widget = createByJs(this, getElement().getId(), createDataArray(map), getTitle());
	}

}