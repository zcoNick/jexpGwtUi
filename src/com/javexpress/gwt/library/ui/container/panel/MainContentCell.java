package com.javexpress.gwt.library.ui.container.panel;

import com.google.gwt.user.client.DOM;
import com.javexpress.gwt.library.ui.AbstractContainer;

public class MainContentCell extends AbstractContainer {

	public static enum CellSize {
		Small_10("col-sm-10");
		private String	value;

		private CellSize(String pvalue) {
			this.value = pvalue;
		}
	}

	public static enum OffsetSize {
		Small_1("col-sm-offset-1");
		private String	value;

		private OffsetSize(String pvalue) {
			this.value = pvalue;
		}
	}

	public MainContentCell(CellSize cellSize) {
		super(DOM.createDiv());
		getElement().setClassName(cellSize.value);
	}

	public void setOffsetSize(OffsetSize offsetSize) {
		getElement().addClassName(offsetSize.value);
	}

}