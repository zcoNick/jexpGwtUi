package com.javexpress.gwt.library.ui.data.jqplot;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public abstract class JqPlotPanel extends SimplePanel implements ISizeAwareWidget {

	public static WidgetBundles fillResources(WidgetBundles parent, boolean bars, boolean pies) {
		WidgetBundles core = new WidgetBundles("JqPlot Charting", parent);
		core.addStyleSheet("scripts/jqplot/jquery.jqplot.min.css");
		core.addJavaScript("scripts/jqplot/jquery.jqplot.min.js");
		WidgetBundles extra = new WidgetBundles("JqPlot Charting Extras", core);
		if (bars)
			extra.addJavaScript("scripts/jqplot/jqplot.barRenderer.min.js");
		if (pies) {
			extra.addJavaScript("scripts/jqplot/jqplot.pieRenderer.min.js");
			extra.addJavaScript("scripts/jqplot/jqplot.categoryAxisRenderer.min.js");
		}
		return extra;
	}

	protected JavaScriptObject widget;
	private String title;
	private boolean showLegend;
	private Command dataRequester;
	private IJqPlotLabelRenderer labelRenderer;

	public IJqPlotLabelRenderer getLabelRenderer() {
		return labelRenderer;
	}

	public void setLabelRenderer(IJqPlotLabelRenderer labelRenderer) {
		this.labelRenderer = labelRenderer;
	}

	public Command getDataRequester() {
		return dataRequester;
	}

	public void setDataRequester(Command dataRequester) {
		this.dataRequester = dataRequester;
	}

	public boolean isShowLegend() {
		return showLegend;
	}

	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}

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

	@Override
	protected void onUnload() {
		dataRequester = null;
		labelRenderer = null;
		destroyByJs(widget);
		widget = null;
		super.onUnload();
	}

	protected native void destroyByJs(JavaScriptObject widget) /*-{
		widget.destroy();
	}-*/;

	public void refresh() {
		if (dataRequester != null)
			dataRequester.execute();
	}

}