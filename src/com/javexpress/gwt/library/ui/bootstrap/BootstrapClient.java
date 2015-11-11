package com.javexpress.gwt.library.ui.bootstrap;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyCodes;
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
import com.javexpress.gwt.library.ui.dialog.WindowView;
import com.javexpress.gwt.library.ui.event.ApplicationReadyEvent;
import com.javexpress.gwt.library.ui.event.SessionExpiredEvent;
import com.javexpress.gwt.library.ui.event.handler.ApplicationReadyEventHandler;
import com.javexpress.gwt.library.ui.form.IJiraEnabledForm;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JexpCallback;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public abstract class BootstrapClient extends ClientContext implements ProvidesResize, ApplicationReadyEventHandler {

	@Override
	public void onModuleLoad() {
		super.onModuleLoad();
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
		ClientContext.EVENT_BUS.addHandler(ApplicationReadyEvent.TYPE, this);
	}

	protected void handleOnCoreLibraryInjectFinished() {
		DOM.getElementById("appLoading").removeFromParent();
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				handleHistoryValueChanged(event.getValue());
			}
		});
	}

	protected void onUnload() {
		resourceInjector.destroyUI();
	}

	protected void handleOnKeyDown(KeyDownEvent event) {
		if (event.isControlKeyDown() && event.getNativeKeyCode() == ClientContext.HELP_KEYCODE) {
			event.preventDefault();
			event.stopPropagation();
		}
		if (event.isShiftKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_F12) {
			event.preventDefault();
			event.stopPropagation();
			ClientContext.instance.openJiraForm(new IJiraEnabledForm() {
				@Override
				public void openJiraIssue() {
				}

				@Override
				public Long getModuleId() {
					return null;
				}

				@Override
				public String getFormQualifiedName() {
					return "Main";
				}
			});
		}
		if (event.getNativeKeyCode() == KeyCodes.KEY_F5) {
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
		BootstrapTheme bsTheme = (BootstrapTheme) resourceInjector;
		bsTheme.handleHistoryValueChanged(value);
	}

	public static void showInWindow(IUIComposite form) {
		instance.showInWindow(form, true);
	}

	public static void showInWindow(IUIComposite form, JexpCallback<WindowView> callback) {
		instance.showInWindow(form, true, callback);
	}

	@Override
	public void onApplicationReady(ApplicationReadyEvent event) {
		final String href = Window.Location.getHref();
		if (href.indexOf("#") > -1) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					String path = href.substring(href.indexOf("#") + 1);
					int qs = path.indexOf("?");
					if (qs > -1)
						path = path.substring(0, qs);
					handleHistoryValueChanged(path);
				}
			});
		}
	}

}