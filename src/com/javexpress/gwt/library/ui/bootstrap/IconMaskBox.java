package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class IconMaskBox extends IconTextBox {

	private String		mask;
	protected JsonMap	options;

	public IconMaskBox(Widget parent, String id, ICssIcon icon, String mask) {
		super(parent, id, icon);
		JsUtil.ensureId(parent, this, WidgetConst.MASKEDITBOX_PREFIX, id);
		options = createDefaultOptions();
		setMask(mask);
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getMask() {
		return this.mask;
	}

	public void setClearIncomplete(boolean clearIncomplete) {
		options.set("clearIncomplete", clearIncomplete);
	}

	public void setPlaceHolder(String placeHolder) {
		options.set("placeholder", placeHolder);
	}

	public void setRepeat(int repeat) {
		options.setInt("repeat", repeat);
	}

	public void setGreedy(boolean greedy) {
		options.set("greedy", greedy);
	}

	public void setNumericInput(boolean numericInput) {
		options.set("numericInput", numericInput);
	}

	public void setRadixPoint(String radixPoint) {
		options.set("radixPoint", radixPoint);
	}

	public void setSkipRadixDance(boolean skipRadixDance) {
		options.set("skipRadixDance", skipRadixDance);
	}

	public void setRightAlignNumerics(boolean rightAlignNumerics) {
		options.set("rightAlignNumerics", rightAlignNumerics);
	}

	public void setShowMaskOnFocus(boolean showMaskOnFocus) {
		options.set("showMaskOnFocus", showMaskOnFocus);
	}

	public void setShowMaskOnHover(boolean showMaskOnHover) {
		options.set("showMaskOnHover", showMaskOnHover);
	}

	private JsonMap createDefaultOptions() {
		options = new JsonMap();
		options.set("placeholder", "_");
		return options;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getInputElement(), mask, options.getJavaScriptObject());
	}

	private native void createByJs(IconMaskBox x, Element element, String mask, JavaScriptObject options) /*-{
		$wnd.$(element).inputmask(mask, options);
	}-*/;

	@Override
	protected void onUnload() {
		options = null;
		destroyByJs(getInputElement());
		super.onUnload();
	}

	private native void destroyByJs(Element element) /*-{
		$wnd.$(element).inputmask('remove');
	}-*/;

	@Override
	public boolean validate(final boolean focusedBefore) {
		return (!isRequired() || _isCompleted(getElement())) && JsUtil.validateWidget(this, focusedBefore);
	}

	private native boolean _isCompleted(Element element) /*-{
		return $wnd.$(element).inputmask("isComplete");
	}-*/;

	@Override
	public String getValue() {
		String s = super.getValue();
		return JsUtil.isEmpty(s) ? null : s;
	}

}