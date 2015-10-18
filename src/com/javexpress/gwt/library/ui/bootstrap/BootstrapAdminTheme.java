package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;
import com.javexpress.common.model.item.FormDef;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.container.dashboard.ProvidesDashboardConfig;
import com.javexpress.gwt.library.ui.event.ApplicationReadyEvent;
import com.javexpress.gwt.library.ui.event.FormOpenRequest;
import com.javexpress.gwt.library.ui.event.handler.FormOpenRequestHandler;
import com.javexpress.gwt.library.ui.form.DashboardForm;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JexpCallback;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class BootstrapAdminTheme extends BootstrapTheme implements ISideBarHandler, INavBarHandler {

	protected DashboardForm				dashForm;
	protected ApplicationMainContent	mainContent;
	protected MainContentView			dashView;
	protected ApplicationSideBar		sideBar;
	protected ApplicationNavBar			navBar;
	protected ApplicationFooter			footer;
	protected ApplicationHeaderPanel	headerPanel;

	@Override
	protected void prepareCommons(ClientContext clientContext) {
		super.prepareCommons(clientContext);
		ClientContext.EVENT_BUS.addHandler(FormOpenRequest.TYPE, new FormOpenRequestHandler() {
			@Override
			public void onFormOpenRequested(FormOpenRequest formOpenRequest) {
				FormDef fd = formOpenRequest.getFormDef();
				if (fd.isInWorkpane())
					showInView(formOpenRequest.getCode(), formOpenRequest.getForm());
				else
					ClientContext.instance.showInWindow(formOpenRequest.getForm(), true);
			}
		});
	}

	@Override
	public void prepareUI(ClientContext clientContext) {
		prepareCommons(clientContext);
		injectUI(getClient().getApplicationCssPath(), new Command() {
			@Override
			public void execute() {
				buildGUI();
				ClientContext.EVENT_BUS.fireEvent(new ApplicationReadyEvent());
			}
		});
	}

	@Override
	public void handleHistoryValueChanged(final String path) {
		MainContentView cached = mainContent.findView(path);
		if (cached != null) {
			mainContent.showView(cached);
			return;
		}
		if (path.equals("dashboard"))
			mainContent.add(dashView);
		if (getNavHandler() == null)
			return;
		JexpCallback<IUIComposite> callBack = new JexpCallback<IUIComposite>() {
			@Override
			protected void onResult(IUIComposite result) {
				if (result == null)
					return;
				showInView(path, result);
			}
		};
		getNavHandler().handleNavigation(path, callBack);
	}

	public void showInView(String path, IUIComposite result) {
		MainContentView view = mainContent.createView(result.getId());
		view.setContents(result);
		mainContent.addView(path, view);
	}

	protected void createAndRegisterDashboard() {
		if (!(ClientContext.instance instanceof ProvidesDashboardConfig))
			return;
		ProvidesDashboardConfig pdc = (ProvidesDashboardConfig) ClientContext.instance;
		dashForm = new DashboardForm("dashboard", pdc.getDashboardColumns());
	}

	@Override
	public void navLinkClicked(String path) {
	}

	@Override
	public void sideLinkClicked(final String path) {
	}

	protected void buildGUI() {
		RootPanel body = RootPanel.get();
		body.setStyleName(getSkinName());

		JsUtil.setNumeralLibLanguage(LocaleInfo.getCurrentLocale().getLocaleName());
	}

}