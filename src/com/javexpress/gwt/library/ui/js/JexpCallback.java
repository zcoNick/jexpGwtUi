package com.javexpress.gwt.library.ui.js;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javexpress.gwt.library.ui.form.ICallbackAware;

public abstract class JexpCallback<T> implements AsyncCallback<T> {

	private ICallbackAware	listener;

	public ICallbackAware getListener() {
		return listener;
	}

	public void setListener(ICallbackAware listener) {
		this.listener = listener;
	}

	public JexpCallback() {
		this(null);
	}

	public JexpCallback(ICallbackAware listener) {
		this.listener = listener;
		if (listener != null)
			listener.callbackStarted();
	}

	@Override
	public void onSuccess(T result) {
		try {
			onResult(result);
		} finally {
			onComplete();
		}
	}

	protected abstract void onResult(T result);

	@Override
	public void onFailure(final Throwable caught) {
		try {
			JsUtil.handleError(listener != null ? listener.getId() : null, caught);
		} finally {
			onComplete();
		}
	}

	protected void onComplete() {
		if (listener != null)
			listener.callbackCompleted();
	}

}