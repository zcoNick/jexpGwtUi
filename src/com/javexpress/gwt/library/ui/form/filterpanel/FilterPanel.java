package com.javexpress.gwt.library.ui.form.filterpanel;

import java.text.ParseException;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.FieldSetElement;
import com.google.gwt.dom.client.LegendElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.SilkIcon;
import com.javexpress.gwt.library.ui.container.buttonbar.ButtonBar;
import com.javexpress.gwt.library.ui.form.Form;
import com.javexpress.gwt.library.ui.form.IFormFactory;
import com.javexpress.gwt.library.ui.form.button.Button;
import com.javexpress.gwt.library.ui.form.button.TextButton;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class FilterPanel extends AbstractFilterPanel {

	private FieldSetElement	fieldSet;
	private LegendElement	legend;
	private Element			buttonsDiv;
	private ButtonBar		buttonBar;

	public FilterPanel(final Form parent, final String id, boolean fitToParent) {
		super(DOM.createFieldSet(), parent, id);
		fieldSet = getElement().cast();
		fieldSet.setTitle(IFormFactory.nlsCommon.filtreler());
		legend = DOM.createLegend().cast();
		legend.setInnerText(IFormFactory.nlsCommon.filtreler());
		fieldSet.appendChild(legend);
		setStyleName("ui-widget ui-widget-content ui-corner-all jexpBorderBox jexpFilterPanel");
		if (fitToParent) {
			getElement().getStyle().setRight(0, Unit.PX);
			getElement().getStyle().setBottom(0, Unit.PX);
		}
		setMargin(0);

		filtersDiv.getStyle().setPosition(Position.ABSOLUTE);
		filtersDiv.getStyle().setDisplay(Display.INLINE_BLOCK);
		filtersDiv.getStyle().setTop(1.5, Unit.EM);
		filtersDiv.getStyle().setLeft(0, Unit.PX);
		filtersDiv.getStyle().setRight(0, Unit.PX);
		filtersDiv.getStyle().setProperty("bottom", WidgetConst.HEIGHT_BUTTONBAR);
		filtersDiv.getStyle().setOverflow(Overflow.AUTO);

		buttonsDiv = DOM.createDiv().cast();
		buttonsDiv.getStyle().setPosition(Position.ABSOLUTE);
		buttonsDiv.getStyle().setDisplay(Display.INLINE_BLOCK);
		buttonsDiv.getStyle().setBottom(0, Unit.PX);
		buttonsDiv.getStyle().setLeft(0, Unit.PX);
		buttonsDiv.getStyle().setRight(0, Unit.PX);
		buttonsDiv.getStyle().setProperty("height", WidgetConst.HEIGHT_BUTTONBAR);
		buttonsDiv.getStyle().setOverflow(Overflow.HIDDEN);
		getElement().appendChild(buttonsDiv);
	}

	@Override
	protected void onLoad() {
		final FilterPanel that = this;
		buttonBar = new ButtonBar(true);
		TextButton tbTemizle = new TextButton(that, "temizle", IFormFactory.nlsCommon.temizle());
		tbTemizle.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				try {
					clearFilters(true);
				} catch (ParseException e) {
					JsUtil.handleError(that, e);
				}
			}
		});
		buttonBar.add(tbTemizle);
		final Button btUygula = new Button(that, "uygula", IFormFactory.nlsCommon.uygula());
		btUygula.setIcon(SilkIcon.magnifier);
		btUygula.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				applyFilters(true);
			}
		});
		buttonBar.add(btUygula);
		add(buttonBar, buttonsDiv);
		super.onLoad();
		createByJs(this, getElement());
	}

	private native void createByJs(FilterPanel x, Element el) /*-{
		$wnd.$("legend", $wnd.$(el)).addClass("ui-state-default").css({
			'margin-left' : '10px',
			'border' : '0'
		});
	}-*/;

	@Override
	protected void onUnload() {
		if (buttonBar != null)
			remove(buttonBar);
		buttonBar = null;
		buttonsDiv = null;
		super.onUnload();
	}

}