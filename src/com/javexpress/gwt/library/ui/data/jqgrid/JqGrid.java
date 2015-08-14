package com.javexpress.gwt.library.ui.data.jqgrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.IJsonServicePoint;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.JexpWidget;
import com.javexpress.gwt.library.ui.data.DecimalColumn;
import com.javexpress.gwt.library.ui.data.GridToolItem;
import com.javexpress.gwt.library.ui.data.IDataViewer;
import com.javexpress.gwt.library.ui.data.IGridListener;
import com.javexpress.gwt.library.ui.data.IToolItemHandler;
import com.javexpress.gwt.library.ui.data.LinkColumn;
import com.javexpress.gwt.library.ui.data.ListColumn;
import com.javexpress.gwt.library.ui.data.ListColumn.Formatter;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;
import com.javexpress.gwt.library.ui.menu.IMenuHandler;
import com.javexpress.gwt.library.ui.menu.JqMenuItem;
import com.javexpress.gwt.library.ui.menu.JqPopupMenu;

public class JqGrid<T extends Serializable> extends JexpWidget implements IDataViewer {

	public static WidgetBundles fillResources(WidgetBundles parent) {
		WidgetBundles wb = new WidgetBundles("jqGrid 4.6.0", parent);
		wb.addStyleSheet("scripts/jqgrid/" + (JsUtil.USE_BOOTSTRAP ? "bootstrap" : "jquery") + ".ui.jqgrid.css");
		wb.addJavaScript("scripts/jqgrid/grid.locale-" + LocaleInfo.getCurrentLocale().getLocaleName() + ".js");
		wb.addJavaScript("scripts/jqgrid/jquery.jqGrid-4.6.0.min.js");

		WidgetBundles jexp = new WidgetBundles("JavExpress jqGrid Extensions", wb);
		if (JsUtil.USE_BOOTSTRAP)
			jexp.addStyleSheet("scripts/javexpress/jexpGrids-0.1.css");
		jexp.addJavaScript("scripts/jqgrid/jexpJqGrid-0.1.js");
		return jexp;
	}

	protected JsonMap				options;
	private boolean					keyNavigation	= true;
	protected final Element			table;
	private Element					pager;
	private JavaScriptObject		widget;
	private IGridListener			listener;
	private List<ListColumn>		columns			= new ArrayList<ListColumn>();
	protected List<GridToolItem>	tools			= new ArrayList<GridToolItem>();
	private Serializable			widgetData;
	private boolean					waitingFocus;
	private boolean					useSmallFonts;
	private String					keyColumnName;
	private int						maxHeight		= 0;

	public String getKeyColumnName() {
		return keyColumnName;
	}

	@Override
	public void setKeyColumnName(String keyColumnName) {
		this.keyColumnName = keyColumnName;
	}

	@Override
	public Serializable getWidgetData() {
		return widgetData;
	}

	@Override
	public void setWidgetData(Serializable widgetData) {
		this.widgetData = widgetData;
	}

	public IGridListener getListener() {
		return listener;
	}

	@Override
	public void setListener(final IGridListener listener) {
		this.listener = listener;
	}

	public boolean isKeyNavigation() {
		return keyNavigation;
	}

	public void setKeyNavigation(boolean keyNavigation) {
		this.keyNavigation = keyNavigation;
	}

	public boolean isUseSmallFonts() {
		return useSmallFonts;
	}

	public void setUseSmallFonts(boolean useSmallFonts) {
		this.useSmallFonts = useSmallFonts;
	}

	/** Designer compatible constructor */
	public JqGrid(final Widget parent, final String id, boolean fitToParent, String keyColumnName) {
		this(parent, id, fitToParent, null, keyColumnName, true);
	}

