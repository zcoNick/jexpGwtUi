package com.javexpress.gwt.library.ui.form;

import java.io.Serializable;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;

public interface IDataBindable<T extends Serializable> extends IsWidget, HasValue<T> {

	public void setDataBindingHandler(DataBindingHandler handler);

	public DataBindingHandler getDataBindingHandler();

}