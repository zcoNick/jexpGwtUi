package com.javexpress.gwt.library.ui.container.layout;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.ui.form.label.Label;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class GridLayout extends Grid {

	public static enum GridLayoutAlignment {
		beginning, center, end;
	}

	public GridLayout() {
		super();
	}

	public GridLayout(final int rows, final int columns) {
		this(rows, columns, false);
	}

	@Deprecated
	public GridLayout(final int rows, final int columns, final boolean fillWidth) {
		super(rows, columns);
		getElement().addClassName("jexpBorderBox");
		if (fillWidth)
			setWidth("100%");
	}

	public void setWidget(int row, int column, Label label) {
		label.append(label.isRequired() ? " *:" : " :");
		super.setWidget(row, column, label);
		if (label.isRequired())
			getCellFormatter().addStyleName(row, column, "jexpRequired");
	}

	public void setLabel(int row, int column, String label) {
		setLabel(row, column, label, false);
	}

	public void setLabel(final int row, final int column, final String label, final boolean hAlignRight) {
		setLabel(row, column, label, hAlignRight, false, false);
	}

	public void setLabel(final int row, final int column, final String label, final boolean hAlignRight, final boolean required) {
		setLabel(row, column, label, hAlignRight, required, false);
	}

	public void setLabel(final int row, final int column, final String label, final boolean hAlignRight, final boolean required, final boolean vAlignTop) {
		setWidget(row, column, new Label(label + (required ? " *:" : " :")), hAlignRight, vAlignTop);
		if (required)
			getCellFormatter().addStyleName(row, column, "jexpRequired");
	}

	public void setWidget(final int row, final int column, final Widget widget, final boolean hAlignRight) {
		setWidget(row, column, widget, hAlignRight, false);
	}

	public void setWidget(final int row, final int column, final Widget widget, final boolean hAlignRight, final boolean vAlignTop) {
		super.setWidget(row, column, widget);
		if (hAlignRight)
			getCellFormatter().setHorizontalAlignment(row, column, JsUtil.isLTR() ? HasHorizontalAlignment.ALIGN_RIGHT : HasHorizontalAlignment.ALIGN_LEFT);
		if (vAlignTop)
			getCellFormatter().setVerticalAlignment(row, column, HasVerticalAlignment.ALIGN_TOP);
	}

	public void setColspan(final int row, final int col, final int spanCount) {
		getCellFormatter().getElement(row, col).setAttribute("colspan", String.valueOf(spanCount));
		List<Element> toRemove = new ArrayList<Element>();
		for (int x = 1; x < spanCount && col + x < getCellCount(row); x++)
			toRemove.add(getCellFormatter().getElement(row, col + x));
		for (Element f : toRemove)
			if (f != null)
				f.removeFromParent();
	}

	/** This method must be finally invoked after all cells are filled */
	public void setRowspan(final int row, final int col, final int spanCount) {
		getCellFormatter().getElement(row, col).setAttribute("rowspan", String.valueOf(spanCount));
		List<Element> toRemove = new ArrayList<Element>();
		for (int x = 1; x < spanCount && col + x < getRowCount(); x++)
			toRemove.add(getCellFormatter().getElement(row + x, col));
		for (Element f : toRemove)
			f.removeFromParent();
	}

	public void setColWidth(int column, double width, Unit unit) {
		getColumnFormatter().getElement(column).getStyle().setWidth(width, unit);
	}

	public void setColWidth(int column, String width) {
		getColumnFormatter().getElement(column).setAttribute("width", width);
	}

	public void setCellTextAlign(int row, int column, TextAlign textAlign) {
		getCellFormatter().getElement(row, column).getStyle().setTextAlign(textAlign);
	}

	public void setCellHAlign(int row, int column, GridLayoutAlignment alignment) {
		getCellFormatter().setHorizontalAlignment(row, column, (alignment == GridLayoutAlignment.beginning ? HasHorizontalAlignment.ALIGN_LEFT :
				(alignment == GridLayoutAlignment.center ? HasHorizontalAlignment.ALIGN_CENTER : HasHorizontalAlignment.ALIGN_RIGHT)));
	}

	public void setCellVAlign(int row, int column, GridLayoutAlignment alignment) {
		getCellFormatter().setVerticalAlignment(row, column, alignment == GridLayoutAlignment.beginning ? HasVerticalAlignment.ALIGN_TOP :
				(alignment == GridLayoutAlignment.center ? HasVerticalAlignment.ALIGN_MIDDLE : HasVerticalAlignment.ALIGN_BOTTOM));
	}

	public void setColWidths(String colWidthsComma) {
	}

}