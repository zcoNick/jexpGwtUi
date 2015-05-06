package com.javexpress.gwt.library.ui.container.catalogview;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.ICssIcon;
import com.javexpress.gwt.library.ui.container.panel.JexpSimplePanel;
import com.javexpress.gwt.library.ui.js.JsUtil;

@Deprecated
public class CatalogView extends JexpSimplePanel {

	private Element			left, imageContainer, catalogs;
	private Element			viewEl;
	private ICatalogHandler	handler;
	private Widget			view;

	public CatalogView(final Widget parent, final String id) {
		super(DOM.createDiv());
		JsUtil.ensureId(parent, this, WidgetConst.CATALOGVIEW_PREFIX, id);
		getElement().setClassName("ui-helper-reset ui-helper-clearfix ui-widget ui-widget-content jexpBorderBox jesCatalogView");
		getElement().getStyle().setPosition(Position.RELATIVE);
		getElement().getStyle().setDisplay(Display.BLOCK);

		left = DOM.createDiv();
		left.getStyle().setPosition(Position.ABSOLUTE);
		left.getStyle().setDisplay(Display.INLINE_BLOCK);
		left.getStyle().setTop(0, Unit.PX);
		left.getStyle().setLeft(0, Unit.PX);
		left.getStyle().setWidth(17, Unit.PCT);
		left.setClassName("leftBlock");
		//position:absolute;display: inline-block; top:0px;left:0px;width:17%;

		Element table = DOM.createTable();
		table.getStyle().setWidth(100, Unit.PCT);
		table.getStyle().setHeight(100, Unit.PCT);
		table.setAttribute("cellpadding", "0");
		table.setAttribute("cellspacing", "0");

		Element row = DOM.createTR();
		row.setAttribute("height", "5%");
		row.setAttribute("valign", "middle");
		imageContainer = DOM.createTD();
		imageContainer.setAttribute("align", "center");
		row.appendChild(imageContainer);
		table.appendChild(row);

		row = DOM.createTR();
		row.setAttribute("height", "93%");
		row.setAttribute("valign", "top");
		Element td = DOM.createTD();
		catalogs = DOM.createTable();
		catalogs.setAttribute("cellpadding", "0");
		catalogs.setAttribute("cellspacing", "0");
		catalogs.getStyle().setWidth(100, Unit.PCT);
		catalogs.setClassName("ui-widget-content ui-corner-left jexpBorderBox catalogContainer");
		td.appendChild(catalogs);
		row.appendChild(td);
		table.appendChild(row);
		left.appendChild(table);
		getElement().appendChild(left);

		viewEl = DOM.createDiv();
		viewEl.setClassName("ui-widget-content ui-corner-all jexpBorderBox");
		viewEl.getStyle().setPosition(Position.ABSOLUTE);
		viewEl.getStyle().setDisplay(Display.BLOCK);
		viewEl.getStyle().setTop(0, Unit.PCT);
		viewEl.getStyle().setBottom(0, Unit.PCT);
		viewEl.getStyle().setRight(0, Unit.PX);
		viewEl.getStyle().setLeft(17, Unit.PCT);
		viewEl.getStyle().setOverflow(Overflow.AUTO);
		//position:absolute;display:inline-block;left:15%;top:0px;right:1px;bottom:0px;overflow: auto;
		getElement().appendChild(viewEl);
	}

	public void setViewerOverflow(Overflow overflow) {
		viewEl.getStyle().setOverflow(overflow);
	}

	public void setCatalogWidthPct(final double w) {
		left.getStyle().setWidth(w, Unit.PCT);
		viewEl.getStyle().setLeft(w, Unit.PCT);
	}

	public Element getViewContainer() {
		return viewEl;
	}

	public Element getImageContainer() {
		return imageContainer;
	}

	private String	current;

	public void addCatalog(final ICssIcon icon, final String title, final String code) {
		Element tr = DOM.createTR();
		tr.setId(getElement().getId() + "_t" + catalogs.getChildCount());
		tr.setAttribute("catalog", code);
		tr.setAttribute("valign", "middle");
		tr.setClassName("catalog ui-corner-left ui-corner-bottom ui-state-default");

		Element td = DOM.createTD();
		td.setAttribute("width", "1em");
		td.getStyle().setPadding(5, Unit.PX);
		Element el = DOM.createSpan();
		el.setClassName("ui-icon " + icon.getCssClass());
		el.setId(getElement().getId() + "_i" + catalogs.getChildCount());
		td.appendChild(el);
		tr.appendChild(td);

		td = DOM.createTD();
		el = DOM.createSpan();
		el.setId(getElement().getId() + "_l" + catalogs.getChildCount());
		el.setInnerText(title);
		td.appendChild(el);
		tr.appendChild(td);

		catalogs.appendChild(tr);
		if (current == null)
			current = code;
		if (isAttached())
			_bindEvents(this, tr, code);
	}

	public String getSelected() {
		return view == null ? null : current;
	}

	private native void _bindEvents(CatalogView x, Element element, String catalog) /*-{
		$wnd
				.$(element)
				.click(
						function() {
							var r = $wnd.$(this);
							$wnd.$("tr", r.parent()).removeClass(
									"ui-state-active").addClass(
									"ui-state-default");
							x.@com.javexpress.gwt.library.ui.container.catalogview.CatalogView::setActiveCatalog(Ljava/lang/String;)(catalog);
						}).hover(function() {
					$wnd.$(this).addClass("ui-state-hover");
				}, function() {
					$wnd.$(this).removeClass("ui-state-hover");
				});
	}-*/;

	public void setCatalogHandler(final ICatalogHandler iCatalogHandler) {
		handler = iCatalogHandler;
	}

	public void setActiveCatalog(final String catalog) throws Exception {
		if (handler != null) {
			TableElement t = catalogs.cast();
			NodeList<TableRowElement> list = t.getRows();
			for (int i = 0; i < list.getLength(); i++) {
				TableRowElement tre = list.getItem(i);
				if (tre.getAttribute("catalog").equals(catalog)) {
					tre.addClassName("ui-state-active");
					break;
				}
			}
			if (view != null) {
				orphan(view);
				JsUtil.clearChilds(viewEl);
			}
			view = handler.createCatalogView(this, catalog);
			if (view != null) {
				viewEl.appendChild(view.getElement());
				if (isAttached())
					adopt(view);
			}
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (view != null)
			adopt(view);
		TableElement t = catalogs.cast();
		NodeList<TableRowElement> list = t.getRows();
		for (int i = 0; i < list.getLength(); i++) {
			TableRowElement tre = list.getItem(i);
			_bindEvents(this, tre, tre.getAttribute("catalog"));
		}
	}

	@Override
	protected void onUnload() {
		left = null;
		imageContainer = null;
		catalogs = null;
		viewEl = null;
		handler = null;
		if (view != null)
			orphan(view);
		view = null;
		super.onUnload();
	}

	@Override
	public void onResize() {
		if (view != null && view instanceof RequiresResize)
			((RequiresResize) view).onResize();
	}

	public void refresh() throws Exception {
		setActiveCatalog(current);
	}

}