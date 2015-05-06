package com.javexpress.gwt.library.ui.container.tabset;

import com.google.gwt.dom.client.Style.Unit;
import com.javexpress.gwt.library.ui.ClientModule;
import com.javexpress.gwt.library.ui.form.SplitLayoutForm;

public abstract class SplitPaneForm<CM extends ClientModule> extends SplitLayoutForm {

	private final CM	module;

	public SplitPaneForm(String id, boolean horizontal, CM module) {
		super(id, horizontal);
		this.module = module;
		setWidth("auto");
		setHeight("100%");
		setStyleName("ui-widget-content");
		getElement().getStyle().setBorderWidth(0, Unit.PX);
	}

	protected CM getModule() {
		return module;
	}

	@Override
	public String getHeader() {
		FormDef fd = getFormDef();
		return module.getNls().getString(fd.getTitle() != null ? fd.getTitle() : fd.getId());
	}

	@Override
	public String getWidth() {
		return null;
	}

	@Override
	public String getHeight() {
		return null;
	}

	@Override
	public void onClose() {
	}

}