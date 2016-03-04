package com.javexpress.gwt.library.ui.form.upload;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.IJsonServicePoint;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class DropZone extends JexpSimplePanel {

	public static WidgetBundles fillResources(WidgetBundles parent) {
		WidgetBundles wb = new WidgetBundles("DropZone", parent);
		wb.addStyleSheet("scripts/dropzone/dropzone-4.0.1.min.css");
		wb.addJavaScript("scripts/dropzone/dropzone-4.0.1.min.js");
		return wb;
	}

	private JsonMap				options;
	private IDropZoneListener	listener;
	private JavaScriptObject	dropzone;

	public IDropZoneListener getListener() {
		return listener;
	}

	public void setListener(IDropZoneListener listener) {
		this.listener = listener;
	}

	public DropZone(Widget parent, String id, String uploadName) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, getElement(), WidgetConst.DROPZONE_PREFIX, id);
		options = new JsonMap();
		options.set("dictDefaultMessage", ClientContext.nlsCommon.dpzDefault());
		options.set("dictFallbackMessage", ClientContext.nlsCommon.dpzFallbackMessage());
		options.set("dictFallbackText", ClientContext.nlsCommon.dpzFallbackText());
		options.set("dictInvalidFileType", ClientContext.nlsCommon.dpzInvalidFileType());
		options.set("dictFileTooBig", ClientContext.nlsCommon.dpzFileTooBig());
		options.set("dictResponseError", ClientContext.nlsCommon.dpzResponseError());
		options.set("dictCancelUpload", ClientContext.nlsCommon.dpzCancelUpload());
		options.set("dictCancelUploadConfirmation", ClientContext.nlsCommon.dpzCancelUploadConfirmation());
		options.set("dictRemoveFile", ClientContext.nlsCommon.sil());
		options.set("dictMaxFilesExceeded", ClientContext.nlsCommon.dpzMaxFilesExceeded());

		setFilesizeBase(1024);
		setAddRemoveLinks(true);
		setUploadName(uploadName);
		setAutoProcessQueue(false);
	}

	public void setUploadName(String uploadName) {
		options.set("paramName", uploadName);
	}

	public void setMethod(String method) {
		options.set("method", method);
	}

	public void setMaxFileSizeMB(double inmb) {
		options.set("maxFilesize", inmb);
	}

	public void setParallelUploads(int parallelUploads) {
		options.setInt("parallelUploads", parallelUploads);
	}

	public void setFilesizeBase(int filesizeBase) {
		options.setInt("filesizeBase", filesizeBase);
	}

	public void setMaxFiles(int maxFiles) {
		options.setInt("maxFiles", maxFiles);
	}

	public void setThumbnailWidth(int thumbnailWidth) {
		options.setInt("thumbnailWidth", thumbnailWidth);
	}

	public void setThumbnailHeight(int thumbnailHeight) {
		options.setInt("thumbnailHeight", thumbnailHeight);
	}

	public void setUploadMultiple(boolean uploadMultiple) {
		options.setBool("uploadMultiple", uploadMultiple);
	}

	public void setAddRemoveLinks(boolean addRemoveLinks) {
		options.setBool("addRemoveLinks", addRemoveLinks);
	}

	public void setAutoProcessQueue(boolean autoProcessQueue) {
		options.setBool("autoProcessQueue", autoProcessQueue);
	}

	public void setClickable(boolean clickable) {
		options.setBool("clickable", clickable);
	}

	public void setCreateImageThumbnails(boolean createImageThumbnails) {
		options.setBool("createImageThumbnails", createImageThumbnails);
	}

	public void setAction(IJsonServicePoint methodEnum) {
		options.set("url", JsUtil.getServiceUrl(methodEnum));
	}

	public void setAcceptedFiles(String acceptedFiles) {
		options.set("acceptedFiles", acceptedFiles);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		dropzone = createByJs(this, getElement(), options.getJavaScriptObject());
	}

	private native JavaScriptObject createByJs(DropZone x, Element el, JavaScriptObject options) /*-{
		var jso = new $wnd.Dropzone(el, options);
		jso
				.on(
						"drop",
						function(file) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnDrop(Lcom/google/gwt/user/client/Event;)(event);
						});
		jso
				.on(
						"addedfile",
						function(file) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnAddedFile(Lcom/google/gwt/core/client/JavaScriptObject;)(file);
						});
		jso
				.on(
						"removedfile",
						function(file) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnRemovedFile(Lcom/google/gwt/core/client/JavaScriptObject;)(file);
						});
		jso
				.on(
						"thumbnail",
						function(file, dataurl) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnThumbnail(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(file,dataurl);
						});
		jso
				.on(
						"error",
						function(file) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnError(Lcom/google/gwt/core/client/JavaScriptObject;)(file);
						});
		jso
				.on(
						"processing",
						function(file) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnProcessing(Lcom/google/gwt/core/client/JavaScriptObject;)(file);
						});
		jso
				.on(
						"uploadprogress",
						function(file, bytesSent) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnUploadProgress(Lcom/google/gwt/core/client/JavaScriptObject;I)(file,bytesSent);
						});
		jso
				.on(
						"sending",
						function(file, xhr, formData) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnBeforeSend(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(file,xhr,formData);
						});
		jso
				.on(
						"success",
						function(file, response) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnSuccess(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(file,response);
						});
		jso
				.on(
						"successmultiple",
						function(files) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnSuccessMultiple(Lcom/google/gwt/core/client/JavaScriptObject;)(files);
						});
		jso
				.on(
						"complete",
						function(file) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnComplete(Lcom/google/gwt/core/client/JavaScriptObject;)(file);
						});
		jso
				.on(
						"completemultiple",
						function(files) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnCompleteMultiple(Lcom/google/gwt/core/client/JavaScriptObject;)(files);
						});
		jso
				.on(
						"queuecomplete",
						function() {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnQueueComplete()();
						});
		jso
				.on(
						"totaluploadprogress",
						function(uploadProgress, totalBytes, totalBytesSent) {
							x.@com.javexpress.gwt.library.ui.form.upload.DropZone::fireOnTotalUploadProgress(III)(uploadProgress,totalBytes,totalBytesSent);
						});
		return jso;
	}-*/;

	public void processQueue() {
		_processQueue(this, dropzone);
	}

	public native void _processQueue(DropZone x, JavaScriptObject dropzone) /*-{
		dropzone.processQueue();
	}-*/;

	//--EVENTS
	private void fireOnDrop(Event event) {
		if (listener != null)
			listener.onDrop(event);
	}

	private void fireOnAddedFile(JavaScriptObject file) {
		if (listener != null)
			listener.addedFile(new JsonMap(file));
	}

	private void fireOnRemovedFile(JavaScriptObject file) {
		if (listener != null)
			listener.removedFile(new JsonMap(file));
	}

	private void fireOnThumbnail(JavaScriptObject file, String dataUrl) {
		if (listener != null)
			listener.onThumbnail(new JsonMap(file), dataUrl);
	}

	private void fireOnError(JavaScriptObject file) {
		if (listener != null)
			listener.onError(new JsonMap(file));
	}

	private void fireOnProcessing(JavaScriptObject file) {
		if (listener != null)
			listener.onProcessing(new JsonMap(file));
	}

	private void fireOnUploadProgress(JavaScriptObject file, int bytesSent) {
	}

	private void fireOnBeforeSend(JavaScriptObject file, JavaScriptObject xhr, JavaScriptObject formData) {
		if (listener != null)
			listener.beforeSend(new JsonMap(file), new JsonMap(formData));
	}

	private void fireOnSuccess(JavaScriptObject file, String response) {
	}

	private void fireOnSuccessMultiple(JavaScriptObject files) {
	}

	private void fireOnComplete(JavaScriptObject file) {
	}

	private void fireOnCompleteMultiple(JavaScriptObject files) {
	}

	private void fireOnQueueComplete() {
	}

	private void fireOnTotalUploadProgress(int uploadProgress, int totalBytes, int totalBytesSent) {
	}

}