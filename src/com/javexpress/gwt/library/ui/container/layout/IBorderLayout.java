package com.javexpress.gwt.library.ui.container.layout;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;

public interface IBorderLayout extends ISizeAwareWidget,HasWidgets {
	
	public static enum Position {
		TOP, BOTTOM, CENTER, LEFT, RIGHT;
	}
	
	Widget getTopWidget();
	void setTopWidget(final Widget widget);
	
	Widget getLeftWidget();
	void setLeftWidget(final Widget widget);
	void setLeftWidget(final Widget widget, String width);
	
	Widget getCenterWidget();
	void setCenterWidget(final Widget widget);

	Widget getBottomWidget();
	void setBottomWidget(final Widget widget);

	int getWidgetCount();

	Widget getWidget(int i);

	boolean isLayoutFilled();

}