package com.javexpress.gwt.library.ui.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.JqIcon;
import com.javexpress.gwt.library.ui.container.panel.SimplePanelFocusable;
import com.javexpress.gwt.library.ui.form.Form;
import com.javexpress.gwt.library.ui.form.IFormFactory;
import com.javexpress.gwt.library.ui.form.IJiraEnabledForm;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.form.IWindow;
import com.javexpress.gwt.library.ui.form.IWindowContainer;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.ResourceInjector;

public class DialogBox extends SimplePanelFocusable implements IWindowContainer {

	private Element				container;
	private Element				closeSpan;
	private Element				helpSpan;
	private Element				maxMinSpan;
	private boolean				modal		= true;
	private boolean				draggable	= true;
	private boolean				resizable	= false;
	private boolean				maximizable	= false;
	private Element				titleSpan;
	private Element				toolDiv;
	private Focusable			opener;
	private Command				closeListener;
	private JavaScriptObject	transferEffectPartner;
	private Element				titleDiv;

	public JavaScriptObject getTransferEffectPartner() {
		return transferEffectPartner;
	}

	public DialogBox setTransferEffectPartner(JavaScriptObject transferEffectPartner) {
		this.transferEffectPartner = transferEffectPartner;
		return this;
	}

	public boolean isModal() {
		return modal;
	}

	public Focusable getOpener() {
		return opener;
	}

	public void setOpener(Focusable opener) {
		this.opener = opener;
	}

