package com.javexpress.gwt.library.ui.container.buttonbar;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ButtonBar extends FlexTable {

	private FlowPanel	leftPanel;
	private FlowPanel	panel;

	public ButtonBar() {
		this(false);
	}

	/** Designer compatible constructor */
	public ButtonBar(boolean center) {
		super();
		setStyleName("ui-state-default jexpBorderBox");
		setWidth("100%");
		setHeight("100%");
		setCellPadding(0);
		setCellSpacing(2);
		getElement().getStyle().setBorderWidth(0, Unit.PX);
		panel = new FlowPanel();
		setWidget(0, 1, panel);
		getCellFormatter().setHorizontalAlignment(0, 1, center ? HasHorizontalAlignment.ALIGN_CENTER : (JsUtil.isLTR() ? HasHorizontalAlignment.ALIGN_RIGHT : HasHorizontalAlignment.ALIGN_LEFT));
		getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	@Override
	public void add(Widget button) {
		panel.add(button);
	}

	public void addLeftButton(Widget button) {
		if (leftPanel == null) {
			leftPanel = new FlowPanel();
			setWidget(0, 0, leftPanel);
		}
		leftPanel.add(button);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	@Override
	protected void onUnload() {
		leftPanel = null;
		panel = null;
		super.onUnload();
	}

}