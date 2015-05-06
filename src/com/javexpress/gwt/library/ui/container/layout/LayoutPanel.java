package com.javexpress.gwt.library.ui.container.layout;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;
import com.javexpress.gwt.library.ui.container.layout.IBorderLayout.Position;

public class LayoutPanel extends AbstractContainer {

	private Position	position;
	private boolean		closable;
	private boolean		slidable;
	private boolean		resizable;
	private boolean		allowOverflow;
	private String		minSize;
	private String		size;
	private String		maxSize;
	private boolean		useBorder;

	public Position getPosition() {
		return position;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

	public boolean isClosable() {
		return closable;
	}

	public void setClosable(final boolean closable) {
		this.closable = closable;
	}

	public boolean isSlidable() {
		return slidable;
	}

	public void setSlidable(final boolean slidable) {
		this.slidable = slidable;
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(final boolean resizable) {
		this.resizable = resizable;
	}

	public boolean isAllowOverflow() {
		return allowOverflow;
	}

	public void setAllowOverflow(final boolean allowOverflow) {
		this.allowOverflow = allowOverflow;
	}

	public String getMinSize() {
		return minSize;
	}

	public void setMinSize(final String minSize) {
		this.minSize = minSize;
	}

	public String getSize() {
		return size;
	}

	public void setSize(final String size) {
		this.size = size;
	}

	public String getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(final String maxSize) {
		this.maxSize = maxSize;
	}

	public boolean isUseBorder() {
		return useBorder;
	}

	public void setUseBorder(final boolean useBorder) {
		this.useBorder = useBorder;
	}
	
	public LayoutPanel(final Position pos) {
		this(pos, null);
	}
	
	public LayoutPanel(final Position pos, final String id) {
		super(DOM.createDiv());
		this.position = pos;
		getElement().setId(id==null?DOM.createUniqueId():id);
		getElement().setClassName("ui-layout-" + renderPosition() + (useBorder ? " ui-widget-content" : ""));
	}

	String renderPosition() {
		switch (position){
			case TOP:return "north";
			case LEFT:return "west";
			case BOTTOM:return "south";
			case RIGHT:return "east";
		}
		return "center";
	}

	public void renderOverflow(final JavaScriptObject layout) {
		if (allowOverflow)
			_renderOverflow(layout, renderPosition());
	}

	private native void _renderOverflow(JavaScriptObject layout, String position) /*-{
		layout.allowOverflow(position);
	}-*/;
	
}