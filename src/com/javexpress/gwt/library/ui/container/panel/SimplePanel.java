package com.javexpress.gwt.library.ui.container.panel;

import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.bootstrap.IResponsiveCell;

public class SimplePanel extends AbstractContainer implements IResponsiveCell {

	private Integer xsSize = 12;
	private Integer smSize;
	private Integer mdSize;
	private Integer lgSize;

	public SimplePanel() {
		super(DOM.createDiv());
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
		super.onAttach();
	}

	@Override
	protected void onUnload() {
		xsSize = null;
		smSize = null;
		mdSize = null;
		lgSize = null;
		super.onUnload();
	}

	public void setMinHeight(String value) {
		getElement().getStyle().setProperty("minHeight", value);
	}

	public void setMaxHeight(String value) {
		getElement().getStyle().setProperty("maxHeight", value);
	}

}