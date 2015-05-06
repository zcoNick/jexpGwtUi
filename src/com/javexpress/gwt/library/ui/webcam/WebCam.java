package com.javexpress.gwt.library.ui.webcam;

import java.beans.Beans;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class WebCam extends JexpSimplePanel {

	public static void fillResources(final WidgetBundles wb) {
		wb.addJavaScript("scripts/photobooth/photobooth-0.7.min.js");
	}

	private String	value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private IWebCamListener	listener;

	public IWebCamListener getListener() {
		return listener;
	}

	public void setListener(final IWebCamListener listener) {
		this.listener = listener;
	}

	public WebCam(final Widget parent, final String id) {
		this(parent, id, null);
	}

	public WebCam(final Widget parent, final String id, final IWebCamListener listener) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.WEBCAM_PREFIX, id);
		addStyleName("jexpBorderBox");
		setListener(listener);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!Beans.isDesignTime())
			createByJs(this, getElement(), JsUtil.isBrowserFF());
	}

	private native void createByJs(WebCam x, Element el, boolean firefox) /*-{
		var pb = $wnd.$(el).photobooth();
		pb
				.on(
						"image",
						function(event, dataUrl) {
							x.@com.javexpress.gwt.library.ui.webcam.WebCam::fireOnImage(Ljava/lang/String;)(dataUrl);
						});
	}-*/;

	@Override
	protected void onUnload() {
		listener = null;
		if (!Beans.isDesignTime())
			destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element el) /*-{
		$wnd.$(el).data("photobooth").destroy();
	}-*/;

	//--EVENTS

	private void fireOnImage(final String dataUrl) {
		value = dataUrl;
		if (listener != null)
			listener.onImage(value);
	}

}