package com.javexpress.gwt.library.ui.event;

import com.google.gwt.event.shared.GwtEvent;
import com.javexpress.gwt.library.ui.event.handler.NavigationItemClickedHandler;

public class NavigationItemClicked extends GwtEvent<NavigationItemClickedHandler> {

	public static Type<NavigationItemClickedHandler>	TYPE	= new Type<NavigationItemClickedHandler>();

	private int											origin;
	private String										path;

	public int getOrigin() {
		return origin;
	}

	public void setOrigin(int origin) {
		this.origin = origin;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public NavigationItemClicked(String path, int origin) {
		this.path = path;
		this.origin = origin;
	}

	@Override
	public Type<NavigationItemClickedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(NavigationItemClickedHandler handler) {
		handler.onNavigationItemClicked(this);
	}

}