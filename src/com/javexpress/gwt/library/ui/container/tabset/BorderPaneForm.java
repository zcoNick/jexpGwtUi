package com.javexpress.gwt.library.ui.container.tabset;

import com.google.gwt.dom.client.Style.Unit;
import com.javexpress.application.model.item.FormDef;
import com.javexpress.gwt.library.ui.ClientModule;
import com.javexpress.gwt.library.ui.form.DivLayoutForm;
import com.javexpress.gwt.library.ui.form.IJiraEnabledForm;

public abstract class BorderPaneForm<CM extends ClientModule> extends DivLayoutForm implements IJiraEnabledForm {

	private final CM	module;

	public BorderPaneForm(String id, CM module) {
		super(id);
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

	/** DESIGNER:DISABLE{PROPERTY=PROP_WIDTH,OF=this} */
	@Override
	public String getWidth() {
		return null;
	}

	/** DESIGNER:DISABLE{PROPERTY=PROP_WIDTH,OF=this} */
	@Override
	public String getHeight() {
		return null;
	}

	@Override
	public void onClose() {
	}

	@Override
	public long getModuleId() {
		return module.getModuleId();
	}

	@Override
	public String getFormQualifiedName() {
		return this.getClass().getName();
	}

	@Override
	public void openJiraIssue() {
		openJiraIssue(this);
	}

}