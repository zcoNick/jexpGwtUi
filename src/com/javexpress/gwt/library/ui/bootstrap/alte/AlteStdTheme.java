package com.javexpress.gwt.library.ui.bootstrap.alte;

import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.RootPanel;
import com.javexpress.common.model.item.FormDef;
import com.javexpress.common.model.item.MenuNode;
import com.javexpress.common.model.item.type.Pair;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationFooter;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationMainContainer;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationMainContent;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationNotificationDropdown;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationSideBar;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationUserInfoDropdown;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;
import com.javexpress.gwt.library.ui.bootstrap.BootstrapTheme;
import com.javexpress.gwt.library.ui.bootstrap.INavBarHandler;
import com.javexpress.gwt.library.ui.bootstrap.ISideBarHandler;
import com.javexpress.gwt.library.ui.bootstrap.MainContentView;
import com.javexpress.gwt.library.ui.bootstrap.SideBarItem;
import com.javexpress.gwt.library.ui.container.dashboard.ProvidesDashboardConfig;
import com.javexpress.gwt.library.ui.event.ApplicationReadyEvent;
import com.javexpress.gwt.library.ui.event.FormOpenRequest;
import com.javexpress.gwt.library.ui.event.handler.FormOpenRequestHandler;
import com.javexpress.gwt.library.ui.form.DashboardForm;
import com.javexpress.gwt.library.ui.form.IThemeNavigationHandler;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JexpCallback;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class AlteStdTheme extends BootstrapTheme implements ISideBarHandler, INavBarHandler {

	protected ApplicationMainContent	mainContent;
	protected DashboardForm				dashForm;
	protected MainContentView			dashView;
	protected ApplicationSideBar		sideBar;
	protected ApplicationNavBarAlte		navBar;
	protected ApplicationFooter			footer;
	protected IThemeNavigationHandler	navHandler;

	@Override
	public String getThemeName() {
		return "AdminLTE UI 2.3.0";
	}

	@Override
	public String getSkinName() {
		return "skin-blue fixed";
	}

	@Override
	public void addStyleSheets(WidgetBundles wb, int phase) {
		switch (phase) {
			case 0:
				wb.addStyleSheet("themes/alte/AdminLTE-2.3.0.min.css");
				wb.addStyleSheet("themes/alte/skins/skin-blue.min.css");
				break;
			case 1000:
				wb.addStyleSheet("themes/alte/javexpress-gwt-library.ui.css");
				break;
		}
	}

	@Override
	public void addJavaScripts(WidgetBundles wb, int phase) {
		switch (phase) {
			case 10:
				wb.addJavaScript("scripts/slimscroll/jquery.slimscroll-1.3.3.min.js");
				break;
			case 100:
				wb.addJavaScript("scripts/alte/app.js");
				break;
		}
	}

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
	}

	protected void buildGUI(Map<Long, List<MenuNode>> menuNodes, Map<Long, List<Pair<Long, String>>> reportNodes, Map<Long, List<Pair<Long, String[]>>> processNodes) {
		RootPanel body = RootPanel.get();
		body.setStyleName(getSkinName());

		JsUtil.setNumeralLibLanguage(LocaleInfo.getCurrentLocale().getLocaleName());

		ApplicationHeaderPanelAlte headerPanel = new ApplicationHeaderPanelAlte("headerbar");
		headerPanel.setBrand(getClient().getApplicationCssIcon(), getClient().getApplicationBrand());

		ApplicationNotificationDropdown tasks = headerPanel.createNotificationDropdown("tasks", WContext.Grey, FaIcon.tasks);
		tasks.setBadge(WContext.Grey, "4");
		ApplicationNotificationDropdown notifications = headerPanel.createNotificationDropdown("notifications", WContext.Purple, FaIcon.bell);
		notifications.setBadge(WContext.Important, "8");
		ApplicationNotificationDropdown messages = headerPanel.createNotificationDropdown("messages", WContext.Green, FaIcon.envelope);
		messages.setBadge(WContext.Success, "5");
		ApplicationUserInfoDropdown userInfo = headerPanel.createUserInfoDropdown("userinfo", WContext.light_blue);
		userInfo.setUser(JexpGwtUser.instance.getFullName());

		ApplicationMainContainer mainContainer = new ApplicationMainContainerAlte(body, "maincontainer");

		sideBar = mainContainer.createSideBar("sidebar");
		sideBar.setLinkHandler(this);
		mainContainer.add(sideBar);
		navBar = headerPanel.createNavBar();
		navBar.setLinkHandler(this);
		headerPanel.addNavBar(navBar);

		body.add(headerPanel);

		mainContent = mainContainer.createMainContent();

		footer = mainContainer.createFooter();

		createAndRegisterDashboard();

		ClientContext.EVENT_BUS.fireEvent(new ApplicationReadyEvent());
	}

	protected void createAndRegisterDashboard() {
		if (!(ClientContext.instance instanceof ProvidesDashboardConfig))
			return;
		ProvidesDashboardConfig pdc = (ProvidesDashboardConfig) ClientContext.instance;
		dashForm = new DashboardForm("dashboard", pdc.getDashboardColumns());
	}

	@Override
	public void applyIconInputGroupStyles(Element element, Element input, Element icon, ICssIcon iconClass) {
		element.setClassName("form-group has-feedback");
		input.setClassName("form-control");
		icon.setClassName("alte-icon " + (iconClass != null ? iconClass.getCssClass() : "") + " form-control-feedback");
	}

	@Override
	public void applyIconStyles(Element iconSpan, ICssIcon iconClass) {
		iconSpan.addClassName("alte-icon " + (iconClass != null ? iconClass.getCssClass() : ""));
	}

	public void showInView(String path, IUIComposite result) {
		MainContentView view = mainContent.createView(result.getId());
		view.setContents(result);
		mainContent.addView(path, view);
	}

	protected SideBarItem addSideBarItem(String id, String path, String text, String icon) {
		SideBarItem sbi = sideBar.createSideBarItem(id, path);
		sbi.setText(text);
		sbi.setIconClass(icon != null ? icon : FaIcon.puzzle_piece.getCssClass());
		sideBar.addItem(sbi);
		return sbi;
	}

	@Override
	public void navLinkClicked(final String path) {
		sideLinkClicked(path);
	}

	@Override
	public void sideLinkClicked(final String path) {
		MainContentView cached = mainContent.findView(path);
		if (cached != null) {
			mainContent.showView(cached);
			return;
		}
		if (path.equals("dashboard"))
			mainContent.add(dashView);
		JexpCallback<IUIComposite> callBack = new JexpCallback<IUIComposite>() {
			@Override
			protected void onResult(IUIComposite result) {
				if (result == null)
					return;
				showInView(path, result);
			}
		};
		navHandler.handleNavigation(path, callBack);
	}

}