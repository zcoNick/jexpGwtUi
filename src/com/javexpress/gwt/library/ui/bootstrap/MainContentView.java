package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.form.IUICompositeView;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class MainContentView extends AbstractContainer implements IUICompositeView {

	public MainContentView(String id) {
		super(DOM.createDiv());
		getElement().setId("v_" + id);
		getElement().addClassName("jexp-view");
	}

	@Override
	public String getId() {
		return getElement().getId();
	}

	public abstract void setContents(IUIComposite form);

	public IUIComposite getContents() {
		return (IUIComposite) (getWidgetCount() == 0 ? null : getWidget(0));
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		final IUIComposite cmp = getContents();
		addToolItem(FaIcon.question_circle, ClientContext.nlsCommon.yardim(), true, new Command() {
			@Override
			public void execute() {
				ClientContext.instance.openHelp((IUIComposite) getWidget(0));
			}
		});
		if (cmp.isSupportsAction(IUIComposite.ACT_INSERTRECORD)) {
			addToolItem(FaIcon.plus_circle, ClientContext.nlsCommon.yeni(), false, new Command() {
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

	@Override
	public abstract void setHeader(ICssIcon icon, String title);

	protected abstract void addToolItem(FaIcon icon, String title, boolean right, Command command);

	protected native void bindOnClick(Element el, Command command) /*-{
		$wnd.$(el).click(function() {
			command.@com.google.gwt.user.client.Command::execute()();
		});
	}-*/;

	@Override
	protected void onUnload() {
		_destroyByJs(getElement(), ".ub_" + getId());
		super.onUnload();
	}

	private native void _destroyByJs(Element el, String ubSel) /*-{
		$wnd.$(ubSel, $wnd.$(el)).off();
	}-*/;

	public void onHide() {
		IUIComposite form = getContents();
		form.onHide();
	}

	public void onShow() {
		IUIComposite form = getContents();
		form.onShow();
	}

}