	public JqGrid(final Widget parent, final String id, boolean fitToParent, final IJsonServicePoint servicePoint, String keyColumnName, final boolean autoLoad) {
		super();
		this.keyColumnName = keyColumnName;
		setElement(DOM.createDiv());
		if (!JsUtil.USE_BOOTSTRAP)
			getElement().addClassName("jexpBorderBox");
		getElement().addClassName("jexpJqGrid");
		if (fitToParent) {
			setWidth("auto");
			setHeight("100%");
		}
		table = DOM.createTable();
		table.setId(JsUtil.ensureId(parent, this, WidgetConst.JQGRID_PREFIX, id));
		getElement().setId(table.getId() + "_cnt");
		getElement().appendChild(table);
		options = createDefaultOptions(autoLoad);
		if (servicePoint != null)
			setListing(servicePoint);
		pager = DOM.createDiv();
		if (JsUtil.isRTL())
			pager.setDir("rtl");
		pager.setId(table.getId() + "_pager");
		pager.addClassName("jesBorderFix");
		getElement().appendChild(pager);
	}

	@Override
	public void setListing(final IJsonServicePoint listingEnum) {
		options.set("url", JsUtil.getServiceUrl(listingEnum));
	}

	private JsonMap createDefaultOptions(final boolean autoLoad) {
		options = new JsonMap();
		options.set("mtype", "POST");
		options.set("datatype", autoLoad ? "json" : "local");
		options.set("deepempty", true);
		options.set("altRows", true);
		options.set("scroll", true);
		options.set("scrollrows", true);
		options.set("sortable", true);
		options.set("shrinkToFit", true);
		options.setInt("rowNum", 50);
		options.set("viewrecords", true);
		options.set("gridview", true);// Subgrid will not work but performance gain is 5-10 times
		options.setInt("width", Window.getClientWidth() / 2);
		if (JsUtil.isRTL())
			options.set("direction", "rtl");
		return options;
	}

	public void setLoadingMessage(String text) {
		if (JsUtil.isEmpty(text)) {
			options.set("loadui", "disable");
		} else {
			options.set("loadui", "enable");
			options.set("loadtext", text);
		}
	}

	@Override
	public void setAutoLoad(boolean autoLoad) {
		if (!isAttached())
			options.set("datatype", autoLoad ? "json" : "local");
	}

	@Override
	public void setPaging(boolean dataPaging) {
		if (dataPaging) {
			options.setInt("rowNum", 50);
			options.set("scrollRows", true);
		} else {
			options.setInt("rowNum", Integer.MAX_VALUE);
			options.clear("scrollRows");
		}
	}

	public void setGroupSummary(boolean value) {
		options.set("groupSummary", value);
	}

	public void setRowsPerPage(int rowsPerPage) {
		options.setInt("rowNum", rowsPerPage);
	}

	public void setAltRows(boolean value) {
		options.set("altRows", value);
	}

	@Override
	public void setMultiSelect(boolean value) {
		options.set("multiselect", value);
		//options.set("multikey", "shiftKey");
	}

	public void setMultiBoxOnly(boolean value) {
		options.set("multiboxonly", value);
	}

	@Override
	public void setFitColumns(boolean value) {
		options.set("shrinkToFit", value);
	}

	@Override
	public void loadData() {
		if (options.get("datatype").isString().stringValue().equals("local")) {
			JsonMap opts = new JsonMap();
			opts.set("datatype", "json");
			_setOptions(widget, opts.getJavaScriptObject());
		}
		_reload(widget);
	}

