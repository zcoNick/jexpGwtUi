package com.javexpress.gwt.library.ui.bootstrap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.ClientContext;
import com.javexpress.gwt.library.ui.ICssIcon;

public class Bootstrap {

	public static enum HeadingSize {
		h1, h2, h3, h4, h5, h6
	}

	public static enum WSize {
		small, normal, large
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
		Info("info"), Danger("danger"), Warning(
				"warning"), Light("light"), Yellow("yellow"), Pink("pink"),White("white");
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

	public static Element createIconAnchor(Element parent, String anchorClass, String iconClass) {
		Element a = DOM.createAnchor();
		a.setClassName(anchorClass);
		Element i = DOM.createElement("i");
		i.setClassName(iconClass);
		a.appendChild(i);
		if (parent!=null)
			parent.appendChild(a);
		return a;
	}

	public static Element createIconTextAnchor(String anchorClass, ICssIcon iconClass, String text, boolean rightIcon) {
		Element a = DOM.createAnchor();
		a.setClassName(anchorClass);
		Element i = DOM.createElement("i");
		ClientContext.resourceInjector.applyIconStyles(i, iconClass);
		if (text!=null){
			Element span = DOM.createSpan();
			span.setInnerText(text);
			if (rightIcon) {
				a.appendChild(span);
				a.appendChild(i);
			} else {
				a.appendChild(i);
				a.appendChild(span);
			}
		}
		return a;
	}

	public static Element createIconTextAnchor(String anchorClass, ICssIcon iconClass, String text) {
		return createIconTextAnchor(anchorClass, iconClass, text, false);
	}

	public static void setTooltip(Element elm, String tooltip) {
		setTooltip(elm, tooltip, 2500);
	}

	public static native void setTooltip(Element elm, String tooltip, int duration) /*-{
		if (!tooltip) {
			$wnd.$(elm).tooltip('destroy');
			return;
		}
		var options = {
			placement : "auto right",
			title : tooltip,
			trigger : "manual"
		};
		$wnd.$(elm).tooltip(options).on("shown.bs.tooltip", function() {
			$wnd.setTimeout(function() {
				$wnd.$(elm).tooltip('destroy');
			}, duration);
		}).tooltip('show');
	}-*/;

	public static Element createHeading(HeadingSize headingSize) {
		switch (headingSize) {
			case h1:
				return DOM.createElement("h1");
			case h2:
				return DOM.createElement("h2");
			case h3:
				return DOM.createElement("h3");
			case h4:
				return DOM.createElement("h4");
			case h5:
				return DOM.createElement("h5");
			case h6:
				return DOM.createElement("h6");
		}
		return null;
	}
	
}