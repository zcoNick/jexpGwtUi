package com.javexpress.gwt.library.ui.form;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.container.layout.DivPanelInfo;
import com.javexpress.gwt.library.ui.container.panel.SplitPanel;

public abstract class SplitLayoutForm extends Form {

	private SplitPanel		layout;
	private final boolean	horizontal;

	public SplitPanel getLayout() {
		return layout;
	}

	public SplitLayoutForm(final String id, boolean horizontal) {
		super(id);
		this.horizontal = horizontal;
	}

	public SplitLayoutForm(boolean horizontal) {
		this(null, horizontal);
	}

	@Override
	protected void createGUI() {
		if (layout != null)
			return;
		layout = new SplitPanel(that, "lyt", horizontal, true);
		setWidget(layout);
		try {
			DivPanelInfo dock = createLeftOrTopWidget();
			if (dock != null)
				setLeftWidget(dock);

			dock = createRightOrBottomWidget();
			if (dock != null)
				setRightWidget(dock);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Unit getLayoutUnits() {
		return Unit.PCT;
	}

	protected Widget getTopWidget() {
		return layout.getLeftOrTopWidget();
	}

	protected Widget getLeftWidget() {
		return getTopWidget();
	}

	protected void setTopWidget(final DivPanelInfo divPanel) throws Exception {
		layout.setLeftOrTopWidget(divPanel.widget, divPanel.size);
	}

	protected void setLeftWidget(final DivPanelInfo divPanel) throws Exception {
		setTopWidget(divPanel);
	}

	protected Widget getRightWidget() {
		return getBottomWidget();
	}

	protected void setRightWidget(final DivPanelInfo divPanel) throws Exception {
		setBottomWidget(divPanel);
	}

	protected Widget getBottomWidget() {
		return layout.getRightOrBottomWidget();
	}

	protected void setBottomWidget(final DivPanelInfo divPanel) throws Exception {
		layout.setRightOrBottomWidget(divPanel.widget, divPanel.size);
	}

	protected DivPanelInfo createLeftOrTopWidget() throws Exception {
		return null;
	}

	protected DivPanelInfo createRightOrBottomWidget() throws Exception {
		return null;
	}

	@Override
	protected abstract Focusable getFocusWidget();

	@Override
	public void onShow() {
		super.onShow();
		onResize();
	}

	@Override
	public void onClose() {
	}

	@Override
	protected void onUnload() {
		layout = null;
		super.onUnload();
	}

	@Override
	public void onResize() {
		if (layout != null)
			layout.onResize();
	}

}