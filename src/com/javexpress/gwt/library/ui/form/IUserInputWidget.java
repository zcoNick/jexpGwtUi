package com.javexpress.gwt.library.ui.form;

import java.io.Serializable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.user.client.ui.Focusable;

public interface IUserInputWidget<T extends Serializable> extends Focusable, HasChangeHandlers, IDataBindable {

	public boolean isRequired();

	public void setRequired(boolean required);

	public boolean validate(boolean focusedBefore);

	public T getValue();

	public Element getElement();

	public void setEnabled(boolean enabled);

	public void setValidationError(String validationError);

}