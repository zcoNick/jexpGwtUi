package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.form.IWrappedInput;

public class LabelControlCell extends FormGroupCell {

	private String	label;
	private boolean	required;
	private Element	div;
	private Integer	labelXsCols		= 4;
	private Integer	labelSmCols;
	private Integer	labelMdCols;
	private Integer	labelLgCols;
	private Integer	controlXsCols	= 8;
	private Integer	controlSmCols;
	private Integer	controlMdCols;
	private Integer	controlLgCols;
	private String	labelId;

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public LabelControlCell() {
		super();

		div = DOM.createDiv();
		div.setClassName("jexpControlContainer");
		getElement().appendChild(div);
	}

	public Integer getLabelXsCols() {
		return labelXsCols;
	}

	public void setLabelXsCols(Integer labelXsCols) {
		this.labelXsCols = labelXsCols;
	}

	public Integer getLabelSmCols() {
		return labelSmCols;
	}

	public void setLabelSmCols(Integer labelSmCols) {
		this.labelSmCols = labelSmCols;
	}

	public Integer getLabelMdCols() {
		return labelMdCols;
	}

	public void setLabelMdCols(Integer labelMdCols) {
		this.labelMdCols = labelMdCols;
	}

	public Integer getLabelLgCols() {
		return labelLgCols;
	}

	public void setLabelLgCols(Integer labelLgCols) {
		this.labelLgCols = labelLgCols;
	}

	public Integer getControlXsCols() {
		return controlXsCols;
	}

	public void setControlXsCols(Integer controlXsCols) {
		this.controlXsCols = controlXsCols;
	}

	public Integer getControlSmCols() {
		return controlSmCols;
	}

	public void setControlSmCols(Integer controlSmCols) {
		this.controlSmCols = controlSmCols;
	}

	public Integer getControlMdCols() {
		return controlMdCols;
	}

	public void setControlMdCols(Integer controlMdCols) {
		this.controlMdCols = controlMdCols;
	}

	public Integer getControlLgCols() {
		return controlLgCols;
	}

	public void setControlLgCols(Integer controlLgCols) {
		this.controlLgCols = controlLgCols;
	}

	@Override
	public void add(Widget child) {
		add(child, div);
		if (child instanceof IWrappedInput)
			((IWrappedInput) child).getInputElement().addClassName("form-control");
		else
			child.addStyleName("form-control");
		if (child instanceof com.google.gwt.user.client.ui.FlowPanel)
			child.addStyleName("form-control-container");
		if (child instanceof CheckBox)
			child.addStyleName("jexp-ui-formcontrol-no-border");
	}

	@Override
	protected void onAttach() {
		if (label != null) {
			Element l = DOM.createLabel();
			if (labelXsCols != null)
				l.addClassName("col-xs-" + labelXsCols);
			if (labelSmCols != null)
				l.addClassName("col-sm-" + labelSmCols);
			if (labelMdCols != null)
				l.addClassName("col-md-" + labelMdCols);
			if (labelLgCols != null)
				l.addClassName("col-lg-" + labelLgCols);
			l.addClassName(" control-label no-padding-right" + (required ? " jexp-ui-field-required" : ""));
			if (labelId != null)
				l.setId(labelId);
			l.setInnerHTML(label);
			l.setTitle(label);
			getElement().insertFirst(l);
		} else {
			if (labelXsCols != null)
				div.addClassName("col-xs-offset-" + labelXsCols);
			if (labelSmCols != null)
				div.addClassName("col-sm-offset-" + labelSmCols);
			if (labelMdCols != null)
				div.addClassName("col-md-offset-" + labelMdCols);
			if (labelLgCols != null)
				div.addClassName("col-lg-offset-" + labelLgCols);
		}
		if (getWidgetCount() > 0) {
			if (controlXsCols != null)
				div.addClassName("col-xs-" + controlXsCols);
			if (controlSmCols != null)
				div.addClassName("col-sm-" + controlSmCols);
			if (controlMdCols != null)
				div.addClassName("col-md-" + controlMdCols);
			if (controlLgCols != null)
				div.addClassName("col-lg-" + controlLgCols);
		}
		super.onAttach();
	}

	@Override
	protected void onUnload() {
		label = null;
		div = null;
		labelXsCols = null;
		labelSmCols = null;
		labelMdCols = null;
		labelLgCols = null;
		labelId = null;
		super.onUnload();
	}

}