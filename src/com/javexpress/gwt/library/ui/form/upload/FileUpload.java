package com.javexpress.gwt.library.ui.form.upload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.fw.ui.data.control.Label;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.JqIcon;
import com.javexpress.gwt.library.ui.dialog.ConfirmationListener;
import com.javexpress.gwt.library.ui.form.button.Button;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class FileUpload extends FormPanel implements ChangeHandler, SubmitCompleteHandler {

	public static interface FileUploadHandler {
		public void onComplete(boolean success, String result, String errorMessage);
	}

	private final TableElement								table;
	private InputElement									teName;
	private Button											btGonder;
	private com.google.gwt.user.client.ui.FileUpload		fileUploader;
	private List<com.google.gwt.user.client.ui.FileUpload>	widgets;
	private Map<String, Serializable>						postParams;
	private List<FileUploadHandler>							completeHandlers;
	private int												last	= 0;
	private String[]										fileExtensions;
	private boolean											submitOnchange;

	public boolean isMulti() {
		return widgets != null;
	}

	public void setMulti(final boolean multi) {
		if (widgets == null)
			widgets = new ArrayList<com.google.gwt.user.client.ui.FileUpload>();
	}

	public boolean isSubmitOnchange() {
		return submitOnchange;
	}

	public void setSubmitOnchange(final boolean submitOnchange) {
		this.submitOnchange = submitOnchange;
	}

	public void setFileExtensions(final String exts) {
		this.fileExtensions = new String[] { exts };
	}

	public void setFileExtensions(final String[] exts) {
		this.fileExtensions = exts;
	}

	public FileUpload(final Widget parent, final String id, final ServiceDefTarget defTarget, final Enum method) {
		this(parent, id, false, null, defTarget, method);
	}

	public FileUpload(final Widget parent, final String id, final boolean askName, final String title, final ServiceDefTarget defTarget, final Enum method) {
		this(parent, id, askName, title, false, defTarget, method);
	}

	public FileUpload(final Widget parent, final String id, final boolean askName, final String title, final boolean multi, final ServiceDefTarget defTarget, final Enum method) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.FILEUPLOAD_PREFIX, id);

		setAction(defTarget.getServiceEntryPoint() + "." + method.toString());
		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);
		addSubmitCompleteHandler(this);

		table = DOM.createTable().cast();
		table.setCellPadding(0);
		table.setCellSpacing(0);
		if (askName) {
			Element tr = DOM.createTR();
			Element td = DOM.createTD();
			td.setInnerText("Adı :");
			td.getStyle().setTextAlign(TextAlign.RIGHT);
			tr.appendChild(td);
			td = DOM.createTD();
			teName = DOM.createInputText().cast();
			td.appendChild(teName);
			tr.appendChild(td);
			table.appendChild(tr);
		}
		Element tr = DOM.createTR();
		Element td = DOM.createTD();
		td.setInnerText(title != null ? title : "");
		td.getStyle().setTextAlign(TextAlign.RIGHT);
		tr.appendChild(td);
		createFileUploader();
		td = DOM.createTD();
		td.appendChild(fileUploader.getElement());
		tr.appendChild(td);
		if (multi) {
			widgets = new ArrayList<com.google.gwt.user.client.ui.FileUpload>();
			addStyleName("ui-widget ui-widget-content");
		}
		table.appendChild(tr);

		TableElement outer = DOM.createTable().cast();
		outer.setWidth("100%");
		tr = DOM.createTR();
		tr.setAttribute("valign", "top");
		td = DOM.createTD();
		td.appendChild(table);
		tr.appendChild(td);
		if (multi) {
			td = DOM.createTD();
			btGonder = new Button(this, getElement().getId() + "_send", "Gönder");
			btGonder.setIcon(JqIcon.arrowreturnthick_1_n);
			btGonder.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(final ClickEvent event) {
					submit();
				}
			});
			td.getStyle().setPaddingTop(4, Unit.PX);
			td.appendChild(btGonder.getElement());
			tr.appendChild(td);
		}
		outer.appendChild(tr);
		getElement().appendChild(outer);
	}

	private void createFileUploader() {
		fileUploader = new com.google.gwt.user.client.ui.FileUpload();
		fileUploader.setName("uploadedFile" + (isMulti() ? "[]" : ""));
		fileUploader.addChangeHandler(this);
	}

	public void setParams(final Map<String, Serializable> params) {
		this.postParams = params;
	}

	public void addCompleteHandler(final FileUploadHandler command) {
		if (this.completeHandlers == null)
			this.completeHandlers = new ArrayList<FileUploadHandler>();
		this.completeHandlers.add(command);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		adopt(fileUploader);
		if (isMulti())
			adopt(btGonder);
	}

	@Override
	protected void onUnload() {
		if (btGonder != null)
			orphan(btGonder);
		btGonder = null;
		if (fileUploader != null)
			orphan(fileUploader);
		fileUploader = null;
		if (widgets != null)
			for (Widget w : widgets)
				orphan(w);
		widgets = null;
		postParams = null;
		completeHandlers = null;
		fileExtensions = null;
		super.onUnload();
	}

	@Override
	public void onChange(final ChangeEvent event) {
		event.stopPropagation();
		if (fileExtensions != null) {
			boolean accept = false;
			for (String e : fileExtensions)
				if (fileUploader.getFilename().endsWith("." + e)) {
					accept = true;
					break;
				}
			if (!accept) {
				JsUtil.message(getElement().getId(), "Bu dosya tipini ekleyemez/seçemezsiniz");
				return;
			}
		}
		if (isMulti()) {
			final int start = teName != null ? 1 : 0;
			final String id = getElement().getId() + ":" + (last++);
			TableCellElement oldTitleTd = table.getRows().getItem(start).getCells().getItem(0);
			Element tr = DOM.createTR();
			Element td = DOM.createTD();
			td.setInnerText(oldTitleTd.getInnerText());
			tr.appendChild(td);

			td = DOM.createTD();
			com.google.gwt.user.client.ui.FileUpload old = fileUploader;
			old.getElement().getStyle().setDisplay(Display.NONE);
			widgets.add(old);
			createFileUploader();
			td.appendChild(fileUploader.getElement());
			tr.appendChild(td);

			table.insertRow(start);

			table.getRows().getItem(start + 1).deleteCell(0);
			td = DOM.createTD();
			td.setInnerText(old.getFilename());
			table.getRows().getItem(start + 1).appendChild(td);
			table.getRows().getItem(start + 1).setId(id);
			Label span = new Label();
			span.setIcon(JqIcon.trash);
			final Widget that = this;
			span.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(final ClickEvent event) {
					JsUtil.confirm(that, "Onay", "Dosya ekleme kuyruğundan çıkarılacaktır. Emin misiniz?", new ConfirmationListener() {
						@Override
						public void onOk() throws Exception {
							NodeList<TableRowElement> nl = table.getRows();
							for (int i = start; i < nl.getLength(); i++)
								if (nl.getItem(i).getId().equals(id)) {
									table.deleteRow(i);
									break;
								}
						}
					});
				}
			});
			table.getRows().getItem(start + 1).getCells().getItem(0).setAlign("right");
			table.getRows().getItem(start + 1).getCells().getItem(0).appendChild(span.getElement());
			adopt(fileUploader);
			adopt(span);
		} else if (submitOnchange)
			submit();
	}

	@Override
	public void submit() {
		if (isMulti())
			fileUploader.removeFromParent();
		if (postParams != null)
			for (String key : postParams.keySet()) {
				Serializable val = postParams.get(key);
				if (val != null) {
					Hidden h = new Hidden(key);
					h.setValue(val.toString());
					getElement().appendChild(h.getElement());
				}
			}
		if (teName != null) {
			Hidden h = new Hidden("name");
			h.setValue(teName.getValue());
			getElement().appendChild(h.getElement());
		}
		super.submit();
	}

	@Override
	public void setWidth(final String width) {
		super.setWidth(width.replaceAll("100%", "99%"));
	}

	@Override
	public void setHeight(final String height) {
		super.setHeight(height.replaceAll("100%", "99%"));
	}

	@Override
	public void onSubmitComplete(final SubmitCompleteEvent event) {
		boolean hasError = !event.getResults().startsWith("OK!");
		if (completeHandlers != null)
			for (FileUploadHandler cmd : completeHandlers)
				cmd.onComplete(!hasError, !hasError ? event.getResults().substring(3) : null, hasError ? event.getResults() : null);
	}

	public boolean isAskName() {
		return teName != null;
	}

}