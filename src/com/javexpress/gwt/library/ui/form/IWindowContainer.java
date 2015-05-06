package com.javexpress.gwt.library.ui.form;

import com.google.gwt.user.client.ui.ProvidesResize;

public interface IWindowContainer extends ProvidesResize {
	
	public void close();

	public void setHeader(String header);

}