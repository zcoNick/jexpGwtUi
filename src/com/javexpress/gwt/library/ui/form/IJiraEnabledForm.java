package com.javexpress.gwt.library.ui.form;

public interface IJiraEnabledForm {

	public final static byte	TYPE_ERROR		= 0;
	public final static byte	TYPE_REQUEST	= 1;

	public long getModuleId();

	public void openJiraIssue();

	public String getFormQualifiedName();

}
