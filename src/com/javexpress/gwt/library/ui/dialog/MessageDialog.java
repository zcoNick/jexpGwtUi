package com.javexpress.gwt.library.ui.dialog;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.FaIcon;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class MessageDialog extends JexpSimplePanel {

	private ICssIcon	icon;
	private String		message;
	private String		title;

	public MessageDialog(final Widget parent, final String id, final ICssIcon icon, final String title, final String message) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.MESSAGEDIALOG_PREFIX, id);
		this.title = title;
		this.message = message;
		this.icon = icon;
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
			html.append("<div class='modal-header btn-info' style='padding:7px'>");
			html.append("<button type='button' class='close' data-dismiss='modal' aria-label='").append(ClientContext.nlsCommon.kapat()).append("'><span aria-hidden='true'>&times;</span></button>");
			html.append("<h4 class='modal-title'>").append(title).append("</h4>");
			html.append("</div>");
			html.append("<div class='modal-body'>");
			html.append("<p>").append(message);
			html.append("</p>");
			html.append("</div>");
			html.append("<div class='modal-footer'>");
			html.append("<button type='button' id='").append(getElement().getId()).append("_ok' class='btn btn-primary' data-dismiss='modal'><i class='ace-icon ").append(FaIcon.close.getCssClass()).append("'></i><span>").append(ClientContext.nlsCommon.tamam()).append("</span></button>");
			html.append("</div></div></div>");
			getElement().setInnerHTML(html.toString());
			createByJsBs(this, getElement());
		} else {
			getElement().setAttribute("title", title);
			createByJs(this, getElement().getId(), icon.getCssClass(), message);
		}
	}

	private native void createByJsBs(MessageDialog x, Element elm) /*-{
		$wnd
				.$(elm)
				.modal('show')
				.on(
						'hidden.bs.modal',
						function(e) {
							x.@com.javexpress.gwt.library.ui.dialog.MessageDialog::fireOnOk()();
						});
	}-*/;

	private native void createByJs(MessageDialog x, String id, String type, String message) /*-{
		var pr = "<p><span class='ui-icon ui-icon-alert' style='float:left'></span>"
				+ message + "</p>";
		var div = $wnd.$("#" + id).append(pr);
		div
				.dialog({
					autoOpen : true,
					resizable : false,
					modal : true,
					minWidth : 300,
					maxWidth : 600,
					height : 'auto',
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
								x.@com.javexpress.gwt.library.ui.dialog.MessageDialog::fireOnOk()();
								div.dialog("close");
							}
						}
					}
				});
	}-*/;

	private native void destroyByJs(MessageDialog x, Element elm) /*-{
		$wnd.$(elm).empty().off();
	}-*/;

	public static void showAlert(final Widget parent, final String title, final String message) {
		showAlert(parent.getElement().getId(), title, message);
	}

	public static void showAlert(final String id, final String title, final String message) {
		new MessageDialog(null, id, FaIcon.warning, title, message);
	}

	public static void showInfo(final Widget parent, final String title, final String message) {
		showInfo(parent.getElement().getId(), title, message);
	}

	public static void showInfo(final String id, final String title, final String message) {
		new MessageDialog(null, id, FaIcon.info_circle, title, message);
	}

	//---------- EVENTS
	public void fireOnOk() throws Exception {
		removeFromParent();
	}

	@Override
	protected void onUnload() {
		icon = null;
		message = null;
		title = null;
		destroyByJs(this, getElement());
		super.onUnload();
	}

}