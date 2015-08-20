package com.javexpress.gwt.library.ui.dialog;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.AbstractContainerFocusable;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.UIComposite;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.form.IUICompositeView;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class WindowView extends AbstractContainerFocusable implements IUICompositeView, RequiresResize, ProvidesResize {

	private Element	windowDiv;
	private Element	headerDiv;
	private Element	mainDiv;
	private Element	btClose;
	private boolean	draggable;
	private boolean	maximizable;
	private boolean	helpVisible;
	private Element	helpSpan;
	private Element	headerEl;

	public boolean isDraggable() {
		return draggable;
	}

	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	public boolean isMaximizable() {
		return maximizable;
	}

	public void setMaximizable(boolean maximizable) {
		this.maximizable = maximizable;
	}

	public boolean isHelpVisible() {
		return helpVisible;
	}

	public void setHelpVisible(boolean helpVisible) {
		this.helpVisible = helpVisible;
	}

	public WindowView(String id, boolean modal) {
		super(DOM.createDiv());
		if (modal) {
			setStyleName("jexp-ui-window-modal modal in");
			Element backdrop = DOM.createDiv();
			backdrop.setClassName("modal-backdrop in");
			backdrop.getStyle().setHeight(com.google.gwt.user.client.Window.getClientHeight(), Unit.PX);
			backdrop.getStyle().setZIndex(JsUtil.calcDialogZIndex());
			getElement().appendChild(backdrop);
		}
		getElement().setAttribute("style", "display: block; padding-right: 17px; z-index:" + JsUtil.calcDialogZIndex());

		windowDiv = DOM.createDiv();
		windowDiv.setClassName("modal-dialog jexp-ui-window widget-box jexpShadow");
		windowDiv.setTabIndex(-1);
		windowDiv.setAttribute("role", "dialog");
		windowDiv.setId(WidgetConst.WINDOWPREFIX + "_" + id);
		windowDiv.setAttribute("zindex", String.valueOf(JsUtil.calcDialogZIndex()));
		getElement().appendChild(windowDiv);

		headerDiv = DOM.createDiv();
		headerDiv.setClassName("widget-header");
		windowDiv.appendChild(headerDiv);

		Element body = DOM.createDiv();
		body.setClassName("widget-body");
		mainDiv = DOM.createDiv();
		mainDiv.setClassName("widget-main container-fluid");
		body.appendChild(mainDiv);
		windowDiv.appendChild(body);
	}

	public void setForm(IUIComposite form) {
		add((Widget) form, mainDiv);
		setDraggable(form.isDraggable());
		setMaximizable(form.isMaximizable());
		setHelpVisible(form.getHelpIndex() != null);
		form.setCloseHandler(new Command() {
			@Override
			public void execute() {
				close();
			}
		});
	}

	@Override
	public void setFocus(boolean focused) {
		if (focused) {
			((Focusable) getWidget(0)).setFocus(true);
		}
	}

	@Override
	protected void doAttachChildren() {
		updateWidth();
		windowDiv.setAttribute("aria-describedby", "dialog-confirm");
		windowDiv.setAttribute("aria-labelledby", getId() + "_title");

		IUIComposite form = (IUIComposite) getWidget(0);
		fillHeader(form.getIcon(), form.getHeader());
		form.setAttachedTo(this);

		super.doAttachChildren();
	}

	private void updateWidth() {
		IUIComposite form = (IUIComposite) getWidget(0);
		String width = form.getWidth();
		String marginLeft = "auto";
		if (width == null && form instanceof UIComposite) {
			UIComposite uic = (UIComposite) form;
			int screenW = Window.getClientWidth();
			double pct = 50;
			Integer xs = JsUtil.nvl(uic.getXsSize(), 0);
			Integer sm = JsUtil.nvl(uic.getSmSize(), xs);
			Integer md = JsUtil.nvl(uic.getMdSize(), sm);
			Integer lg = JsUtil.nvl(uic.getLgSize(), md);
			if (xs > 0 && screenW < 768)
				pct = JsUtil.RESPONSIVE_COL_WIDTHS[xs];
			else if (sm > 0 && screenW >= 768 && screenW < 992)
				pct = JsUtil.RESPONSIVE_COL_WIDTHS[sm];
			else if (md > 0 && screenW >= 992 && screenW < 1200)
				pct = JsUtil.RESPONSIVE_COL_WIDTHS[md];
			else if (lg > 0)
				pct = JsUtil.RESPONSIVE_COL_WIDTHS[lg];
			width = String.valueOf(pct) + "%";
			marginLeft = Math.ceil((100 - pct) / 2) + "%";
		}
		String zindex = windowDiv.getAttribute("zindex");
		windowDiv.setAttribute("style", "z-index:" + zindex + ";" + (width != null ? "width:" + width + ";" : "") + "margin:20px " + marginLeft + ";min-width:100px;display:block");
	}

	private void fillHeader(ICssIcon icon, String header) {
		setHeader(icon, header);

		Element tools = DOM.createDiv();
		tools.setClassName("widget-toolbar");

		if (helpVisible) {
			helpSpan = DOM.createAnchor();
			helpSpan.setId(windowDiv.getId() + "_help");
			helpSpan.addClassName("jexpWindowToolItem ub_" + windowDiv.getId());
			helpSpan.setInnerHTML("<i class='ace-icon fa fa-question'></i>");
			helpSpan.setTitle(ClientContext.nlsCommon.yardim());
			tools.appendChild(helpSpan);
		}

		btClose = DOM.createAnchor();
		btClose.setId(windowDiv.getId() + "_close");
		btClose.addClassName("ub_" + windowDiv.getId());
		btClose.setInnerHTML("<i class='ace-icon fa fa-times'></i>");
		btClose.setTitle(ClientContext.nlsCommon.kapat());
		tools.appendChild(btClose);

		headerDiv.appendChild(tools);
	}

	@Override
	public void setHeader(ICssIcon icon, String header) {
		if (headerEl == null) {
			headerEl = DOM.createElement("h5");
			headerEl.setClassName("widget-title");
			headerDiv.appendChild(headerEl);
		}
		if (icon != null)
			header = "<i class='ace-icon " + icon.getCssClass() + "'></i>" + header;
		headerEl.setInnerHTML(header);
	}

	private native void bindOnClick(Element el, Command command) /*-{
		$wnd.$(el).click(function() {
			command.@com.google.gwt.user.client.Command::execute()();
		});
	}-*/;

	public void show() {
		RootPanel.get().add(this);
		IUIComposite form = (IUIComposite) getWidget(0);
		setFocus(true);
		form.onShow();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		bindOnClick(btClose, new Command() {
			@Override
			public void execute() {
				close();
			}
		});
		if (helpSpan != null)
			bindOnClick(helpSpan, new Command() {
				@Override
				public void execute() {
					openHelp();
				}
			});
		if (isDraggable()) {
			JsonMap opts = new JsonMap();
			opts.set("handle", ".widget-header");
			opts.set("opacity", 0.5);
			opts.set("cursor", "move");
			JsUtil.draggable(windowDiv, opts.getJavaScriptObject());
		}
	}

	public void openHelp() {
		Widget w = getWidget(0);
		if (w instanceof IUIComposite) {
			ClientContext.instance.openHelp((IUIComposite) w);
		}
	}

	public void close() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				removeFromParent();
			}
		});
	}

	@Override
	protected void onUnload() {
		headerEl = null;
		headerDiv = null;
		mainDiv = null;
		_destroyByJs(getElement(), ".ub_" + windowDiv.getId());
		windowDiv = null;
		super.onUnload();
	}

	private native void _destroyByJs(Element el, String ubSel) /*-{
		$wnd.$(ubSel, $wnd.$(el)).off();
	}-*/;

	@Override
	public void onResize() {
		updateWidth();
		if (getWidget(0) instanceof RequiresResize)
			((RequiresResize) getWidget(0)).onResize();
	}

	//--EVENTS
	private void fireResized(int w, int h) {
		windowDiv.getStyle().setWidth(w, Unit.PX);
		windowDiv.getStyle().setHeight(h, Unit.PX);
		if (getWidget(0) instanceof RequiresResize)
			((RequiresResize) getWidget(0)).onResize();
	}

}