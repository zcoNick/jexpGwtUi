package com.javexpress.gwt.library.ui.data.editgrid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.javexpress.gwt.library.ui.data.ListColumn.SummaryType;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public abstract class EditGridDataAdapter<T> {

	public ArrayList<T> getData(final JsArray data) {
		ArrayList<T> list = new ArrayList<T>();
		for (int i = 0; i < data.length(); i++) {
			JsonMap json = new JsonMap(data.get(i));
			T t = createTarget(i, json);
			if (t != null)
				list.add(t);
		}
		return list;
	}

	@Deprecated
	protected void addData(final List<T> data, final JSONArray array) {
		for (T s : data) {
			addData(s, array);
		}
	}

	@Deprecated
	public JsonMap addData(final T s, final JSONArray array) {
		JsonMap json = getAsJson(s);
		array.set(array.size(), json);
		return json;
	}

	public JsonMap addData(final T s, final JsArray array) {
		JsonMap json = getAsJson(s);
		array.set(array.length(), json.getJavaScriptObject());
		return json;
	}

	public JsonMap getAsJson(final T s) {
		JsonMap json = new JsonMap();
		loadSource(json, s);
		return json;
	}

	protected abstract T createTarget(int index, JsonMap data);

	protected abstract void loadSource(JsonMap data, T dto);

	/** @param row
	 *            : row index
	 * @param cell
	 *            : cell index
	 * @param field
	 *            : fieldName
	 * @param summaryType
	 *            : Cell's SummaryType
	 * @param jsonMap
	 *            : Current row data */
	public void gridCellChanged(int row, int cell, String field, SummaryType summaryType, JsonMap rowData) {
	}

	protected BigDecimal getDecimal(final JSONObject data, final String key) {
		return JsUtil.asDecimal(data.get(key));
	}

	/** @return true if the cell can be edited */
	public boolean gridBeforeEditCell(final int row, final int cell, final String field, final JsonMap data) {
		return true;
	}

	public void gridBeforeCellEditorDestroy(final int row, final int cell, final String field, final JsonMap data) {
	}

	public void gridRowsDeleted(final List<JsonMap> deletedRowDatas) {
	}

	public boolean canDeleteRows(final List<JsonMap> rowDatas) {
		return true;
	}

}