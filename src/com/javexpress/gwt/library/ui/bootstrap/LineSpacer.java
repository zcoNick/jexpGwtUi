package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;

public class LineSpacer extends SimplePanel implements IResponsiveCell {

	public LineSpacer(String size) {
		super(DOM.createDiv());
		if (size.equals("xsmall"))
			addStyleName("space-2");
		else if (size.equals("small"))
			addStyleName("space-4");
		else if (size.equals("medium"))
			addStyleName("space-6");
		else if (size.equals("big"))
			addStyleName("space-8");
	}

	@Override
	public Integer getXsSize() {
		return 12;
	}

	@Override
	public void setXsSize(Integer xsSize) {
	}

	@Override
	public Integer getSmSize() {
		return getXsSize();
	}

	@Override
	public void setSmSize(Integer smSize) {
	}

	@Override
	public Integer getMdSize() {
		return getSmSize();
	}

	@Override
	public void setMdSize(Integer mdSize) {
	}

	@Override
	public Integer getLgSize() {
		return getMdSize();
	}

	@Override
	public void setLgSize(Integer lgSize) {
	}

}