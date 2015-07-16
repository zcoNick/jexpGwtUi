package com.javexpress.gwt.library.ui.facet;

import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface ProvidesModuleUtils {

	String getModuleNls(Long moduleId, String key);

	ServiceDefTarget getModuleServiceTarget(long moduleId);

}