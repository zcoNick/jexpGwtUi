package com.javexpress.gwt.library.ui.form;

import com.google.gwt.core.shared.GWT;
import com.javexpress.common.model.item.ControllerAction;
import com.javexpress.gwt.library.shared.nls.CommonResources;
import com.javexpress.gwt.library.ui.data.IDataViewer;
import com.javexpress.gwt.library.ui.js.JexpCallback;

public interface IFormFactory {

	public static CommonResources	nlsCommon	= GWT.create(CommonResources.class);

	public String getFactoryAlias();

	public void createForm(final String code, final ControllerAction mode, final IDataViewer grid, final BpmAction bpmAction, final JexpCallback<IUIComposite> callback);

}