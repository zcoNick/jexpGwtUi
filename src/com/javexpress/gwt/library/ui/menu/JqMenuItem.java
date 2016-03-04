package com.javexpress.gwt.library.ui.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.JexpWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

@Deprecated
public class JqMenuItem extends JexpWidget {

	private Command				command;
	private IMenuHandler		handler;
	private List<JqMenuItem>	children;

	public JqMenuItem(final String id, final String label) {
		this(id, label, null);
	}

	public JqMenuItem(final String id, final String label, final IMenuHandler menuHandler) {
		this(id, null, label, menuHandler);
	}

	public JqMenuItem(final String id, final ICssIcon icon, final String label, final IMenuHandler menuHandler) {
		super();
		this.handler = menuHandler;
		Element li = DOM.createElement("li");
		li.setAttribute("code", id);
		if (JsUtil.isRTL())
			li.getStyle().setFloat(Float.RIGHT);
		Element a = DOM.createAnchor();
		a.setInnerText(label);
		a.setAttribute("name", id);
		li.appendChild(a);
		if (icon != null) {
			Element span = DOM.createSpan();
			span.addClassName("ui-icon " + icon.getCssClass());
			a.appendChild(span);
		}
		setElement(li);
		JsUtil.ensureId(null, this, WidgetConst.MENUITEM_PREFIX, id);
	}

	public void setHasSeperator(final boolean hasSeperator) {
		if (hasSeperator)
			getElement().addClassName("ui-menubar-item-seperator");
		else
			getElement().removeClassName("ui-menubar-item-seperator");
	}

	public void add(final JqMenuItem mi) {
		if (children == null)
			children = new ArrayList<JqMenuItem>();
		Element ul = null;
		NodeList<Element> nl = getElement().getElementsByTagName("ul");
		if (nl.getLength() > 0)
			ul = nl.getItem(0);
		if (ul == null) {
			ul = DOM.createElement("ul");
			ul.addClassName("ui-shadow");
			getElement().appendChild(ul);
		}
		ul.appendChild(mi.getElement());
		children.add(mi);
	}

	public Command getCommand() {
		return command;
	}

	public JqMenuItem setCommand(final Command command) {
		this.command = command;
		return this;
	}

	public IMenuHandler getHandler() {
		return handler;
	}

	public void setHandler(IMenuHandler handler) {
		this.handler = handler;
	}

	public void fillHandlers(final Map<String, IMenuHandler> handlers, final Map<String, Command> commands) {
		if (command != null)
			commands.put(getElement().getAttribute("id"), command);
		if (handler != null)
			handlers.put(getElement().getAttribute("id"), handler);
		if (children != null)
			for (JqMenuItem mi : children)
				mi.fillHandlers(handlers, commands);
	}

	@Override
	protected void onUnload() {
		command = null;
		handler = null;
		if (children != null)
			for (JqMenuItem mi : children)
				mi.removeFromParent();
		children = null;
		super.onUnload();
	}

}