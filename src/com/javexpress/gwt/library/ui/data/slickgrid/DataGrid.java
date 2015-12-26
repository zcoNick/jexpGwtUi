package com.javexpress.gwt.library.ui.data.slickgrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.IJsonServicePoint;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.data.CurrencyColumn;
import com.javexpress.gwt.library.ui.data.DecimalColumn;
import com.javexpress.gwt.library.ui.data.GridToolItem;
import com.javexpress.gwt.library.ui.data.IDataViewer;
import com.javexpress.gwt.library.ui.data.IGridListener;
import com.javexpress.gwt.library.ui.data.IToolItemHandler;
import com.javexpress.gwt.library.ui.data.LinkColumn;
import com.javexpress.gwt.library.ui.data.ListColumn;
import com.javexpress.gwt.library.ui.data.ListColumn.Formatter;
import com.javexpress.gwt.library.ui.data.ListColumn.SummaryType;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.menu.JqPopupMenu;

public class DataGrid<T extends Serializable> extends BaseSlickGrid<ListColumn> implements IDataViewer {

	private boolean						autoLoad;
	private int							maxHeight	= 0;
	private boolean						dataPaging	= true;
	private JavaScriptObject			loader;
	private IGridListener				listener;
	private Element						recInfo, loadingPanel;
	private DataGridStyler				styler;
	private JsArray<JavaScriptObject>	currentGroupDef;
	private List<GroupingDefinition>	grouping	= null;

	private JavaScriptObject getLoader() {
		return loader;
	}

