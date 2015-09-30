package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ICssIcon;

public class IconTextBox extends BaseWrappedInput<String> {

	private ICssIcon	icon;
	private boolean		rightIcon;

	public ICssIcon getIcon() {
		return icon;
	}

	public void setIcon(ICssIcon icon) {
		this.icon = icon;
	}

	public boolean isRightIcon() {
		return rightIcon;
	}

	public void setRightIcon(boolean rightIcon) {
		this.rightIcon = rightIcon;
	}

	public IconTextBox(Widget parent, String id, ICssIcon icon) {
		super(parent, WidgetConst.TEXTBOX_PREFIX, id, null);
		getElement().setClassName("input-group");

		input = createInputText();
		getElement().appendChild(input);

		this.icon = icon;
	}

	@Override
	protected void doAttachChildren() {
		if (icon != null) {
			Element span = DOM.createSpan();
			span.setClassName("input-group-addon");
			span.setInnerHTML("<i class='ace-icon " + icon.getCssClass() + "'></i>");
			if (rightIcon)
				getElement().appendChild(span);
			else
				getElement().insertFirst(span);
		}
		super.doAttachChildren();
	}

	protected Element createInputText() {
		return ((InputElement) DOM.createInputText().cast());
	}

	@Override
	public String getValue() {
		return ((InputElement) input).getValue();
	}

	public void setMaxLength(int maxlength) {
		getElement().setPropertyInt("maxlength", maxlength);
	}

	@Override
	protected void setRawValue(String value) {
		((InputElement) input).setValue(value);
	}

}