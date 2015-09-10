package com.javexpress.gwt.library.ui.event;

import com.google.gwt.event.shared.GwtEvent;
import com.javexpress.gwt.library.ui.event.handler.FormShowInWindowRequestHandler;
import com.javexpress.gwt.library.ui.form.IUIComposite;

public class FormShowInWindowRequest extends GwtEvent<FormShowInWindowRequestHandler> {

	public static Type<FormShowInWindowRequestHandler>	TYPE	= new Type<FormShowInWindowRequestHandler>();
	private IUIComposite								form;
	private boolean										modal;
	private Integer										posX, posY;

	public FormShowInWindowRequest(IUIComposite form, boolean modal) {
		this.form = form;
		this.modal = modal;
	}

	public FormShowInWindowRequest(IUIComposite form, boolean modal, Integer x, Integer y) {
		this(form, modal);
		this.posX = x;
		this.posY = y;
	}

	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	public IUIComposite getForm() {
		return form;
	}

	public void setForm(IUIComposite form) {
		this.form = form;
	}

	public Integer getPosX() {
		return posX;
	}

	public void setPosX(Integer posX) {
		this.posX = posX;
	}

	public Integer getPosY() {
		return posY;
	}

	public void setPosY(Integer posY) {
		this.posY = posY;
	}

	@Override
	public Type<FormShowInWindowRequestHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FormShowInWindowRequestHandler handler) {
		handler.onFormShowInWindowRequested(this);
	}

}