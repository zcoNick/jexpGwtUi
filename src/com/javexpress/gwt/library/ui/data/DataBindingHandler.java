package com.javexpress.gwt.library.ui.data;

import com.javexpress.gwt.library.ui.form.IDataBindable;

public abstract class DataBindingHandler<T extends IDataBindable> {

	private T	control;

	public T getControl() {
		return control;
	}

	public void setControl(T control) {
		this.control = control;
	}

	public abstract void execute(boolean exRead) throws Exception;

}
