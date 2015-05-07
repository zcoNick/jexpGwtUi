package com.javexpress.gwt.library.ui.dialog;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ConfirmDialog extends JexpSimplePanel {

	private String					message;
	private ConfirmationListener	listener;
	private String					title;

	public ConfirmDialog(final Widget parent, final String id, final String title, final String message, final ConfirmationListener listener) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.CONFIRMDIALOG_PREFIX, id);
		this.title = title;
		this.message = message;
		this.listener = listener;
		RootPanel.get().add(this);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (JsUtil.USE_BOOTSTRAP) {
			getElement().setClassName("modal fade");
			getElement().setAttribute("role", "dialog");
			StringBuffer html = new StringBuffer();
			html.append("<div class='modal-dialog'>");
			html.append("<div class='modal-content'>");
			html.append("<div class='modal-header btn-danger' style='padding:7px'>");
			html.append("<button type='button' class='close' data-dismiss='modal' aria-label='").append(ClientContext.nlsCommon.kapat()).append("'><span aria-hidden='true'>&times;</span></button>");
			html.append("<h4 class='modal-title'>").append(title).append("</h4>");
			html.append("</div>");
			html.append("<div class='modal-body'>");
			html.append("<p>").append(message);
			html.append("</p>");
			html.append("</div>");
			html.append("<div class='modal-footer'>");
			html.append("<button type='button' id='").append(getElement().getId()).append("_ok' class='btn btn-danger' data-dismiss='modal'><i class='ace-icon ").append(FaIcon.check.getCssClass()).append("'></i><span>").append(ClientContext.nlsCommon.tamam()).append("</span></button>");
			html.append("<button type='button' id='").append(getElement().getId()).append("_cancel' class='btn btn-primary' data-dismiss='modal'><i class='ace-icon ").append(FaIcon.close.getCssClass()).append("'></i><span>").append(ClientContext.nlsCommon.vazgec()).append("</span></button>");
			html.append("</div></div></div>");
			getElement().setInnerHTML(html.toString());
			createByJsBs(this, getElement(), getElement().getId());
		} else
			createByJs(this, getElement().getId(), message);
	}

	private native void createByJsBs(ConfirmDialog x, Element elm, String id) /*-{
		var dlg = $wnd.$(elm);
		$wnd
				.$("#" + id + "_ok", dlg)
				.click(
						function(event) {
							x.@com.javexpress.gwt.library.ui.dialog.ConfirmDialog::fireOnOk()();
						});
		$wnd
				.$("#" + id + "_cancel", dlg)
				.click(
						function(event) {
							x.@com.javexpress.gwt.library.ui.dialog.ConfirmDialog::fireOnCancel()();
						});
		dlg
				.modal('show')
				.on(
						'hidden.bs.modal',
						function(e) {
							x.@com.javexpress.gwt.library.ui.dialog.ConfirmDialog::fireOnCancel()();
						});
	}-*/;

	private native void destroyByJs(ConfirmDialog x, Element elm) /*-{
		$wnd.$(elm).empty().off();
	}-*/;

	private native void createByJs(ConfirmDialog x, String id, String message) /*-{
		var pr = "<p><span class='ui-icon ui-icon-alert' style='float:left'></span>"
				+ message + "</p>";
		var div = $wnd.$("#" + id).append(pr);
		div
				.dialog({
					autoOpen : true,
					resizable : false,
					modal : true,
					height : 'auto',
					minWidth : 300,
					maxWidth : 600,
					show : "bounce",
					dialogClass : "no-close jesMaxZindex",
					close : function(event, ui) {
						div.dialog('destroy').remove();
					},
					buttons : {
						'Tamam' : {
							id : id + "_ok",
							text : "Tamam",
							click : function() {
								x.@com.javexpress.gwt.library.ui.dialog.ConfirmDialog::fireOnOk()();
								div.dialog("close");
							}
						},
						'Vazgeç' : {
							id : id + "_cnl",
							text : "Vazgeç",
							click : function() {
								x.@com.javexpress.gwt.library.ui.dialog.ConfirmDialog::fireOnCancel()();
								div.dialog("close");
							}
						}
					}
				});
	}-*/;

	@Override
	protected void onUnload() {
		message = null;
		listener = null;
		title = null;
		destroyByJs(this, getElement());
		super.onUnload();
	}

	//---------- EVENTS
	public void fireOnOk() throws Exception {
		if (listener != null)
			listener.onOk();
		removeFromParent();
	}

	public void fireOnCancel() {
		if (listener != null)
			listener.onCancel();
		removeFromParent();
	}

}