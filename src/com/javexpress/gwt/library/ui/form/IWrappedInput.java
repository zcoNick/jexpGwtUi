package com.javexpress.gwt.library.ui.form;

import java.io.Serializable;

import com.google.gwt.dom.client.Element;

public interface IWrappedInput<V extends Serializable> extends ISingleValueWidget<V> {

	public Element getInputElement();

}