	private JSONValue createColumnModel(final ListColumn column, final int index) {
		JsonMap model = new JsonMap();
		model.set("name", column.getField());
		model.set("label", column.getTitle() != null ? column.getTitle() : "");
		if (column.getAlign() != null)
			model.set("align", column.getAlign().toString());
		else if (column.getFormatter() == Formatter.currency || column.getFormatter() == Formatter.number)
			model.set("align", "right");
		if (column.getFormatter() != null && column.getFormatter() != Formatter.number) {
			if (column.getFormatter() != Formatter.map)
				model.set("formatter", column.getFormatter().toString());
			if (column.getFormatter() == Formatter.date) {
				model.set("formatter", column.getFormatter().toString());
				JsonMap f = new JsonMap();
				String fmt = JexpGwtUser.getDateFormat().replaceAll("dd", "d").replaceAll("MM", "m").replaceAll("yyyy", "Y").replaceAll("yy", "Y");
				f.set("newformat", fmt);
				f.set("srcformat", fmt);
				model.set("formatoptions", f);
			} else if (column.getFormatter() == Formatter.timestamp) {
				model.set("formatter", column.getFormatter().toString());
				JsonMap f = new JsonMap();
				String fmt = JexpGwtUser.getTimeStampFormat().replaceAll("dd", "d").replaceAll("MM", "m").replaceAll("yyyy", "Y").replaceAll("yy", "Y");
				f.set("newformat", fmt);
				f.set("srcformat", fmt);
				f.set("defaultValue", "");
				model.set("formatoptions", f);
			} else if (column.getFormatter() == Formatter.currency) {
				//model.set("formatter", column.getFormatter().toString());
				model.set("formatter", Formatter.currency.toString());
				JsonMap f = new JsonMap();
				f.set("thousandsSeparator", String.valueOf(JexpGwtUser.getCurrencyGroupChar()));
				f.set("decimalSeparator", String.valueOf(JexpGwtUser.getCurrencyDecimalChar()));
				f.setInt("decimalPlaces", ((DecimalColumn) column).getDecimalPlaces());
				f.set("defaultValue", "0" + String.valueOf(JexpGwtUser.getCurrencyDecimalChar()) + "00");
				model.set("formatoptions", f);
			} else if (column.getFormatter() == Formatter.map) {
				model.set("formatter", "select");
				StringBuilder vals = new StringBuilder();
				boolean first = true;
				for (String k : column.getMapKeys()) {
					vals.append(!first ? ";" : "").append(k).append(":").append(column.getMapValue(k));
					first = false;
				}
				JsonMap editOptions = new JsonMap();
				editOptions.set("value", vals.toString());
				model.set("editoptions", editOptions);
			} else if (column.getFormatter() == Formatter.link) {
				model.set("name", keyColumnName);
				model.set("formatter", column.getFormatter().toString() + index);
				model.set("jqicon", ((LinkColumn) column).getIcon().getCssClass());
				model.set("hint", ((LinkColumn) column).getHint());
				model.set("label", " ");
			} else if (column.getFormatter() == Formatter.checkbox) {
				model.set("formatter", column.getFormatter().toString() + index);
				model.set("editable", true);
				model.set("edittype", column.getFormatter().toString() + index);
				JsonMap editOptions = new JsonMap();
				editOptions.set("value", "1:0");
				model.set("editoptions", editOptions);
				JsonMap formatOptions = new JsonMap();
				formatOptions.set("disabled", false);
				model.set("formatoptions", formatOptions);
			}
		}
		if (!column.isSortable())
			model.set("sortable", column.isSortable());
		if (column.isHidden())
			model.set("hidden", true);
		if (column.isFrozen())
			model.set("frozen", true);
		if (column.getSummaryType() != null) {
			model.set("summaryType", column.getSummaryType().toString());
			model.set("summaryTpl", column.getSummaryTemplate());
			setGroupSummary(true);
		}
		if (column.getWidth() != null)
			model.set("width", column.getWidth());
		if (!options.get("shrinkToFit").isBoolean().booleanValue()) {
			model.set("fixed", true);
		}
		return model;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		JSONArray colModel = new JSONArray();
		int i = 0;
		boolean hasFrozenColumns = false;
		JsArrayString arrGroupableColumns = JsArrayString.createArray().cast();
		for (ListColumn column : columns) {
			colModel.set(i++, createColumnModel(column, i - 1));
			if (column.isFrozen())
				hasFrozenColumns = true;
			if (column.isSorted()) {
				options.set("sortname", column.getField());
				if (column.isSortDesc())
					options.set("sortorder", "desc");
			}
			if (!column.isHidden() && column.isGroupable())
				arrGroupableColumns.push(column.getField());
		}
		JsonMap nav = new JsonMap();
		nav.set("edit", false);
		nav.set("add", false);
		nav.set("del", false);
		nav.set("search", false);
		if (JsUtil.USE_BOOTSTRAP) {
			nav.set("refreshicon", "fa fa-refresh green");
			nav.set("refreshtitle", ClientContext.nlsCommon.yenile());
		}
		widget = createByJs(this, table, table.getId(), listener, options.getJavaScriptObject(), nav.getJavaScriptObject(), colModel.getJavaScriptObject(), hasFrozenColumns, keyNavigation, listener != null && listener.hasRowStyler(), useSmallFonts, arrGroupableColumns, keyColumnName, maxHeight);
		if (!options.get("shrinkToFit").isBoolean().booleanValue()) {
			GridToolItem tiHideShow = new GridToolItem("hideshow", null, FaIcon.arrows_h, ClientContext.nlsCommon.hideShowColumn());
			tiHideShow.setEndsWithSeparator(true);
			tiHideShow.setHandler(new IToolItemHandler() {
				@Override
				public void execute(final String itemId, Event event) {
					JsonMap opt = new JsonMap();
					opt.set("title", ClientContext.nlsCommon.alanlariSecin());
					_openColumnChooser(widget, opt.getJavaScriptObject());
				}
			});
			tools.add(0, tiHideShow);
		}
		beforeRenderToolItems();
		for (GridToolItem tool : tools)
			renderToolItem(tool);
	}

