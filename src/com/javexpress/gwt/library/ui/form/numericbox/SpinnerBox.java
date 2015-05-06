package com.javexpress.gwt.library.ui.form.numericbox;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class SpinnerBox extends NumericBox {

	private JsonMap	spinnerOptions;

	public SpinnerBox(final Widget parent, final String id) {
		super(parent, id);
	}

	public SpinnerBox(final Widget parent, final String id, final boolean alignRight) {
		super(parent, id, alignRight);
	}

	@Override
	protected JsonMap createDefaultOptions() {
		super.createDefaultOptions();
		options.set("spinnerOptions", spinnerOptions = new JsonMap());
		return options;
	}

	public SpinnerBox minMax(final int min, final int max) {
		return setup(min, max, 1);
	}

	public SpinnerBox setup(final int min, final int max, final int step) {
		return setup(min, max, step, 5);
	}

	public SpinnerBox setup(final int min, final int max, final int step, final int pageStep) {
		spinnerOptions.setInt("min", min);
		spinnerOptions.setInt("max", max);
		spinnerOptions.setInt("step", step);
		spinnerOptions.setInt("page", pageStep);
		return this;
	}

	@Override
	protected void onUnload() {
		spinnerOptions = null;
		_destroySpinner(widget);
		super.onUnload();
	}

	private native void _destroySpinner(JavaScriptObject widget) /*-{
		widget.spinner('destroy');
	}-*/;

}