package com.javexpress.gwt.library.ui.form;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.container.layout.DivBorderLayout;
import com.javexpress.gwt.library.ui.container.layout.DivPanelInfo;

public abstract class DivLayoutForm2 extends Form {

	protected DivBorderLayout	layout;

	public DivBorderLayout getLayout() {
		return layout;
	}

	public DivLayoutForm2(final String id) {
		super(id);
	}

	public DivLayoutForm2() {
		this(null);
	}

	@Override
	protected void createGUI() throws Exception {
		if (layout != null)
			return;
		layout = new DivBorderLayout();
		layout.setFitToParent(true);
		createTopWidget();
		createLeftWidget();
		createRightWidget();
		createBottomWidget();
		createCenterWidget();
		setWidget(layout);
	}

	/** DESIGNER:DROPTARGETMETHOD{POSITION=center,OF=layout} */
	protected void createCenterWidget() throws Exception {
	}

	/** DESIGNER:DROPTARGETMETHOD{POSITION=bottom,OF=layout} */
	protected void createBottomWidget() throws Exception {
	}

	/** DESIGNER:DROPTARGETMETHOD{POSITION=right,OF=layout} */
	protected void createRightWidget() throws Exception {
	}

	/** DESIGNER:DROPTARGETMETHOD{POSITION=left,OF=layout} */
	protected void createLeftWidget() throws Exception {
	}

	/** DESIGNER:DROPTARGETMETHOD{POSITION=top,OF=layout} */
	protected void createTopWidget() throws Exception {
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
	protected void nullify() {
		layout = null;
		super.nullify();
	}

	@Override
	public void onResize() {
		layout.onResize();
	}

}