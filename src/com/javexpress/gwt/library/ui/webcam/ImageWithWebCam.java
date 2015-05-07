package com.javexpress.gwt.library.ui.webcam;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.data.image.Image;
import com.javexpress.gwt.library.ui.dialog.JexpPopupPanel;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class ImageWithWebCam extends JexpSimplePanel {

	private Image	image;
	private boolean	readOnly	= false;
	private int		width		= 200, height = 200;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/** Designer compatible constructor */
	public ImageWithWebCam(final Widget parent, final String id) {
		super();
		JsUtil.ensureId(parent, this, WidgetConst.IMAGEWITHWEBCAM_PREFIX, id);
		addStyleName("jexpBorderBox");
		getElement().getStyle().setPadding(0, Unit.PX);
		getElement().getStyle().setMargin(0, Unit.PX);
		setWidth(width + "px");
		setHeight((height + 3) + "px");

		add(image = new Image(this, id + "img", true));
		image.getElement().setTitle("Yeni Resim için çift tıklayın.\nTemizlemek için Ctrl tuşu basılıyken çift tıklayın.");
		image.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if (readOnly)
					return;
				if (event.isControlKeyDown()) {
					if (JsUtil.confirm(ClientContext.nlsCommon.resimSilmeOnay()))
						image.setValue(null);
				} else {
					final JexpPopupPanel popPanel = new JexpPopupPanel(true, true);
					popPanel.getElement().getStyle().setZIndex(9999);
					final WebCam wc = new WebCam(image, "webcam");
					popPanel.addCloseHandler(new CloseHandler<PopupPanel>() {
						@Override
						public void onClose(CloseEvent<PopupPanel> event) {
							event.getTarget().removeFromParent();
						}
					});
					wc.setListener(new IWebCamListener() {
						@Override
						public void onImage(String dataUrl) {
							setValue(dataUrl);
							popPanel.hide(true);
						}
					});
					wc.setWidth(Math.max(width, 200) + "px");
					wc.setHeight(Math.max(height, 200) + "px");
					popPanel.add(wc);
					popPanel.setPopupPosition(image.getAbsoluteLeft(), image.getAbsoluteTop());
					popPanel.show();
				}
			}
		});
	}

	public String getValue() {
		return image.getValue();
	}

	public void setValue(String value) {
		image.setValue(value);
	}

}