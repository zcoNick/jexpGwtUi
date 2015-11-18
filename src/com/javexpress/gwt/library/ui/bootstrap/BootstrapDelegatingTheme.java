package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;
import com.javexpress.common.model.item.FormDef;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.event.ApplicationReadyEvent;
import com.javexpress.gwt.library.ui.event.FormOpenRequest;
import com.javexpress.gwt.library.ui.event.handler.FormOpenRequestHandler;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JexpCallback;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class BootstrapDelegatingTheme extends BootstrapTheme {

	protected ApplicationMainContent	mainContent;

	@Override
	protected void prepareCommons(ClientContext clientContext) {
		super.prepareCommons(clientContext);
		ClientContext.EVENT_BUS.addHandler(FormOpenRequest.TYPE, new FormOpenRequestHandler() {
			@Override
			public void onFormOpenRequested(FormOpenRequest formOpenRequest) {
				final String path = formOpenRequest.getCode();
				MainContentView cached = mainContent.findView(path);
				if (cached != null) {
					mainContent.showView(cached);
					return;
				}
				/*if (path.equals("dashboard"))
					mainContent.add(dashView);*/
				if (getNavHandler() == null)
					return;
				FormDef fd = formOpenRequest.getFormDef();
				if (fd != null) {
					if (fd.isInWorkpane())
						showInView(formOpenRequest.getCode(), formOpenRequest.getForm());
					else
						ClientContext.instance.showInWindow(formOpenRequest.getForm(), true);
					return;
				} else {
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
				createApplicationMainContent();
				ClientContext.EVENT_BUS.fireEvent(new ApplicationReadyEvent());
			}
		});
	}

	protected void createApplicationMainContent() {
		Element el = getApplicationMainContentElementToWrap();
		mainContent = new ApplicationMainContent(el) {
			@Override
			public MainContentView createView(String id) {
				return createMainContentView(id);
			}
		};
	}

	protected abstract Element getApplicationMainContentElementToWrap();

	protected abstract MainContentView createMainContentView(String id);

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

	protected void buildGUI() {
		RootPanel body = RootPanel.get();
		String skin = getSkinName();
		if (skin != null)
			body.setStyleName(skin);

		JsUtil.setNumeralLibLanguage(LocaleInfo.getCurrentLocale().getLocaleName());
	}

}