	protected void beforeRenderToolItems() {
	}

	protected native void _openColumnChooser(JavaScriptObject widget, JavaScriptObject options) /*-{
		widget.jqGrid('columnChooser', options);
	}-*/;

	private native JavaScriptObject createByJs(JqGrid x, Element elm, String id, IGridListener listener, JavaScriptObject options, JavaScriptObject navOptions, JavaScriptObject colModel, boolean hasFrozenColumns, boolean keyNavigation, boolean hasRowStyler, boolean useSmallFonts, JsArrayString groupCols, String keyColumnName, int maxHeight) /*-{
		var el = $wnd.$(elm);
		options.colModel=colModel;
		if (maxHeight>0)
			options.height='auto';
		options.pager="#"+id+"_pager";
		options.jsonReader={page:"page",total:"total",records:"count",root:"data",repeatitems:false,id:keyColumnName};
		options.treeReader={page:"page",total:"total",records:"count",root:"data",repeatitems:false,id:keyColumnName,parent_id_field:"parent"};
		for (var i=0;i<colModel.length;i++){
			if (colModel[i]["formatter"]){
				if (colModel[i]["formatter"].substring(0,4)=="link"){
					colModel[i]["formatter"]=function(cellValue, options, rowObject){
						if (x.@com.javexpress.gwt.library.ui.data.jqgrid.JqGrid::isRenderCell(ILjava/lang/String;)(options.pos,cellValue+""))
							return "<span class='ui-icon "+options.colModel.jqicon+" jexpHandCursor' grid=\""+id+"\" col=\""+options.pos+"\" title=\""+options.colModel.hint+"\" value=\""+cellValue+"\">";
						else
							return "";
					};
				}
			}
		}
		options.beforeSelectRow=function(rowId,event){
		var rowData = el.getRowData(rowId);
		return x.@com.javexpress.gwt.library.ui.data.jqgrid.JqGrid::fireOnBeforeRowSelect(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(rowId,rowData);
		};
		options.onSelectRow=function(rowId,status){
		var rowData = el.getRowData(rowId);
		x.@com.javexpress.gwt.library.ui.data.jqgrid.JqGrid::fireOnRowSelect(Ljava/lang/String;ZLcom/google/gwt/core/client/JavaScriptObject;)(rowId,status,rowData);
		};
		options.ondblClickRow=function(rowId){
		var rowData = el.getRowData(rowId);
		x.@com.javexpress.gwt.library.ui.data.jqgrid.JqGrid::fireOnRowDoubleClick(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(rowId,rowData);
		};
		options.gridComplete=function(){
		$wnd.$("span.ui-icon[grid='"+id+"']", el).click(function(){
		var e = $wnd.$(this);
		var rowId = e.attr("value");
		var rowData = el.getRowData(rowId);
		x.@com.javexpress.gwt.library.ui.data.jqgrid.JqGrid::fireOnColumnLinkClick(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(e,e.attr("col"),rowId,rowData);
		});
		};
		options.loadComplete=function(data){
		x.@com.javexpress.gwt.library.ui.data.jqgrid.JqGrid::fireOnDataLoaded(Lcom/google/gwt/core/client/JavaScriptObject;)(data);
		};
		if (listener!=null){
		if (hasRowStyler){
		options.rowattr=function(rd) {
		var classes = x.@com.javexpress.gwt.library.ui.data.jqgrid.JqGrid::fireOnRowAttr(Lcom/google/gwt/core/client/JavaScriptObject;)(rd);
		if (classes) 
		return { "class": classes };
		else
		return null;
		}
		}
		options.serializeGridData=function(postData){
		postData.gridId=id;
		x.@com.javexpress.gwt.library.ui.data.jqgrid.JqGrid::fireOnSerializeGridData(Lcom/google/gwt/core/client/JavaScriptObject;)(postData);
		return $wnd.JexpUI.AjaxUtils.serialize(postData);
		}
		}
		el.jqGrid(options);
		el.jqGrid("navGrid",options.pager,navOptions);
		if (hasFrozenColumns)
		el.jqGrid("setFrozenColumns");
		if (keyNavigation){
		el.jqGrid("bindKeys",{
		"onEnter":function( rowId ) { 
		var rowData = el.getRowData(rowId);
		x.@com.javexpress.gwt.library.ui.data.jqgrid.JqGrid::fireOnRowDoubleClick(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(rowId,rowData);
		}
		});
		}
		if (groupCols.length>0){
		el.parent().parent().parent().find("tr.ui-jqgrid-labels > th > div").each(function(){
		var f = $wnd.$(this).attr("id");
		f = f.substring(f.lastIndexOf("_")+1);
		for (var i=0;i<groupCols.length;i++){
		if (f==groupCols[i]){
		$wnd.$("<span class='ui-icon ui-icon-arrowstop-1-s jesHeaderMenuIcon'></span>").click(function(e){
		//jqgh_jqg_tetkikler-fr_lb_tetkikislemleripane_tetkikAdi
		x.@com.javexpress.gwt.library.ui.data.jqgrid.JqGrid::fireOnHeaderColumnContextMenu(Ljava/lang/String;II)(f,e.pageX,e.pageY);
		return false;
		}).prependTo($wnd.$(this).parent());
		break;
		}
		}
		});
		}
		var bdiv = $wnd.$("#gview_"+id+" > .ui-jqgrid-bdiv", el);
		if (useSmallFonts)
		bdiv.addClass("jqGridSmallFonts");
		if (maxHeight>0)
		bdiv.css("max-height", maxHeight+"px");			
		return el;
	}-*/;

