package com.javexpress.gwt.library.ui.bootstrap.alte;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Panel;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.ApplicationMainContainer;
import com.javexpress.gwt.library.ui.bootstrap.BootstrapTheme;
import com.javexpress.gwt.library.ui.event.ExceptionThrownEvent;
import com.javexpress.gwt.library.ui.event.FormShowInWindowRequest;
import com.javexpress.gwt.library.ui.event.handler.ExceptionThrownEventHandler;
import com.javexpress.gwt.library.ui.event.handler.FormShowInWindowRequestHandler;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class AlteStdTheme extends BootstrapTheme {

	@Override
	public String getThemeName() {
		return "AdminLTE UI 2.1.1";
	}

	@Override
	public String getSkinName() {
		return "skin-blue fixed";
	}

	@Override
	public void injectCore(JsonMap requireConfig, final Command onload) {
		super.injectCore(requireConfig, onload);
	}

	@Override
	public void addStyleSheets(WidgetBundles wb, int phase) {
		switch (phase) {
			case 0:
				wb.addStyleSheet("themes/alte/AdminLTE-2.1.2.min.css");
				wb.addStyleSheet("themes/alte/skins/skin-blue.min.css");
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

	public ApplicationMainContainer createMainContainer(Panel parent, String id) {
		ApplicationMainContainer mc = new ApplicationMainContainerAlte(parent, id);
		//parent.add(mc);dont add!
		return mc;
	}

	@Override
	public void prepareUI(ClientContext clientContext) {
		prepareAlteCommons(clientContext);
	}

	public void prepareAlteCommons(ClientContext clientContext) {
		ClientContext.EVENT_BUS.addHandler(FormShowInWindowRequest.TYPE, new FormShowInWindowRequestHandler() {
			@Override
			public void onFormShowInWindowRequested(FormShowInWindowRequest formShowInWindowRequest) {
				IUIComposite form = formShowInWindowRequest.getForm();
				com.javexpress.gwt.library.ui.dialog.WindowView w = new com.javexpress.gwt.library.ui.dialog.WindowView(form.getId());
				w.setForm(form);
				w.show();
			}
		});
		ClientContext.EVENT_BUS.addHandler(ExceptionThrownEvent.TYPE, new ExceptionThrownEventHandler() {
			@Override
			public void onExceptionThrown(ExceptionThrownEvent exceptionThrownEvent) {
				com.javexpress.gwt.library.ui.bootstrap.ErrorDialog.showError(exceptionThrownEvent.getWindowId(), exceptionThrownEvent.getAppException());
			}
		});
	}

	@Override
	public void applyIconInputGroupStyles(Element element, Element input, Element icon, ICssIcon iconClass) {
		element.setClassName("form-group has-feedback");
		input.setClassName("form-control");
		icon.setClassName("alte-icon " + iconClass.getCssClass() + " form-control-feedback");
	}

}