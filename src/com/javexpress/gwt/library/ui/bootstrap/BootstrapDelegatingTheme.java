package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.event.ApplicationReadyEvent;
import com.javexpress.gwt.library.ui.event.FormOpenRequest;
import com.javexpress.gwt.library.ui.event.handler.FormOpenRequestHandler;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JexpCallback;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class BootstrapDelegatingTheme extends BootstrapTheme {

	@Override
	protected void prepareCommons(ClientContext clientContext) {
		super.prepareCommons(clientContext);
		ClientContext.EVENT_BUS.addHandler(FormOpenRequest.TYPE, new FormOpenRequestHandler() {
			@Override
			public void onFormOpenRequested(final FormOpenRequest formOpenRequest) {
				final String path = formOpenRequest.getPath();
				if (getNavHandler() == null)
					return;
				if (formOpenRequest.getForm() != null) {
					if (formOpenRequest.isInWorkPane())
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
							if (formOpenRequest.isInWorkPane())
								showInView(formOpenRequest.getPath(), result);
							else
								ClientContext.instance.showInWindow(result, true);
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
				ClientContext.EVENT_BUS.fireEvent(new ApplicationReadyEvent());
			}
		});
	}

	@Override
	public void handleHistoryValueChanged(final String path) {
		FormOpenRequest req = new FormOpenRequest(null, path, null);
		ClientContext.EVENT_BUS.fireEvent(req);
	}

	public abstract void showInView(String path, IUIComposite result);

	protected void buildGUI() {
		RootPanel body = RootPanel.get();
		String skin = getSkinName();
		if (skin != null)
			body.setStyleName(skin);

		JsUtil.setNumeralLibLanguage(LocaleInfo.getCurrentLocale().getLocaleName());
	}

}