package com.javexpress.gwt.library.ui.form.upload;

import com.google.gwt.user.client.Event;
import com.javexpress.gwt.library.ui.js.JsonMap;

public interface IDropZoneListener {

	void onDrop(final Event event);

	void onThumbnail(final JsonMap fileInfo, final String dataUrl);

	void onError(final JsonMap fileInfo);

	void onProcessing(final JsonMap fileInfo);

	void beforeSend(final JsonMap fileInfo, final JsonMap requestData);

	void addedFile(final JsonMap fileInfo);

	void removedFile(final JsonMap fileInfo);

	void complete(final JsonMap fileInfo);

	void queueComplete(final JsonMap fileInfo);

}