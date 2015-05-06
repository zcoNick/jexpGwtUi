package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ProgressBar extends SimplePanel {

	private int			min		= 0;
	private int			max		= 0;
	private int			value	= 0;
	private WContext	wcontext;
	private Element		bar;

	public ProgressBar(Widget parent, String id) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.PROGRESSBAR_PREFIX, id);
		getElement().setClassName("progress jexp-ui-progress");
		bar = DOM.createDiv();
		bar.setClassName("progress-bar");
		bar.setAttribute("role", "progressbar");
		getElement().appendChild(bar);
	}

	public WContext getWcontext() {
		return wcontext;
	}

	public void setWcontext(WContext wcontext) {
		if (isAttached() && this.wcontext != null)
			bar.removeClassName("progress-bar-" + this.wcontext.getValue());
		this.wcontext = wcontext;
		if (isAttached())
			updateProgress();
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
		if (isAttached())
			updateProgress();
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
		if (isAttached())
			updateProgress();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		if (isAttached())
			updateProgress();
	}

	public int increment() {
		setValue(value + 1);
		return value;
	}

	@Override
	protected void onLoad() {
		updateProgress();
		super.onLoad();
	}

	private void updateProgress() {
		int pct = value <= min ? 0 : value * 100 / max;
		if (wcontext != null)
			bar.addClassName("progress-bar-" + wcontext.getValue());
		bar.getStyle().setWidth(pct, Unit.PCT);
		bar.setInnerText(pct + "%");
	}

	public void setAnimated(boolean animated) {
		if (!animated) {
			bar.removeClassName("progress-bar-striped");
			bar.removeClassName("active");
		} else {
			bar.addClassName("progress-bar-striped");
			bar.addClassName("active");
		}
	}

	@Override
	protected void onUnload() {
		wcontext = null;
		bar = null;
		super.onUnload();
	}

}