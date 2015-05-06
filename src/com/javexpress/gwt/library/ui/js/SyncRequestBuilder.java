package com.javexpress.gwt.library.ui.js;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.RequestBuilder.Method;

public class SyncRequestBuilder {
	
	private final JsonMap options;

	public SyncRequestBuilder(Method method, String url) {
		options = new JsonMap();
		options.set("type", method.toString());
		options.set("async", false);
		options.set("processData", false);
		options.set("url", url);
		options.set("cache", false);
		options.set("dataType", "text");
	}

	public void sendRequest(String data, SyncRequestCallback requestCallback) {
		options.set("data", data);
		_sendRequest(options.getJavaScriptObject(), requestCallback);
	}
	
	private static native void _sendRequest(JavaScriptObject options, SyncRequestCallback x) /*-{
		options.error=function(jqXHR, textStatus, errorThrown){
			x.@com.javexpress.gwt.library.ui.js.SyncRequestCallback::onError(Ljava/lang/String;Ljava/lang/String;)(textStatus,errorThrown);
		};
		options.success=function(data, textStatus, jqXHR){
			x.@com.javexpress.gwt.library.ui.js.SyncRequestCallback::onSuccess(Ljava/lang/String;Ljava/lang/String;)(textStatus,data);
		};
		$wnd.$.ajax(options);		
	}-*/;

}
