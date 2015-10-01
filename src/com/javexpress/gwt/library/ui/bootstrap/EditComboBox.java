package com.javexpress.gwt.library.ui.bootstrap;

import java.io.Serializable;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.form.combobox.IItemsChangeHandler;
import com.javexpress.gwt.library.ui.form.combobox.IKeyValueList;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class EditComboBox extends BaseWrappedInput<String>implements IKeyValueList {

	private Element				btChevron;
	private Element				ul;
	private IItemsChangeHandler	itemsChangeHandler;

	public IItemsChangeHandler getItemsChangeHandler() {
		return itemsChangeHandler;
	}

	public void setItemsChangeHandler(IItemsChangeHandler itemsChangeHandler) {
		this.itemsChangeHandler = itemsChangeHandler;
	}

	public EditComboBox(final Widget parent, final String id) {
		super(parent, WidgetConst.EDITCOMBOBOX_PREFIX, id, "jexpEditComboBox");

		input = DOM.createInputText();
		JsUtil.ensureSubId(getElement(), input, "inp");
		input.addClassName("form-control");
		getElement().appendChild(input);

		btChevron = DOM.createSpan();
		btChevron.setClassName("input-group-addon jexpHandCursor");
		btChevron.setAttribute("data-toggle", "dropdown");
		btChevron.setInnerHTML("<span class='caret'></span>");
		getElement().appendChild(btChevron);

		ul = DOM.createElement("ul");
		ul.addClassName("dropdown-menu");
		ul.getStyle().setProperty("maxHeight", "150px");
		ul.getStyle().setOverflow(Overflow.AUTO);
		getElement().appendChild(ul);
	}

	private boolean dropUp = false;

	public boolean isDropUp() {
		return dropUp;
	}

	public void setDropUp(boolean dropUp) {
		this.dropUp = dropUp;
	}

	@Override
	protected void onLoad() {
		if (isDropUp())
			getElement().addClassName("dropup");
		else
			getElement().addClassName("dropdown");
		super.onLoad();
		createByJs(this, ul);
	}

	private native void createByJs(EditComboBox x, Element element) /*-{
																	var el = $wnd.$("a.jexpLink", element).click(function(e) {
																	x.@com.javexpress.gwt.library.ui.bootstrap.EditComboBox::fireSelection(Ljava/lang/String;)($wnd.$(this).attr("v"));
																	});
																	}-*/;

	private void fireSelection(String value) {
		setValue(value, true);
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$("a.jexpLink", element).off();
	}-*/;

	@Override
	protected void onUnload() {
		btChevron = null;
		destroyByJs(getElement());
		super.onUnload();
	}

	public void addItem(Serializable label, Serializable value, Serializable data) {
		Element li = DOM.createElement("li");
		Element anchor = DOM.createAnchor();
		anchor.setAttribute("href", "#");
		anchor.setInnerHTML(label.toString());
		anchor.setClassName("jexpLink");
		anchor.setAttribute("v", value.toString());
		if (data != null)
			anchor.setAttribute("d", data.toString());
		li.appendChild(anchor);
		ul.appendChild(li);
	}

	@Override
	public String getValue() {
		return input.getAttribute("v");
	}

	@Override
	protected boolean setRawValue(String value) {
		for (int i = 0; i < ul.getChildCount(); i++) {
			Element anchor = (Element) ul.getChild(i).getFirstChild();
			if (anchor.getAttribute("v").equals(value)) {
				input.setAttribute("v", value);
				((InputElement) input).setValue(anchor.getInnerHTML());
				return true;
			}
		}
		input.setAttribute("v", "");
		((InputElement) input).setValue("");
		return false;
	}

	public String getText() {
		return ((InputElement) input).getValue();
	}

	public void setText(String text) {
		((InputElement) input).setValue(text);
	}

	public void setMaxLength(int maxlength) {
		((InputElement) input).setMaxLength(maxlength);
	}

	public void removeItems() {
		JsUtil.removeJQEvents(ul);
		JsUtil.clearChilds(ul);
	}

	@Override
	public void setKeyValueDataItems(JSONObject itm) {
		if (itm != null) {
			for (String lb : itm.keySet()) {
				JSONArray arr = itm.get(lb).isArray();//0-Value,1-Data,2-Path
				String data = JsUtil.asString(arr.get(1));
				String path = JsUtil.asString(arr.get(2));
				if (path != null) {
					addItem(JsUtil.repeat("-", (path.split("\\.").length - 1) * 2) + lb, JsUtil.asString(arr.get(0)), data);
				} else
					addItem(lb, JsUtil.asString(arr.get(0)), data);
			}
		}
		onItemListChanged();
	}

	public <T extends EditComboBox> T setItems(final Map<? extends Serializable, ? extends Serializable> map, ConstantsWithLookup nls) {
		removeItems();
		if (map == null)
			return (T) this;
		for (Serializable key : map.keySet()) {
			if (nls == null)
				addItem(map.get(key), key, null);
			else {
				String constant = map.get(key).toString();
				if (constant.startsWith("@")) {
					try {
						String nlsValue = nls.getString(constant.substring(1));
						addItem(nlsValue, key, null);
					} catch (Exception ex) {
						try {
							String nlsValue = ClientContext.nlsCommon.getString(constant.substring(1));
							addItem(nlsValue, key, null);
						} catch (Exception ex1) {
							addItem(constant, key, null);
						}
					}
				} else
					addItem(constant, key, null);
			}
		}
		onItemListChanged();
		return (T) this;
	}

	protected void onItemListChanged() {
		createByJs(this, ul);
		if (itemsChangeHandler != null)
			itemsChangeHandler.onItemsChanged();
	}

}