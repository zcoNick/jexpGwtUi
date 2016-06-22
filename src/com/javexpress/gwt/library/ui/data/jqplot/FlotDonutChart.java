package com.javexpress.gwt.library.ui.data.jqplot;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class FlotDonutChart extends FlotPieChart {

	public FlotDonutChart(final Widget parent, final String id) {
		this(parent, id, false);
	}

	public FlotDonutChart(final Widget parent, final String id, boolean fitToParent) {
		super(parent, id, fitToParent);
	}

	@Override
	protected native JavaScriptObject createByJs(FlotPieChart x, Element el, String id, JavaScriptObject data,
			String title) /*-{
		if (!data || data.length == 0)
			return null;
		var options = {
			series : {
				pie : {
					show : true,
					radius : 1,
					innerRadius : 0.5,
					label : {
						show : true,
						radius : 0.8,
						formatter : function(label, series) {
							var element = '<div style="font-size:9pt;text-align:center;padding:2px;color:'
									+ 'white'//series.color
									+ ';">'
									+ label
									+ '<br/>'
									+ series.data[0][1] + '</div>';
							return element;
						},
						threshold : 0.1
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
		$wnd.$(el).bind(
				"plothover",
				function(event, pos, item) {
					if (item) {
						$wnd.console.debug("donutitem hover: series="
								+ (item.seriesIndex + 1) + ", x="
								+ item.datapoint[0].toFixed(2) + ", y="
								+ item.datapoint[1].toFixed(2), item);
					}
				});

		$wnd.$(el).bind(
				"plotclick",
				function(event, pos, item) {
					if (item) {
						$wnd.console.debug("donutitem click: series="
								+ (item.seriesIndex + 1) + ", x="
								+ item.datapoint[0].toFixed(2) + ", y="
								+ item.datapoint[1].toFixed(2), item);
					}
				});
		return el;
	}-*/;

}