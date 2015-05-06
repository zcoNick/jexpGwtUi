package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.fw.client.GwtBootstrapApplication;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.form.IFormFactory;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.form.IUICompositeView;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class MainContentView extends AbstractContainer implements IUICompositeView {

	private Element	header;
	private Element	row;
	private Element	h1;

	public MainContentView(String id) {
		super(DOM.createDiv());
		getElement().setId("v_" + id);
		getElement().addClassName("jexp-view");

		header = DOM.createDiv();
		header.setClassName("page-header");
		getElement().appendChild(header);

		row = DOM.createDiv();
		row.setClassName("col-xs-12 no-padding");
		getElement().appendChild(row);
	}

	@Override
	public String getId() {
		return getElement().getId();
	}

	public void setContents(IUIComposite form) {
		setHeader(form.getIcon(), form.getHeader());
		Widget w = (Widget) form;
		w.addStyleName("container-fluid no-padding");
		add((Widget) form, row);
	}

	public void setHeader(ICssIcon icon, String title) {
		if (h1 != null)
			JsUtil.clearChilds(h1);
		else {
			h1 = DOM.createElement("h1");
			header.appendChild(h1);
		}
		if (icon != null)
			title = "<i class=\"ace-icon " + icon.getCssClass() + " smaller-90\"></i>&nbsp;" + title;
		h1.setInnerHTML(title + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		final IUIComposite cmp = (IUIComposite) getWidget(0);
		addToolItem(FaIcon.questionCircle, IFormFactory.nlsCommon.yardim(), true, new Command() {
			@Override
			public void execute() {
				GwtBootstrapApplication.openHelp((IUIComposite) getWidget(0));
			}
		});
		if (cmp.isSupportsAction(IUIComposite.ACT_INSERTRECORD)) {
			addToolItem(FaIcon.plusCircle, IFormFactory.nlsCommon.yeni(), false, new Command() {
				@Override
				public void execute() {
					try {
						cmp.performAction(IUIComposite.ACT_INSERTRECORD);
					} catch (Exception e) {
						JsUtil.handleError(getParent(), e);
					}
				}
			});
		}
	}

	protected void addToolItem(FaIcon icon, String title, boolean right, Command command) {
		Element i = DOM.createElement("i");
		i.setClassName("ub_" + getId() + " ace-icon " + icon.getCssClass() + " " + (right ? "pull-right" : "") + " jexpMainViewToolItem");
		i.setTitle(title);
		bindOnClick(i, command);
		h1.appendChild(i);
	}

	private native void bindOnClick(Element el, Command command) /*-{
		$wnd.$(el).click(function() {
			command.@com.google.gwt.user.client.Command::execute()();
		});
	}-*/;

	@Override
	protected void onUnload() {
		header = null;
		row = null;
		h1 = null;
		_destroyByJs(getElement(), ".ub_" + getId());
		super.onUnload();
	}

	private native void _destroyByJs(Element el, String ubSel) /*-{
		$wnd.$(ubSel, $wnd.$(el)).off();
	}-*/;

	public void onHide() {
		IUIComposite form = (IUIComposite) getWidget(0);
		form.onHide();
	}

	public void onShow() {
		IUIComposite form = (IUIComposite) getWidget(0);
		form.onShow();
	}

}