	@Override
	protected void onUnload() {
		options = null;
		pager = null;
		widget = null;
		listener = null;
		columns = null;
		widgetData = null;
		if (tools != null)
			for (GridToolItem gti : tools)
				gti.unload();
		tools = null;
		destroyByJs(table, table.getId() + "_pager", "alertmod_" + table.getId());
		super.onUnload();
	}

	private native void destroyByJs(Element element, String pager, String alertMod) /*-{
		$wnd.$(element).jqGrid("unbindKeys").jqGrid('GridUnload').remove();
		$wnd.$("#" + pager).remove();
		$wnd.$("#" + alertMod).remove();
	}-*/;

	@Override
	public void addColumn(final ListColumn column) {
		columns.add(column);
	}

	@Override
	public void addToolItem(final GridToolItem toolItem) {
		tools.add(toolItem);
	}

	@Override
	public void onResize() {
		_updateSize(widget, getElement(), getElement().getId(), pager, options != null && options.get("footerrow") != null, maxHeight, JsUtil.USE_BOOTSTRAP);
	}

	@Override
	public void setGroupColumn(String... field) {
		if (isAttached()) {
			_clearGrouping(widget);
			if (columns == null) {
				options.clear("grouping");
				return;
			}
		}
		options.set("grouping", true);
		if (!isAttached()) {
			JsonMap gd = new JsonMap();
			JSONArray names = new JSONArray();
			JSONArray summaries = new JSONArray();
			int i = 0;
			for (String f : field) {
				names.set(i++, new JSONString(f));
				summaries.set(i++, JSONBoolean.getInstance(true));
			}
			JSONArray arrBool = new JSONArray();
			gd.put("groupField", names);
			gd.put("groupSummary", summaries);
			i = 0;
			for (String f : field)
				arrBool.set(i++, JSONBoolean.getInstance(true));
			gd.put("groupColumnShow", arrBool);
			gd.set("groupDataSorted", true);
			options.set("groupingView", gd);
			/*
			 groupField : ['name'], groupSummary : [true], groupColumnShow : [true], groupText : ['<b>{0}</b>'], groupCollapse : false, groupOrder: ['asc']
			 */
		} else {
			JsArrayString arrField = JsArrayString.createArray().cast();
			for (String f : field)
				arrField.push(f);
			_groupBy(widget, arrField);
		}
	}

