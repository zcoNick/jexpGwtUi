package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WContext;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WPull;
import com.javexpress.gwt.library.ui.bootstrap.Bootstrap.WSize;
import com.javexpress.gwt.library.ui.form.ICallbackAware;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class Button extends ButtonBase implements ICallbackAware {

	private Integer		size;
	private String		text;
	private WPull		wpull;
	private WSize		wsize;
	private WContext	wcontext;
	private String		iconClass;
	private String		textClass;
	private boolean		highlight;
	private Element		iconSpan;
	private Element		textSpan;
	private boolean		disabled	= false;
	private Integer		tag;

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
		if (isAttached())
			textSpan.setInnerText(text);
	}

	public WPull getWpull() {
		return wpull;
	}

	public void setWpull(WPull wpull) {
		this.wpull = wpull;
	}

	public WSize getWsize() {
		return wsize;
	}

	public void setWsize(WSize wsize) {
		this.wsize = wsize;
	}

	public WContext getWcontext() {
		return wcontext;
	}

	public void setWcontext(WContext wcontext) {
		this.wcontext = wcontext;
		if (isAttached())
			updateStyleContext();
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIcon(ICssIcon icon) {
		this.iconClass = icon.getCssClass();
		if (isAttached())
			iconSpan.setClassName("ace-icon " + iconClass);
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public String getTextClass() {
		return textClass;
	}

	public void setTextClass(String textClass) {
		this.textClass = textClass;
	}

	public Button(Widget parent, String id) {
		this(parent, id, null);
	}

	public Button(Widget parent, String id, String text) {
		super(Document.get().createPushButtonElement());
		JsUtil.ensureId(parent, this, WidgetConst.BUTTON_PREFIX, id);
		this.text = text;
	}

	@Override
	protected void onLoad() {
		updateStyleContext();
		iconSpan = DOM.createElement("i");
		iconSpan.setClassName("ace-icon " + iconClass);
		getElement().appendChild(iconSpan);
		textSpan = DOM.createSpan();
		textSpan.setClassName(textClass);
		textSpan.setInnerText(text);
		getElement().appendChild(textSpan);
		super.onLoad();
	}

	private void updateStyleContext() {
		String clazz = "";
		if (size != null)
			clazz = "width-" + size;
		if (wpull != null)
			clazz += " " + wpull.getValue();
		clazz += " btn";
		if (wsize != null) {
			switch (wsize) {
				case Small:
					clazz += " btn-sm";
					break;
			}
		}
		if (wcontext != null) {
			switch (wcontext) {
				case Primary:
					clazz += " btn-primary";
					break;
				case Success:
					clazz += " btn-success";
					break;
				case Purple:
					clazz += " btn-purple";
					break;
				case Pink:
					clazz += " btn-pink";
					break;
				case Info:
					clazz += " btn-info";
					break;
				case Inverse:
					clazz += " btn-inverse";
					break;
				case Grey:
					clazz += " btn-grey";
					break;
				case Warning:
					clazz += " btn-warning";
					break;
				case Light:
					clazz += " btn-light";
					break;
				case Yellow:
					clazz += " btn-yellow";
					break;
				case Danger:
					clazz += " btn-danger";
					break;
				default:
					if (highlight)
						clazz += " btn-primary";
					break;
			}
		} else if (highlight)
			clazz += " btn-primary";
		getElement().setClassName(clazz);
	}

	public void click() {
		if (!disabled)
			((ButtonElement) getElement().cast()).click();
	}

	@Override
	public void callbackStarted() {
		if (iconSpan != null)
			iconSpan.addClassName("fa-spin");
		textSpan.setInnerHTML("...");
		disabled = true;
		getElement().setAttribute("disabled", "true");
	}

	@Override
	public void callbackCompleted() {
		if (isAttached()) {
			if (iconSpan != null)
				iconSpan.removeClassName("fa-spin");
			textSpan.setInnerHTML(text);
			disabled = false;
			getElement().removeAttribute("disabled");
		}
	}

	@Override
	public String getId() {
		return getElement().getId();
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
		if (isAttached())
			updateStyleContext();
	}

	@Override
	protected void onUnload() {
		size = null;
		text = null;
		wpull = null;
		wsize = null;
		wcontext = null;
		iconClass = null;
		textClass = null;
		iconSpan = null;
		tag = null;
		textSpan = null;
		super.onUnload();
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public Integer getTag() {
		return tag;
	}

}