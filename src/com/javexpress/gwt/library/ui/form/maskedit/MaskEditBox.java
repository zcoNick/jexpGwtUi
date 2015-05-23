package com.javexpress.gwt.library.ui.form.maskedit;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.bootstrap.LabelControlCell;
import com.javexpress.gwt.library.ui.data.DataBindingHandler;
import com.javexpress.gwt.library.ui.form.IUserInputWidget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.js.WidgetBundles;

public class MaskEditBox extends TextBoxBase implements IUserInputWidget<String> {

	public static void fillResources(WidgetBundles wb) {
		wb.addJavaScript("scripts/inputmask/jquery.inputmask.bundle-3.1.62.min.js");//http://jsfiddle.net/6nNJs/4/
	}

	private boolean				required;
	private String				mask;
	protected JsonMap			options;
	private DataBindingHandler	dataBinding;

	@Override
	public boolean isRequired() {
		return required;
	}

	@Override
	public void setRequired(final boolean required) {
		this.required = required;
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

	public MaskEditBox(final Widget parent, final String id, String mask) {
		super(DOM.createInputText());
		JsUtil.ensureId(parent, this, WidgetConst.MASKEDITBOX_PREFIX, id);
		setStylePrimaryName("gwt-TextBox");
		options = createDefaultOptions();
		setMask(mask);
	}

	private JsonMap createDefaultOptions() {
		options = new JsonMap();
		options.set("placeholder", "_");
		return options;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		createByJs(this, getElement(), mask, options.getJavaScriptObject());
	}

	private native void createByJs(MaskEditBox x, Element element, String mask, JavaScriptObject options) /*-{
																											$wnd.$(element).inputmask(mask, options);
																											}-*/;

	@Override
	protected void onUnload() {
		options = null;
		dataBinding = null;
		destroyByJs(getElement());
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

}