	public void setModal(final boolean modal) {
		this.modal = modal;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void setDraggable(final boolean draggable) {
		this.draggable = draggable;
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(final boolean resizable) {
		this.resizable = resizable;
	}

	public boolean isMaximizable() {
		return maximizable;
	}

	public void setMaximizable(boolean maximizable) {
		this.maximizable = maximizable;
		if (maximizable && maxMinSpan == null) {
			maxMinSpan = DOM.createSpan();
			maxMinSpan.setClassName("ui-icon " + JqIcon.newwin.getCssClass() + " ui-cursor-hand");
			maxMinSpan.getStyle().setFloat(Float.RIGHT);
			maxMinSpan.getStyle().setMarginTop(0.1, Unit.EM);
			maxMinSpan.getStyle().setMarginRight(2, Unit.PX);
			maxMinSpan.setAttribute("s", "0");
			toolDiv.appendChild(maxMinSpan);
		} else if (maxMinSpan != null) {
			maxMinSpan.removeFromParent();
			maxMinSpan = null;
		}
	}

	public DialogBox(final IUIComposite form) {
		this(form.getId(), form.getIcon(), form.getHeader(), form.getWidth(), form.getHeight(), (Widget) form);
		setMaximizable(form.isMaximizable());
		setResizable(form.isResizable());
		setHelpIndex(form.getHelpIndex());
	}

	public DialogBox(final String id, final String title, final String width, final String height, final Widget widget) {
		this(id, null, title, width, height, widget);
	}

	public DialogBox(final String id, final ICssIcon icon, final String title, final String width, final String height, Widget widget) {
		super();
		JsUtil.ensureId(null, this, WidgetConst.DIALOG_PREFIX, id);
		getElement().addClassName("ui-dialog ui-widget ui-widget-content ui-corner-all ui-shadow jexpDialog");

		createTitle(icon, title);
		createContainer(id != null ? id : getElement().getId().substring(WidgetConst.DIALOG_PREFIX.length()));

		double calc = 0;
		if (width.endsWith("em")) {
			calc = Double.valueOf(width.replaceAll("em", ""));
			getElement().getStyle().setLeft(Math.ceil((Window.getClientWidth() / 16 / 2) - (calc / 2)), Unit.EM);
			container.getStyle().setWidth(Math.ceil(calc), Unit.EM);
		} else {
			if (width.endsWith("%"))
				calc = JsUtil.calcPercent(Window.getClientWidth(), width);
			else
				calc = Double.valueOf(width.replaceAll("px", ""));
			getElement().getStyle().setLeft(Math.ceil((Window.getClientWidth() - calc) / 2), Unit.PX);
			container.getStyle().setWidth(Math.ceil(calc), Unit.PX);
		}

		if (height.endsWith("em")) {
			calc = Double.valueOf(height.replaceAll("em", ""));
			getElement().getStyle().setTop(Math.ceil((Window.getClientHeight() / 16 / 2) - (calc / 2)), Unit.EM);
			container.getStyle().setHeight(Math.ceil(calc), Unit.EM);
		} else {
			if (height.endsWith("%"))
				calc = JsUtil.calcPercent(Window.getClientHeight(), height);
			else
				calc = Double.valueOf(height.replaceAll("px", ""));
			getElement().getStyle().setTop(Math.ceil(((Window.getClientHeight() - calc) / 2)) - 12, Unit.PX);
			container.getStyle().setHeight(Math.ceil(calc), Unit.PX);
		}

		setWidget(widget);
		if (!GWT.isProdMode())
			titleSpan.setTitle(widget.getClass().getName());

		if (widget instanceof IWindow) {
			((IWindow) widget).setWindowContainer(this);
		}
	}

	private void createTitle(final ICssIcon icon, String title) {
		titleDiv = DOM.createDiv();
		titleDiv.getStyle().setWidth(100, Unit.PCT);
		titleDiv.addClassName("ui-dialog-titlebar " + (ResourceInjector.theme.isInverted() ? "ui-state-active " : "ui-widget-header ") + "ui-corner-top");
		titleDiv.setAttribute("style", "display:block");
		if (icon != null) {
			Element iconDiv = DOM.createDiv();
			iconDiv.getStyle().setDisplay(Display.INLINE_BLOCK);
			iconDiv.getStyle().setFloat(JsUtil.isLTR() ? Float.LEFT : Float.RIGHT);
			iconDiv.getStyle().setMargin(2, Unit.PX);
			Element iconSpan = DOM.createSpan();
			iconSpan.setClassName("ui-icon " + icon.getCssClass());
			iconDiv.appendChild(iconSpan);
			titleDiv.appendChild(iconDiv);
		} else
			title = "&nbsp;" + title;
		titleSpan = DOM.createSpan();
		titleSpan.addClassName("ui-dialog-title");
		JsUtil.ensureRtlClass(titleSpan);
		titleSpan.setInnerHTML(title);
		titleDiv.appendChild(titleSpan);

		toolDiv = DOM.createDiv();
		toolDiv.getStyle().setDisplay(Display.INLINE_BLOCK);
		toolDiv.getStyle().setPosition(Position.ABSOLUTE);
		if (JsUtil.isLTR())
			toolDiv.getStyle().setRight(18, Unit.PX);
		else
			toolDiv.getStyle().setLeft(18, Unit.PX);
		titleDiv.appendChild(toolDiv);

		closeSpan = DOM.createSpan();
		closeSpan.setId(getElement().getId() + "_cls");
		closeSpan.addClassName("ui-icon ui-icon-closethick ui-cursor-hand");
		closeSpan.setTitle(IFormFactory.nlsCommon.kapat());
		closeSpan.getStyle().setFloat(JsUtil.isLTR() ? Float.RIGHT : Float.LEFT);
		closeSpan.getStyle().setMarginTop(0.1, Unit.EM);
		closeSpan.getStyle().setMarginRight(2, Unit.PX);
		titleDiv.appendChild(closeSpan);

		getElement().appendChild(titleDiv);
	}

	private void createContainer(final String id) {
		container = DOM.createDiv();
		container.addClassName("ui-widget-content ui-corner-bottom jesFormContainer");
		Style s = container.getStyle();
		s.setPosition(Position.RELATIVE);
		s.setDisplay(Display.BLOCK);
		s.setOverflow(Overflow.HIDDEN);
		getElement().appendChild(container);
	}

	@Override
	public void setWidget(final Widget w) {
		if (w != null) {
			w.getElement().getStyle().setWidth(100, Unit.PCT);
			w.getElement().getStyle().setHeight(100, Unit.PCT);
		}
		super.setWidget(w);
	}

	@Override
	protected com.google.gwt.user.client.Element getContainerElement() {
		return (com.google.gwt.user.client.Element) container;
	}

	public void setClosable(final boolean value) {
		closeSpan.getStyle().setDisplay(value ? Display.BLOCK : Display.NONE);
		if (JsUtil.isLTR())
			toolDiv.getStyle().setRight(value ? 18 : 0, Unit.PX);
		else
			toolDiv.getStyle().setLeft(value ? 18 : 0, Unit.PX);
	}

	public void show() {
		int zIndex = JsUtil.calcDialogZIndex();
		if (modal) {
			Element fake = DOM.createDiv();
			fake.addClassName("ui-widget-overlay");
			fake.setId("m_" + getElement().getId());
			fake.getStyle().setZIndex(zIndex);
			RootPanel.get().getElement().appendChild(fake);
		}
		getElement().getStyle().setZIndex(zIndex + 1);
		RootPanel.get().add(this);
		if (transferEffectPartner != null)
			JsUtil.transfer(transferEffectPartner, getElement());
		postCreate(this, getElement(), closeSpan, maxMinSpan, helpSpan, draggable, resizable ? container : null);
		if (opener != null)
			opener.setFocus(false);
		setFocus(true);
	}

	private native void postCreate(DialogBox x, Element element, Element kid, Element mid, Element hid, boolean draggable, Element resizable) /*-{
		var closeButton = $wnd.$(kid);
		closeButton.mouseenter(function() {
			closeButton.addClass("ui-shadow");
		}).mouseleave(function() {
			closeButton.removeClass("ui-shadow");
		}).click(function() {
			x.@com.javexpress.gwt.library.ui.dialog.DialogBox::close()();
		});
		if (mid){
			var maxMinButton = $wnd.$(mid);
			maxMinButton.mouseenter(function() {
				maxMinButton.addClass("ui-shadow");
			}).mouseleave(function() {
				maxMinButton.removeClass("ui-shadow");
			}).click(function() {
				x.@com.javexpress.gwt.library.ui.dialog.DialogBox::fireMaxMin(Ljava/lang/String;)(maxMinButton.attr("s"));
			});
		}
		var helpButton = $wnd.$(hid);
		helpButton.mouseenter(function() {
			helpButton.addClass("ui-shadow");
		}).mouseleave(function() {
			helpButton.removeClass("ui-shadow");
		}).click(function() {
			x.@com.javexpress.gwt.library.ui.dialog.DialogBox::openHelp()();
		});
		if (resizable) {
			$wnd
					.$(resizable)
					.resizable(
							{
								autoHide : true,
								delay : 500,
								handles : "se",
								ghost : true,
								stop : function(event, ui) {
									x.@com.javexpress.gwt.library.ui.dialog.DialogBox::fireResized(II)(ui.size.width,ui.size.height);
								}
							});
		}
		if (draggable) {
			$wnd.$(element).draggable({
				handle : ".ui-dialog-titlebar",
				opacity : .15,
				cursor : "move",
			});
		}
	}-*/;

	@Override
	protected void onLoad() {
		super.onLoad();
		if (getWidget() instanceof IWindow)
			((IWindow) getWidget()).onShow();
	}

	@Override
	protected void onUnload() {
		if (transferEffectPartner != null)
			JsUtil.transfer(getElement(), transferEffectPartner);
		transferEffectPartner = null;
		remove(getWidget());
		container = null;
		closeSpan = null;
		maxMinSpan = null;
		helpSpan = null;
		titleSpan = null;
		toolDiv = null;
		if (closeListener != null)
			closeListener.execute();
		closeListener = null;
		if (opener != null)
			opener.setFocus(true);
		opener = null;
		titleDiv = null;
		destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).empty().off();
	}-*/;

	@Override
	public void close() {
		removeOverlay(getElement().getId());
		removeFromParent();
	}

	public void openHelp() {
		if (getWidget() instanceof Form)
			((Form) getWidget()).openHelp();
	}

	public void openJiraIssue() {
		if (getWidget() instanceof IJiraEnabledForm)
			((IJiraEnabledForm) getWidget()).openJiraIssue();
	}

	private static native void removeOverlay(String id) /*-{
		$wnd.$("#m_" + id).remove();
	}-*/;

	@Override
	public void setHeader(final String header) {
		titleSpan.setInnerHTML(header);
	}

	public void setHelpIndex(final String helpIndex) {
		if (JsUtil.isEmpty(helpIndex))
			return;
		helpSpan = DOM.createSpan();
		helpSpan.setClassName("ui-icon " + JqIcon.help.getCssClass() + " ui-cursor-hand");
		helpSpan.getStyle().setFloat(Float.RIGHT);
		helpSpan.getStyle().setMarginTop(0.1, Unit.EM);
		helpSpan.getStyle().setMarginRight(2, Unit.PX);
		toolDiv.appendChild(helpSpan);
	}

	@Override
	public void setWidth(String width) {
		super.setWidth(width);
	}

	public void setPadding(int padding) {
		container.getStyle().setPadding(padding, Unit.PX);
	}

	@Override
	protected Focusable getFocusWidget() {
		if (getWidget() instanceof Focusable)
			return (Focusable) getWidget();
		return null;
	}

	public void setCloseListener(Command command) {
		this.closeListener = command;
	}

	private void fireResized(int w, int h) {
		container.getStyle().setWidth(w, Unit.PX);
		container.getStyle().setHeight(h, Unit.PX);
		if (getWidget() instanceof RequiresResize)
			((RequiresResize) getWidget()).onResize();
	}

	private void fireMaxMin(String state) {
		if (state.equals("0")) {
			//Normal Boyut -> Tam Ekran
			maxMinSpan.setAttribute("t", String.valueOf(getElement().getAbsoluteTop()));
			maxMinSpan.setAttribute("l", String.valueOf(getElement().getAbsoluteLeft()));
			maxMinSpan.setAttribute("w", String.valueOf(container.getOffsetWidth()));
			maxMinSpan.setAttribute("h", String.valueOf(container.getOffsetHeight()));
			int w = Window.getClientWidth();
			int h = Window.getClientHeight();
			getElement().getStyle().setTop(1, Unit.PX);
			getElement().getStyle().setLeft(1, Unit.PX);
			container.getStyle().setWidth(w - 8, Unit.PX);
			container.getStyle().setHeight(h - 28, Unit.PX);
			maxMinSpan.setAttribute("s", "1");
		} else {
			int t = Integer.valueOf(maxMinSpan.getAttribute("t"));
			int l = Integer.valueOf(maxMinSpan.getAttribute("l"));
			int w = Integer.valueOf(maxMinSpan.getAttribute("w"));
			int h = Integer.valueOf(maxMinSpan.getAttribute("h"));
			getElement().getStyle().setTop(t, Unit.PX);
			getElement().getStyle().setLeft(l, Unit.PX);
			container.getStyle().setWidth(w, Unit.PX);
			container.getStyle().setHeight(h, Unit.PX);
			maxMinSpan.removeAttribute("t");
			maxMinSpan.removeAttribute("l");
			maxMinSpan.removeAttribute("w");
			maxMinSpan.removeAttribute("h");
			maxMinSpan.setAttribute("s", "0");
		}
		if (getWidget() instanceof IWindow)
			((IWindow) getWidget()).onResize();
	}

}