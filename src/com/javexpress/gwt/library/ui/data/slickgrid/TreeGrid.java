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
import com.javexpress.gwt.library.ui.data.jqgrid.ExpandColumn;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.menu.JqPopupMenu;

public class TreeGrid<T extends Serializable> extends BaseSlickGrid<ListColumn> implements IDataViewer {

	private boolean				autoLoad;
	private int					maxHeight	= 0;
	private JavaScriptObject	loader;
	private IGridListener		listener;
	private Element				recInfo, loadingPanel;
	private DataGridStyler		styler;

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

	public TreeGrid(Widget parent, String id, boolean fitToParent, String keyColumnName) {
		this(parent, id, fitToParent, null, keyColumnName, true, null);
	}

	/** Designer compatible constructor */
	public TreeGrid(Widget parent, String id, boolean fitToParent, String keyColumnName, String header) {
		this(parent, id, fitToParent, null, keyColumnName, true, header);
	}

	public TreeGrid(Widget parent, String id, boolean fitToParent, final IJsonServicePoint servicePoint, String keyColumnName, boolean autoLoad, String header) {
		super(parent, WidgetConst.DATAGRID_PREFIX, id, fitToParent, keyColumnName, header);

		getElement().addClassName("jexpDataGrid jexpTreeGrid");
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
		options.setInt("maxRowsToFetch", 32768);
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
		if (column instanceof ExpandColumn)
			model.set("expander", true);
		return model;
	}

	@Override
	protected JavaScriptObject createJsObject(JSONArray colModel) {
		JavaScriptObject jso = createByJs(this, getContainer().getId(), getOptions().getJavaScriptObject(), colModel.getJavaScriptObject(), getKeyColumnName(), "jexpDataGridLoadingPanel jexpDataGridLoadingIndicator", getData(), autoLoad, JsUtil.calcDialogZIndex(), loadingPanel, recInfo, ClientContext.nlsCommon.kayitBulunamadi(), ClientContext.nlsCommon.grupla(), styler, maxHeight);
		autoLoad = false;
		return jso;
	}

