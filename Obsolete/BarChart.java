package com.javexpress.gwt.library.ui.data.jqplot;

import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class BarChart extends BaseLabelValueChart {

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

	protected native JavaScriptObject createByJs(BarChart x, String id, JavaScriptObject data, String title) /*-{
		var options = {
			seriesDefaults : {
				renderer : $wnd.$.jqplot.BarRenderer,
				rendererOptions : {
					varyBarColor : true
				},
				pointLabels : {
					show : true
				}
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