	private native void _clearGrouping(JavaScriptObject widget) /*-{
		widget.jqGrid('groupingRemove');
	}-*/;

	private native void _groupBy(JavaScriptObject widget, JsArrayString field) /*-{
		widget.jqGrid('groupingGroupBy', field);
	}-*/;

	private native void _updateSize(JavaScriptObject widget, Element el, String elId, Element pagerEl, boolean hasFooterRow, int maxHeight, boolean usingBootstrap) /*-{
		var parent = $wnd.$(el);//475 in pane 
		$wnd.console.debug(parent.width());
		widget.setGridWidth(Math.round(parent.width()
				- (usingBootstrap ? 0 : 2)), true);
		if (maxHeight == 0) {
			var hdiv = Math.round($wnd.$("#" + elId + " .ui-jqgrid-hdiv")
					.height()) + 1;//22+1
			var pager = Math.round($wnd.$(pagerEl).height()) + 1;//25+1
			widget.setGridHeight(Math.round(parent.height() - 1 - hdiv - pager
					- (hasFooterRow ? 22 : 0)));
		}
	}-*/;

	private void renderToolItem(final GridToolItem tool) {
		if (tool.isStartsWithSeparator())
			_addtoolseparator(tool, widget, table.getId());
		JsonMap options = new JsonMap();
		options.set("id", table.getId() + "_" + tool.getId());
		options.set("buttonicon", tool.getIcon().getCssClass() + (JsUtil.isNotEmpty(tool.getIconClass()) ? " " + tool.getIconClass() : ""));
		options.set("caption", tool.getCaption() != null ? tool.getCaption() : "");
		if (tool.getHint() != null)
			options.set("title", tool.getHint());
		_addToolItem(tool, widget, table.getId(), options.getJavaScriptObject());
		if (tool.isEndsWithSeparator())
			_addtoolseparator(tool, widget, table.getId());
	}

	private native void _addToolItem(GridToolItem x, JavaScriptObject widget, String id, JavaScriptObject options) /*-{
		options.onClickButton = function(event) {
			x.@com.javexpress.gwt.library.ui.data.GridToolItem::executeHandler(Lcom/google/gwt/user/client/Event;)(event);
		}
		widget.jqGrid("navButtonAdd", "#" + id + "_pager", options);
	}-*/;

	private native void _addtoolseparator(GridToolItem x, JavaScriptObject widget, String id) /*-{
		widget.jqGrid('navSeparatorAdd', "#" + id + "_pager", {});
	}-*/;

