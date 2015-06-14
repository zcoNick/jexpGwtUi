package com.javexpress.gwt.library.ui.form.radiogroup;

import java.io.Serializable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.LabelControlCell;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class OptionSet extends JexpSimplePanel implements IUserInputWidget<String> {

	private boolean				required;
	private DataBindingHandler	dataBinding;

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(final boolean required) {
		this.required = required;
	}

	public OptionSet(final Widget parent, final String id) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.OPTIONSET_PREFIX, id);
	}

	public void addItem(final Serializable label, final Serializable value) {
		Element ei = DOM.createInputRadio(getElement().getId());
		if (value != null)
			ei.setAttribute("value", value.toString());
		String eiId = JsUtil.ensureSubId(getElement(), ei, String.valueOf(getElement().getChildCount()));
		getElement().appendChild(ei);

		Element el = DOM.createLabel();
		el.setAttribute("for", eiId);
		el.setInnerHTML(label.toString());
		getElement().appendChild(el);

		if (isAttached())
			_refresh(getElement());
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement());
	}

	private native void createByJs(OptionSet x, Element element) /*-{
		$wnd.$(element).buttonset();
	}-*/;

	@Override
	protected void onUnload() {
		dataBinding = null;
		destroyByJs(getElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).buttonset('destroy');
	}-*/;

	@Override
	public int getTabIndex() {
		return getElement().getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
	}

	@Override
	public void setFocus(boolean focused) {
	}

	@Override
	public void setTabIndex(int index) {
		getElement().setTabIndex(index);
	}

	@Override
	public boolean validate(boolean focusedBefore) {
		return JsUtil.validateWidget(this, focusedBefore);
	}

	@Override
	public String getValue() {
		NodeList<Node> nodes = getElement().getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.getItem(i);
			if (node.getNodeName().equals("INPUT")) {
				Element el = Element.as(node).cast();
				return el.getAttribute("value");
			}
		}
		return null;
	}

	public void setValue(Serializable value) {
		String v = value != null ? value.toString() : null;
		NodeList<Node> nodes = getElement().getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.getItem(i);
			if (node.getNodeName().equals("INPUT")) {
				Element el = Element.as(node).cast();
				if (el.getAttribute("value").equals(v))
					el.setAttribute("checked", "checked");
				else
					el.removeAttribute("checked");
			}
		}
		if (isAttached())
			_refresh(getElement());
	}

	private native void _refresh(Element element) /*-{
		$wnd.$(element).buttonset('refresh');
	}-*/;

	@Override
	public void setEnabled(boolean locked) {
	}

	@Override
	public void setValidationError(String validationError) {
		if (JsUtil.USE_BOOTSTRAP) {
			Widget nw = getParent() instanceof LabelControlCell ? getParent() : this;
			if (validationError == null)
				nw.removeStyleName("has-error");
			else
				nw.addStyleName("has-error");
		}
		setTitle(validationError);
	}

	@Override
	public void setDataBindingHandler(DataBindingHandler handler) {
		this.dataBinding = handler;
		dataBinding.setControl(this);
	}

	@Override
	public DataBindingHandler getDataBindingHandler() {
		return dataBinding;
	}

	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
	}

}