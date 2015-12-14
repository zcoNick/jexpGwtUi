package com.javexpress.gwt.library.ui.data.jqplot;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public abstract class JqPlotPanel extends SimplePanel implements ISizeAwareWidget {

	public static WidgetBundles fillResources(WidgetBundles parent, boolean bars, boolean pies) {
		WidgetBundles jexp = new WidgetBundles("JqPlot Charting", parent);
		jexp.addStyleSheet("scripts/jqplot/jquery.jqplot.min.css");
		jexp.addJavaScript("scripts/jqplot/jquery.jqplot.min.js");
		if (bars)
			jexp.addJavaScript("scripts/jqplot/jqplot.barRenderer.min.js");
		if (pies) {
			jexp.addJavaScript("scripts/jqplot/jqplot.pieRenderer.min.js");
			jexp.addJavaScript("scripts/jqplot/jqplot.categoryAxisRenderer.min.js");
		}
		return jexp;
	}

	private String title;

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(final String title) {
		this.title = title;
	}

	public JqPlotPanel() {
		super(DOM.createDiv());
		if (!JsUtil.USE_BOOTSTRAP)
			getElement().addClassName("ui-widget-content");
	}

	@Override
	public void onResize() {
		refresh();
	}

	protected abstract void refresh();

}