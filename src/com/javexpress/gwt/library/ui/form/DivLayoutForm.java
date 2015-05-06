package com.javexpress.gwt.library.ui.form;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.container.layout.DivBorderLayout;
import com.javexpress.gwt.library.ui.container.layout.DivPanelInfo;

public abstract class DivLayoutForm extends Form {

	private DivBorderLayout	layout;

	public DivBorderLayout getLayout() {
		return layout;
	}

	public DivLayoutForm(final String id) {
		super(id);
	}

	public DivLayoutForm() {
		this(null);
	}

	@Override
	protected void createGUI() {
		if (layout != null)
			return;
		layout = new DivBorderLayout(true);
		setWidget(layout);
		try {
			DivPanelInfo dock = createTopWidget();
			if (dock != null)
				setTopWidget(dock);

			dock = createBottomWidget();
			if (dock != null)
				setBottomWidget(dock);

			dock = createLeftWidget();
			if (dock != null)
				setLeftWidget(dock);

			dock = createRightWidget();
			if (dock != null)
				setRightWidget(dock);

			DivPanelInfo cw = createCenterWidget();
			if (cw != null)
				setCenterWidget(cw);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected Unit getLayoutUnits() {
		return Unit.PCT;
	}

	protected Widget getTopWidget() {
		return layout.getTopWidget();
	}

	protected void setTopWidget(final DivPanelInfo divPanel) throws Exception {
		layout.setTopWidget(divPanel);
	}

	protected Widget getLeftWidget() {
		return layout.getLeftWidget();
	}

	protected void setLeftWidget(final DivPanelInfo divPanel) throws Exception {
		layout.setLeftWidget(divPanel);
	}

	protected Widget getRightWidget() {
		return layout.getRightWidget();
	}

	protected void setRightWidget(final DivPanelInfo divPanel) throws Exception {
		layout.setRightWidget(divPanel);
	}

	protected Widget getCenterWidget() {
		return layout.getCenterWidget();
	}

	protected void setCenterWidget(final DivPanelInfo divPanel) {
		layout.setCenterWidget(divPanel.widget, divPanel.scrollable);
	}

	protected Widget getBottomWidget() {
		return layout.getBottomWidget();
	}

	protected void setBottomWidget(final DivPanelInfo divPanel) throws Exception {
		layout.setBottomWidget(divPanel);
	}

	protected DivPanelInfo createTopWidget() throws Exception {
		return null;
	}

	protected DivPanelInfo createLeftWidget() throws Exception {
		return null;
	}

	protected DivPanelInfo createRightWidget() throws Exception {
		return null;
	}

	protected DivPanelInfo createCenterWidget() throws Exception {
		return null;
	}

	protected DivPanelInfo createBottomWidget() {
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