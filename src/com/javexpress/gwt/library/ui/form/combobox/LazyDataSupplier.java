package com.javexpress.gwt.library.ui.form.combobox;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

@Deprecated
public abstract class LazyDataSupplier<T, V extends Serializable, L extends Serializable> implements AsyncCallback<List<T>> {

	@Override
	public void onSuccess(final List<T> result) {
		if (result == null)
			return;
		fillItems(result);
		dataLoadCompleted(true);
	}

	protected abstract void loadData(AsyncCallback<List<T>> callback);

	protected abstract void fillItems(List<T> result);

	protected abstract V getValue(T t);

	protected abstract L getLabel(T t);

	@Override
	public void onFailure(final Throwable caught) {
		GWT.log("Couldnt get list/combo items", caught);
		dataLoadCompleted(false);
	}

	protected Serializable getData(final T t) {
		return null;
	}

	protected void dataLoadCompleted(final boolean success) {
	}

}