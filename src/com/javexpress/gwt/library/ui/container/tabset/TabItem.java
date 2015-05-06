package com.javexpress.gwt.library.ui.container.tabset;

import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.ICssIcon;

public class TabItem {

	private ICssIcon	icon;
	private String		title;
	private String		iconExtraClasses;
	private Widget		widget;
	private String		id;
	private boolean		closable;

	public String getIconExtraClasses() {
		return iconExtraClasses;
	}

	public void setIconExtraClasses(String iconExtraClasses) {
		this.iconExtraClasses = iconExtraClasses;
	}

	public ICssIcon getIcon() {
		return icon;
	}

	public void setIcon(ICssIcon icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Widget getWidget() {
		return widget;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isClosable() {
		return closable;
	}

	public void setClosable(boolean closable) {
		this.closable = closable;
	}

	/** Designer compatible constructor */
	public TabItem(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

}