package com.javexpress.gwt.library.ui.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;
import com.javexpress.common.model.item.exception.AppException;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.container.layout.DivBorderLayout;
import com.javexpress.gwt.library.ui.form.IFormFactory;
import com.javexpress.gwt.library.ui.form.button.Button;
import com.javexpress.gwt.library.ui.form.label.Label;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ErrorDialog extends JexpPopupPanel {

	public static void showError(String windowId, AppException ae) {
		ErrorDialog d = new ErrorDialog(windowId, ae);
		int w = (int) (Window.getClientWidth() * 0.55);
		int h = (int) (Window.getClientHeight() * 0.4);
		int t = (int) ((Window.getClientHeight() - h) * 0.4);
		int l = (Window.getClientWidth() - w) / 2;
		d.setPixelSize(w, h);
		d.setPopupPosition(l, t);
		d.show();
	}

	private AppException	appException;
	private DivBorderLayout	layout;

	public ErrorDialog(String id, AppException appException) {
		super(false, true);
		JsUtil.ensureId(null, this, WidgetConst.ERRORDIALOG_PREFIX, id);
		getElement().getStyle().setZIndex(JsUtil.calcDialogZIndex());
		addStyleName("ui-widget-content jexpBorderBox jexpErrorDialog");
		this.appException = appException;

		layout = new DivBorderLayout(true);

		try {
			createTopPanel();
			createBottomPanel();
			createCenterPanel();
		} catch (Exception e) {
			e.printStackTrace();
		}

		setWidget(layout);
	}

	private void createTopPanel() throws Exception {
		int row = 0;
		FlexTable table = new FlexTable();
		table.setWidth("100%");
		table.setHeight("100%");
		Label l = new Label(appException.isLocalizable() ? IFormFactory.nlsCommon.hataBaslikKontrollu() : IFormFactory.nlsCommon.hataBaslikKontrolsuz());
		l.setStyleName("jesErrorTitle");
		table.setWidget(row++, 0, l);

		l = new Label(appException.isLocalizable() ? GwtApplication.getModuleNls(appException.getModuleId()).getString("error_" + appException.getErrorCode()) : (appException.getMessage() == null ? IFormFactory.nlsCommon.taninmayanHata() : appException.getMessage()));
		l.setStyleName("jesErrorMessage");
		table.setWidget(row++, 0, l);

		l = new Label(IFormFactory.nlsCommon.hataHaritasi());
		table.setWidget(row, 0, l);
		table.getCellFormatter().getElement(row, 0).setAttribute("valign", "bottom");

		layout.setTopWidget(table, "10em");
	}

	private void createCenterPanel() throws Exception {
		SimplePanel p = new SimplePanel();
		p.addStyleName("jesErrorTrace");
		p.setWidth("100%");
		p.setHeight("100%");
		p.getElement().getStyle().setOverflow(Overflow.AUTO);
		p.getElement().setInnerHTML("&nbsp;" + appException.getTraceHtml());
		layout.setCenterWidget(p);
	}

	private void createBottomPanel() throws Exception {
		FlexTable t = new FlexTable();
		t.setWidth("100%");
		t.setHeight("100%");
		t.addStyleName("jesErrorBottomPanel");

		int row = -1;

		if (appException.isLocalizable()) {
			Label l = new Label(IFormFactory.nlsCommon.hataCozumLinki());
			l.setStyleName("jesErrorSolutionLink");
			l.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					String url = GWT.getHostPageBaseURL() + "showSolution?" + (appException.getModuleId() != null ? "m=" + appException.getModuleId() + "&" : "") + "e=" + appException.getErrorCode();
					JsUtil.openWindow(url, null);
				}
			});
			t.setWidget(++row, 0, l);
		}

		Label jiraLink = new Label(layout, "jira");
		jiraLink.addStyleName("jesErrorJiraLink");
		jiraLink.setText(IFormFactory.nlsCommon.hataJiraLink());
		jiraLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*AsyncRunningTaskForm<Result<String>> task = new AsyncRunningTaskForm<Result<String>>() {
					@Override
					public String getHeader() {
						return IFormFactory.nlsCommon.Jira_baslik();
					}

					@Override
					protected void startTask(AsyncCallback<Result<String>> callback) {
						String smry = appException.getMessage() != null ? appException.getMessage() : (appException.getErrorCode());
						GwtApplication.sistemClient.getService().jiraBildir(appException.getModuleId(), smry == null ? "Unknown / Empty" : smry,
								appException.getTraceHtml(), JsUtil.getUserAgent() + ",Prmt:" + GWT.getPermutationStrongName(),
								JiraTalepTuruEnum.Hata, callback);
					}

					@Override
					protected void onComplete(boolean success, Result<String> result, Throwable caught) throws Exception {
						if (success) {
							if (result != null) {
								if (result.isSucceded()) {
									JsUtil.openWindow(result.getResult(), null);
									Window.alert(IFormFactory.nlsCommon.jiraBildirildi() + "\n" + result.getWarning());
									hide();
								} else
									Window.alert("Jira:" + result.getError());
							}
						}
					}
				};
				task.start();*/
			}
		});
		t.setWidget(++row, 0, jiraLink);
		//t.getCellFormatter().getElement(row, 0).getStyle().setVerticalAlign(VerticalAlign.MIDDLE);

		Button btKapat = new Button(this, "ok", IFormFactory.nlsCommon.kapat());
		btKapat.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide(false);
			}
		});
		t.setWidget(row, 1, btKapat);
		//t.getCellFormatter().getElement(row, 1).setAttribute("valign", "middle");
		t.getCellFormatter().getElement(row, 1).setAttribute("align", "right");

		layout.setBottomWidget(t, appException.isLocalizable() ? "5em" : "3.5em");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		JsUtil.shakeWidget(this);
	}

	@Override
	protected void onUnload() {
		appException = null;
		layout = null;
		super.onUnload();
	}

}