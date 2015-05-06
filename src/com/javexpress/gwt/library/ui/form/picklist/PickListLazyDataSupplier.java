package com.javexpress.gwt.library.ui.form.picklist;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class PickListLazyDataSupplier<T, V extends Serializable> implements AsyncCallback<List<T>> {

	private PickList	pickList;

	protected void fillItems(final PickList pickList) {
		this.pickList = pickList;
		loadData(this);
	}

	@Override
	public void onSuccess(final List<T> result) {
		if (result == null)
			return;
		for (T t : result)
			pickList.addItem(getValue(t), getLabel(t), isSelected(t));
	}

	protected abstract void loadData(AsyncCallback<List<T>> callBack);

	protected abstract V getValue(T t);

	protected abstract String getLabel(T t);

	protected abstract boolean isSelected(T t);

	@Override
	public void onFailure(final Throwable caught) {
		GWT.log("Couldnt get list/combo items", caught);
	}

}