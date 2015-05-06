package com.javexpress.gwt.library.ui.js;

import com.google.gwt.http.client.RequestBuilder;

public class AsyncRequestBuilder extends RequestBuilder {

	public AsyncRequestBuilder(Method httpMethod, String url) {
		super(httpMethod, url);
	}

}