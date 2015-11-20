package com.javexpress.gwt.library.ui.event;

import com.google.gwt.event.shared.GwtEvent;
import com.javexpress.common.model.item.FormDef;
import com.javexpress.gwt.library.ui.event.handler.FormOpenRequestHandler;
import com.javexpress.gwt.library.ui.form.IUIComposite;

public class FormOpenRequest extends GwtEvent<FormOpenRequestHandler> {

	public static Type<FormOpenRequestHandler>	TYPE	= new Type<FormOpenRequestHandler>();
	private FormDef								formDef;
	private String								code;
	private IUIComposite						form;
	private boolean								inWorkPane;

	public FormOpenRequest(FormDef formDef, String code, IUIComposite form) {
		this.formDef = formDef;
		if (formDef != null)
			inWorkPane = formDef.isInWorkpane();
		this.code = code;
		this.form = form;
	}

	public boolean isInWorkPane() {
		return inWorkPane;
	}

	public void setInWorkPane(boolean inWorkPane) {
		this.inWorkPane = inWorkPane;
	}

	public FormDef getFormDef() {
		return formDef;
	}

	public void setFormDef(FormDef formDef) {
		this.formDef = formDef;
	}

	public String getPath() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public IUIComposite getForm() {
		return form;
	}

	public void setForm(IUIComposite form) {
		this.form = form;
	}

	@Override
	public Type<FormOpenRequestHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FormOpenRequestHandler handler) {
		handler.onFormOpenRequested(this);
	}

}