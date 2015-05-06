package com.javexpress.gwt.library.ui.data.progressbar;

import java.beans.Beans;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.form.IFormFactory;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ProgressBar extends SimplePanel {

	private Element	label;
	private int		workTotal;
	private int		workDone	= 0;

	public int getWorkTotal() {
		return workTotal;
	}

	public void setWorkTotal(int workTotal) {
		this.workTotal = workTotal;
	}

	public int getWorkDone() {
		return workDone;
	}

	public ProgressBar(final Widget parent, final String id) {
		this(parent, id, 0);
	}

	public ProgressBar(final Widget parent, final String id, final int workTotal) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.PROGRESSBAR_PREFIX, id);
		getElement().appendChild(label = DOM.createDiv());
		JsUtil.ensureSubId(getElement(), label, "lb");
		label.addClassName("ui-progress-label");
		getElement().setClassName(null);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!Beans.isDesignTime()) {
			createByJs(getElement(), label, IFormFactory.nlsCommon.tamamlandi());
		}
	}

	private native void createByJs(Element element, Element label, String completedString) /*-{
		var pr = $wnd.$(element);
		pr = pr.progressbar({
			value : false,
			change : function() {
				$wnd.$(label).text(pr.progressbar("value") + " %");
			},
			complete : function() {
				$wnd.$(label).text(completedString);
			}
		});
	}-*/;

	public void setWorkDone(int done) {
		workDone = done;
		if (workTotal == 0) {
			setPercent((byte) 100);
			return;
		}
		int pct = (done * 100) / workTotal;
		setPercent((byte) pct);
	}

	public void setPercent(byte pct) {
		setPercent_(getElement(), pct);
	}

	public void indeterminate() {
		setPercent((byte) -1);
	}

	private native void setPercent_(Element element, byte pct) /*-{
		$wnd.$(element).progressbar("value", pct == -1 ? false : pct);
	}-*/;

	@Override
	protected void onUnload() {
		if (!Beans.isDesignTime())
			destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).progressbar('destroy');
	}-*/;

	public String getId() {
		return getElement().getId();
	}

	public void increment() {
		setWorkDone(workDone + 1);
	}

}