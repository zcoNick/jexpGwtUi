package com.javexpress.gwt.library.ui.facet;

import java.io.Serializable;
import java.util.Map;

import com.javexpress.common.model.item.BpmAction;
import com.javexpress.common.model.item.ControllerAction;
import com.javexpress.gwt.library.ui.data.IDataViewer;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JexpCallback;

public interface IFormResolver {

	public void createForm(final String code, final Map<String, Serializable> args, final ControllerAction mode, final IDataViewer grid, final BpmAction bpmAction, final JexpCallback<IUIComposite> callback);

}