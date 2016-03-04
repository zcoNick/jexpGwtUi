package com.javexpress.gwt.library.ui.bootstrap;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.js.JsUtil;

public abstract class ApplicationMainContent extends AbstractContainer {

	protected static final int				VIEW_CACHE_SIZE	= 20;

	private Map<String, MainContentView>	viewCache		= new LinkedHashMap<String, MainContentView>(VIEW_CACHE_SIZE);
	private MainContentView					currentView		= null;

	protected Element						page;

	public ApplicationMainContent(Element el) {
		super(el);
	}

	public void addView(String path, MainContentView view) {
		try {
			hideCurrent();
			add(view, page);
			viewCache.put(path, view);
			currentView = view;
			currentView.onShow();
			if (viewCache.size() > VIEW_CACHE_SIZE) {
				Iterator<String> iter = viewCache.keySet().iterator();
				iter.next();//Dashboard
				String second = iter.next();
				MainContentView cached = viewCache.get(second);
				viewCache.remove(second);
				cached.removeFromParent();
			}
		} catch (Exception e) {
			JsUtil.handleError(this, e);
		}
	}

	private void hideCurrent() {
		if (currentView != null) {
			currentView.getElement().getStyle().setDisplay(Display.NONE);
			currentView.onHide();
			currentView = null;
		}
	}

	public MainContentView findView(String path) {
		return viewCache.get(path);
	}

	public void showView(MainContentView cached) {
		hideCurrent();
		cached.getElement().getStyle().setDisplay(Display.BLOCK);
		currentView = cached;
		currentView.onShow();
	}

	public MainContentView getCurrentView() {
		return currentView;
	}

	@Override
	protected void onUnload() {
		viewCache = null;
		currentView = null;
		page = null;
		super.onUnload();
	}

	public abstract MainContentView createView(String id);

}