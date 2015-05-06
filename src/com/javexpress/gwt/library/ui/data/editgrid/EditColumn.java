package com.javexpress.gwt.library.ui.data.editgrid;

import com.google.gwt.dom.client.Element;
import com.javexpress.gwt.library.ui.data.Column;
import com.javexpress.gwt.library.ui.data.ListColumn.SummaryType;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class EditColumn extends Column {

	private EditorType	editorType;
	private boolean		required;
	private JsonMap		options;
	private Element		control;
	private String		hint;
	private String		cssClass;
	private SummaryType	summaryType;
	private boolean		canTriggerInsert = true;
	private boolean		editable = true;

	public static enum EditorType {
		check, text, longtext, number, decimal, date, autocomplete, combo, hidden, link, rowcombo, mask;
	}
	
	public boolean isEditable() {
		return editable;
	}

	public EditColumn setEditable(boolean editable) {
		this.editable = editable;
		return this;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(final String hint) {
		this.hint = hint;
	}

	public String getCssClass() {
		return cssClass;
	}

	public EditColumn setCssClass(String cssClass) {
		this.cssClass = cssClass;
		return this;
	}

	public SummaryType getSummaryType() {
		return summaryType;
	}

	public void setSummaryType(final SummaryType summaryType) {
		this.summaryType = summaryType;
	}

	/** Designer compatible constructor */
	public EditColumn(final String title, final String field) {
		this(title, field, null);
	}

	public EditColumn(final String title, final String field, String width) {
		this(title, null, field, width);
	}

	public EditColumn(final String title, final String hint, final String field, final String width) {
		this(title, hint, field, null, width);
	}

	public EditColumn(final String title, final String hint, final String field, final ColumnAlign align, final String width) {
		this(title, hint, field, null, width, null);
	}

	public EditColumn(final String title, final String hint, final String field, final ColumnAlign align, final String width, final SummaryType summaryType) {
		this(title, hint, field, align, width, EditorType.text, summaryType);
	}

	protected EditColumn(final String title, final String hint, final String field, final ColumnAlign align, final String width, final EditorType editorType, final SummaryType summaryType) {
		super(title, field, align, width, editorType == EditorType.hidden, false);
		this.hint = hint;
		this.editorType = editorType;
		this.summaryType = summaryType;
	}

	public EditorType getEditorType() {
		return editorType;
	}

	public void setEditorType(final EditorType editorType) {
		this.editorType = editorType;
	}

	public JsonMap getOptions() {
		return options;
	}

	public void setOptions(final JsonMap options) {
		this.options = options;
	}

	public Element getControl() {
		return control;
	}

	public void setControl(final Element control) {
		this.control = control;
	}

	public boolean isCanTriggerInsert() {
		return canTriggerInsert;
	}

	public EditColumn cannotTriggerInsert() {
		this.canTriggerInsert = false;
		return this;
	}

	public EditColumn canTriggerInsert() {
		this.canTriggerInsert = true;
		return this;
	}

}