	@Override
	public JsonMap getSelectedData() {
		Serializable l = getSelectedId();
		if (l != null)
			return getRowData(l);
		else
			return null;
	}

	private JsonMap getRowData(final Serializable rowId) {
		return new JsonMap(_getRowData(widget, rowId));
	}

	//???
	private native JavaScriptObject _getRowData(JavaScriptObject widget, Serializable rowId) /*-{
		return widget.getRowData(rowId);
	}-*/;

	@Override
	public String getSelectedId() {
		return _getSelectedId(widget);
	}

	@Override
	public List<String> getSelectedIds() {
		JsArrayString arr = _getSelectedIds(widget);
		if (arr != null && arr.length() > 0) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < arr.length(); i++) {
				String s = arr.get(i);
				if (JsUtil.isNotEmpty(s))
					list.add(s);
			}
			return list.isEmpty() ? null : list;
		}
		return null;
	}

	private native String _getSelectedId(JavaScriptObject widget) /*-{
		return widget.jqGrid("getGridParam", "selrow");
	}-*/;

	private native JsArrayString _getSelectedIds(JavaScriptObject widget) /*-{
		return widget.jqGrid("getGridParam", "selarrrow");
	}-*/;

	@Override
	public void refresh() {
		waitingFocus = true;
		_reload(widget);
	}

	@Override
	public void refresh(final Serializable rowId) {
		if (rowId == null)
			waitingFocus = true;
		_refresh(widget, rowId);
		if (rowId != null)
			table.focus();
	}

	public void setCaption(final String caption) {
		if (isAttached())
			_setCaption(widget, caption);
		else
			options.set("caption", caption);
	}

	private native void _setCaption(JavaScriptObject widget, String caption) /*-{
		widget.jqGrid("setCaption", caption);
	}-*/;

	private native void _setOptions(JavaScriptObject widget, JavaScriptObject opts) /*-{
		widget.jqGrid("setGridParam", opts);
	}-*/;

	private native void _reload(JavaScriptObject widget) /*-{
		widget.trigger("reloadGrid");
	}-*/;

	private native void _refresh(JavaScriptObject widget, Serializable rowId) /*-{
		widget.trigger("reloadGrid");
		if (rowId)
			$wnd.setTimeout(function() {
				widget.setSelection(rowId);
			}, 250);
	}-*/;

	@Override
	public HandlerRegistration addContextMenuHandler(ContextMenuHandler handler) {
		return addDomHandler(handler, ContextMenuEvent.getType());
	}

	@Override
	public void setPopupMenu(JqPopupMenu menu) {
		addContextMenuHandler(menu);
	}

	@Override
	public boolean canOpenContextMenu(JqPopupMenu menu) throws Exception {
		if (listener != null)
			return listener.onGridContextMenu(menu, getSelectedId(), getSelectedData());
		return false;
	}

	@Override
	public void clearData() {
		if (widget != null)
			_clearData(widget);
	}

	@Override
	public void clearSelection() {
		if (widget != null)
			_clearSelection(widget);
	}

	//---------- EVENTS
	private void fireOnHeaderColumnContextMenu(final String field, final int x, final int y) {
		for (final ListColumn column : columns) {
			if (column.getField().equals(field) && column.isGroupable()) {
				final JqPopupMenu menu = new JqPopupMenu() {
					IMenuHandler	handler	= new IMenuHandler() {
												@Override
												public void menuItemClicked(String code) {
													if (code.equals("group"))
														setGroupColumn(field);
												}
											};

					@Override
					public void prepareMenu() {
						JqMenuItem mig = new JqMenuItem("group", ClientContext.nlsCommon.grupla(), handler);//if isgroupable
						add(mig);
					}
				};
				menu.popUp(x, y);
				break;
			}
		}
	}

	private boolean fireOnBeforeRowSelect(final String rowId, final JavaScriptObject rowData) throws Exception {
		if (listener != null) {
			return listener.onGridBeforeRowSelect(rowId, new JsonMap(rowData));
		} else
			return true;
	}

	private void fireOnRowSelect(final String rowId, boolean selected, final JavaScriptObject rowData) throws Exception {
		if (listener != null) {
			listener.onGridRowSelect(rowId, selected, new JsonMap(rowData));
		}
	}

	private void fireOnRowDoubleClick(final String rowId, final JavaScriptObject rowData) throws Exception {
		if (listener != null) {
			listener.onGridRowDoubleClick(rowId, new JsonMap(rowData));
		}
	}

	private String fireOnRowAttr(final JavaScriptObject rowData) throws Exception {
		if (listener != null) {
			return listener.getRowStyle(new JsonMap(rowData));
		}
		return null;
	}

	protected void fireOnSerializeGridData(final JavaScriptObject postData) throws Exception {
		if (listener != null) {
			listener.onGridBeforeDataRequest(new JsonMap(postData));
		}
	}

	private boolean isRenderCell(final int pos, final String rowId) throws Exception {
		ListColumn column = columns.get(options.containsKey("multiselect") ? pos - 1 : pos);
		if (column instanceof LinkColumn)
			return ((LinkColumn) column).isRenderCell(rowId);
		return false;
	}

	private void fireOnColumnLinkClick(JavaScriptObject linkElement, final String col, final String value, final JavaScriptObject rowData) throws Exception {
		int pos = Integer.parseInt(col);
		ListColumn column = columns.get(options.containsKey("multiselect") ? pos - 1 : pos);
		if (column instanceof LinkColumn)
			((LinkColumn) column).cellClicked(linkElement, value, new JsonMap(rowData));
	}

	private void fireOnDataLoaded(JavaScriptObject data) throws Exception {
		if (waitingFocus)
			setFocus(true);
		if (listener != null) {
			listener.onGridDataLoaded(data);
		}
	}

	public void hideColumn(String field) {
		_hideColumn(widget, field);
	}

	private native void _hideColumn(JavaScriptObject widget, String field) /*-{
		widget.jqGrid("hideCol", field);
	}-*/;

	public void showColumn(String field) {
		_showColumn(widget, field);
	}

	public void selectRow(Serializable rowId, boolean fireOnSelect) {
		_selectRow(widget, rowId.toString(), fireOnSelect);
	}

	private native void _selectRow(JavaScriptObject widget, String rowId, boolean fireOnSelect) /*-{
		widget.jqGrid("setSelection", rowId, fireOnSelect);
	}-*/;

	private native void _showColumn(JavaScriptObject widget, String field) /*-{
		widget.jqGrid("showCol", field);
	}-*/;

	private native void _clearData(JavaScriptObject widget) /*-{
		widget.jqGrid("clearGridData", true);
	}-*/;

	private native void _clearSelection(JavaScriptObject widget) /*-{
		widget.jqGrid("resetSelection");
	}-*/;

	@Override
	public void setSelectedIds(List<? extends Serializable> values, boolean fireOnSelect) {
		clearSelection();
		if (values != null)
			for (Serializable val : values)
				selectRow(val, fireOnSelect);
	}

	@Override
	public int getTabIndex() {
		return getElement().getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
	}

	private native String getFirstRowId(JavaScriptObject jso) /*-{
		return jso.getDataIDs()[0];
	}-*/;

	@Override
	public void setFocus(boolean focused) {
		if (focused) {
			String currId = getSelectedId();
			if (JsUtil.isEmpty(currId))
				currId = getFirstRowId(widget);
			if (!JsUtil.isEmpty(currId)) {
				selectRow(currId, true);
				waitingFocus = false;
			} else
				waitingFocus = true;
			table.focus();
		} else {
			waitingFocus = false;
			table.blur();
		}
	}

	@Override
	public void setTabIndex(int index) {
		getElement().setTabIndex(index);
	}

	@Override
	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	@Override
	public void performAutoSizeColumns() {
	}

	@Override
	public void setDataExportOptions(boolean useForeignKeysAsVariable) {
	}

}