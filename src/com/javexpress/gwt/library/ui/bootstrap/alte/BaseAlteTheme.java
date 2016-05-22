package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.ui.RootPanel;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationMainContainer;
import com.javexpress.gwt.library.ui.bootstrap.BootstrapAdminTheme;
import com.javexpress.gwt.library.ui.bootstrap.SideBarItem;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public abstract class BaseAlteTheme extends BootstrapAdminTheme {

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
				wb.addJavaScript("scripts/slimscroll/jquery.slimscroll-1.3.6.min.js");
				break;
			case 100:
				wb.addJavaScript("scripts/alte/app.js");
				break;
		}
	}

	@Override
	protected void buildGUI() {
		super.buildGUI();

		RootPanel body = RootPanel.get();

		headerPanel = new ApplicationHeaderPanelAlte("headerbar");
		headerPanel.setBrand(getClient().getApplicationCssIcon(), getClient().getApplicationBrand());

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
	}

	@Override
	public String getFontIconPrefixClass() {
		return "alte-icon";
	}

	@Override
	public void applyIconInputGroupStyles(Element element, Element input, Element icon, ICssIcon iconClass) {
		element.setClassName("form-group has-feedback");
		input.setClassName("form-control");
		icon.setClassName(getFontIconPrefixClass() + " " + (iconClass != null ? iconClass.getCssClass() : "") + " form-control-feedback");
	}

	@Override
	public void applyIconStyles(Element iconSpan, ICssIcon iconClass) {
		iconSpan.addClassName(getFontIconPrefixClass() + " " + (iconClass != null ? iconClass.getCssClass() : ""));
	}

	protected SideBarItem addSideBarItem(String id, String path, String text, String icon) {
		SideBarItem sbi = sideBar.createSideBarItem(id, path);
		sbi.setText(text);
		sbi.setIconClass(icon != null ? icon : FaIcon.puzzle_piece.getCssClass());
		sideBar.addItem(sbi);
		return sbi;
	}

	@Override
	public void applyCheckboxStyles(Element element, InputElement check, Element label) {
	}
	
	@Override
	public void applyCheckboxColorContext(InputElement check, WContext context) {
	}
	
}