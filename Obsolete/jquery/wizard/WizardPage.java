package com.javexpress.gwt.library.ui.form.wizard;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class WizardPage extends JexpSimplePanel {
	
	protected Wizard wizard;
	protected WizardPage that;

	public WizardPage(final Wizard wizard, final String id) {
		this.wizard = wizard;
		JsUtil.ensureId(wizard, this, WidgetConst.WIZARDPAGE_PREFIX, id);
		setWidth("100%");
		setHeight("100%");
		setWidget(createGUI());
		that = this;
	}

	protected abstract Widget createGUI();
	
	protected abstract Focusable getFocusWidget();

	protected abstract void onPageActivated();

	protected abstract void onPageDeactivated();
	
	protected abstract boolean hasBack();
	
	protected abstract boolean hasNext(); 

	protected abstract WizardPage getNextPage();

	protected abstract WizardPage getBackPage();

	protected boolean canGoNext() {
		return true;
	}
	
	@Override
	protected void onUnload() {
		wizard = null;
		that = null;
		super.onUnload();
	}

}