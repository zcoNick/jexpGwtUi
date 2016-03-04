package com.javexpress.gwt.library.ui.event;

import com.google.gwt.event.shared.GwtEvent;
import com.javexpress.gwt.library.ui.dialog.WindowView;
import com.javexpress.gwt.library.ui.event.handler.FormShowInWindowRequestHandler;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.js.JexpCallback;

public class FormShowInWindowRequest extends GwtEvent<FormShowInWindowRequestHandler> {

	public static Type<FormShowInWindowRequestHandler>	TYPE	= new Type<FormShowInWindowRequestHandler>();
	private IUIComposite								form;
	private boolean										modal;
	private Integer										posX, posY;
	private JexpCallback<WindowView>					callback;

	public FormShowInWindowRequest(IUIComposite form, boolean modal) {
		this.form = form;
		this.modal = modal;
	}

	public FormShowInWindowRequest(IUIComposite form, boolean modal, Integer x, Integer y, JexpCallback<WindowView> callback) {
		this(form, modal);
		this.posX = x;
		this.posY = y;
		this.callback = callback;
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

	public JexpCallback<WindowView> getCallback() {
		return callback;
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