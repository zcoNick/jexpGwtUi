package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WPull;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WSize;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.menu.DropDownMenu;
import com.javexpress.gwt.library.ui.menu.IMenuHandler;

public class MenuButton extends ComplexPanel {

	private Integer			size;
	private String			text;
	private WPull			wpull;
	private WSize			wsize;
	private WContext		wcontext;
	private ICssIcon		iconClass;
	private String			textClass;
	private boolean			highlight;
	private Element			iconSpan;
	private Element			textSpan;
	private boolean			disabled	= false;
	private Integer			tag;
	private boolean			dropUp		= false;
	private Element			button;
	private IMenuHandler	handler;

	public IMenuHandler getHandler() {
		return handler;
	}

	public void setHandler(IMenuHandler handler) {
		this.handler = handler;
	}

	public boolean isDropUp() {
		return dropUp;
	}

	public void setDropUp(boolean dropUp) {
		this.dropUp = dropUp;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		if (isAttached())
			textSpan.setInnerText(text + " ");
	}

	public WPull getWpull() {
		return wpull;
	}

	public void setWpull(WPull wpull) {
		this.wpull = wpull;
	}

	public WSize getWsize() {
		return wsize;
	}

	public void setWsize(WSize wsize) {
		this.wsize = wsize;
	}

	public WContext getWcontext() {
		return wcontext;
	}

	public void setWcontext(WContext wcontext) {
		this.wcontext = wcontext;
		if (isAttached())
			updateStyleContext();
	}

	public ICssIcon getIcon() {
		return iconClass;
	}

	public void setIcon(ICssIcon icon) {
		this.iconClass = icon;
		if (isAttached())
			ClientContext.resourceInjector.applyIconStyles(iconSpan, icon);
	}

	public String getTextClass() {
		return textClass;
	}

	public void setTextClass(String textClass) {
		this.textClass = textClass;
	}

	public MenuButton(Widget parent, String id) {
		this(parent, id, null);
	}

	public MenuButton(Widget parent, String id, String text) {
		super();
		setElement(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.MENUBUTTON_PREFIX, id);
		this.text = text;
		button = DOM.createButton();
		getElement().appendChild(button);
		iconSpan = DOM.createElement("i");
		button.appendChild(iconSpan);
		textSpan = DOM.createSpan();
		textSpan.setClassName(textClass);
		if (text != null)
			textSpan.setInnerText(text + " ");
		button.appendChild(textSpan);
		Element caret = DOM.createSpan();
		caret.setClassName("caret");
		button.appendChild(caret);
	}

	@Override
	protected void onLoad() {
		if (isDropUp())
			getElement().addClassName("dropup jexpMenuButton");
		else
			getElement().addClassName("dropdown jexpMenuButton");
		updateStyleContext();
		ClientContext.resourceInjector.applyIconStyles(iconSpan, iconClass);
		super.onLoad();
		button.setAttribute("data-toggle", "dropdown");
	}

	private void updateStyleContext() {
		String clazz = "";
		if (size != null)
			clazz = "width-" + size;
		if (wpull != null)
			clazz += " " + wpull.getValue();
		clazz += " btn";
		if (wsize != null) {
			switch (wsize) {
				case small:
					clazz += " btn-sm";
					break;
				case large:
					clazz += " btn-lg";
					break;
			}
		}
		if (wcontext != null) {
			switch (wcontext) {
				case Primary:
					clazz += " btn-primary";
					break;
				case Success:
					clazz += " btn-success";
					break;
				case Purple:
					clazz += " btn-purple";
					break;
				case Pink:
					clazz += " btn-pink";
					break;
				case Info:
					clazz += " btn-info";
					break;
				case Grey:
					clazz += " btn-grey";
					break;
				case Warning:
					clazz += " btn-warning";
					break;
				case Light:
					clazz += " btn-light";
					break;
				case Yellow:
					clazz += " btn-yellow";
					break;
				case Danger:
					clazz += " btn-danger";
					break;
				default:
					if (highlight)
						clazz += " btn-primary";
					break;
			}
		} else if (highlight)
			clazz += " btn-primary dropdown-toggle";
		button.setClassName(clazz.trim());
	}

	public void click() {
		if (!disabled)
			((ButtonElement) getElement().cast()).click();
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
		if (isAttached())
			updateStyleContext();
	}

	@Override
	protected void onUnload() {
		size = null;
		text = null;
		wpull = null;
		wsize = null;
		wcontext = null;
		iconClass = null;
		textClass = null;
		iconSpan = null;
		tag = null;
		textSpan = null;
		handler = null;
		button = null;
		super.onUnload();
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public Integer getTag() {
		return tag;
	}

	public void setDropDownMenu(DropDownMenu menu) {
		add(menu, getElement());
		menu.setHandler(new IMenuHandler() {
			@Override
			public void menuItemClicked(String code, Event event) {
				if (handler != null)
					handler.menuItemClicked(code, event);
			}
		});
	}
	
	public void setInverted(boolean inverted){
		if (inverted)
			getElement().addClassName("btn-inverse");
		else
			getElement().removeClassName("btn-inverse");
	}

}