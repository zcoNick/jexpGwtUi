package com.javexpress.gwt.library.ui.data.editgrid;

import java.math.BigDecimal;

import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.ui.data.ListColumn.SummaryType;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class EditDecimalColumn extends EditColumn {

	/*
	 * Precision is 2 by default
	 */
	public EditDecimalColumn(final String title, final String field, String width) {
		this(title, null, field, width, null);
	}

	/** Designer compatible constructor */
	public EditDecimalColumn(final String title, final String field) {
		this(title, field, null, 2);
	}

	public EditDecimalColumn(final String title, final String field, final String width, Integer decimals) {
		this(title, null, field, width, null);
		setDecimals(decimals == null ? 2 : decimals);
	}

	/*
	 * Precision is 2 by default
	 */
	public EditDecimalColumn(final String title, final String field, final String width, final SummaryType summaryType) {
		this(title, null, field, width, summaryType);
	}

	/*
	 * Precision is 2 by default
	 */
	public EditDecimalColumn(final String title, final String hint, final String field, final String width, final SummaryType summaryType) {
		super(title, hint, field, ColumnAlign.right, width, EditorType.decimal, summaryType);
		JsonMap options = new JsonMap();
		setOptions(options);
		options.set("aSep", String.valueOf(JexpGwtUser.getCurrencyGroupChar()));
		options.set("aDec", String.valueOf(JexpGwtUser.getCurrencyDecimalChar()));
		options.setInt("dGroup", 3);
		options.set("aPad", false);//sondaki sıfırlar
		options.set("lZero", "deny");
	}

	public int getDecimals() {
		return getOptions().getInt("mDec");
	}

	public int getGroupCount() {
		return getOptions().getInt("dGroup");
	}

	public String getGroupChar() {
		return getOptions().getString("aSep");
	}

	public String getDecimalChar() {
		return getOptions().getString("aDec");
	}

	public void setMinValue(BigDecimal min) {
		getOptions().set("vMin", min);
	}

	public void setMaxValue(BigDecimal max) {
		getOptions().set("vMax", max);
	}

	@Deprecated
	public EditDecimalColumn setPrecision(int precision) {
		return setDecimals(precision);
	}

	public EditDecimalColumn setDecimals(int decimals) {
		getOptions().setInt("mDec", decimals);
		return this;
	}

	public EditDecimalColumn setDefaultZero(boolean defaultZero) {
		getOptions().set("defaultZero", defaultZero);
		getOptions().set("wEmpty", "zero");
		return this;
	}

	public boolean isEmptyDecimals() {
		return !getOptions().getBoolean("aPad");
	}

	public void setEmptyDecimals(boolean emptyDecimals) {
		getOptions().set("aPad", !emptyDecimals);
	}

}