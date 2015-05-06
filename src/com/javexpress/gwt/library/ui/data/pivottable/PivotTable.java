package com.javexpress.gwt.library.ui.data.pivottable;

import java.beans.Beans;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.JexpWidget;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class PivotTable extends JexpWidget implements ISizeAwareWidget{

	public static void fillResources(final List<String> styleSheets, final List<String> javaScripts) {
		styleSheets.add("scripts/pivottable/pivot.css");
		//javaScripts.add("scripts/pivottable/grid.locale-" + LocaleInfo.getCurrentLocale().getLocaleName() + ".js");
		javaScripts.add("scripts/pivottable/pivot-20140623.js");
		//javaScripts.add("scripts/pivottable/gchart_renderers-20131112.js");
	}
	
	public static enum PivotRenderer {
		Table("Table"),TableBarchart("Table Barchart"),Heatmap("Heatmap"),RowHeatmap("Row Heatmap"),ColHeatmap("Col Heatmap"),LineChart("Line Chart"),BarChart("Bar Chart"),
		StackedBarChart("Stacked Bar Chart"),AreaChart("Area Chart");
		protected String label;
		PivotRenderer(String label){
			this.label = label;
		}
		public String getLabel(){
			return label;
		}
	}

	protected JsonMap			options;
	private JavaScriptObject	widget;
	private Map<String,String>	fields = new LinkedHashMap<String,String>();
	private JSONArray rows = new JSONArray();
	private JSONArray cols = new JSONArray();
	private Serializable		widgetData;
	private IPivotTableListener listener;

	public IPivotTableListener getListener() {
		return listener;
	}

	public void setListener(IPivotTableListener listener) {
		this.listener = listener;
	}

	public Serializable getWidgetData() {
		return widgetData;
	}

	public void setWidgetData(Serializable widgetData) {
		this.widgetData = widgetData;
	}
	
	public void setRenderer(PivotRenderer renderer){
		options.set("rendererName", renderer.label);
	}
	
	public void addField(String label, String field){
		fields.put(field, label);
	}
	
	public void addRow(String field){
		rows.set(rows.size(), new JSONString(fields.get(field)));
	}

	public void addCol(String field){
		cols.set(cols.size(), new JSONString(fields.get(field)));
	}
	
	public PivotTable(final Widget parent, final String id, final ServiceDefTarget service, final Enum method) {
		this(parent, id, service, method, true);
	}

	public PivotTable(final Widget parent, final String id, final ServiceDefTarget service, final Enum method, final boolean autoLoad) {
		this(parent, id, service, method, autoLoad, null);
	}

	public PivotTable(final Widget parent, final String id, final ServiceDefTarget service, final Enum method, final boolean autoLoad, final JsonMap pOptions) {
		super();
		setElement(DOM.createTable());
		JsUtil.ensureId(parent, this, WidgetConst.JQGRID_PREFIX, id);
		options = pOptions == null ? createDefaultOptions(autoLoad) : pOptions;
		options.set("url", service.getServiceEntryPoint() + "." + method.toString());
	}

	private JsonMap createDefaultOptions(final boolean autoLoad) {
		options = new JsonMap();
		return options;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!Beans.isDesignTime()) {
			options.put("rows", rows);
			options.put("cols", cols);
			widget = createByJs(this, getElement(), options.getJavaScriptObject(), fields);
		}
	}

	private native JavaScriptObject createByJs(PivotTable x, Element element, JavaScriptObject options, Map<String, String> fields) /*-{
		var el = $wnd.$(element);
        var derivers = $wnd.$.pivotUtilities.derivers;
        options.renderers = $wnd.$.extend($wnd.$.pivotUtilities.renderers, $wnd.$.pivotUtilities.gchart_renderers);
        options.derivedAttributes = {};
        var iter = fields.keySet().iterator();
        while (iter.hasNext()){
        	var key = iter.next();
        	var l = fields.get(key);
        	options.derivedAttributes[l] = function(data){
        		return data[key];
        	};
        }
		$wnd.$.getJSON(options.url, x.@com.javexpress.gwt.library.ui.data.pivottable.PivotTable::calculatePostData()() )
			.done(function( json ) {
	            el.pivotUI(json, options);
			})
			.fail(function( jqxhr, textStatus, error ) {
				if ($wnd.console)
					$wnd.console.log( "Request Failed: " + err );
			});
		return el;
	}-*/;
	
	protected JavaScriptObject calculatePostData(){
		JsonMap postData = new JsonMap();
		if (listener!=null)
			listener.fillPivotPostData(postData);
		return postData.getJavaScriptObject();
	}

	@Override
	protected void onUnload() {
		options = null;
		widget = null;
		fields = null;
		rows = null;
		cols = null;
		widgetData = null;
		listener = null;
		destroyByJs(getElement(), getElement().getId() + "_pager");
		super.onUnload();
	}

	private native void destroyByJs(Element element, String pager) /*-{
		$wnd.$(element).remove();
	}-*/;

	@Override
	public void onResize() {
	}

}