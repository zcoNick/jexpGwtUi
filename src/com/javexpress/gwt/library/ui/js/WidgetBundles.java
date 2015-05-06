package com.javexpress.gwt.library.ui.js;

import java.util.ArrayList;
import java.util.List;

public class WidgetBundles {

	private String			name;
	private List<String>	styleSheets	= new ArrayList<String>();
	private List<String>	javaScripts	= new ArrayList<String>();
	private WidgetBundles	parent;

	public void addStyleSheet(String path) {
		styleSheets.add(path);
	}

	public WidgetBundles getParent() {
		return parent;
	}

	public void addJavaScript(String path) {
		javaScripts.add(path);
	}

	public List<String> getStyleSheets() {
		return styleSheets;
	}

	public List<String> getJavaScripts() {
		return javaScripts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WidgetBundles(String name) {
		super();
		this.name = name;
	}

	public WidgetBundles(String name, WidgetBundles parent) {
		this(name);
		this.parent = parent;
	}

}