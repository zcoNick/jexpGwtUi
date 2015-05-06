package com.javexpress.gwt.library.ui.form.filterpanel;

import com.google.gwt.user.client.ui.Widget;
import com.javexpress.application.model.item.DatasetFilterDescriptor;

public interface IFilterLabelAndControlDataResolver {

	String handleFilterLabelNls(String substring);

	boolean handleFilterControlData(Widget widget, DatasetFilterDescriptor fd);

}