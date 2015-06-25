package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.ICssIcon;

public class Bootstrap {

	public static enum WSize {
		Small, Medium, Large
	}

	public static enum WPull {
		Right("pull-right");
		private String	value;

		public String getValue() {
			return value;
		}

		private WPull(String pv) {
			value = pv;
		}
	}

	public static enum WContext {
		Default("default"), Primary("primary"), Grey("grey"), Purple("purple"), Orange(
				"orange"),
		Important("important"), Green("green"), Success("success"), light_blue(
				"light-blue"),
		Info("light-blue"), Danger("danger"), Inverse("inverse"), Warning(
				"warning"), Light("light"), Yellow("yellow"), Pink("pink");
		private String	value;

		public String getValue() {
			return value;
		}

		private WContext(String pv) {
			value = pv;
		}
	}

	public static Element createSpace(int size) {
		Element el = DOM.createDiv();
		el.setClassName("space" + (size > -1 ? "-" + size : ""));
		return el;
	}

	public static Node createSpacer() {
		return createSpace(-1);
	}

	public static Element createBlockLabel() {
		Element el = DOM.createLabel();
		el.setClassName("block clearfix");
		return el;
	}

	public static Element createInlineLabel() {
		Element el = DOM.createLabel();
		el.setClassName("inline");
		return el;
	}

	public static Element createIconText(String textBoxIcon, String placeHolder) {
		Element span = DOM.createSpan();
		span.setClassName("block input-icon input-icon-right");
		Element input = DOM.createInputText();
		input.setClassName("form-control");
		if (placeHolder != null)
			input.setAttribute("placeholder", placeHolder);
		span.appendChild(input);
		Element i = DOM.createElement("i");
		i.setClassName("ace-icon fa " + textBoxIcon);
		span.appendChild(i);
		return span;
	}

	public static Element createIconPassword(String textBoxIcon, String placeHolder) {
		Element span = DOM.createSpan();
		span.setClassName("block input-icon input-icon-right");
		Element input = DOM.createInputPassword();
		input.setClassName("form-control");
		if (placeHolder != null)
			input.setAttribute("placeholder", placeHolder);
		span.appendChild(input);
		Element i = DOM.createElement("i");
		i.setClassName("ace-icon fa " + textBoxIcon);
		span.appendChild(i);
		return span;
	}

	public static Element[] createCheckbox(String text) {
		Element ch = DOM.createInputCheck();
		ch.setClassName("ace");
		Element span = DOM.createSpan();
		span.setClassName("lbl");
		span.setInnerText(text);
		return new Element[] { ch, span };
	}

	public static Element createButton(int size, WPull wpull, WSize wsize, WContext wcontext, String icon, String textClass, String text) {
		Element button = DOM.createButton();
		return makeButton(button, size, wpull, wsize, wcontext, icon, textClass, text);
	}

	public static Element makeButton(Element button, Integer size, WPull wpull, WSize wsize, WContext wcontext, String icon, String textClass, String text) {
		String clazz = "";
		if (size != null)
			clazz = "width-" + size;
		if (wpull != null)
			clazz += " " + wpull.value;
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
			}
		}
		button.setClassName(clazz);
		Element i = DOM.createElement("i");
		i.setClassName("ace-icon " + icon);
		button.appendChild(i);
		Element span = DOM.createSpan();
		span.setClassName(textClass);
		span.setInnerText(text);
		button.appendChild(span);
		return button;
	}

	public static Element createIconTextAnchor(String anchorClass, ICssIcon iconClass, String text, boolean rightIcon) {
		Element a = DOM.createAnchor();
		a.setClassName(anchorClass);
		Element i = DOM.createElement("i");
		i.setClassName("ace-icon " + iconClass.getCssClass());
		Element span = DOM.createSpan();
		span.setInnerText(text);
		if (rightIcon) {
			a.appendChild(span);
			a.appendChild(i);
		} else {
			a.appendChild(i);
			a.appendChild(span);
		}
		return a;
	}

	public static Element createIconTextAnchor(String anchorClass, ICssIcon iconClass, String text) {
		return createIconTextAnchor(anchorClass, iconClass, text, false);
	}

	public static void setTooltip(Element elm, String tooltip) {
		setTooltip(elm, tooltip, 3500);
	}

	public static native void setTooltip(Element elm, String tooltip, int duration) /*-{
		var options = {
			placement : "auto bottom",
			title : tooltip,
			//container : "#" + elm,
			//viewport : "#" + elm,
			trigger : "manual"
		};
		$wnd.$(elm).tooltip(options).on("hidden.bs.tooltip", function() {
			setTimeout(function() {
				$wnd.$(elm).tooltip('destroy');
			}, duration);
		}).tooltip('show');
	}-*/;

}