package com.javexpress.gwt.library.ui.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javexpress.common.model.item.Result;
import com.javexpress.gwt.fw.ui.library.form.IFormFactory;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.JqIcon;
import com.javexpress.gwt.library.ui.container.buttonbar.ButtonBar;
import com.javexpress.gwt.library.ui.container.layout.DivBorderLayout;
import com.javexpress.gwt.library.ui.container.layout.GridLayout;
import com.javexpress.gwt.library.ui.form.IJiraEnabledForm;
import com.javexpress.gwt.library.ui.form.button.Button;
import com.javexpress.gwt.library.ui.form.combobox.ComboBox;
import com.javexpress.gwt.library.ui.form.label.Label;
import com.javexpress.gwt.library.ui.form.textbox.TextAreaBox;
import com.javexpress.gwt.library.ui.form.textbox.TextBox;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class NewJiraIssueDialog extends JexpPopupPanel {

	private DivBorderLayout		layout;
	private IJiraEnabledForm	form;

	public NewJiraIssueDialog(String id, IJiraEnabledForm form) {
		super(false, true);
		this.form = form;
		JsUtil.ensureId(null, this, WidgetConst.JIRADIALOG_PREFIX, id);
		getElement().getStyle().setZIndex(JsUtil.calcDialogZIndex());
		addStyleName("ui-widget-content jexpBorderBox jesJiraDialog");

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

	private ComboBox	turu;
	private TextBox		ozet;
	private TextAreaBox	aciklama;

	private void createTopPanel() throws Exception {
		GridLayout l = new GridLayout(5, 4, true);
		l.setCellPadding(0);
		l.setCellSpacing(0);
		l.setColWidth(0, 10, Unit.EM);
		l.setColWidth(1, 8, Unit.EM);
		l.setColWidth(2, 10, Unit.EM);

		Label lb = new Label(IFormFactory.nlsCommon.Jira_baslik());
		lb.setStyleName("jesJiraTitle");

		l.setWidget(0, 0, lb);
		l.getCellFormatter().setStyleName(0, 0, "ui-state-active");
		l.setColspan(0, 0, 4);

		l.setLabel(2, 0, IFormFactory.nlsCommon.Jira_turu(), true, true);
		l.setWidget(2, 1, turu = new ComboBox(this, "turu"));
		turu.setItemsEnum(CommonEnums.JiraTalepTuruEnum.asItems());
		turu.setRequired(true);
		turu.setValueInt(0);
		l.setLabel(2, 2, IFormFactory.nlsCommon.Jira_ozet(), true, true);
		l.setWidget(2, 3, ozet = new TextBox(this, "ozet"));
		ozet.setMaxLength(200);
		ozet.setRequired(true);
		ozet.setWidth("95%");

		l.setLabel(4, 0, IFormFactory.nlsCommon.Jira_detayAciklama());

		layout.setTopWidget(l, "8em");
	}

	private void createCenterPanel() throws Exception {
		aciklama = new TextAreaBox(this, "aciklama");
		aciklama.setWidth("99%");
		aciklama.setHeight("99%");
		layout.setCenterWidget(aciklama);
	}

	private void createBottomPanel() throws Exception {
		ButtonBar bb = new ButtonBar();

		Button btGonder = new Button(this, "gonder", IFormFactory.nlsCommon.gonder());
		btGonder.setIcon(JqIcon.check);
		btGonder.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AsyncRunningTaskForm<Result<String>> task = new AsyncRunningTaskForm<Result<String>>(NewJiraIssueDialog.this, "newjiraas") {
					@Override
					public String getHeader() {
						return IFormFactory.nlsCommon.Jira_baslik();
					}

					@Override
					protected void startTask(AsyncCallback<Result<String>> callback) {
						GwtApplication.sistemClient.getService().jiraBildir(form.getModuleId(), ozet.getText(), aciklama.getText(), "Form:" + form.getFormQualifiedName() + ",Prmt:" + GWT.getPermutationStrongName() + ",UA:" + JsUtil.getUserAgent(),
								JiraTalepTuruEnum.parse(turu.getValueInt()), callback);
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
				task.start();
			}
		});
		bb.add(btGonder);

		Button btKapat = new Button(this, "ok", IFormFactory.nlsCommon.kapat());
		btKapat.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide(false);
			}
		});
		bb.add(btKapat);

		layout.setBottomWidget(bb, WidgetConst.HEIGHT_BUTTONBAR);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		ozet.setFocus(true);
	}

	@Override
	protected void onUnload() {
		layout = null;
		form = null;
		super.onUnload();
	}

	public static void open(IJiraEnabledForm form) {
		NewJiraIssueDialog d = new NewJiraIssueDialog("issue", form);
		int w = (int) (Window.getClientWidth() * 0.45);
		int h = (int) (Window.getClientHeight() * 0.4);
		int t = (int) ((Window.getClientHeight() - h) * 0.4);
		int l = (Window.getClientWidth() - w) / 2;
		d.setPixelSize(w, h);
		d.setPopupPosition(l, t);
		d.show();
	}

}