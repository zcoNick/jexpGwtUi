package com.javexpress.gwt.library.ui.data.image;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.common.model.item.type.Pair;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class Image extends JexpSimplePanel {

	private String					url;
	private Pair<String, String>	imageScaledTo;
	private boolean					useViewPort	= true;

	public boolean isUseViewPort() {
		return useViewPort;
	}

	public void setUseViewPort(boolean useViewPort) {
		this.useViewPort = useViewPort;
	}

	public Pair<String, String> getImageScaledTo() {
		return imageScaledTo;
	}

	public void setImageScaledTo(final Pair<String, String> imageScaledTo) {
		this.imageScaledTo = imageScaledTo;
	}

	public void setImageScaledTo(final String width, final String height) {
		this.imageScaledTo = new Pair<String, String>(width, height);
	}

	public Image(final Widget parent, String id, final boolean fitToParent) {
		super();
		if (fitToParent) {
			setWidth("100%");
			setHeight("100%");
			setStyleName("ui-widget ui-widget-content ui-corner-all");
		}
		JsUtil.ensureId(parent, this, WidgetConst.IMAGE_PREFIX, id);
		getElement().getStyle().setOverflow(Overflow.AUTO);
		getElement().getStyle().setPadding(0, Unit.PX);
		getElement().getStyle().setMargin(0, Unit.PX);
	}

	public void load(final ServiceDefTarget service, final Enum method, final Serializable key) {
		load(service, method, key, true);
	}

	public void load(final ServiceDefTarget service, final Enum method, final Serializable key, final boolean disableCaching) {
		if (service != null)
			url = service.getServiceEntryPoint() + "." + method.toString();
		if (isAttached())
			setWidget(new Label(ClientContext.nlsCommon.yukleniyor()));
		String u = url + "?key=" + key.toString() + (disableCaching ? "&tm=" + new Date().getTime() : "");
		setImage(u);
	}

	private void setImage(String u) {
		if (getWidget() != null)
			getWidget().removeFromParent();
		if (u == null)
			return;
		final com.google.gwt.user.client.ui.Image image = new com.google.gwt.user.client.ui.Image(u);
		if (!useViewPort) {
			image.setWidth("100%");
			image.setHeight("98.4%");
		}
		if (imageScaledTo != null) {
			image.addLoadHandler(new LoadHandler() {
				@Override
				public void onLoad(final LoadEvent event) {
					image.setWidth(imageScaledTo.getLeft());
					image.setHeight(imageScaledTo.getRight());
				}
			});
		}
		setWidget(image);
	}

	@Override
	public String getId() {
		return getElement().getId();
	}

	public HandlerRegistration addClickHandler(final ClickHandler handler) {
		getElement().addClassName("ui-cursor-hand");
		return addDomHandler(handler, ClickEvent.getType());
	}

	public HandlerRegistration addDoubleClickHandler(final DoubleClickHandler handler) {
		getElement().addClassName("ui-cursor-hand");
		return addDomHandler(handler, DoubleClickEvent.getType());
	}

	public void setValue(String value) {
		setImage(value);
	}

	public String getValue() {
		if (getWidget() == null)
			return null;
		String s = getWidget().getElement().getAttribute("src");
		return JsUtil.isNotEmpty(s) ? s : null;
	}

	public void setImagePath(String file) {
		setValue(GWT.getModuleBaseForStaticFiles() + "images/" + file);
	}

}