	private native JavaScriptObject createByJs(TreeGrid x, String elGridId, JavaScriptObject options, JavaScriptObject columns, String keyColumnName, String loadingCssClassName, JavaScriptObject data, boolean autoLoad, int zIndex, Element loadingPanel, Element recInfo, String noRecFoundMessage, String groupMessage, DataGridStyler styler, int maxHeight) /*-{
		var sortModel = null;
		var hasGroupable = false;
		var expandCol = null;
		for (var i = 0; i < columns.length; i++) {
			var model = columns[i];
			if (model.sortedAsc)
				sortModel = model;

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
				if (model.jexpSummaryType == "sum")
					model.groupTotalsFormatter = $wnd.JexpDecimalSumFormatter;
			} else if (model.formatter == "currency") {
				model.formatter = $wnd.JexpCurrencyFormatter;
				if (model.jexpSummaryType == "sum")
					model.groupTotalsFormatter = $wnd.JexpDecimalSumFormatter;
			} else if (model.formatter == "link") {
				model.formatter = $wnd.JexpLinkFormatter;
			} else if (model.formatter == "percentbar") {
				model.formatter = $wnd.JexpPercentCompleteBarFormatter;
			}

			if (model.expander && !expandCol) {
				expandCol = model;
			}
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
		dataView = new $wnd.Slick.Data.DataView();
		x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::setDataView(Lcom/google/gwt/core/client/JavaScriptObject;)(dataView);

		//https://groups.google.com/forum/embed/#!topic/slickgrid/4ado2RxZsG8
		if (expandCol) {
			var originalFormatter = expandCol.formatter;
			expandCol.formatter = function(row, cell, value, columnDef,
					dataContext) {
				value = value.replace(/&/g, "&amp;").replace(/</g, "&lt;")
						.replace(/>/g, "&gt;");
				var spacer = "<span style='display:inline-block;height:1px;width:"
						+ (15 * dataContext["indent"]) + "px'></span>";
				var idx = dataView.getIdxById(dataContext.id);
				if (data[idx + 1] && data[idx + 1].indent > data[idx].indent) {
					if (dataContext._collapsed) {
						return spacer
								+ " <span class='toggle expand'></span>&nbsp;"
								+ value;
					} else {
						return spacer
								+ " <span class='toggle collapse'></span>&nbsp;"
								+ value;
					}
				} else {
					return spacer + " <span class='toggle'></span>&nbsp;"
							+ value;
				}
			}
		}

		var grid = new $wnd.Slick.Grid("#" + elGridId, dataView, columns,
				options);
		grid.setSelectionModel(new $wnd.Slick.RowSelectionModel(
				options.multiSelect ? {
					selectActiveRow : true
				} : {}));
		if (checkboxSelector)
			grid.registerPlugin(checkboxSelector);

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

						if (!dataView && maxHeight > 0) {//DataView ayrı listener var onRowCountChanged
							x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireUpdateParentSize(I)(data.length);
						}
					} else {
						recInfo.innerHTML = noRecFoundMessage;

						if (!dataView && maxHeight > 0) {//DataView ayrı listener var onRowCountChanged
							x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireUpdateParentSize(I)(1);
						}
					}

					$wnd.$(loadingPanel).removeClass(loadingCssClassName);

					dataView.beginUpdate();
					dataView.setItems(loader.data, keyColumnName);
					if (currentGroupDef) {
						dataView
								.setGrouping(currentGroupDef.length > 1 ? currentGroupDef
										: currentGroupDef[0]);
					}
					x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireOnDataLoaded(IILcom/google/gwt/core/client/JavaScriptObject;)(args.from, args.to, data);
					dataView.endUpdate();
				});

		dataView.onRowCountChanged
				.subscribe(function(e, args) {
					x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireUpdateParentSize(I)(args.current);
					grid.updateRowCount();
					grid.render();
				});
		dataView.onRowsChanged.subscribe(function(e, args) {
			grid.invalidateRows(args.rows);
			grid.render();
		});

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
						if (!args.rows || args.rows.length == 0)
							return;
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
			loader.ensureData(0, options.maxRowsToFetch);
		}
		$wnd
				.$("#" + elGridId)
				.bind(
						"linkclicked",
						function(event, linkElement, row, cell, field,
								columnKey, value) {
							var rowData = @com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::resolveRowData(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;I)(dataView, data, row);
							x.@com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::fireLinkClicked(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;ILjava/lang/String;ILjava/lang/String;)(linkElement,rowData,cell,field,columnKey,value);
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

	private native JsArray<JavaScriptObject> _getSelectedRowsData(TreeGrid x, JavaScriptObject grid, JavaScriptObject dataView, JavaScriptObject data) /*-{
		var selectedIndexes = grid.getSelectedRows();
		if (selectedIndexes && selectedIndexes.length > 0) {
			var arr = [];
			var filled = false;
			for (var i = 0; i < selectedIndexes.length; i++) {
				var rowData = @com.javexpress.gwt.library.ui.data.slickgrid.DataGrid::resolveRowData(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;I)(dataView, data, selectedIndexes[i]);
				if (rowData) {
					arr[i] = rowData;
					filled = true;
				}
			}
			return filled ? arr : null;
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
	public void setColumnReorder(boolean value) {
		getOptions().set("enableColumnReorder", value);
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
		grid.setSelectedRows(rowIndexes ? rowIndexes : []);
		loader.disableSelectEventFire = false;
	}-*/;

	@Override
	public void setListing(IJsonServicePoint listingEnum) {
		getOptions().set("dataURL", JsUtil.getServiceUrl(listingEnum));
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
	}

	private void fireOnDataLoaded(int from, int to, JavaScriptObject data) throws Exception {
		if (listener != null) {
			try {
				listener.onGridDataLoaded(data);
			} catch (Exception e) {
				JsUtil.handleError(this, e);
			}
		}
	}

	private void fireLinkClicked(JavaScriptObject linkElement, JavaScriptObject rowData, int cell, String field, int columnKey, String value) {
		for (ListColumn ec : getColumns())
			if (ec instanceof LinkColumn) {
				LinkColumn elc = (LinkColumn) ec;
				if (elc.getColumnKey() == columnKey) {
					elc.cellClicked(linkElement, value, rowData != null ? new JsonMap(rowData) : null);
					break;
				}
			}
	}

	private int lastCalculatedSize = 0;

	private void fireUpdateParentSize(int dataLength) {
		int calculated = Math.min(maxHeight, Math.max(85, TOPPANEL_HEIGHT + ((dataLength + 2) * (getOptions().getInt("rowHeight", 24) + 2))));
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
		super.onUnload();
	}

	@Override
	public void setPaging(boolean dataPaging) {
	}

	@Override
	public void setDataExportOptions(boolean useForeignKeysAsVariable, String quote, String sep, Integer readBlockSize) {
	}

	@Override
	public void addGrouping(GroupingDefinition groupingItem) {
	}

	@Override
	public void applyGrouping() {
	}

}