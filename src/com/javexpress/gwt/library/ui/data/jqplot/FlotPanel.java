package com.javexpress.gwt.library.ui.data.jqplot;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public abstract class FlotPanel extends SimplePanel implements ISizeAwareWidget {

	public static WidgetBundles fillResources(WidgetBundles parent, boolean bars, boolean pies) {
		WidgetBundles core = new WidgetBundles("Flot Charting 0.8.3", parent);
		core.addJavaScript("scripts/flot/jquery.flot.min.js");

		WidgetBundles extra = new WidgetBundles("Flot Charting Extras", core);
		extra.addJavaScript("scripts/flot/jquery.flot.resize.min.js");
		if (bars)
			extra.addJavaScript("scripts/flot/jquery.flot.categories.js");
		if (pies)
			extra.addJavaScript("scripts/flot/jquery.flot.pie.min.js");
		return extra;
	}

	protected JavaScriptObject widget;
	private String title;
	private boolean showLegend;
	private Command dataRequester;
	private IChartLabelRenderer labelRenderer;

	public IChartLabelRenderer getLabelRenderer() {
		return labelRenderer;
	}

	public void setLabelRenderer(IChartLabelRenderer labelRenderer) {
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

	public FlotPanel() {
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
		destroyByJs(getElement(), widget);
		widget = null;
		super.onUnload();
	}

	protected native void destroyByJs(Element el, JavaScriptObject widget) /*-{
		$wnd.$(el).off();
		widget.destroy();
	}-*/;

	public void refresh() {
		if (dataRequester != null)
			dataRequester.execute();
	}

}