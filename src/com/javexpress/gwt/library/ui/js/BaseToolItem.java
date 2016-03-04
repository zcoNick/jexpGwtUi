package com.javexpress.gwt.library.ui.js;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.Event;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.data.IToolItemHandler;

public class BaseToolItem {

	private String				id;
	private String				caption;
	private ICssIcon			icon;
	private String				iconClass;
	private String				hint;
	private IToolItemHandler	handler;
	private boolean				startsWithSeparator;
	private boolean				endsWithSeparator;
	private Element				element;
	private boolean				visible	= true;

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
		if (!visible)
			element.getStyle().setDisplay(visible ? Display.INLINE_BLOCK : Display.NONE);
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public IToolItemHandler getHandler() {
		return handler;
	}

	public void setHandler(IToolItemHandler handler) {
		this.handler = handler;
	}

	public ICssIcon getIcon() {
		return icon;
	}

	public void setIcon(final ICssIcon icon) {
		this.icon = icon;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(final String hint) {
		this.hint = hint;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(final String caption) {
		this.caption = caption;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public boolean isStartsWithSeparator() {
		return startsWithSeparator;
	}

	public void setStartsWithSeparator(boolean startsWithSeparator) {
		this.startsWithSeparator = startsWithSeparator;
	}

	public boolean isEndsWithSeparator() {
		return endsWithSeparator;
	}

	public void setEndsWithSeparator(boolean endsWithSeparator) {
		this.endsWithSeparator = endsWithSeparator;
	}

	/** Designer compatible constructor */
	public BaseToolItem(final String id) {
		super();
		this.id = id;
	}

	public BaseToolItem(final String id, final String caption, final ICssIcon icon, final String hint) {
		this(id);
		this.caption = caption;
		this.icon = icon;
		this.hint = hint;
	}

	public void executeHandler(Event event) {
		if (handler != null)
			handler.execute(id, event);
	}

	public void unload() {
		handler = null;
		element = null;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		if (element != null)
			element.getStyle().setDisplay(visible ? Display.INLINE_BLOCK : Display.NONE);
	}

}