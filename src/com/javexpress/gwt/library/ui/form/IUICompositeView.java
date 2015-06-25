package com.javexpress.gwt.library.ui.form;

import com.google.gwt.user.client.ui.RequiresResize;
import com.javexpress.gwt.library.ui.ICssIcon;

public interface IUICompositeView extends RequiresResize {

	void setHeader(ICssIcon icon, String header);

}