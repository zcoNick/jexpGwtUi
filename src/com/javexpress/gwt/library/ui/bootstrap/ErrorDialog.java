package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.javexpress.common.model.item.Result;
import com.javexpress.common.model.item.exception.AppException;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.dialog.AsyncRunningTaskForm;
import com.javexpress.gwt.library.ui.dialog.MessageDialog;
import com.javexpress.gwt.library.ui.form.IFormFactory;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ErrorDialog extends JexpSimplePanel {

	private AppException	appException;

	public ErrorDialog(String id, AppException ae) {
		super();
		JsUtil.ensureId(null, this, WidgetConst.ERRORDIALOG_PREFIX, id);
		getElement().getStyle().setZIndex(JsUtil.calcDialogZIndex());
		addStyleName("error-container jexpErrorDialog");
		this.appException = ae;
		createGUI();
	}

	private void createGUI() {
		StringBuffer html = new StringBuffer("<div class='well'>");
		html.append("<h1 class='header red lighter smaller'><span class='red bigger-125'><i class='ace-icon fa fa-random'></i></span> ");
		html.append(appException.isLocalizable() ? IFormFactory.nlsCommon.hataBaslikKontrollu() : IFormFactory.nlsCommon.hataBaslikKontrolsuz());
		html.append("</h1>");

		html.append("<h3 class='lighter smaller'>");
		if (appException.isLocalizable())
			html.append(GwtBootstrapApplication.getModuleNls(appException.getModuleId()).getString("error_" + appException.getErrorCode())).append("</h3>");
		else
			html.append("En kısa zamanda çözmek için <i class='ace-icon fa fa-wrench icon-animated-wrench bigger-125'></i>çalışacağız!</h3>");

		html.append("<div class='space'></div>");

		html.append("<h4 class='red lighter smaller'>Hata izlencesi :</h4>");
		html.append("<div class='jexpStackTrace'>");
		html.append(appException.getTraceHtml());
		html.append("</div>");

		html.append("<div><h4 class='red lighter smaller'>Bu sürede, aşağıdakileri deneyebilirsiniz:</h4>");
		html.append("<ul class='list-unstyled spaced inline bigger-110 margin-15'>");
		if (appException.isLocalizable())
			html.append("<li id='").append(getElement().getId()).append("_s' class='jexpHandCursor'><i class='ace-icon fa fa-hand-o-right blue'></i>Çözüm önerilerine gözatın</li>");
		html.append("<li id='").append(getElement().getId()).append("_j' class='jexpHandCursor'><i class='ace-icon fa fa-hand-o-right red jexpHandCursor'></i>Sorunu yazılım ekibine bildirin</li>");
		html.append("</ul></div>");

		html.append("<div class='space'></div>");

		html.append("<div class='center'>");
		html.append("<a class='btn btn-primary jexpHandCursor' id='").append(getElement().getId()).append("_c'>");
		html.append("<i class='ace-icon fa fa-close'></i>").append(IFormFactory.nlsCommon.kapat());
		html.append("</a>");
		html.append("</div>");

		html.append("</div>");

		getElement().setInnerHTML(html.toString());
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJsBs(this, getElement().getId());
	}

	private native void createByJsBs(ErrorDialog x, String id) /*-{
																$wnd
																.$("#" + id + "_c")
																.click(
																function(e) {
																x.@com.javexpress.gwt.library.ui.bootstrap.ErrorDialog::removeFromParent()();
																});
																$wnd
																.$("#" + id + "_s")
																.click(
																function(e) {
																x.@com.javexpress.gwt.library.ui.bootstrap.ErrorDialog::fireShowSolution()();
																});
																$wnd
																.$("#" + id + "_j")
																.click(
																function(e) {
																x.@com.javexpress.gwt.library.ui.bootstrap.ErrorDialog::fireJira()();
																});
																}-*/;

	@Override
	protected void onUnload() {
		destroyByJs(this, getElement());
		super.onUnload();
	};

	private native void destroyByJs(ErrorDialog x, Element elm) /*-{
																$wnd.$(elm).empty().off();
																}-*/;

	private void show() {
		RootPanel.get().add(this);
	}

	private void fireShowSolution() {
		String url = GWT.getHostPageBaseURL() + "showSolution?" + (appException.getModuleId() != null ? "m=" + appException.getModuleId() + "&" : "") + "e=" + appException.getErrorCode();
		JsUtil.openWindow(url, null);
	}

	private void fireJira() {
		AsyncRunningTaskForm<Result<String>> task = new AsyncRunningTaskForm<Result<String>>(this, "jira") {
			@Override
			protected String getHeader() {
				return IFormFactory.nlsCommon.Jira_baslik();
			}

			@Override
			protected void startTask(AsyncCallback<Result<String>> callback) {
				String smry = appException.getMessage() != null ? appException.getMessage() : (appException.getErrorCode());
				GwtBootstrapApplication.sistemClient.getService().jiraBildir(appException.getModuleId(), smry == null ? "Unknown / Empty" : smry,
						appException.getTraceHtml(), JsUtil.getUserAgent() + ",Prmt:" + GWT.getPermutationStrongName(),
						JiraTalepTuruEnum.Hata, callback);
			}

			@Override
			protected void onComplete(boolean success, Result<String> result, Throwable caught) throws Exception {
				if (success) {
					if (result != null) {
						if (result.isSucceded()) {
							JsUtil.openWindow(result.getResult(), null);
							MessageDialog.showInfo(this, "Jira", IFormFactory.nlsCommon.jiraBildirildi() + "<br/>" + result.getWarning());
							removeFromParent();
						} else
							MessageDialog.showAlert(this, IFormFactory.nlsCommon.hata(), "Jira : " + result.getError());
					}
				} else if (caught != null)
					JsUtil.handleError(this, caught);
			}
		};
		task.start();
	}

	public static void showError(String windowId, AppException ae) {
		ErrorDialog d = new ErrorDialog(windowId, ae);
		d.show();
	}

}
