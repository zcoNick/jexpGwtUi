package com.javexpress.gwt.library.ui.data.slickgrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.container.panel.ContainerWithBar;
import com.javexpress.gwt.library.ui.data.Column;
import com.javexpress.gwt.library.ui.data.Column.ColumnAlign;
import com.javexpress.gwt.library.ui.data.GridToolItem;
import com.javexpress.gwt.library.ui.data.GridToolMenu;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public abstract class BaseSlickGrid<CT extends Column> extends ContainerWithBar {

	public static WidgetBundles fillResources(WidgetBundles parent) {
		WidgetBundles wb = new WidgetBundles("EditGrid", parent);
		wb.addStyleSheet("scripts/slickgrid/slick.grid.css");
		wb.addStyleSheet("scripts/slickgrid/slick.columnpicker.css");
		wb.addStyleSheet("scripts/slickgrid/slick.headermenu.css");
		//wb.addStyleSheet("scripts/slickgrid/slick.pager.css");
		wb.addJavaScript("scripts/slickgrid/slick.core.js");
		//wb.addJavaScript("scripts/slickgrid/slick.cellrangedecorator.js");
		//wb.addJavaScript("scripts/slickgrid/slick.cellrangeselector.js");
		wb.addJavaScript("scripts/slickgrid/slick.cellselectionmodel.js");
		wb.addJavaScript("scripts/slickgrid/slick.rowselectionmodel.js");
		//wb.addJavaScript("scripts/slickgrid/slick.rowmovemanager.js");
		//wb.addJavaScript("scripts/slickgrid/slick.formatters.js");
		wb.addJavaScript("scripts/slickgrid/slick.editors.js");
		wb.addJavaScript("scripts/slickgrid/slick.grid.js");
		wb.addJavaScript("scripts/slickgrid/jexp.dataview.js");
		wb.addJavaScript("scripts/slickgrid/slick.columnpicker.js");
		wb.addJavaScript("scripts/slickgrid/slick.headermenu.js");
		wb.addJavaScript("scripts/slickgrid/slick.groupitemmetadataprovider.js");
		wb.addJavaScript("scripts/slickgrid/slick.checkboxselectcolumn.js");
		//wb.addJavaScript("scripts/slickgrid/slick.pager.js");

		WidgetBundles jexp = new WidgetBundles("JavExpress DataGrid Extensions", wb);
		jexp.addJavaScript("scripts/slickgrid/jexp.remotemodel.js");
		jexp.addJavaScript("scripts/slickgrid/jexp.formatters.js");
		return jexp;
	}

	private JsonMap								options;
	private JsArray								data	= JsArray.createArray().cast();
	private JavaScriptObject					jsObject;
	private List<CT>							columns	= new ArrayList<CT>();
	private List<GridToolItem>					tools	= new ArrayList<GridToolItem>();
	private String								keyColumnName;
	private JavaScriptObject					dataView;
	private Serializable						widgetData;
	private com.google.gwt.user.client.Element	topPanel;

	public Serializable getWidgetData() {
		return widgetData;
	}

	public void setWidgetData(Serializable widgetData) {
		this.widgetData = widgetData;
	}

	public JavaScriptObject getDataView() {
		return dataView;
	}

	protected void setDataView(JavaScriptObject dv) {
		this.dataView = dv;
	}

	public String getKeyColumnName() {
		return keyColumnName;
	}

	public List<CT> getColumns() {
		return columns;
	}

	public void addColumn(CT column) {
		columns.add(column);
	}

	public void addToolItem(final GridToolItem toolItem) {
		tools.add(toolItem);
	}

	public void addToolItem(final GridToolItem toolItem, int index) {
		tools.add(index, toolItem);
	}

	public JavaScriptObject getJsObject() {
		return jsObject;
	}

	public JsonMap getOptions() {
		return options;
	}

	public void setKeyColumnName(String keyColumnName) {
		this.keyColumnName = keyColumnName;
	}

	protected BaseSlickGrid(Widget parent, String prefix, String id, boolean fitToParent, String keyColumnName, String header) {
		super(fitToParent);
		JsUtil.ensureId(parent, this, prefix, id);

		topPanel = header != null ? DOM.createDiv() : null;
		if (topPanel != null) {
			topPanel.setClassName("grid-header");
			topPanel.getStyle().setPosition(Position.ABSOLUTE);
			topPanel.getStyle().setDisplay(Display.BLOCK);
			topPanel.getStyle().setTop(0, Unit.PX);
			topPanel.getStyle().setLeft(0, Unit.PX);
			topPanel.getStyle().setRight(0, Unit.PX);
			topPanel.getStyle().setHeight(18, Unit.PX);
			setHeader(header);
		}

		createPanels();

		getElement().addClassName("jexpSlickGridContainer");

		JsUtil.ensureSubId(getElement(), getContainer(), "g");
		getContainer().addClassName("jexpSlickGridView");

		JsUtil.ensureSubId(getElement(), getToolContainer(), "t");
		getToolContainer().addClassName("jexpGridToolbar");

		options = createDefaultOptions();
		this.keyColumnName = keyColumnName;
	}

	@Override
	protected int createTopPanel() {
		return topPanel != null ? 18 : 0;
	}

	public void setHeader(String title) {
		if (topPanel != null)
			topPanel.setInnerHTML("<label>" + title + "</label>");
	}

	protected JsonMap createDefaultOptions() {
		JsonMap options = new JsonMap();
		options.set("forceFitColumns", true);
		options.setInt("headerRowHeight", 18);
		options.setInt("topPanelHeight", 18);
		options.set("enableColumnReorder", false);
		return options;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		JSONArray colModel = new JSONArray();
		int i = 0;
		for (CT column : columns) {
			JsonMap cm = createColumnModel(column, i);
			if (cm != null)
				colModel.set(i++, cm);
		}
		renderToolItems();
		jsObject = createJsObject(colModel);
	}

	protected abstract JavaScriptObject createJsObject(JSONArray colModel);

	protected void renderToolItems() {
		for (GridToolItem ti : tools)
			if (ti instanceof GridToolMenu)
				renderToolMenu((GridToolMenu) ti, true);
			else
				renderToolItem(ti, true);
	}

	protected Element renderToolItem(GridToolItem ti, boolean enabled) {
		Element span = addToolItemElement(ti.getId(), ti.getIcon(), ti.getCaption(), ti.getHint(), ti.getIconClass(), enabled, ti.isStartsWithSeparator(), ti.isEndsWithSeparator());
		_bindToolElement(ti, span);
		ti.setElement(span);
		return span;
	}

	protected Element renderToolMenu(GridToolMenu ti, boolean enabled) {
		if (ti.isStartsWithSeparator()) {
			Element divsep = DOM.createDiv();
			divsep.addClassName("toolseparator");
			divsep.getStyle().setDisplay(Display.INLINE_BLOCK);
			Element sep = DOM.createSpan();
			sep.addClassName("ui-separator");
			divsep.appendChild(sep);
			getToolContainer().appendChild(divsep);
		}
		Element div = DOM.createDiv();
		JsUtil.ensureSubId(getElement(), div, ti.getId());
		div.addClassName("toolitem dropup");
		div.getStyle().setDisplay(Display.INLINE_BLOCK);
		Element anchor = DOM.createAnchor();
		if (ti.getHint() != null)
			anchor.setTitle(ti.getHint());
		if (!enabled) {
			anchor.setAttribute("disabled", "true");
			anchor.addClassName("disabled");
		}
		ClientContext.resourceInjector.applyIconStyles(anchor, ti.getIcon());
		anchor.addClassName("jexpHandCursor dropdown-toggle");
		if (JsUtil.isNotEmpty(ti.getCaption()))
			anchor.setInnerHTML(ti.getCaption());
		if (JsUtil.isNotEmpty(ti.getIconClass())) {
			anchor.addClassName(ti.getIconClass());
		}
		add(ti.getDropDown(), div);
		getToolContainer().appendChild(div);
		anchor.setAttribute("data-toggle", "dropdown");
		div.insertFirst(anchor);

		if (ti.isEndsWithSeparator()) {
			Element divsep = DOM.createDiv();
			divsep.addClassName("toolseparator");
			divsep.getStyle().setDisplay(Display.INLINE_BLOCK);
			Element sep = DOM.createSpan();
			sep.addClassName("ui-separator");
			divsep.appendChild(sep);
			getToolContainer().appendChild(divsep);
		}
		return anchor;
	}

	private native void _bindToolElement(GridToolItem gi, Element el) /*-{
		$wnd
				.$(el)
				.click(
						function(e) {
							if ($wnd.$(this).attr("disabled") == "disabled")
								return;
							gi.@com.javexpress.gwt.library.ui.data.GridToolItem::executeHandler(Lcom/google/gwt/user/client/Event;)(e);
						});
	}-*/;

	private native void _unbindToolElement(GridToolItem gi, Element el) /*-{
		gi.@com.javexpress.gwt.library.ui.data.GridToolItem::unload()();
		$wnd.$(el).off();
	}-*/;

	protected JsonMap createColumnModel(final CT column, final int index) {
		if (column.isHidden())
			return null;
		JsonMap model = new JsonMap();
		model.set("id", column.getField());
		model.set("field", column.getField());
		column.setColumnKey(column.hashCode());
		model.setInt("columnKey", column.getColumnKey());
		model.set("name", column.getTitle() != null ? column.getTitle() : "");
		if (column.getWidth() != null)
			model.setInt("width", Integer.parseInt(column.getWidth()));
		String cssClass = column.getStyleNames();
		if (JsUtil.isEmpty(cssClass))
			cssClass = "";
		if (column.getAlign() == ColumnAlign.right)
			cssClass += " jexpRightAlign";
		else if (column.getAlign() == ColumnAlign.center)
			cssClass += " jexpCenter";
		if (JsUtil.isNotEmpty(cssClass))
			model.set("cssClass", cssClass);
		return model;
	}

	public void setData(JsArray data) throws Exception {
		if (isAttached())
			throw new Exception("Cannot modify editingDataList after widget has initialized");
		this.data = data;
	}

	public void redraw() {
		if (isAttached())
			_redraw(jsObject);
	}

	private native void _redraw(JavaScriptObject grid) /*-{
		grid.invalidate();
	}-*/;

	private native void _autosizeColumns(JavaScriptObject grid) /*-{
		grid.autosizeColumns();
	}-*/;

	public void performAutoSizeColumns() {
		_autosizeColumns(jsObject);
	}

	@Override
	public void onResize() {
		_updateSize(jsObject, getContainer());
	}

	private native void _updateSize(JavaScriptObject slick, Element element) /*-{
		slick.resizeCanvas();
	}-*/;

	protected native void _updateOptionBool(JavaScriptObject slick, String poption, boolean pvalue) /*-{
		var opt = {};
		opt[poption] = pvalue;
		slick.setOptions(opt);
		slick.invalidate();
	}-*/;

	protected native JavaScriptObject _getSelectedRows(JavaScriptObject grid) /*-{
		return grid.getSelectedRows();
	}-*/;

	public JsArrayInteger getSelectedRowIndexes() {
		if (!isAttached())
			return null;
		JavaScriptObject obj = _getSelectedRows(getJsObject());
		if (obj == null)
			return null;
		JsArrayInteger arr = obj.cast();
		return arr.length() > 0 ? arr : null;
	}

	private native void _destroyByJs(JavaScriptObject slick, Element elGrid, Element topPanel) /*-{
		$wnd.$(".slick-viewport", $wnd.$(elGrid)).off();
		$wnd.$(elGrid).unbind("linkclicked");
		slick.destroy();
		delete slick;
		if (topPanel)
			$wnd.$(topPanel).empty().off();
	}-*/;

	protected JsArray getData() {
		return data;
	}

	public int getRowCount() {
		return getData().length();
	}

	public JsonMap getRowData(int index) {
		return new JsonMap(data.get(index));
	}

	@Override
	protected void onUnload() {
		data = null;
		widgetData = null;
		dataView = null;
		keyColumnName = null;
		options = null;
		columns = null;
		if (tools != null)
			for (GridToolItem ti : tools) {
				Element el = ti.getElement();
				if (el != null)
					_unbindToolElement(ti, el);
			}
		tools = null;
		_destroyByJs(jsObject, getContainer(), topPanel);
		jsObject = null;
		topPanel = null;
		super.onUnload();
	}

}