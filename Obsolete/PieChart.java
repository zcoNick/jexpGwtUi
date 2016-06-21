package com.javexpress.gwt.library.ui.data.jqplot;

import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class PieChart extends BaseLabelValueChart {

	public PieChart(final Widget parent, final String id) {
		this(parent, id, false);
	}

	public PieChart(final Widget parent, final String id, boolean fitToParent) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.JQPLOT_PIE_PREFIX, id);
		if (fitToParent) {
			setWidth("auto");
			setHeight("100%");
		}
	}

	private native JavaScriptObject createByJs(PieChart x, String id, JavaScriptObject data, String title) /*-{
		var options = {
			seriesDefaults : {
				renderer : $wnd.$.jqplot.PieRenderer,
				rendererOptions : {
					showDataLabels : true,
					dataLabels : 'value',
					dataLabelFormatString : '%.2f'
				}
			},
			legend : {
				show : true,
				location : 'e'
			}
		};
		if (title)
			options.title = title;
		var el = $wnd.$.jqplot(id, [ data ], options);
		$wnd.$("#" + id).bind("jqplotDataClick",
				function(e, seriesIndex, pointIndex, data) {
					//$('#info2c').html('series: '+seriesIndex+', point: '+pointIndex+', data: '+data+ ', pageX: '+e.pageX+', pageY: '+e.pageY);
				});
		return el;
	}-*/;

	@Override
	public void setValueMap(Map<String, Number> map) {
		// plot1.replot();???
		if (widget != null)
			destroyByJs(widget);
		widget = null;
		if (map != null)
			widget = createByJs(this, getElement().getId(), createDataArray(map), getTitle());
	}

}