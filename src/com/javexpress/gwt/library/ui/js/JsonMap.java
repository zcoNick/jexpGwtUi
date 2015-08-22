package com.javexpress.gwt.library.ui.js;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;

public class JsonMap extends JSONObject {

	public JsonMap() {
		super();
	}

	public JsonMap(final JavaScriptObject jsValue) {
		super(jsValue);
	}

	public JsonMap set(final String key, final String value) {
		put(key, value != null ? new JSONString(value) : JSONNull.getInstance());
		return this;
	}

	public JsonMap set(final String key, final Date value) {
		put(key, value != null ? new JSONString(JexpGwtUser.formatDate(value)) : JSONNull.getInstance());
		return this;
	}

	public JsonMap setLong(final String key, final Long value) {
		put(key, value != null ? new JSONNumber(value.doubleValue()) : JSONNull.getInstance());
		return this;
	}

	public JsonMap set(final String key, final BigDecimal value) {
		put(key, value != null ? new JSONNumber(value.doubleValue()) : JSONNull.getInstance());
		return this;
	}

	public JsonMap set(final String key, final Double value) {
		put(key, value != null ? new JSONNumber(value.doubleValue()) : JSONNull.getInstance());
		return this;
	}

	public JsonMap set(final String key, final JsonMap value) {
		put(key, value != null ? value : JSONNull.getInstance());
		return this;
	}

	public JsonMap setBool(final String key, final Boolean value) {
		put(key, value != null ? JSONBoolean.getInstance(value) : JSONNull.getInstance());
		return this;
	}

	public JsonMap set(final String key, final boolean value) {
		put(key, JSONBoolean.getInstance(value));
		return this;
	}

	public JsonMap setInt(final String key, final Integer value) {
		put(key, value != null ? new JSONNumber(value.doubleValue()) : JSONNull.getInstance());
		return this;
	}

	public JsonMap setShort(final String key, final Short value) {
		put(key, value != null ? new JSONNumber(value.doubleValue()) : JSONNull.getInstance());
		return this;
	}

	public JsArray set(final String key, final JsArray array) {
		put(key, new JSONObject(array));
		return array;
	}

	public JsArrayNumber set(final String key, final JsArrayNumber array) {
		put(key, new JSONObject(array));
		return array;
	}

	public JsArrayString set(final String key, final JsArrayString array) {
		put(key, new JSONObject(array));
		return array;
	}

	public JsArray getArray(final String key) {
		JSONValue jsonobj = get(key);
		if (jsonobj == null)
			return null;
		return jsonobj.isArray().getJavaScriptObject().cast();
	}

	public JsonMap clear(final String key) {
		put(key, JSONNull.getInstance());
		return this;
	}

	public String getString(final String key) {
		JSONValue v = get(key);
		return v == null || v instanceof JSONNull ? null : v.isString().stringValue();
	}

	public boolean getBoolean(final String key) {
		JSONValue v = get(key);
		if (v == null || v instanceof JSONNull)
			return false;
		return v == null ? null : v.isBoolean().booleanValue();
	}

	public Double getDouble(final String key) {
		JSONValue v = get(key);
		if (v instanceof JSONNull)
			return null;
		return v == null ? null : (v instanceof JSONString) ? JsUtil.asDouble(v.isString().stringValue()) : v.isNumber().doubleValue();
	}

	public Date getDate(final String key) {
		JSONValue v = get(key);
		if (v == null || v instanceof JSONNull)
			return null;
		return v == null ? null : (v instanceof JSONString) ? JsUtil.asDate(v.isString().stringValue()) : null;
	}

	public BigDecimal getDecimal(String key) {
		return getDecimal(key, null);
	}

	public BigDecimal getDecimal(String key, Double defaultValue) {
		JSONValue v = get(key);
		if (v == null || v instanceof JSONNull)
			return defaultValue == null ? null : new BigDecimal(defaultValue);
		return (v instanceof JSONString) ? JsUtil.asDecimal(v.isString().stringValue()) : BigDecimal.valueOf(v.isNumber().doubleValue());
	}

	public Integer getInt(String key) {
		return JsUtil.asInteger(get(key));
	}

	public int getInt(String key, int defaultValue) {
		JSONValue v = get(key);
		if (v == null || v instanceof JSONNull)
			return defaultValue;
		if (v instanceof JSONString)
			return JsUtil.asInteger(v.isString().stringValue());
		return (int) v.isNumber().doubleValue();
	}

	public Long getLong(String key) {
		return JsUtil.asLong(get(key));
	}

	public Short getShort(String key) {
		return JsUtil.asShort(get(key));
	}

	public Byte getByte(String key) {
		return JsUtil.asByte(get(key));
	}

	public Long getLongOrNull(String key) {
		try {
			return JsUtil.asLong(get(key));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public void mergeWith(JsonMap other) {
		if (other == null)
			return;
		for (String key : other.keySet())
			put(key, other.get(key));
	}

	public void setDefault(String key, String value, String def) {
		set(key, value != null ? value : def);
	}

	public void setLikeStart(String key, String value) {
		if (JsUtil.isEmpty(value))
			set(key, "");
		else {
			set(key, value.indexOf("%") == -1 ? value + "%" : value);
		}
	}

	public void setLikeEnd(String key, String value) {
		if (JsUtil.isEmpty(value))
			set(key, "");
		else {
			set(key, value.indexOf("%") == -1 ? "%" + value : value);
		}
	}

	public void setLike(String key, String value) {
		if (JsUtil.isEmpty(value))
			set(key, "");
		else {
			set(key, value.indexOf("%") == -1 ? "%" + value + "%" : value);
		}
	}

	public JsonMap createFiltersMap() {
		JsonMap map = new JsonMap();
		put("filters", map);
		return map;
	}

	public String serialize() {
		return _serialize(getJavaScriptObject());
	}

	private static native String _serialize(JavaScriptObject jso) /*-{
		return $wnd.JexpUI.AjaxUtils.serialize(jso);
	}-*/;

	public static JsonMap parse(String json) {
		JSONValue v = JSONParser.parseStrict(json);
		if (v == null)
			return null;
		return new JsonMap(v.isObject().getJavaScriptObject());
	}

	public boolean isEmpty(String key) {
		JSONValue v = get(key);
		if (v == null || v instanceof JSONNull)
			return true;
		return v.isString().stringValue().trim().equals("");
	}

}