package com.javexpress.gwt.library.ui.facet;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javexpress.common.model.item.Result;

public interface ProvidesJira {

	void createJiraIssue(long moduleId, String summary, String description, String userAgent, byte issueType, AsyncCallback<Result<String>> callback);

}