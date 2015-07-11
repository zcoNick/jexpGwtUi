package com.javexpress.gwt.library.ui.dialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
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
	private Map<String, String>		options;

	public ConfirmDialog(final Widget parent, final String id, final String title, final String message) {
		this(parent, id, title, message, null);
	}

	public ConfirmDialog(final Widget parent, final String id, final String title, final String message, final ConfirmationListener listener) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.CONFIRMDIALOG_PREFIX, id);
		this.title = title;
		this.message = message;
		this.listener = listener;
	}

	public void addOption(String id, String title) {
		if (options == null)
			options = new LinkedHashMap<String, String>();
		options.put(id, title);
	}

	public void setListener(ConfirmationListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (JsUtil.USE_BOOTSTRAP) {
			getElement().setClassName("modal fade");
			getElement().setAttribute("role", "dialog");
			StringBuffer html = new StringBuffer();
			html.append("<div class='modal-dialog jexpShadow'>");
			html.append("<div class='modal-content'>");
			html.append("<div class='modal-header btn-danger' style='padding:7px'>");
			html.append("<button type='button' class='close' data-dismiss='modal' aria-label='").append(ClientContext.nlsCommon.kapat()).append("'><span aria-hidden='true'>&times;</span></button>");
			html.append("<h4 class='modal-title'>").append(title).append("</h4>");
			html.append("</div>");
			html.append("<div class='modal-body'>");
			html.append("<p>").append(message);
			html.append("</p>");
			if (options != null) {
				for (String oid : options.keySet()) {
					String title = options.get(oid);
					String did = getElement().getId() + "_" + oid;
					html.append("<p><input type='checkbox' id='").append(did).append("'><label style='font-size:0.9em' for='").append(did).append("'>").append(title).append("</label>");
					html.append("</p>");
				}
			}
			html.append("</div>");
			html.append("<div class='modal-footer'>");
			html.append("<button type='button' id='").append(getElement().getId()).append("_ok' class='btn btn-danger' data-dismiss='modal'><i class='ace-icon ").append(FaIcon.check.getCssClass()).append("'></i><span>").append(ClientContext.nlsCommon.tamam()).append("</span></button>");
			html.append("<button type='button' id='").append(getElement().getId()).append("_cancel' class='btn btn-primary' data-dismiss='modal'><i class='ace-icon ").append(FaIcon.close.getCssClass()).append("'></i><span>").append(ClientContext.nlsCommon.vazgec()).append("</span></button>");
			html.append("</div></div></div>");
			getElement().setInnerHTML(html.toString());
			createByJsBs(this, getElement(), getElement().getId());
		} else
			createByJs(this, getElement().getId(), message, ClientContext.nlsCommon.tamam(), ClientContext.nlsCommon.vazgec());
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
				.modal({
					backdrop : false,
					show : true
				})
				.on(
						'hidden.bs.modal',
						function(e) {
							x.@com.javexpress.gwt.library.ui.dialog.ConfirmDialog::fireOnCancel()();
						});
	}-*/;

	private native void destroyByJs(ConfirmDialog x, Element elm) /*-{
		$wnd.$(elm).empty().off();
	}-*/;

	private native void createByJs(ConfirmDialog x, String id, String message, String sok, String scancel) /*-{
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
						sok : {
							id : id + "_ok",
							text : sok,
							click : function() {
								x.@com.javexpress.gwt.library.ui.dialog.ConfirmDialog::fireOnOk()();
								div.dialog("close");
							}
						},
						scancel : {
							id : id + "_cnl",
							text : scancel,
							click : function() {
								x.@com.javexpress.gwt.library.ui.dialog.ConfirmDialog::fireOnCancel()();
								div.dialog("close");
							}
						}
					}
				});
	}-*/;

	public List<String> getSelections() {
		if (options == null || options.isEmpty())
			return null;
		List<String> sels = new ArrayList<String>();
		for (String oid : options.keySet()) {
			String did = getElement().getId() + "_" + oid;
			InputElement ch = DOM.getElementById(did).cast();
			if (ch.isChecked())
				sels.add(oid);
		}
		return sels.isEmpty() ? null : sels;
	}

	@Override
	protected void onUnload() {
		message = null;
		listener = null;
		title = null;
		options = null;
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

	public void show() {
		RootPanel.get().add(this);
	}

}