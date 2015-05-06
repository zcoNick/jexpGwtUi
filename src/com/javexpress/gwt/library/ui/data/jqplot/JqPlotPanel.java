package com.javexpress.gwt.library.ui.data.jqplot;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;

public abstract class JqPlotPanel extends SimplePanel implements ISizeAwareWidget {

	private String	title;

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(final String title) {
		this.title = title;
	}

	public JqPlotPanel() {
		super(DOM.createDiv());
		getElement().addClassName("ui-widget-content");
	}

	@Override
	public void onResize() {
		refresh();
	}

	protected abstract void refresh();

}