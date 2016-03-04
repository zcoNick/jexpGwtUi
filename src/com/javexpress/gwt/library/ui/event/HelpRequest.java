package com.javexpress.gwt.library.ui.event;

import com.google.gwt.event.shared.GwtEvent;
import com.javexpress.gwt.library.ui.event.handler.HelpRequestHandler;
import com.javexpress.gwt.library.ui.form.IUIComposite;

public class HelpRequest extends GwtEvent<HelpRequestHandler> {

	public static Type<HelpRequestHandler>	TYPE	= new Type<HelpRequestHandler>();
	private IUIComposite					form;

	public HelpRequest(IUIComposite form) {
		this.form = form;
	}

	public IUIComposite getForm() {
		return form;
	}

	public void setForm(IUIComposite form) {
		this.form = form;
	}

	@Override
	public Type<HelpRequestHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(HelpRequestHandler handler) {
		handler.onHelpRequested(this);
	}

}