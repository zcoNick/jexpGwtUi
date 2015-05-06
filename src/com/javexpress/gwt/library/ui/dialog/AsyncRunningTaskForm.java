package com.javexpress.gwt.library.ui.dialog;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.fw.ui.library.form.IFormFactory;
import com.javexpress.gwt.library.ui.bootstrap.FlatPanel;
import com.javexpress.gwt.library.ui.bootstrap.ProgressBar;
import com.javexpress.gwt.library.ui.form.ICallbackAware;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class AsyncRunningTaskForm<T> extends FlatPanel implements AsyncCallback<T> {

	private ProgressBar		progress;
	private ICallbackAware	callaware;

	public AsyncRunningTaskForm(Widget parent, String id) {
		super(parent, id);
		addStyleName("jexpShadow");
		setWidth("200px");

		if (parent instanceof ICallbackAware)
			callaware = (ICallbackAware) parent;

		getElement().getStyle().setPosition(Position.FIXED);
		getElement().getStyle().setTop(parent.getElement().getAbsoluteTop() + 30, Unit.PX);
		getElement().getStyle().setLeft(parent.getElement().getAbsoluteLeft() + (parent.getElement().getClientWidth() / 2) - 100, Unit.PX);
		getElement().getStyle().setZIndex(JsUtil.calcDialogZIndex());

		setHeader(getHeader());

		progress = new ProgressBar(this, "prg");
		add(progress);
	}

	protected String getHeader() {
		return IFormFactory.nlsCommon.lutfenBekleyin();
	}

	public void start() {
		start(null, 1);
	}

	public void start(String title, int totalWork) {
		if (callaware != null)
			callaware.callbackStarted();
		if (JsUtil.isNotEmpty(title))
			setHeader(title);
		progress.setMax(totalWork + 1);
		if (!isAttached())
			RootPanel.get().add(this);
		startTask(this);
		progress.increment();
	}

	protected abstract void startTask(AsyncCallback<T> callback);

	@Override
	public final void onFailure(Throwable caught) {
		progress.setAnimated(true);
		try {
			onComplete(false, null, caught);
			if (callaware != null)
				callaware.callbackCompleted();
			removeFromParent();
		} catch (Exception e) {
			JsUtil.handleError(this, e);
		}
	}

	@Override
	public final void onSuccess(T result) {
		progress.increment();
		try {
			onComplete(true, result, null);
			if (callaware != null)
				callaware.callbackCompleted();
			removeFromParent();
		} catch (Exception e) {
			JsUtil.handleError(this, e);
		}
	}

	protected abstract void onComplete(boolean success, T result, Throwable caught) throws Exception;

}