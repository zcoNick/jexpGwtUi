package com.javexpress.gwt.library.ui.form.button;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class TextButton extends Anchor {
	
	private ICssIcon icon;

	public TextButton(final Widget parent, final String id) {
		this(parent, id, null);
	}
	
	/** Designer compatible constructor */
	public TextButton(final Widget parent, final String id, final String text) {
		super(text);
		JsUtil.ensureId(parent, this, WidgetConst.TEXTBUTTON_PREFIX, id);
		setTitle(text);
		setStyleName("jexpTextButton");
	}
	
	public ICssIcon getIcon() {
		return icon;
	}

	public void setIcon(ICssIcon icon) {
		this.icon = icon;
	}

	@Override
	protected void onLoad() {
		if (icon!=null){
			Element i = DOM.createSpan();
			i.setClassName(icon.getCssClass());
			getElement().insertFirst(i);
		}
		super.onLoad();
	}

}