	private void setLoader(JavaScriptObject loader) {
		this.loader = loader;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	@Override
	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public boolean isMultiSelect() {
		return getOptions().getBoolean("multiSelect");
	}

	@Override
	public void setMultiSelect(boolean multiSelect) {
		getOptions().set("multiSelect", multiSelect);
	}

	public DataGrid(Widget parent, String id, boolean fitToParent, String keyColumnName) {
		this(parent, id, fitToParent, null, keyColumnName, true, null);
	}

	/** Designer compatible constructor */
	public DataGrid(Widget parent, String id, boolean fitToParent, String keyColumnName, String header) {
		this(parent, id, fitToParent, null, keyColumnName, true, header);
	}

	public DataGrid(Widget parent, String id, boolean fitToParent, final IJsonServicePoint servicePoint, String keyColumnName, boolean autoLoad, String header) {
		super(parent, WidgetConst.DATAGRID_PREFIX, id, fitToParent, keyColumnName, header);

		getElement().addClassName("jexpDataGrid");
		setHeight("100px");

		recInfo = DOM.createDiv();
		recInfo.setInnerHTML(ClientContext.nlsCommon.kayitBulunamadi());
		recInfo.addClassName("jexpDataGridRecordInfo");
		getToolContainer().appendChild(recInfo);

		loadingPanel = DOM.createDiv();
		loadingPanel.getStyle().setDisplay(Display.INLINE_BLOCK);
		loadingPanel.getStyle().setFloat(Float.RIGHT);
		getToolContainer().appendChild(loadingPanel);

		GridToolItem tiRefresh = new GridToolItem("refresh", null, FaIcon.refresh, ClientContext.nlsCommon.yenile());
		tiRefresh.setIconClass("green");
		tiRefresh.setEndsWithSeparator(true);
		tiRefresh.setHandler(new IToolItemHandler() {
			@Override
			public void execute(final String itemId, Event event) {
				refresh();
			}
		});
		addToolItem(tiRefresh);

		this.autoLoad = autoLoad;

		getOptions().setInt("rowsPerPage", 50);
		if (servicePoint != null)
			setListing(servicePoint);
	}

	protected native void showFilters(Element tiSpan) /*-{
		$wnd.$(tiSpan).popover("show");
	}-*/;

	@Override
	protected JsonMap createDefaultOptions() {
		JsonMap options = super.createDefaultOptions();
		options.setInt("rowHeight", 24);
		//options.set("enableCellNavigation", false);
		options.set("multiColumnSort", true);
		return options;
	}

	public void setMaxHeight(String maxHeight) {
		getOptions().set("autoHeight", true);
		getContainer().getStyle().setProperty("max-height", maxHeight);
	}

	@Override
	protected JsonMap createColumnModel(ListColumn column, int index) {
		JsonMap model = super.createColumnModel(column, index);
		if (model == null)
			return null;

		if (column.isGroupable())
			model.set("jexpGroupable", true);

		if (column.getSummaryType() != null)
			model.set("jexpSummaryType", column.getSummaryType().toString());

		if (column.isSortable()) {
			model.set("sortable", true);
			if (column.isSorted()) {
				model.set("sortedAsc", !column.isSortDesc());
				if (!column.isSortDesc())
					model.set("defaultSortAsc", false);
			}
		}

		if (column.getFormatter() == Formatter.bool || column.getFormatter() == Formatter.checkbox) {
			model.set("formatter", "bool");
		} else if (column.getFormatter() == Formatter.link) {
			model.set("field", getKeyColumnName());
			model.set("formatter", "link");
			LinkColumn lc = (LinkColumn) column;
			model.set("linkIconClass", lc.getIcon().getCssClass());
			model.set("linkTitle", lc.getHint());
			model.set("linkOwner", getContainer().getId());
			int linkIndex = getColumns().indexOf(lc);
			model.setInt("linkIndex", linkIndex);
			lc.setLinkIndexInGrid(linkIndex);
			model.set("hasListener", lc.getListener() != null);
		} else if (column.getFormatter() == Formatter.date) {
			model.set("formatter", "date");
			model.set("dateFormat", JexpGwtUser.getDateFormat());
		} else if (column.getFormatter() == Formatter.decimal) {
			DecimalColumn dc = (DecimalColumn) column;
			model.set("formatter", "decimal");
			JsonMap options = new JsonMap();
			model.set("numeralFormat", JsUtil.createNumeralFormat(dc.getDecimalPlaces(), dc.isEmptyDecimals()));
			model.set("options", options);
		} else if (column.getFormatter() == Formatter.currency) {
			CurrencyColumn dc = (CurrencyColumn) column;
			model.set("formatter", "currency");
			JsonMap options = new JsonMap();
			model.set("numeralFormat", JsUtil.createNumeralFormat(dc.getDecimalPlaces(), dc.isEmptyDecimals(), JexpGwtUser.instance.getCurrencySymbol()));
			model.set("options", options);
		} else if (column.getFormatter() == Formatter.timestamp) {
			model.set("formatter", "timestamp");
			model.set("timestampFormat", JexpGwtUser.getTimeStampFormat());
		} else if (column.getFormatter() == Formatter.percentBar) {
			model.set("formatter", "percentBar");
		} else if (column.getFormatter() == Formatter.map) {
			model.set("formatter", "map");
			JsonMap map = new JsonMap();
			for (String k : column.getMapKeys())
				map.set(k, SafeHtmlUtils.htmlEscape(column.getMapValue(k).toString()));
			model.set("map", map);
		}
		return model;
	}

	@Override
	protected JavaScriptObject createJsObject(JSONArray colModel) {
		if (currentGroupDef != null)
			setPaging(false);
		JavaScriptObject jso = createByJs(this, getContainer().getId(), getOptions().getJavaScriptObject(), colModel.getJavaScriptObject(), getKeyColumnName(), "jexpDataGridLoadingPanel jexpDataGridLoadingIndicator", getData(), autoLoad, JsUtil.calcDialogZIndex(), loadingPanel, recInfo, ClientContext.nlsCommon.kayitBulunamadi(), ClientContext.nlsCommon.grupla(), styler, dataPaging, maxHeight, currentGroupDef);
		autoLoad = false;
		return jso;
	}

	private native JavaScriptObject createByJs(DataGrid x, String elGridId, JavaScriptObject options, JavaScriptObject columns, String keyColumnName, String loadingCssClassName, JavaScriptObject data, boolean autoLoad, int zIndex, Element loadingPanel, Element recInfo, String noRecFoundMessage, String groupMessage, DataGridStyler styler, boolean dataPaging, int maxHeight, JsArray<JavaScriptObject> currentGroupDef) /*-{
		var sortModel = null;
		var hasGroupable = false;
		for (var i = 0; i < columns.length; i++) {
			var model = columns[i];
			if (model.sortedAsc)
				sortModel = model;
			if (model.jexpGroupable) {
				options.rowsPerPage = 0;
				hasGroupable = true;
				model.header = {
					menu : {
						items : [ {
							iconCssClass : "jexpGroupIcon",
							title : groupMessage,
							command : "group"
						} ]
					}
				};
			}
			if (model.formatter == "bool") {
				model.formatter = $wnd.JexpBoolFormatter;
			} else if (model.formatter == "map") {
				model.formatter = $wnd.JexpMapFormatter;
			} else if (model.formatter == "date") {
				model.formatter = $wnd.JexpDateFormatter;
			} else if (model.formatter == "timestamp") {
				model.formatter = $wnd.JexpTimeStampFormatter;
			} else if (model.formatter == "decimal") {
				model.formatter = $wnd.JexpDecimalFormatter;
				if (model.groupTotalsFormatter == "sum")
					model.groupTotalsFormatter = $wnd.JexpCurrencySumFormatter;
			} else if (model.formatter == "currency") {
				model.formatter = $wnd.JexpCurrencyFormatter;
				if (model.groupTotalsFormatter == "sum")
					model.groupTotalsFormatter = $wnd.JexpCurrencySumFormatter;
			} else if (model.formatter == "link") {
				model.formatter = $wnd.JexpLinkFormatter;
			} else if (model.formatter == "percentbar") {
				model.formatter = $wnd.JexpPercentCompleteBarFormatter;
			}

			if (model.jexpSummaryType == "sum")
				model.groupTotalsFormatter = $wnd.JexpSumFormatter;
			else if (model.jexpSummaryType == "avg")
				model.groupTotalsFormatter = $wnd.JexpAvgFormatter;
		}
		var checkboxSelector = null;
		if (options.multiSelect) {
			checkboxSelector = new $wnd.Slick.CheckboxSelectColumn({
				cssClass : "slick-cell-checkboxsel"
			});
			columns.unshift(checkboxSelector.getColumnDefinition());
		}

		options.onBeforeDataRequest = function(postData) {
			x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireOnBeforeDataRequest(Lcom/google/gwt/core/client/JavaScriptObject;)(postData);
		}

		var loader = new $wnd.Slick.Data.RemoteModel(options, data);
		x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::setLoader(Lcom/google/gwt/core/client/JavaScriptObject;)(loader);

		var dataView = null;
		var groupItemMetadataProvider = null;
		if (!dataPaging || hasGroupable) {
			if (hasGroupable) {
				groupItemMetadataProvider = new $wnd.Slick.Data.GroupItemMetadataProvider();
				dataView = new $wnd.Slick.Data.DataView({
					groupItemMetadataProvider : groupItemMetadataProvider
				});
			} else
				dataView = new $wnd.Slick.Data.DataView();
			x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::setDataView(Lcom/google/gwt/core/client/JavaScriptObject;)(dataView);
		}

		var grid = new $wnd.Slick.Grid("#" + elGridId,
				dataView != null ? dataView : loader.data, columns, options);
		grid.setSelectionModel(new $wnd.Slick.RowSelectionModel(
				options.multiSelect ? {
					selectActiveRow : true
				} : {}));
		if (groupItemMetadataProvider)
			grid.registerPlugin(groupItemMetadataProvider);
		if (checkboxSelector)
			grid.registerPlugin(checkboxSelector);
		if (hasGroupable) {
			var headerMenuPlugin = new $wnd.Slick.Plugins.HeaderMenu({});
			//headerMenuPlugin.onBeforeMenuShow.subscribe(function(e, args) {});
			headerMenuPlugin.onCommand
					.subscribe(function(e, args) {
						x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireOnHeaderMenuItemClicked(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(args.command,args.column);
					});
			grid.registerPlugin(headerMenuPlugin);
		}

		var columnpicker = new $wnd.Slick.Controls.ColumnPicker(columns, grid,
				options);

		loader.onDataLoading.subscribe(function() {
			$wnd.$(loadingPanel).addClass(loadingCssClassName);
		});

		loader.lastLength = -1;
		loader.onDataLoaded
				.subscribe(function(e, args) {
					recInfo.innerHTML = "";
					if (data.length > 0) {
						for (var i = args.from; i <= args.to; i++)
							grid.invalidateRow(i);

						recInfo.innerHTML = (args.from + 1) + " - "
								+ Math.min(args.to + 1, data.length) + " / "
								+ data.length + (dataView ? " (L)" : "");

						if (maxHeight > 0) {
							x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireUpdateParentSize(I)(data.length);
						}
					} else {
						recInfo.innerHTML = noRecFoundMessage;

						if (maxHeight > 0) {
							x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireUpdateParentSize(I)(1);
						}
					}

					$wnd.$(loadingPanel).removeClass(loadingCssClassName);

					if (dataView) {
						dataView.beginUpdate();
						dataView.setItems(loader.data, keyColumnName);
						if (currentGroupDef) {
							dataView
									.setGrouping(currentGroupDef.length > 1 ? currentGroupDef
											: currentGroupDef[0]);
						}
						x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireOnDataLoaded(IILcom/google/gwt/core/client/JavaScriptObject;)(args.from, args.to, data);
						dataView.endUpdate();
					} else {
						if (loader.lastLength != data.length) {
							grid.updateRowCount();
							loader.lastLength = data.length;
						}
						x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireOnDataLoaded(IILcom/google/gwt/core/client/JavaScriptObject;)(args.from, args.to, data);
						grid.render();
					}
				});

		if (dataView) {
			//grouping felan var
			dataView.onRowCountChanged.subscribe(function(e, args) {
				grid.updateRowCount();
				grid.render();
			});
			dataView.onRowsChanged.subscribe(function(e, args) {
				grid.invalidateRows(args.rows);
				grid.render();
			});
		} else {
			grid.onViewportChanged.subscribe(function(e, args) {
				var vp = grid.getViewport();
				loader.ensureData(vp.top, vp.bottom);
			});
		}

		grid.onSort.subscribe(function(e, args) {
			loader.clear();
			if (dataView) {
				dataView.setItems([]);
			}
			loader.setSort(args.sortCols);
			grid.invalidateAllRows();
			grid.onViewportChanged.notify();
		});
		if (sortModel) {
			var sortCols = [];
			sortCols[0] = {
				sortAsc : sortModel.sortedAsc,
				sortCol : sortModel
			};
			loader.setSort(sortCols);
			grid.setSortColumn(sortModel.field, sortModel.sortedAsc);
		}

		grid.canCellBeActive = function(row, cell) {
			var rowData = @com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::resolveRowData(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;I)(dataView, data, row);
			if (!rowData)
				return;
			var rowId = rowData[keyColumnName];
			return x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireOnBeforeRowSelect(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(rowId+"",rowData);
		}

		grid.onSelectedRowsChanged
				.subscribe(function(e, args) {
					if (!loader.disableSelectEventFire && !options.multiSelect) {
						var rowData = @com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::resolveRowData(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;I)(dataView, data, args.rows[0]);
						if (!rowData)
							return;
						var rowId = rowData[keyColumnName];
						x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireOnRowSelect(Ljava/lang/String;ZLcom/google/gwt/core/client/JavaScriptObject;)(rowId?rowId+"":null,true,rowData);
						return;
					}
					var sels = args.rows.slice();
					if (!loader.disableSelectEventFire) {
						for (var i = 0; i < sels.length; i++) {
							var exists = false;
							if (loader.lastSelectedRows) {
								for (var l = 0; l < loader.lastSelectedRows.length; l++) {
									if (sels[i] == loader.lastSelectedRows[l]) {
										exists = true;//zaten var
										break;
									}
								}
							}
							if (!exists) {
								var rowData = @com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::resolveRowData(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;I)(dataView, data, sels[i]);
								if (!rowData)
									continue;
								var rowId = rowData[keyColumnName];
								x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireOnRowSelect(Ljava/lang/String;ZLcom/google/gwt/core/client/JavaScriptObject;)(rowId?rowId+"":null,true,rowData);
							}
						}
						if (loader.lastSelectedRows) {
							for (var l = 0; l < loader.lastSelectedRows.length; l++) {
								var found = false;
								for (var i = 0; i < sels.length; i++) {
									if (loader.lastSelectedRows[l] == sels[i]) {
										found = true;
										break;
									}
								}
								if (!found) {
									var rowData = @com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::resolveRowData(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;I)(dataView, data, loader.lastSelectedRows[l]);
									if (!rowData)
										continue;
									var rowId = rowData[keyColumnName];
									x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireOnRowSelect(Ljava/lang/String;ZLcom/google/gwt/core/client/JavaScriptObject;)(rowId?rowId+"":null,false,rowData);
								}
							}
						}
					}
					loader.lastSelectedRows = sels;
				});

		grid.onDblClick
				.subscribe(function(e, args) {
					var rowData = @com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::resolveRowData(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;I)(dataView, data, args.row);
					if (!rowData)
						return;
					var rowId = rowData[keyColumnName];
					x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireOnRowDoubleClick(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(rowId+"",rowData);
				});

		// load the first page
		if (autoLoad) {
			if (dataView) {
				loader.ensureData(0, 9999);
			} else
				grid.onViewportChanged.notify();
		}
		$wnd
				.$("#" + elGridId)
				.bind(
						"linkclicked",
						function(event, linkElement, row, cell, field,
								columnKey, value) {
							x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireLinkClicked(Lcom/google/gwt/core/client/JavaScriptObject;IILjava/lang/String;ILjava/lang/String;)(linkElement,row,cell,field,columnKey,value);
						});
		return grid;
	}-*/;

	private native static JavaScriptObject resolveRowData(JavaScriptObject dataView, JavaScriptObject data, int rowNum)/*-{
		var rowData = dataView ? dataView.getItem(rowNum) : data[rowNum];
		if (!rowData || rowData.__group || rowData.__groupTotals
				|| rowData.__nonDataRow)
			return null;
		return rowData;
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
	public int getTabIndex() {
		return getElement().getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
	}

	@Override
	public void setFocus(boolean focused) {
		if (focused)
			getContainer().focus();
	}

	@Override
	public void setTabIndex(int index) {
		getElement().setTabIndex(index);
	}

	@Override
	public void loadData() {
		refresh();
	}

	@Override
	public void refresh() {
		refresh(null);
	}

	@Override
	public void clearData() {
		_clearData(getJsObject(), loader, getDataView());
		if (listener != null)
			clearSelection();
	}

	private native void _clearData(JavaScriptObject grid, JavaScriptObject loader, JavaScriptObject dataView) /*-{
		if (loader)
			loader.clear();
		if (dataView)
			dataView.setItems([]);
		if (grid)
			grid.invalidateAllRows();
	}-*/;

	@Override
	public void refresh(final Serializable result) {
		if (getJsObject() == null)
			return;
		_refresh(getJsObject(), loader, getDataView());
		if (result != null)
			Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
				@Override
				public boolean execute() {
					List<Serializable> ls = new ArrayList<Serializable>();
					ls.add(result);
					setSelectedIds(ls, true);
					scrollToSelection();
					return false;
				}
			}, 250);
		else
			clearSelection();
	}

	private native void _refresh(JavaScriptObject grid, JavaScriptObject loader, JavaScriptObject dataView) /*-{
		if (loader)
			loader.clear();
		if (dataView)
			dataView.setItems([]);
		grid.invalidateAllRows();
		if (dataView) {
			loader.ensureData(0, 9999);
		} else {
			grid.onViewportChanged.notify();
		}
	}-*/;

	@Override
	public String getSelectedId() {
		if (!isAttached())
			return null;
		JsonMap rd = getSelectedData();
		if (rd == null)
			return null;
		return getKey(rd);
	}

	private String getKey(JSONObject rd) {
		JSONValue v = rd.get(getKeyColumnName());
		if (v == null)
			return null;
		JSONNumber n = v.isNumber();
		if (n != null)
			return String.valueOf((long) n.doubleValue());
		JSONString s = v.isString();
		if (s != null)
			return s.stringValue();
		return v.toString();
	}

	@Override
	public List<String> getSelectedIds() {
		if (!isAttached())
			return null;
		JsArray<JavaScriptObject> obj = _getSelectedRowsData(this, getJsObject(), getDataView(), getData());
		if (obj == null || obj.length() == 0)
			return null;
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < obj.length(); i++) {
			JsonMap rd = new JsonMap(obj.get(i));
			list.add(getKey(rd));
		}
		return list;
	}

	@Override
	public JsonMap getSelectedData() {
		if (!isAttached())
			return null;
		JsArray<JavaScriptObject> obj = _getSelectedRowsData(this, getJsObject(), getDataView(), getData());
		return obj == null || obj.length() == 0 ? null : new JsonMap(obj.get(0));
	}

	private native JsArray<JavaScriptObject> _getSelectedRowsData(DataGrid x, JavaScriptObject grid, JavaScriptObject dataView, JavaScriptObject data) /*-{
		var selectedIndexes = grid.getSelectedRows();
		if (selectedIndexes && selectedIndexes.length > 0) {
			var arr = [];
			for (var i = 0; i < selectedIndexes.length; i++)
				arr[i] = @com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::resolveRowData(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;I)(dataView, data, selectedIndexes[i]);
			return arr;
		}
		return null;
	}-*/;

	@Override
	public void setListener(IGridListener listener) {
		this.listener = listener;
	}

	@Override
	public void setFitColumns(boolean value) {
		getOptions().set("forceFitColumns", value);
	}

	@Override
	public void clearSelection() {
		_setSelectedRows(getJsObject(), loader, null, true);
	}

	@Override
	public void setSelectedIds(List<? extends Serializable> values, boolean fireOnSelect) {
		JsArrayInteger arr = JsArrayInteger.createArray().cast();
		if (values != null) {
			List<Serializable> remaining = new ArrayList<Serializable>();
			remaining.addAll(values);
			for (int i = 0; i < getData().length(); i++) {
				JSONObject rd = new JSONObject(getData().get(i));
				if (rd != null) {
					String id = getKey(rd);
					Serializable found = null;
					for (Serializable s : remaining) {
						if (s.toString().equals(id)) {
							arr.push(i);
							found = s;
							break;
						}
					}
					if (found != null)
						remaining.remove(found);
					if (remaining.isEmpty())
						break;
				}
			}
		}
		_setSelectedRows(getJsObject(), loader, arr, fireOnSelect);
	}

	public void scrollToSelection() {
		_scrollToSelection(getJsObject(), getSelectedRowIndexes());
	}

	private native void _scrollToSelection(JavaScriptObject grid, JsArrayInteger rowIndexes) /*-{
		if (rowIndexes != null && rowIndexes.length > 0)
			grid.scrollRowToTop(rowIndexes[0]);
	}-*/;

	private native void _setSelectedRows(JavaScriptObject grid, JavaScriptObject loader, JsArrayInteger rowIndexes, boolean fireOnSelect) /*-{
		if (!fireOnSelect)
			loader.disableSelectEventFire = true;
		grid.setSelectedRows(rowIndexes != null ? rowIndexes : []);
		loader.disableSelectEventFire = false;
	}-*/;

	private native JavaScriptObject _createAggregator(String func, String field) /*-{
		if (func == "sum")
			return new $wnd.Slick.Data.Aggregators.Sum(field);
		if (func == "avg")
			return new $wnd.Slick.Data.Aggregators.Avg(field);
		if (func == "min")
			return new $wnd.Slick.Data.Aggregators.Min(field);
		if (func == "max")
			return new $wnd.Slick.Data.Aggregators.Max(field);
	}-*/;

	private native JavaScriptObject _createGroupDef(String field, String title, String template, JsArray<JavaScriptObject> aggregators, boolean collapsed, boolean aggregateCollapsed, boolean lazyTotalsCalculation) /*-{
		var gd = {
			getter : field,
			formatter : function(g) {
				var s = title + " : " + g.value
						+ "	<span style='color:green'>(" + g.count
						+ " öğe)</span>";
				return s;
			},
			collapsed : collapsed,
		};
		if (aggregators && aggregators.length > 0) {
			gd.aggregators = aggregators;
			gd.aggregateCollapsed = aggregateCollapsed;
			gd.lazyTotalsCalculation = lazyTotalsCalculation;
		}
		return gd;
	}-*/;

	@Override
	public void addGrouping(GroupingDefinition groupingItem) {
		if (grouping == null)
			grouping = new ArrayList<GroupingDefinition>();
		grouping.add(groupingItem);
	}

	@Override
	public void applyGrouping() {
		JsArray<JavaScriptObject> groupDef = JsArray.createArray().cast();
		for (GroupingDefinition gi : grouping) {
			for (ListColumn col : getColumns()) {
				if (col.getField().equals(gi.getField())) {
					JsArray<JavaScriptObject> aggregators = null;
					if (gi.getAggregators() != null) {
						aggregators = JsArray.createArray().cast();
						for (String field : gi.getAggregators().keySet()) {
							SummaryType colagg = gi.getAggregators().get(field);
							if (colagg != null) {
								aggregators.push(_createAggregator(colagg.toString(), field));
								for (ListColumn col2 : getColumns())
									if (col2.getField().equals(field)) {
										col2.setSummaryType(colagg);
										break;
									}
							}
						}
					}
					groupDef.push(_createGroupDef(col.getField(), col.getTitle(), col.getSummaryTemplate(), aggregators, gi.isCollapsed(), gi.isAggregateCollapsed(), gi.isLazyCalculation()));
					col.setGroupable(true);
					break;
				}
			}
		}
		currentGroupDef = groupDef;
		if (isAttached())
			_setGrouping(getDataView(), currentGroupDef);
	}

	private native void _setGrouping(JavaScriptObject dataView, JavaScriptObject groupDef) /*-{
		dataView.beginUpdate();
		dataView.setGrouping(groupDef);
		dataView.endUpdate();
	}-*/;

	@Override
	public void setListing(IJsonServicePoint listingEnum) {
		getOptions().set("dataURL", JsUtil.getServiceUrl(listingEnum));
	}

	@Override
	public void setPaging(boolean dataPaging) {
		this.dataPaging = dataPaging;
		setRowsPerPage(dataPaging ? 50 : 0);
	}

	public int getRowsPerPage() {
		return getOptions().getInt("rowsPerPage", 50);
	}

	public void setRowsPerPage(int rowsPerPage) {
		getOptions().setInt("rowsPerPage", rowsPerPage);
	}

	@Override
	public void setAutoLoad(boolean autoLoad) {
		this.autoLoad = autoLoad;
	}

	public IGridListener getListener() {
		return listener;
	}

	public DataGridStyler getStyler() {
		return styler;
	}

	public void setStyler(DataGridStyler styler) {
		this.styler = styler;
	}

	//----EVENTS
	protected void fireOnBeforeDataRequest(final JavaScriptObject postData) throws Exception {
		if (listener != null) {
			listener.onGridBeforeDataRequest(new JsonMap(postData));
		}
	}

	private boolean fireOnBeforeRowSelect(final String rowId, final JavaScriptObject rowData) throws Exception {
		if (listener != null) {
			return listener.onGridBeforeRowSelect(rowId, rowData != null ? new JsonMap(rowData) : null);
		} else
			return true;
	}

	private void fireOnRowSelect(final String rowId, boolean selected, final JavaScriptObject rowData) throws Exception {
		if (listener != null) {
			listener.onGridRowSelect(rowId, selected, rowData != null ? new JsonMap(rowData) : null);
		}
	}

	private void fireOnRowDoubleClick(final String rowId, final JavaScriptObject rowData) throws Exception {
		if (listener != null) {
			listener.onGridRowDoubleClick(rowId, rowData != null ? new JsonMap(rowData) : null);
		}
	}

	private void fireOnHeaderMenuItemClicked(String command, JavaScriptObject colDefObj) {
		String field = new JsonMap(colDefObj).getString("field");
		if (command.equals("group")) {
			if (grouping != null)
				grouping.clear();
			else
				grouping = new ArrayList<GroupingDefinition>();
			GroupingDefinition gi = new GroupingDefinition(field, false);
			grouping.add(gi);
			applyGrouping();
		}
	}

	private void fireOnDataLoaded(int from, int to, JavaScriptObject data) throws Exception {
		if (listener != null)
			listener.onGridDataLoaded(data);
	}

	private void fireLinkClicked(JavaScriptObject linkElement, int row, int cell, String field, int columnKey, String value) {
		for (ListColumn ec : getColumns())
			if (ec instanceof LinkColumn) {
				LinkColumn elc = (LinkColumn) ec;
				if (elc.getColumnKey() == columnKey) {
					JavaScriptObject jso = getData().get(row);
					elc.cellClicked(linkElement, value, jso != null ? new JsonMap(jso) : null);
					break;
				}
			}
	}

	@Override
	protected void onAttach() {
		if (grouping != null && !grouping.isEmpty())
			applyGrouping();
		super.onAttach();
	}

	private int lastCalculatedSize = 0;

	private void fireUpdateParentSize(int dataLength) {
		int calculated = Math.min(maxHeight, Math.max(85, 18 + ((dataLength + 2) * (getOptions().getInt("rowHeight", 24) + 2))));
		if (calculated != lastCalculatedSize) {
			setHeight(calculated + "px");
			lastCalculatedSize = calculated;
			onResize();
		}
	}

	@Override
	protected void onUnload() {
		listener = null;
		clearData();
		recInfo = null;
		loadingPanel = null;
		loader = null;
		styler = null;
		grouping = null;
		super.onUnload();
	}

	@Override
	public void setDataExportOptions(boolean useForeignKeysAsVariable) {
	}

}