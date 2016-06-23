package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;
import com.javexpress.common.model.item.FormDef;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;
import com.javexpress.gwt.library.ui.container.dashboard.ProvidesDashboardConfig;
import com.javexpress.gwt.library.ui.event.ApplicationReadyEvent;
import com.javexpress.gwt.library.ui.event.FormOpenRequest;
import com.javexpress.gwt.library.ui.event.NavigationItemClicked;
import com.javexpress.gwt.library.ui.event.handler.FormOpenRequestHandler;
import com.javexpress.gwt.library.ui.form.DashboardForm;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JexpCallback;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class BootstrapAdminTheme extends BootstrapTheme implements ISideBarHandler, INavBarHandler {

	protected DashboardForm dashForm;
	protected ApplicationMainContent mainContent;
	protected MainContentView dashView;
	protected ApplicationSideBar sideBar;
	protected ApplicationNavBar navBar;
	protected ApplicationFooter footer;
	protected ApplicationHeaderPanel headerPanel;

	@Override
	protected void prepareCommons(ClientContext clientContext) {
		super.prepareCommons(clientContext);
		ClientContext.EVENT_BUS.addHandler(FormOpenRequest.TYPE, new FormOpenRequestHandler() {
			@Override
			public void onFormOpenRequested(final FormOpenRequest formOpenRequest) {
				final String path = formOpenRequest.getPath();
				MainContentView cached = mainContent.findView(path);
				if (cached != null) {
					mainContent.showView(cached);
					return;
				}
				if (path.equals("dashboard"))
					mainContent.add(dashView);
				if (getNavHandler() == null)
					return;
				FormDef fd = formOpenRequest.getFormDef();
				if (fd != null) {
					if (fd.isInWorkpane())
						showInView(formOpenRequest.getPath(), formOpenRequest.getForm());
					else
						ClientContext.instance.showInWindow(formOpenRequest.getForm(), true);
					return;
				} else {
					JexpCallback<IUIComposite> callBack = new JexpCallback<IUIComposite>() {
						@Override
						protected void onResult(IUIComposite result) {
							if (result == null)
								return;
							if (formOpenRequest.isPopup())
								ClientContext.instance.showInWindow(result, true);
							else
								showInView(path, result);
						}
					};
					getNavHandler().handleNavigation(path, callBack);
				}
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
			}
		});
	}

	@Override
	public void handleHistoryValueChanged(final String path) {
		FormOpenRequest req = new FormOpenRequest(null, path, null);
		ClientContext.EVENT_BUS.fireEvent(req);
	}

	public void showInView(String path, IUIComposite result) {
		MainContentView view = mainContent.createView(result.getId());
		view.setContents(result);
		mainContent.addView(path, view);
	}

	protected void createAndRegisterDashboard() {
		if (!(ClientContext.instance instanceof ProvidesDashboardConfig))
			return;
		createDashboardForm();
	}

	protected void createDashboardForm() {
		ProvidesDashboardConfig pdc = (ProvidesDashboardConfig) ClientContext.instance;
		dashForm = new DashboardForm("dashboard", pdc.getDashboardColumns()) {
			@Override
			protected void onLoad() {
				super.onLoad();
				ClientContext.EVENT_BUS.fireEvent(new ApplicationReadyEvent());
			}
		};
	}

	@Override
	public void navLinkClicked(String path) {
		ClientContext.EVENT_BUS.fireEvent(new NavigationItemClicked(path, 0));
	}

	@Override
	public void sideLinkClicked(final String path) {
		ClientContext.EVENT_BUS.fireEvent(new NavigationItemClicked(path, 1));
	}

	protected void buildGUI() {
		RootPanel body = RootPanel.get();
		body.setStyleName(getSkinName());

		JsUtil.setNumeralLibLanguage(LocaleInfo.getCurrentLocale().getLocaleName());
	}

	@Override
	public String resolveWContext(WContext wcontext) {
		if (wcontext == null)
			return null;
		switch (wcontext) {
		case Primary:
			return "primary";
		case Success:
			return "success";
		case Purple:
			return "purple";
		case Pink:
			return "pink";
		case Info:
			return "info";
		case Grey:
			return "grey";
		case Warning:
			return "warning";
		case Light:
			return "light";
		case White:
			return "white";
		case Yellow:
			return "yellow";
		case Danger:
			return "danger";
		}
		return null;
	}

}