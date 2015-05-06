package com.javexpress.gwt.library.ui.form;

import com.google.gwt.user.client.ui.IsWidget;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;

public interface IDataBindable extends IsWidget {

	public void setDataBindingHandler(DataBindingHandler handler);

	public DataBindingHandler getDataBindingHandler();

}