package com.javexpress.gwt.library.ui.data.slickgrid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.data.Column.ColumnAlign;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.data.GridToolItem;
import com.javexpress.gwt.library.ui.data.IToolItemHandler;
import com.javexpress.gwt.library.ui.data.ListColumn.SummaryType;
import com.javexpress.gwt.library.ui.data.editgrid.EditAutoCompleteColumn;
import com.javexpress.gwt.library.ui.data.editgrid.EditColumn;
import com.javexpress.gwt.library.ui.data.editgrid.EditColumn.EditorType;
import com.javexpress.gwt.library.ui.data.editgrid.EditComboColumn;
import com.javexpress.gwt.library.ui.data.editgrid.EditDateColumn;
import com.javexpress.gwt.library.ui.data.editgrid.EditDecimalColumn;
import com.javexpress.gwt.library.ui.data.editgrid.EditGridDataAdapter;
import com.javexpress.gwt.library.ui.data.editgrid.EditLinkColumn;
import com.javexpress.gwt.library.ui.data.editgrid.EditMaskColumn;
import com.javexpress.gwt.library.ui.data.editgrid.EditRowComboColumn;
import com.javexpress.gwt.library.ui.form.IDataBindable;
import com.javexpress.gwt.library.ui.form.IFormFactory;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class EditGrid<T extends Serializable> extends BaseSlickGrid<EditColumn> implements IDataBindable {

	private EditGridDataAdapter<T>	dataAdapter;
	private EditGridStyler			styler;
	private GridToolItem			tiCommit;
	private GridToolItem			tiDelete;
	private boolean					deleteAllowed	= true;
	private DataBindingHandler		dataBinding;

	public EditGridDataAdapter<T> getDataAdapter() {
		return dataAdapter;
	}

	public void setDataAdapter(EditGridDataAdapter<T> dataAdapter) {
		this.dataAdapter = dataAdapter;
	}

	public EditGridStyler getStyler() {
		return styler;
	}

	public void setStyler(EditGridStyler styler) {
		this.styler = styler;
	}

	public void setReadOnly(boolean value) {
		getOptions().set("editable", !value);
		if (isAttached())
			_updateOptionBool(getJsObject(), "editable", !value);
	}

	public void setAutoEdit(boolean value) {
		getOptions().set("autoEdit", value);
	}

	public boolean isDeleteAllowed() {
		return deleteAllowed;
	}

	public void setDeleteAllowed(boolean deleteAllowed) {
		this.deleteAllowed = deleteAllowed;
	}

	public EditGrid(Widget parent, String id, boolean fitToParent, String keyColumnName) {
		this(parent, id, fitToParent, keyColumnName, null);
	}

	public EditGrid(Widget parent, String id, boolean fitToParent, String keyColumnName, String header) {
		super(parent, WidgetConst.EDITGRID_PREFIX, id, fitToParent, keyColumnName, header);
		getElement().addClassName("jexpEditGrid");
	}

	@Override
	protected JsonMap createDefaultOptions() {
		JsonMap options = super.createDefaultOptions();
		options.set("editable", true);
		options.set("enableAddRow", true);
		options.set("enableColumnReorder", false);
		options.set("asyncEditorLoading", true);
		options.setInt("asyncEditorLoadDelay", 50);
		options.set("autoEdit", true);
		options.setInt("rowHeight", 24);
		options.set("addNewRowCssClass", "egNewRow");
		return options;
	}

	public void setEnableAddRow(boolean value) {
		getOptions().set("enableAddRow", value);
		if (isAttached())
			_updateOptionBool(getJsObject(), "enableAddRow", value);
	}

	public void setFitColumns(boolean value) {
		getOptions().set("forceFitColumns", value);
	}

	public boolean isEditable() {
		return getOptions().getBoolean("editable");
	}

	@Override
	protected JavaScriptObject createJsObject(JSONArray colModel) {
		return createByJs(this, getContainer().getId(), getOptions().getJavaScriptObject(), colModel.getJavaScriptObject(), getKeyColumnName(), styler, getData());
	}

	@Override
	protected void renderToolItems() {
		if (isEditable()) {
			tiCommit = new GridToolItem("commit", null, FaIcon.check, IFormFactory.nlsCommon.uygula());
			tiCommit.setIconClass("green");
			tiCommit.setHandler(new IToolItemHandler() {
				@Override
				public void execute(Event event) {
					commitCurrentEdit();
				}
			});
			renderToolItem(tiCommit, false);

			tiDelete = new GridToolItem("delete", null, FaIcon.trash, IFormFactory.nlsCommon.sil());
			tiDelete.setIconClass("red");
			tiDelete.setHandler(new IToolItemHandler() {
				@Override
				public void execute(Event event) {
					if (!deleteAllowed)
						return;
					cancelCurrentEdit();
					JsArray<JavaScriptObject> objs = getSelectedRowsData();
					List<JsonMap> datas = new ArrayList<JsonMap>();
					if (objs != null)
						for (int i = 0; i < objs.length(); i++)
							datas.add(new JsonMap(objs.get(i)));
					if (dataAdapter != null) {
						if (!dataAdapter.canDeleteRows(datas))
							return;
					}
					if (JsUtil.confirm(IFormFactory.nlsCommon.kayitSilmeOnayi()))
						deleteSelectedRows(datas);
				}
			});
			renderToolItem(tiDelete, false);
		}
		super.renderToolItems();
	}

	@Override
	protected JsonMap createColumnModel(final EditColumn column, final int index) {
		JsonMap model = super.createColumnModel(column, index);
		if (model == null)
			return null;
		if (column instanceof EditColumn) {
			EditColumn ec = column;
			String cssClass = ec.getCssClass();
			if (JsUtil.isEmpty(cssClass))
				cssClass = "";
			if (ec.getHint() != null && ec.getEditorType() != EditorType.link)
				model.set("toolTip", ec.getHint());
			model.set("required", ec.isRequired());
			if (!ec.isCanTriggerInsert())
				model.set("cannotTriggerInsert", true);
			model.set("editable", ec.isEditable());
			if (ec.isRequired())
				model.set("validator", "requiredFieldValidator");
			if (ec.getSummaryType() != null)
				model.set("summaryType", ec.getSummaryType().toString());
			switch (ec.getEditorType()) {
				case link:
					model.set("editor", "link");
					EditLinkColumn elc = (EditLinkColumn) ec;
					model.set("field", getKeyColumnName());
					model.set("linkIconClass", elc.getIcon().getCssClass());
					model.set("linkTitle", elc.getHint());
					model.set("linkOwner", getContainer().getId());
					int linkIndex = getColumns().indexOf(ec);
					model.setInt("linkIndex", linkIndex);
					elc.setLinkIndexInGrid(linkIndex);
					break;
				case text:
					model.set("editor", "text");
					break;
				case autocomplete:
					model.set("editor", "autocomplete");
					EditAutoCompleteColumn eacc = (EditAutoCompleteColumn) ec;
					model.set("field", eacc.getLabelField());
					JsonMap edop = ec.getOptions();
					edop.set("valueField", ec.getField());
					model.set("editorOptions", edop);
					model.set("hasListener", eacc.hasListener());
					break;
				case combo:
					((EditComboColumn) ec).setGrid(this);
					model.set("editor", "combo");
					model.set("hasListener", ((EditComboColumn) ec).hasListener());
					break;
				case rowcombo:
					EditRowComboColumn ercc = (EditRowComboColumn) ec;
					model.set("editor", "rowcombo");
					model.set("rowDataField", ercc.getRowDataField());
					model.set("labelField", ercc.getLabelField());
					model.set("hasListener", ((EditRowComboColumn) ec).hasListener());
					break;
				case check:
					model.set("editor", "check");
					if (column.getAlign() == ColumnAlign.right)
						model.set("cssClass", cssClass + " jexpRightAlign");
					else if (column.getAlign() == ColumnAlign.center)
						model.set("cssClass", cssClass + " jexpCenter");
					break;
				case decimal:
					EditDecimalColumn edcc = (EditDecimalColumn) ec;
					model.set("editor", "decimal");
					model.set("options", ec.getOptions());
					model.set("numeralFormat", JsUtil.createNumeralFormat(edcc.getDecimals(), edcc.isEmptyDecimals()));
					model.set("cssClass", cssClass + " jexpRightAlign");
					break;
				case longtext:
					model.set("editor", "longtext");
					model.set("saveText", IFormFactory.nlsCommon.kaydet());
					model.set("cancelText", IFormFactory.nlsCommon.vazgec());
					break;
				case date:
					model.set("editor", "date");
					EditDateColumn edc = (EditDateColumn) ec;
					model.set("isRTL", JsUtil.isRTL());
					model.set("language", LocaleInfo.getCurrentLocale().getLocaleName());
					model.set("mask", edc.getMatchingFormat());
					model.set("inputmask", edc.getInputFormat());
					break;
				case mask:
					model.set("editor", "mask");
					EditMaskColumn emc = (EditMaskColumn) ec;
					model.set("inputmask", emc.getMask());
					model.set("placeholder", emc.getPlaceHolder());
					break;
				case number:
					model.set("editor", "number");
					model.set("cssClass", cssClass + " jexpRightAlign");
					break;
			}
		}
		return model;
	}

	private native JavaScriptObject createByJs(EditGrid x, String elGridId, JavaScriptObject options, JavaScriptObject columns, String keyColumnName, EditGridStyler styler, JavaScriptObject data) /*-{
		options.jesrequiredvalidator = function(value) {
			if (value == null || value == undefined || !value.length) {
				return {
					valid : false,
					msg : "This is a required field"
				};
			} else {
				return {
					valid : true,
					msg : null
				};
			}
		}

		var dataView = new $wnd.Slick.Data.DataView();

		//Editor string to objectfactory
		for (var i = 0; i < columns.length; i++) {
			var model = columns[i];
			if (model.validator == "requiredFieldValidator")
				model.validator = options.jesrequiredvalidator;
			if (model.editor == "text")
				model.editor = $wnd.Slick.Editors.Text;
			else if (model.editor == "number")
				model.editor = $wnd.Slick.Editors.Integer;
			else if (model.editor == "check") {
				model.formatter = $wnd.JexpBoolFormatter;
				model.editor = $wnd.JexpCheckboxEditor;
			} else if (model.editor == "longtext")
				model.editor = $wnd.JexpLongTextEditor;
			else if (model.editor == "date")
				model.editor = $wnd.JexpDateEditor;
			else if (model.editor == "mask")
				model.editor = $wnd.JexpMaskEditor;
			else if (model.editor == "autocomplete") {
				model.editor = $wnd.JexpAutoCompleteEditor;
				if (model.hasListener) {
					model.sourceListener = function(coldef, rowData, request) {
						x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireAutoCompleteSource(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(coldef,rowData,request);
					}
					model.selectableListener = function(coldef, rowData,
							selectedItemData) {
						return x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireAutoCompleteItemSelectable(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(coldef,rowData,selectedItemData);
					}
					model.selectListener = function(coldef, rowData,
							selectedItemData) {
						x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireAutoCompleteItemSelected(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(coldef,rowData,selectedItemData);
					}
				}
			} else if (model.editor == "combo") {
				model.editor = $wnd.JexpSelectCellEditor;
				model.formatter = $wnd.JexpComboFormatter;
				if (model.hasListener) {
					model.itemShowing = function(coldef, rowData, value, label,
							data) {
						return x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireIsComboItemShowing(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(coldef,rowData,value,label,data);
					}
					model.selectListener = function(coldef, rowData, value,
							selectedItemData) {
						x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireComboItemSelected(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;Ljava/lang/String;)(coldef,rowData,value,selectedItemData);
					}
				}
				model.lazyInitializer = function(coldef, rowData) {
					x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireComboLazyInitializer(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(coldef,rowData);
				}
			} else if (model.editor == "rowcombo") {
				model.editor = $wnd.JexpRowSelectCellEditor;
				model.formatter = $wnd.JexpRowComboFormatter;
				if (model.hasListener) {
					model.selectListener = function(coldef, rowData, value,
							selectedItemData) {
						x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireRowComboItemSelected(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(coldef,rowData,value,selectedItemData);
					}
				}
			} else if (model.editor == "decimal") {
				model.editor = $wnd.JexpDecimalCellEditor;
				model.formatter = $wnd.JexpDecimalFormatter;
			} else if (model.editor == "link") {
				model.editor = null;
				model.focusable = false;
				model.formatter = $wnd.JexpLinkFormatter;
			}

			if (!model.editable) {
				model.editor = null;
				model.focusable = false;
			}
		}
		var grid = new $wnd.Slick.Grid("#" + elGridId, dataView, columns,
				options);
		grid.setSelectionModel(new $wnd.Slick.RowSelectionModel(options));

		if (styler) {
			var jesRowMetadata = function row_metadata(old_metadata_provider) {
				return function(row) {
					var item = this.getItem(row), ret = old_metadata_provider(row);
					if (item) {
						ret = ret || {};
						ret.cssClasses = (ret.cssClasses || '');
						x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireOnRowMetadata(ILcom/google/gwt/core/client/JavaScriptObject;ZLcom/google/gwt/core/client/JavaScriptObject;)(row, item, false, ret);
					}
					return ret;
				};
			}

			dataView.getItemMetadata = jesRowMetadata(dataView.getItemMetadata);
		}

		grid.onAddNewRow
				.subscribe(function(e, args) {
					var item = x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireNewRowAdded(Lcom/google/gwt/core/client/JavaScriptObject;)(args.item);
					item[keyColumnName] = "é"
							+ (Math.round(Math.random() * 10000));
					$wnd.$.extend(item, args.item);
					dataView.addItem(item);
				});

		// wire up model events to drive the grid
		dataView.onRowCountChanged.subscribe(function(e, args) {
			grid.updateRowCount();
			grid.render();
		});

		dataView.onRowsChanged
				.subscribe(function(e, args) {
					grid.invalidateRows(args.rows);
					grid.render();
					x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireRowsChanged()();
				});

		dataView.beginUpdate();
		dataView.setItems(data, keyColumnName);
		x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::setDataView(Lcom/google/gwt/core/client/JavaScriptObject;)(dataView);
		dataView.endUpdate();

		grid.onCellChange
				.subscribe(function(e, args) {
					var model = columns[args.cell];
					x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireCellChanged(IILjava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(args.row,args.cell,model.field,model.summaryType,args.item);
					dataView.updateItem(args.item.id, args.item);
				});

		grid.onSelectedRowsChanged
				.subscribe(function(e, args) {
					x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireSelectedRowChanged(I)(args.rows[0]);
				});

		grid.onBeforeEditCell
				.subscribe(function(e, args) {
					return x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireBeforeEditCell(IILjava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(args.row,args.cell,args.column.field,args.item,args);
				});

		grid.onBeforeCellEditorDestroy
				.subscribe(function(e, args) {
					var activeCell = args.grid.getActiveCell();
					var model = columns[activeCell.cell];
					x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireBeforeCellEditorDestroy(IILjava/lang/String;)(activeCell.row,activeCell.cell,model.field);
				});

		$wnd
				.$("#" + elGridId)
				.bind(
						"linkclicked",
						function(event, linkElement, row, cell, field,
								linkIndex, value) {
							x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireLinkClicked(Lcom/google/gwt/core/client/JavaScriptObject;IILjava/lang/String;ILjava/lang/String;)(linkElement,row,cell,field,linkIndex,value);
						});
		$wnd
				.$("#" + elGridId + ".slick-viewport")
				.on(
						"blur",
						function(e) {
							x.@com.javexpress.gwt.library.ui.data.slickgrid.EditGrid::fireOnBlur()();
						});
		return grid;
	}-*/;

	public void clear(boolean redraw) {
		for (int i = 0; i < getData().length(); i++) {
			JavaScriptObject obj = getData().get(i);
			JSONValue json = new JSONObject(obj).get(getKeyColumnName());
			if (json instanceof JSONNumber)
				_deleteRowById(getDataView(), Long.valueOf((long) ((JSONNumber) json).doubleValue()));
			else
				_deleteRowById(getDataView(), ((JSONString) json).stringValue());
		}
		if (redraw)
			redraw();
	}

	public void gotoCell(JavaScriptObject grid, int row, int col) {
		_gotoCell(grid, row, col);
	}

	private native void _gotoCell(JavaScriptObject grid, int row, int col) /*-{
		grid.gotoCell(row, col, true);
	}-*/;

	public void setEditingData(final List<T> data) {
		setValue(data);
	}

	public void setValue(final List<T> data) {
		clear(false);
		if (data != null && !data.isEmpty()) {
			addData(data);
		} else
			redraw();
	}

	public void addData(final List<T> data) {
		for (T s : data) {
			JsonMap json = dataAdapter.addData(s, getData());
			if (json.get(getKeyColumnName()) == null || json.get(getKeyColumnName()) instanceof JSONNull) {
				String id = "é" + (Math.round(Math.random() * 10000));
				json.set(getKeyColumnName(), id);
			}
		}
		_setData(getDataView(), getData(), getKeyColumnName());
	}

	private JsonMap getRowDataById(Serializable id) {
		JavaScriptObject obj = _getRowDataById(getDataView(), id);
		return obj == null ? null : new JsonMap(obj);
	}

	private native JavaScriptObject _getRowDataById(JavaScriptObject dataView, Serializable id) /*-{
		return dataView.getItemById(id);
	}-*/;

	public void deleteRowById(Serializable id) {
		List<JsonMap> datas = new ArrayList<JsonMap>();
		JsonMap data = getRowDataById(id);
		if (data != null)
			datas.add(data);
		_deleteRowById(getDataView(), id);
		if (dataAdapter != null)
			dataAdapter.gridRowsDeleted(datas);
	}

	private native void _deleteRowById(JavaScriptObject dataView, Serializable id) /*-{
		dataView.deleteItem(id);
	}-*/;

	private native void _setData(JavaScriptObject dataView, JavaScriptObject data, String keyColumnName) /*-{
		dataView.beginUpdate();
		dataView.setItems(data, keyColumnName);
		dataView.endUpdate();
	}-*/;

	public void addData(final T data) {
		if (dataAdapter != null) {
			if (isAttached()) {
				JsonMap json = dataAdapter.getAsJson(data);
				if (json.get(getKeyColumnName()) instanceof JSONNull) {
					String id = "é" + (Math.round(Math.random() * 10000));
					json.set(getKeyColumnName(), id);
				}
				_addData(getJsObject(), json.getJavaScriptObject());
			} else {
				JsonMap json = dataAdapter.addData(data, getData());
				if (json.get(getKeyColumnName()) instanceof JSONNull) {
					String id = "é" + (Math.round(Math.random() * 10000));
					json.set(getKeyColumnName(), id);
				}
			}
		}
	}

	private native void _addData(JavaScriptObject dataView, JavaScriptObject item) /*-{
		dataView.addItem(item);
	}-*/;

	public void invalidateSelectedRow() {
		if (!isAttached())
			return;
		JavaScriptObject obj = _getSelectedRows(getJsObject());
		if (obj == null)
			return;
		JsArrayInteger arr = obj.cast();
		if (arr.length() == 1)
			_invalidateRow(getJsObject(), arr.get(0));
	}

	private native void _invalidateRow(JavaScriptObject grid, int rowIndex) /*-{
		grid.invalidateRow(rowIndex);
		grid.render();
	}-*/;

	public ArrayList<T> getEditingData() {
		return getValue();
	}

	public ArrayList<T> getValue() {
		if (dataAdapter == null)
			return null;
		return dataAdapter.getData(getData());
	}

	@Override
	protected void onUnload() {
		dataAdapter = null;
		tiDelete = null;
		dataBinding = null;
		styler = null;
		super.onUnload();
	}

	public void commitCurrentEdit() {
		_commitCurrentEdit(getJsObject());
	}

	public void cancelCurrentEdit() {
		_cancelCurrentEdit(getJsObject());
	}

	private native void _commitCurrentEdit(JavaScriptObject slick) /*-{
		var ctrl = slick.getEditController();
		if (ctrl)
			ctrl.commitCurrentEdit();
	}-*/;

	private native void _cancelCurrentEdit(JavaScriptObject slick) /*-{
		var ctrl = slick.getEditController();
		if (ctrl)
			ctrl.cancelCurrentEdit();
	}-*/;

	private void deleteSelectedRows(List<JsonMap> datas) {
		_deleteSelectedRows(getJsObject(), getDataView(), getKeyColumnName());
		if (dataAdapter != null)
			dataAdapter.gridRowsDeleted(datas);
	}

	private native void _deleteSelectedRows(JavaScriptObject grid, JavaScriptObject dataView, String keyColumnName) /*-{
		var selectedIndexes = grid.getSelectedRows();
		for (var i = 0; i < selectedIndexes.length; i++) {
			var item = dataView.getItem(selectedIndexes[i]);
			if (item)
				dataView.deleteItem(item[keyColumnName]);
		}
		grid.invalidateRows(selectedIndexes);//was invalidate
		grid.render();
	}-*/;

	public Integer getSelectedRowIndex() {
		if (!isAttached())
			return null;
		JavaScriptObject obj = _getSelectedRows(getJsObject());
		if (obj == null)
			return null;
		JsArrayInteger arr = obj.cast();
		return arr.length() > 0 ? arr.get(0) : null;
	}

	public JsonMap getSelectedRowData() {
		JsArray<JavaScriptObject> obj = getSelectedRowsData();
		return obj == null || obj.length() == 0 ? null : new JsonMap(obj.get(0));
	}

	public JsArray<JavaScriptObject> getSelectedRowsData() {
		if (!isAttached())
			return null;
		JsArray<JavaScriptObject> obj = _getSelectedRowsData(getJsObject(), getDataView());
		return obj;
	}

	private native JsArray<JavaScriptObject> _getSelectedRowsData(JavaScriptObject grid, JavaScriptObject dataView) /*-{
		var selectedIndexes = grid.getSelectedRows();
		if (selectedIndexes && selectedIndexes.length > 0) {
			var arr = [];
			for (var i = 0; i < selectedIndexes.length; i++)
				arr[i] = dataView.getItem(selectedIndexes[i]);
			return arr;
		}
		return null;
	}-*/;

	public void setNewRowTriggerColumn(EditColumn column) {
		for (EditColumn col : getColumns()) {
			if (col != column)
				col.cannotTriggerInsert();
			else
				col.canTriggerInsert();
		}
	}

	//----EVENTS
	private JavaScriptObject fireNewRowAdded(JavaScriptObject item) {
		return item;
	}

	private void fireOnBlur() {
		commitCurrentEdit();
	}

	private void fireCellChanged(int row, int cell, String field, String summaryType, JavaScriptObject dataObj) {
		if (dataAdapter != null)
			dataAdapter.gridCellChanged(row, cell, field, summaryType != null ? SummaryType.valueOf(summaryType) : null, new JsonMap(dataObj));
	}

	private void fireRowsChanged() {
	}

	private void fireSelectedRowChanged(int row) {
		toggleToolItem(tiDelete, deleteAllowed);
	}

	private boolean fireBeforeEditCell(int row, int cell, String field, JavaScriptObject dataObj, JavaScriptObject args) {
		boolean r = true;
		if (dataAdapter != null)
			r = dataAdapter.gridBeforeEditCell(row, cell, field, dataObj == null ? null : new JsonMap(dataObj));
		toggleToolItem(tiCommit, r);
		return r;
	}

	private void fireBeforeCellEditorDestroy(int row, int cell, String field) {
		if (dataAdapter != null) {
			JavaScriptObject jso = getData().get(row);
			dataAdapter.gridBeforeCellEditorDestroy(row, cell, field, jso != null ? new JsonMap(jso) : null);
		}
		toggleToolItem(tiCommit, false);
	}

	private void fireLinkClicked(JavaScriptObject linkElement, int row, int cell, String field, int linkIndex, String value) {
		for (EditColumn ec : getColumns())
			if (ec.getField().equals(field)) {
				EditLinkColumn elc = (EditLinkColumn) ec;
				if (elc.getLinkIndexInGrid() == linkIndex) {
					JavaScriptObject jso = getData().get(row);
					elc.cellClicked(linkElement, value, jso != null ? new JsonMap(jso) : null);
					break;
				}
			}
	}

	private void fireAutoCompleteItemSelected(JavaScriptObject colDef, JavaScriptObject rowData, JavaScriptObject selectedItemData) {
		JsonMap model = new JsonMap(colDef);
		String fn = model.getString("id");
		for (EditColumn ec : getColumns())
			if (ec.getField().equals(fn)) {
				EditAutoCompleteColumn ecc = (EditAutoCompleteColumn) ec;
				ecc.fireAutoCompleteItemSelected(rowData, selectedItemData);
				break;
			}
	}

	private boolean fireAutoCompleteItemSelectable(JavaScriptObject colDef, JavaScriptObject rowData, JavaScriptObject selectedItemData) {
		JsonMap model = new JsonMap(colDef);
		String fn = model.getString("id");
		for (EditColumn ec : getColumns())
			if (ec.getField().equals(fn)) {
				EditAutoCompleteColumn ecc = (EditAutoCompleteColumn) ec;
				return ecc.fireAutoCompleteItemSelectable(rowData, selectedItemData);
			}
		return true;
	}

	private void fireAutoCompleteSource(JavaScriptObject colDef, JavaScriptObject rowData, JavaScriptObject postData) {
		JsonMap model = new JsonMap(colDef);
		String fn = model.getString("id");
		for (EditColumn ec : getColumns())
			if (ec.getField().equals(fn)) {
				EditAutoCompleteColumn ecc = (EditAutoCompleteColumn) ec;
				ecc.fireAutoCompleteBeforeRequest(rowData, postData);
				break;
			}
	}

	private void fireComboLazyInitializer(JavaScriptObject colDef, JavaScriptObject dataObj) {
		JsonMap model = new JsonMap(colDef);
		if (model.getBoolean("lazyinited"))
			return;
		String fn = model.getString("field");
		for (EditColumn ec : getColumns())
			if (ec.getField().equals(fn)) {
				EditComboColumn ecc = (EditComboColumn) ec;
				boolean inited = ecc.fillItems(model, dataObj, "editorLabels", "editorValues", "editorDatas");
				model.set("lazyinited", inited);
				break;
			}
	}

	private void fireComboItemSelected(JavaScriptObject colDef, JavaScriptObject rowData, String value, String selectedItemData) {
		JsonMap model = new JsonMap(colDef);
		String fn = model.getString("field");
		for (EditColumn ec : getColumns())
			if (ec.getField().equals(fn)) {
				EditComboColumn ecc = (EditComboColumn) ec;
				ecc.fireComboItemSelected(rowData, value, selectedItemData);
				break;
			}
	}

	private void fireRowComboItemSelected(JavaScriptObject colDef, JavaScriptObject rowData, String value, JavaScriptObject selectedItemData) {
		JsonMap model = new JsonMap(colDef);
		String fn = model.getString("field");
		for (EditColumn ec : getColumns())
			if (ec.getField().equals(fn)) {
				EditRowComboColumn ecc = (EditRowComboColumn) ec;
				ecc.fireComboItemSelected(rowData, value, selectedItemData);
				break;
			}
	}

	private boolean fireIsComboItemShowing(JavaScriptObject colDef, JavaScriptObject rowData, String value, String label, String data) {
		JsonMap model = new JsonMap(colDef);
		String fn = model.getString("field");
		for (EditColumn ec : getColumns())
			if (ec.getField().equals(fn)) {
				EditComboColumn ecc = (EditComboColumn) ec;
				return ecc.fireIsComboItemShowing(rowData, value, label, data);
			}
		return true;
	}

	private void fireOnRowMetadata(int row, JavaScriptObject rowData, boolean dirty, JavaScriptObject styleData) {
		JsonMap data = new JsonMap(rowData);
		JsonMap sd = new JsonMap(styleData);
		styler.prepareRowStyle(row, data, dirty, sd);
		if (styler.isRowEditable(row, data)) {
			List<String> restrictedColumns = styler.getDisabledCellNames(row, data, dirty);
			if (restrictedColumns != null) {
				JsonMap columnData = new JsonMap();
				data.put("columns", columnData);
				for (String rc : restrictedColumns) {
					JsonMap cd = new JsonMap();
					cd.set("focusable", false);
					cd.set("selectable", false);//colspan may be adapted by refactoring desing. not necessary for now https://github.com/mleibman/SlickGrid/wiki/Providing-data-to-the-grid
					columnData.put("rc", cd);
				}
			}
		} else {
			sd.set("focusable", false);
			sd.set("selectable", false);
		}
	}

	@Override
	public void setDataBindingHandler(DataBindingHandler handler) {
		this.dataBinding = handler;
		dataBinding.setControl(this);
	}

	@Override
	public DataBindingHandler getDataBindingHandler() {
		return dataBinding;
	}

	public void refreshActiveRowComboItems() {
		_refreshActiveRowComboItems(getJsObject());
	}

	private native void _refreshActiveRowComboItems(JavaScriptObject grid) /*-{
		var editor = grid.getCellEditor();
		if (editor && editor.refreshItems)
			editor.refreshItems.call(grid);
	}-*/;

}