package com.javexpress.gwt.library.ui.form;

import java.io.Serializable;
import java.util.HashMap;

import com.javexpress.common.model.item.FormDef;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.bootstrap.UIComposite;
import com.javexpress.gwt.library.ui.container.dashboard.Dashboard;
import com.javexpress.gwt.library.ui.container.dashboard.IDashboardItemReplaceListener;

public class DashboardForm extends UIComposite {

	private Dashboard	dashboard;

	public DashboardForm(String id, int[] cols) {
		super(id + "_view");
		this.dashboard = new Dashboard(id, cols);
		setHeader(ClientContext.nlsCommon.ozetGorunum());
		add(dashboard);
	}

	public void addWidget(Byte kolon, Byte sira, String bilesenKodu, Long id, HashMap<String, Serializable> parameters) throws Exception {
		dashboard.addWidget(kolon, sira, bilesenKodu, id, parameters);
	}

	public void setListener(IDashboardItemReplaceListener listener) {
		dashboard.setListener(listener);
	}

	public void init() {
		dashboard.init();
	}

	@Override
	public void onShow() {
		super.onShow();
		dashboard.onShow();
	}

	@Override
	public void onHide() {
		super.onHide();
		dashboard.onHide();
	}

	@Override
	public FormDef getFormDef() {
		return null;
	}

}