package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.form.IWrappedInput;

public class FormGroupCell extends AbstractContainer implements IResponsiveCell {

	private Integer	xsSize			= 12;
	private Integer	smSize;
	private Integer	mdSize;
	private Integer	lgSize;
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

	public FormGroupCell() {
		super(DOM.createDiv());
		setStyleName("form-group");

		div = DOM.createDiv();
		div.setClassName("jexpControlContainer");
		getElement().appendChild(div);
	}

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

	@Override
	public Integer getXsSize() {
		return xsSize;
	}

	@Override
	public void setXsSize(Integer xsSize) {
		this.xsSize = xsSize;
	}

	@Override
	public Integer getSmSize() {
		return smSize != null ? smSize : getXsSize();
	}

	@Override
	public void setSmSize(Integer smSize) {
		this.smSize = smSize;
	}

	@Override
	public Integer getMdSize() {
		return mdSize != null ? mdSize : getSmSize();
	}

	@Override
	public void setMdSize(Integer mdSize) {
		this.mdSize = mdSize;
	}

	@Override
	public Integer getLgSize() {
		return lgSize != null ? lgSize : getMdSize();
	}

	@Override
	public void setLgSize(Integer lgSize) {
		this.lgSize = lgSize;
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
		if (xsSize != null)
			getElement().addClassName("col-xs-" + xsSize);
		if (smSize != null)
			getElement().addClassName("col-sm-" + smSize);
		if (mdSize != null)
			getElement().addClassName("col-md-" + mdSize);
		if (lgSize != null)
			getElement().addClassName("col-lg-" + lgSize);
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
		xsSize = null;
		smSize = null;
		mdSize = null;
		lgSize = null;
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