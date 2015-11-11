package com.javexpress.gwt.library.ui.form;

import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.javexpress.common.model.item.BpmAction;
import com.javexpress.common.model.item.FormDef;
import com.javexpress.gwt.library.ui.ICssIcon;

public interface IUIComposite extends IsWidget, HasWidgets, ISizeAwareWidget, Focusable {

	final static byte	ACT_INSERTRECORD	= -128;

	IUICompositeView getAttachedTo();

	void setAttachedTo(IUICompositeView attachedTo);

	void addOnLoadCommand(Command command);

	void addOnUnloadCommand(Command command);

	FormDef getFormDef();

	String getHeader();

	String getWidth();

	String getHeight();

	String getId();

	String getHelpIndex();

	boolean isResizable();

	boolean isDraggable();

	boolean isMaximizable();

	ICssIcon getIcon();

	void setCloseHandler(Command command);

	void onShow();

	void onHide();

	boolean isSupportsAction(byte action);

	void performAction(byte action) throws Exception;

	BpmAction getBpmAction();

	KeyDownHandler getKeyDownHandler();

}