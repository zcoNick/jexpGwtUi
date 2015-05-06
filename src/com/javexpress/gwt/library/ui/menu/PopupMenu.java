package com.javexpress.gwt.library.ui.menu;

import java.beans.Beans;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.HasContextMenuHandlers;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.javexpress.gwt.library.ui.dialog.JexpPopupPanel;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class PopupMenu extends JexpPopupPanel implements ContextMenuHandler {

	private Element						ul;
	private Map<String, Command>		commands;
	private Map<String, IMenuHandler>	handlers;
	private List<MenuItem>				items;

	public PopupMenu() {
		this(null);
	}

	public PopupMenu(HasContextMenuHandlers parent) {
		super(true);
		getElement().addClassName("jexpPopupMenu ui-shadow");
		if (parent != null)
			parent.addContextMenuHandler(this);
	}

	@Override
	public void onContextMenu(ContextMenuEvent event) {
		try {
			boolean pop = true;
			if (event.getSource() instanceof IHasPopupMenu)
				pop = ((IHasPopupMenu) event.getSource()).canOpenContextMenu(this);
			if (pop)
				popUp(event.getNativeEvent());
		} catch (Exception e) {
			JsUtil.handleError(this, e);
		}
	}

	public void popUp(NativeEvent nativeEvent) {
		nativeEvent.preventDefault();
		nativeEvent.stopPropagation();
		popUp(nativeEvent.getClientX(), nativeEvent.getClientY());
	}

	public void popUp(Event event) {
		event.preventDefault();
		event.stopPropagation();
		popUp(event.getClientX(), event.getClientY());
	}

	public void popUp(final int x, final int y) {
		getElement().getStyle().setZIndex(9999);
		setPopupPositionAndShow(new PositionCallback() {
			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = x, top = y;
				if (offsetWidth + x > Window.getClientWidth())
					left = Window.getClientWidth() - offsetWidth - 2;
				if (offsetHeight + y > Window.getClientHeight())
					top = Window.getClientHeight() - offsetHeight - 2;
				setPopupPosition(left, top);
			}
		});
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		this.ul = DOM.createElement("ul");
		getElement().appendChild(ul);
		prepareMenu();
		prepareHandlers();
		if (!Beans.isDesignTime()) {
			createByJs(this, getElement(), ul);
		}
	}

	public void add(MenuItem mi) {
		// Detach new child.
		mi.removeFromParent();

		// Logical attach.
		if (items == null)
			items = new ArrayList<MenuItem>();
		items.add(mi);

		// Physical attach.
		DOM.appendChild(ul, mi.getElement());

		// Adopt.
		adopt(mi);
	}

	public abstract void prepareMenu();

	@Override
	protected void onUnload() {
		commands = null;
		handlers = null;
		items = null;
		if (!Beans.isDesignTime()) {
			destroyByJs(this, ul);
		}
		getElement().removeChild(ul);
		ul = null;
		super.onUnload();
	}

	private native void createByJs(PopupMenu x, Element element, Element ul) /*-{
		var options = { select:function(event, ui){
				var el = event.currentTarget;
				x.@com.javexpress.gwt.library.ui.menu.PopupMenu::fireOnClick(Ljava/lang/String;Ljava/lang/String;)(el.id,el.getAttribute("code"));
			}
		};
		$wnd.$(ul).menu(options);
	}-*/;

	private native void destroyByJs(PopupMenu x, Element ul) /*-{
		$wnd.$(ul).menu("destroy");
	}-*/;

	private void prepareHandlers() {
		handlers = new HashMap<String, IMenuHandler>();
		commands = new HashMap<String, Command>();
		if (items == null || items.isEmpty())
			return;
		Iterator<MenuItem> iter = items.iterator();
		while (iter.hasNext()) {
			iter.next().fillHandlers(handlers, commands);
		}
	}

	//**---- EVENTS
	public void fireOnClick(final String id, final String code) {
		boolean executed = false;
		IMenuHandler handler = handlers.get(id);
		if (handler != null) {
			handler.itemClicked(code);
			executed = true;
		}
		Command command = commands.get(id);
		if (command != null) {
			command.execute();
			executed = true;
		}
		if (executed)
			hide();
	}

}