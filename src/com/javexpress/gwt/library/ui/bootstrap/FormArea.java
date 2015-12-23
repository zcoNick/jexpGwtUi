package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.AbstractContainerFocusable;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;

public class FormArea extends AbstractContainerFocusable implements ISizeAwareWidget {

	private String	maxHeight;

	public FormArea() {
		super(DOM.createForm());
		setStyleName("jexpFormArea form-horizontal");
		getElement().setAttribute("role", "form");
	}

	public String getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(String maxHeight) {
		this.maxHeight = maxHeight;
		applyMaxHeight();
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
	public void onResize() {
		applyMaxHeight();
		super.onResize();
	}

	private void applyMaxHeight() {
		if (maxHeight != null) {
			String mh = maxHeight;
			if (mh.endsWith("%")) {
				mh = (Window.getClientHeight() * Integer.parseInt(mh.substring(0, mh.length() - 1)) / 100) + "px";
			}
			getElement().getStyle().setProperty("maxHeight", mh);
			getElement().getStyle().setOverflow(Overflow.VISIBLE);
		}
	}

}