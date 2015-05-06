package com.javexpress.gwt.library.ui.container.panel;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class Panel extends JexpSimplePanel {

	private Widget		child;
	private DivElement	outer;
	private DivElement	top;
	private DivElement	center;

	public Panel(final String id, final String title) {
		super(DOM.createDiv());
		outer = getElement().cast();
		JsUtil.ensureId(null, this, WidgetConst.PANEL_PREFIX, id);
		outer.setClassName("ui-helper-reset ui-widget ui-widget-content ui-corner-all jexpBorderBox ui-shadow");

		top = DOM.createDiv().cast();
		top.setInnerText(title != null ? title : "");
		top.setAttribute("class", "ui-widget-header ui-corner-top");
		top.getStyle().setHeight(1.5, Unit.EM);
		top.getStyle().setPaddingLeft(3, Unit.PX);
		top.getStyle().setPaddingTop(2, Unit.PX);
		outer.appendChild(top);

		center = DOM.createDiv().cast();
		center.getStyle().setPosition(Position.RELATIVE);
		center.getStyle().setLeft(0, Unit.PX);
		center.getStyle().setRight(0, Unit.PX);
		center.getStyle().setBottom(0, Unit.PX);
		center.getStyle().setOverflow(Overflow.AUTO);
		outer.appendChild(center);
	}

	@Override
	public void add(final Widget pchild) {
		if (child != null) {
			if (isAttached()) {
				orphan(child);
			}
		}
		center.appendChild(pchild.getElement());
		this.child = pchild;
		if (isAttached())
			adopt(child);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		adopt(child);
	}

	@Override
	protected void onUnload() {
		outer = null;
		top = null;
		center = null;
		if (child != null)
			orphan(child);
		child = null;
		super.onUnload();
	}

}