package com.javexpress.gwt.library.ui.form.textbox;

import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.FormGroupCell;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class TextAreaBox extends com.google.gwt.user.client.ui.TextArea implements IUserInputWidget<String> {

	private boolean				required;
	private DataBindingHandler	dataBinding;

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(final boolean required) {
		this.required = required;
	}

	/** Designer compatible constructor */
	public TextAreaBox(final Widget parent, final String id) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.TEXTAREABOX_PREFIX, id);
		if (!JsUtil.USE_BOOTSTRAP)
			setWidth(WidgetConst.WIDTH_MIDDLE);
		setHeight(getDefaultHeight());
	}

	protected String getDefaultHeight() {
		return "3em";
	}

	@Override
	public String getValue() {
		String s = super.getValue();
		return JsUtil.isEmpty(s) ? null : s;
	}

	@Override
	public boolean validate(final boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	public void setMaxLength(int maxLength) {
		getElement().setAttribute("maxLength", String.valueOf(maxLength));
	}

	@Override
	public void setValidationError(String validationError) {
		if (JsUtil.USE_BOOTSTRAP) {
			Widget nw = getParent() instanceof FormGroupCell ? getParent() : this;
			if (validationError == null)
				nw.removeStyleName("has-error");
			else
				nw.addStyleName("has-error");
		}
		setTitle(validationError);
	}

	@Override
	public void setDataBindingHandler(DataBindingHandler handler) {
		this.dataBinding = handler;
		dataBinding.setControl(this);

	}

	@Override
	public DataBindingHandler getDataBindingHandler() {
		return dataBinding;
	}

	@Override
	protected void onUnload() {
		dataBinding = null;
		super.onUnload();
	}
}