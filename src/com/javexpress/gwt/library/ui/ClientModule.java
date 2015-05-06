package com.javexpress.gwt.library.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.javexpress.application.model.item.FormDef;
import com.javexpress.application.model.item.IModuleEnums;
import com.javexpress.application.model.item.MenuNode;
import com.javexpress.application.model.item.OutputFormat;
import com.javexpress.application.model.item.ReportFilterValues;
import com.javexpress.application.model.item.type.Pair;
import com.javexpress.gwt.fw.client.GwtBootstrapApplication;
import com.javexpress.gwt.library.shared.nls.CommonResources;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.form.combobox.KeyValueDataLoader;
import com.javexpress.gwt.library.ui.js.JexpCallback;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.menu.MenuBar;

public abstract class ClientModule<SERVICEASYNC, NLS extends CommonResources, ENUMS extends IModuleEnums> extends JexpEntryPoint {

	protected Map<String, String>	formCodeAndTitles		= new HashMap<String, String>();
	protected Map<String, String>	reportCodeAndTitles		= new HashMap<String, String>();
	public Map<String, String[]>	processCodeAndTitles	= new HashMap<String, String[]>();

	public Map<String, String> getFormCodeAndTitles() {
		return formCodeAndTitles;
	}

	public Map<String, String> getReportCodeAndTitles() {
		return reportCodeAndTitles;
	}

	@Override
	public void onModuleLoad() {
		try {
			GwtBootstrapApplication.addModule(this);
		} catch (Exception e) {
			e.printStackTrace();
			GWT.log("Modül kaydedilemedi", e);
		}
	}

	protected GwtBootstrapApplication getClient() {
		return GwtBootstrapApplication.instance;
	}

	public abstract String getName();

	public abstract void onLoginSuccess();

	public abstract void registerDashboardWidgets() throws Exception;

	public abstract void fillMenuItems(MenuBar menuBar, MenuNode menuNode, List<Pair<Long, String>> reports, final List<Pair<Long, String[]>> processes);

	public abstract long getModuleId();

	public abstract SERVICEASYNC getService();

	public abstract NLS getNls();

	public abstract ENUMS getEnums();

	public abstract void runReportByDesignId(long moduleId, long reportDesignId, ReportFilterValues params, boolean filtreYazdir, OutputFormat format, boolean persist, boolean showImmediately, Command command);

	public abstract void runReportByDesignCode(long moduleId, Enum reportDesignCode, ReportFilterValues params, boolean filtreYazdir, OutputFormat format, boolean persist, boolean showImmediately, Command command);

	public ServiceDefTarget getServiceDefTarget() {
		return (ServiceDefTarget) getService();
	}

	@Override
	public void itemClicked(final String code) {
		createForm(code, null, null, null, new JexpCallback<IUIComposite>() {
			@Override
			protected void onResult(final IUIComposite form) {
				FormDef fd = form.getFormDef();
				if (fd.isInWorkpane())
					//GwtBootstrapApplication.instance.openInWorkPane(form);
					//else
					GwtBootstrapApplication.showInWindow(form);
			}
		});
	}

	public KeyValueDataLoader createKeyValueDataLoader(IUIComposite form) {
		KeyValueDataLoader cdl = new KeyValueDataLoader(getModuleId(), getServiceDefTarget());
		form.addOnLoadCommand(cdl);
		return cdl;
	}

	public void injectResources() {
	}

	public void configRequirePaths(JsonMap paths) {
	}

}