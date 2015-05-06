package com.javexpress.gwt.library.ui.form.button;

import java.beans.Beans;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.fw.ui.library.form.IFormFactory;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.menu.PopupMenu;

public abstract class SplitButton extends AbstractContainer {

	private JsonMap			options;
	private ButtonElement	normalButton;
	private ButtonElement	downButton;
	private PopupMenu		popup;
	private ClickHandler	normalButtonClickHandler;

	public void setIcon(final ICssIcon icon) {
		JsonMap icn = new JsonMap();
		icn.set("primary", icon.getCssClass());
		options.set("icons", icn);
	}

	public SplitButton(final Widget parent, final String id, final String text) {
		this(parent, id, null, text, null);
	}

	public SplitButton(final Widget parent, final String id, ICssIcon icon, final String text, final JsonMap pOptions) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.SPLITBUTTON_PREFIX, "cnt_" + id);
		getElement().addClassName("jesSplitButton");
		options = pOptions == null ? createDefaultOptions() : pOptions;

		normalButton = DOM.createButton().cast();
		normalButton.getStyle().setMargin(0, Unit.PX);
		normalButton.setInnerText(text);
		getElement().appendChild(normalButton);

		if (icon != null)
			setIcon(icon);

		downButton = DOM.createButton().cast();
		downButton.setInnerText(IFormFactory.nlsCommon.opsiyonlar());
		getElement().appendChild(downButton);

		popup = new PopupMenu() {
			@Override
			public void prepareMenu() {
				prepareSplitMenu(this);
			}
		};
	}

	public abstract void prepareSplitMenu(PopupMenu menu);

	private JsonMap createDefaultOptions() {
		options = new JsonMap();
		return options;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!Beans.isDesignTime()) {
			createByJs(this, normalButton, downButton, options.getJavaScriptObject());
		}
	}

	private native void createByJs(SplitButton x, ButtonElement nb, ButtonElement db, JavaScriptObject options) /*-{
		var bt = $wnd
				.$(nb)
				.button(options)
				.click(
						function(event) {
							x.@com.javexpress.gwt.library.ui.form.button.SplitButton::fireOnClick(IIZ)(event.pageX,event.pageY,event.metaKey);
						});
		var dw = $wnd
				.$(db)
				.button({
					text : false,
					icons : {
						primary : "ui-icon-triangle-1-s"
					}
				})
				.click(
						function(event) {
							x.@com.javexpress.gwt.library.ui.form.button.SplitButton::fireOnPopUp(IIZ)(event.pageX,event.pageY,event.metaKey);
						});
		bt.parent().buttonset();
	}-*/;

	public void addClickHandler(final ClickHandler handler) {
		normalButtonClickHandler = handler;
	}

	@Override
	protected void onUnload() {
		options = null;
		normalButtonClickHandler = null;
		if (!Beans.isDesignTime())
			destroyByJs(normalButton, downButton);
		normalButton = null;
		downButton = null;
		popup.removeFromParent();
		popup = null;
		super.onUnload();
	}

	private native void destroyByJs(ButtonElement nb, ButtonElement db) /*-{
		$wnd.$(nb).button('destroy');
		$wnd.$(db).button('destroy');
	}-*/;

	//--EVENTS
	private void fireOnClick(final int x, int y, boolean meta) {
		if (normalButtonClickHandler != null) {
			normalButtonClickHandler.onClick(null);
		}
		else
			fireOnPopUp(x, y, meta);
	}

	private void fireOnPopUp(int x, int y, boolean meta) {
		popup.popUp(x, y);
	}

}