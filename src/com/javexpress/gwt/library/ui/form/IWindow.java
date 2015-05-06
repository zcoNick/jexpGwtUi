package com.javexpress.gwt.library.ui.form;

import com.google.gwt.user.client.ui.HasWidgets;

public interface IWindow extends ISizeAwareWidget, HasWidgets {

	public String getId();

	public String getHeader();

	public String getWidth();

	public String getHeight();

	public void onShow();

	public void onClose();

	public boolean isResizable();

	public boolean isMaximizable();

	public void setWindowContainer(IWindowContainer wc);

}