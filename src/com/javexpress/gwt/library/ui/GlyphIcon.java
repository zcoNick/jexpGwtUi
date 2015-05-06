package com.javexpress.gwt.library.ui;

public enum GlyphIcon implements ICssIcon {

	refresh("refresh");

	private String	icon;

	private GlyphIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String getCssClass() {
		return "glyphicon glyphicon-" + icon;
	}

}