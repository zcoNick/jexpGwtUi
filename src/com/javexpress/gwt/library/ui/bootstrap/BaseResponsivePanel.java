package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.AbstractContainerFocusable;

public class BaseResponsivePanel extends AbstractContainerFocusable implements IResponsiveCell {

	public BaseResponsivePanel(Element elem) {
		super(elem);
	}

	private Integer	xsSize	= 12;
	private Integer	smSize;
	private Integer	mdSize;
	private Integer	lgSize;

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
	protected void doAttachChildren() {
		super.doAttachChildren();
		int xsRemain = 12;
		int smRemain = 12;
		int mdRemain = 12;
		int lgRemain = 12;
		for (int i = 0; i < getChildren().size(); i++) {
			Widget w = getChildren().get(i);
			if (w instanceof IResponsiveCell) {
				IResponsiveCell rc = (IResponsiveCell) w;
				if (rc.getXsSize() != null)
					xsRemain -= rc.getXsSize();
				if (rc.getSmSize() != null)
					smRemain -= rc.getSmSize();
				if (rc.getMdSize() != null)
					mdRemain -= rc.getMdSize();
				if (rc.getLgSize() != null)
					lgRemain -= rc.getLgSize();
			}
			if (xsRemain == 0 || smRemain == 0 || mdRemain == 0 || lgRemain == 0) {
				Element afterOf = w.getElement();
				Element div = DOM.createDiv();
				String visibleClazz = "";
				if (xsRemain == 0) {
					visibleClazz += " visible-xs-block";
					xsRemain = 12;
				}
				if (smRemain == 0) {
					visibleClazz += " visible-sm-block";
					smRemain = 12;
				}
				if (mdRemain == 0) {
					visibleClazz += " visible-md-block";
					mdRemain = 12;
				}
				if (lgRemain == 0) {
					visibleClazz += " visible-lg-block";
					lgRemain = 12;
				}
				div.setClassName("clearfix" + visibleClazz);
				getElement().insertAfter(div, afterOf);
			}
		}
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

}