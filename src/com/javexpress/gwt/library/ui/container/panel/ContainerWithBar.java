package com.javexpress.gwt.library.ui.container.panel;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class ContainerWithBar extends AbstractContainer implements ISizeAwareWidget {

	private Element	container;
	private Element	toolContainer;

	public Element getContainer() {
		return container;
	}

	public Element getToolContainer() {
		return toolContainer;
	}

	@Override
	public String getId() {
		return getElement().getId();
	}

	public ContainerWithBar(boolean fitToParent) {
		super(DOM.createDiv());
		getElement().getStyle().setPosition(Position.RELATIVE);
		getElement().getStyle().setDisplay(Display.BLOCK);
		if (!JsUtil.USE_BOOTSTRAP)
			addStyleName("ui-widget-content ui-corner-all jexpBorderBox");

		if (fitToParent) {
			setWidth("auto");
			setHeight("100%");
		}
	}

	protected void createPanels() {
		int topPanelSize = createTopPanel();

		container = DOM.createDiv();
		container.getStyle().setPosition(Position.ABSOLUTE);
		container.getStyle().setDisplay(Display.BLOCK);
		container.getStyle().setTop(topPanelSize, Unit.PX);
		container.getStyle().setLeft(0, Unit.PX);
		container.getStyle().setRight(0, Unit.PX);
		if (!JsUtil.USE_BOOTSTRAP) {
			container.getStyle().setBottom(2.19, Unit.EM);
		} else
			container.getStyle().setBottom(30, Unit.PX);
		container.addClassName("jexpBorderBox");
		getElement().appendChild(container);

		toolContainer = DOM.createDiv();
		if (!JsUtil.USE_BOOTSTRAP) {
			toolContainer.getStyle().setHeight(2.2, Unit.EM);
			toolContainer.addClassName("ui-state-default");
		}
		toolContainer.getStyle().setPosition(Position.ABSOLUTE);
		toolContainer.getStyle().setDisplay(Display.BLOCK);
		toolContainer.getStyle().setOverflow(Overflow.AUTO);
		toolContainer.getStyle().setLeft(0, Unit.PX);
		toolContainer.getStyle().setRight(0, Unit.PX);
		toolContainer.getStyle().setBottom(0, Unit.PX);
		toolContainer.addClassName("jexpBorderBox");
		getElement().appendChild(toolContainer);
	}

	protected int createTopPanel() {
		return 0;
	}

	protected Element addToolItemElement(String id, ICssIcon icon, String caption, String hint, String iconClass, boolean enabled, boolean startsWithSeperator, boolean endsWithSeperator) {
		if (startsWithSeperator) {
			Element divsep = DOM.createDiv();
			divsep.addClassName("toolseparator");
			divsep.getStyle().setDisplay(Display.INLINE_BLOCK);
			Element sep = DOM.createSpan();
			sep.addClassName("ui-separator");
			divsep.appendChild(sep);
			getToolContainer().appendChild(divsep);
		}
		Element div = DOM.createDiv();
		JsUtil.ensureSubId(getElement(), div, id);
		div.addClassName("toolitem");
		div.getStyle().setDisplay(Display.INLINE_BLOCK);
		Element span = DOM.createSpan();
		if (hint != null)
			span.setTitle(hint);
		if (!enabled)
			span.setAttribute("disabled", "true");
		if (!JsUtil.USE_BOOTSTRAP) {
			if (JsUtil.isEmpty(caption)) {
				span.addClassName("ui-cursor-hand ui-icon ui-icon-only " + icon.getCssClass());
			} else {
				span.addClassName("ui-cursor-hand ui-icon " + icon.getCssClass());
				span.setInnerHTML(caption);
			}
		} else {
			span.addClassName("ace-icon jexpHandCursor " + icon.getCssClass());
			if (JsUtil.isNotEmpty(caption))
				span.setInnerHTML(caption);
		}
		if (JsUtil.isNotEmpty(iconClass)) {
			span.addClassName(iconClass);
		}
		div.appendChild(span);
		_bindHover(div);
		getToolContainer().appendChild(div);
		if (endsWithSeperator) {
			Element divsep = DOM.createDiv();
			divsep.addClassName("toolseparator");
			divsep.getStyle().setDisplay(Display.INLINE_BLOCK);
			Element sep = DOM.createSpan();
			sep.addClassName("ui-separator");
			divsep.appendChild(sep);
			getToolContainer().appendChild(divsep);
		}
		return span;
	}

	protected Element addToolItemElement(String id, ICssIcon icon, String caption, String hint, String iconClass, boolean enabled) {
		return addToolItemElement(id, icon, caption, hint, iconClass, enabled, false, false);
	}

	private native void _bindHover(Element div) /*-{
		$wnd.$(div).hover(function() {
			$wnd.$(this).addClass("ui-state-hover");
		}, function() {
			$wnd.$(this).removeClass("ui-state-hover");
		});
	}-*/;

	@Override
	protected void onUnload() {
		_destroyByJs(container, toolContainer);
		container = null;
		toolContainer = null;
		super.onUnload();
	}

	private native void _destroyByJs(Element container, Element tbar) /*-{
		$wnd.$(container).empty().off();
		$wnd.$(tbar).empty().off();
	}-*/;

}