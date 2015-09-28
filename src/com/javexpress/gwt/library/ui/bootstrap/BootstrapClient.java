package com.javexpress.gwt.library.ui.bootstrap;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.dialog.NewJiraIssueDialog;
import com.javexpress.gwt.library.ui.event.SessionExpiredEvent;
import com.javexpress.gwt.library.ui.form.IJiraEnabledForm;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public abstract class BootstrapClient extends ClientContext implements ProvidesResize {

	@Override
	public void onModuleLoad() {
		ClientContext.instance = this;
		JsUtil.testMode = "true".equalsIgnoreCase(Window.Location.getParameter("javexpress.test_mode"));
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				getLogger().log(Level.ALL, e.getMessage(), e);
				//See MethodDispatch:89
				JsUtil.handleError("GwtBootstrapApplication", e);
			}
		});
		initializeApplication();
	}

	protected abstract Logger getLogger();

	protected native void registerSessionInvalidListener(BootstrapClient app) /*-{
		$wnd.JexpUI.AjaxUtils
				.setListener(function() {
					app.@com.javexpress.gwt.library.ui.bootstrap.BootstrapClient::fireSessionInvalid()();
				});
	}-*/;

	protected void fireSessionInvalid() {
		ClientContext.EVENT_BUS.fireEvent(new SessionExpiredEvent());
	}

	public abstract String getApplicationCssPath();

	protected JsonMap createRequireConfig() {
		return null;
	}

	protected void initializeApplication() {
		Window.setTitle(getApplicationBrand());
		RootPanel root = RootPanel.get();
		Window.setMargin("0");
		Window.enableScrolling(false);
		root.setWidth("100%");
		root.setHeight("100%");
		if (JsUtil.isRTL())
			RootPanel.getBodyElement().setAttribute("dir", "rtl");

		resourceInjector.injectCore(createRequireConfig(), new Command() {
			@Override
			public void execute() {
				Window.addResizeHandler(new ResizeHandler() {
					@Override
					public void onResize(ResizeEvent event) {
						RootPanel body = RootPanel.get();
						for (int w = 0; w < body.getWidgetCount(); w++) {
							Widget widget = body.getWidget(w);
							if (widget instanceof RequiresResize)
								((RequiresResize) widget).onResize();
						}
						History.addValueChangeHandler(new ValueChangeHandler<String>() {
							@Override
							public void onValueChange(ValueChangeEvent<String> event) {
								handleHistoryValueChanged(event.getValue());
							}
						});
					}
				});
				handleOnCoreLibraryInjectFinished();
			}
		});
		Window.addCloseHandler(new CloseHandler<Window>() {
			@Override
			public void onClose(CloseEvent<Window> event) {
				onUnload();
			}
		});
		final KeyDownHandler handler = new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				handleOnKeyDown(event);
			}
		};
		RootPanel.get().addDomHandler(handler, KeyDownEvent.getType());
	}

	protected void handleOnCoreLibraryInjectFinished() {
		DOM.getElementById("appLoading").removeFromParent();
	}

	protected void onUnload() {
		resourceInjector.destroyUI();
	}

	protected void handleOnKeyDown(KeyDownEvent event) {
		if (event.isControlKeyDown() && event.getNativeKeyCode() == 170) {// Ctrl + ?
			event.preventDefault();
			event.stopPropagation();
		}
		if (event.isShiftKeyDown() && event.getNativeKeyCode() == 123) {// Shift + F12
			event.preventDefault();
			event.stopPropagation();
			NewJiraIssueDialog.open(new IJiraEnabledForm() {
				@Override
				public void openJiraIssue() {
				}

				@Override
				public long getModuleId() {
					return getModuleId();
				}

				@Override
				public String getFormQualifiedName() {
					return "Main";
				}
			});
		}
		if (event.getNativeKeyCode() == 116) {// F5
			if (disableF5Key()) {
				event.preventDefault();
				event.stopPropagation();
			}
		}
	}

	protected boolean disableF5Key() {
		return false;
	}

	public abstract String getApplicationBrand();

	public void doLogout() {
		JexpGwtUser.clear();
		String old = Window.Location.getHref().replaceFirst(GWT.getHostPageBaseURL(), "");
		Window.Location.replace(GWT.getModuleName() + "/logout.jsp?u=" + URL.encode(old));
	}

	public String getApplicationCssIcon() {
		return "fa fa-dashboard green";
	}

	protected void handleHistoryValueChanged(String value) {
	}

	public static void showInWindow(IUIComposite form) {
		instance.showInWindow(form, true);
	}

}