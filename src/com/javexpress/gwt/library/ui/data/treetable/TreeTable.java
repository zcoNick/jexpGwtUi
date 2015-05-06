package com.javexpress.gwt.library.ui.data.treetable;

import java.beans.Beans;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.JexpWidget;
import com.javexpress.gwt.library.ui.JqIcon;
import com.javexpress.gwt.library.ui.data.Column;
import com.javexpress.gwt.library.ui.data.GridToolItem;
import com.javexpress.gwt.library.ui.data.IGridListener;
import com.javexpress.gwt.library.ui.data.ListColumn;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class TreeTable extends JexpWidget {

	private final String		url;
	private JavaScriptObject	widget;
	private IGridListener		listener;
	private List<ListColumn>	columns	= new ArrayList<ListColumn>();
	private List<GridToolItem>	tools	= new ArrayList<GridToolItem>();

	public IGridListener getListener() {
		return listener;
	}

	public void setListener(final IGridListener listener) {
		this.listener = listener;
	}

	public TreeTable(final Widget parent, final String id, final ServiceDefTarget service, final Enum method) {
		this(parent, id, service, method, null);
	}

	public TreeTable(final Widget parent, final String id, final ServiceDefTarget service, final Enum method, final JsonMap pOptions) {
		super();
		this.url = service.getServiceEntryPoint() + "." + method;
		setElement(DOM.createTable());
		JsUtil.ensureId(parent, this, WidgetConst.TREETABLE_PREFIX, id);
		getElement().setClassName("treeTable ui-widget ui-widget-content ui-corner-all");
		getElement().setAttribute("cellspacing", "0");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!Beans.isDesignTime()) {
			Element tr = DOM.createTR();
			tr.setAttribute("valign", "top");
			Element td = DOM.createTD();
			Element div = DOM.createDiv();
			div.setClassName("treeTableContainer");
			Element table = DOM.createTable();
			table.setId(getElement().getId() + "_table");
			table.setClassName("treeTableData");
			table.setAttribute("cellpadding", "0");
			table.setAttribute("cellspacing", "0");
			encodeHead(table);
			encodeBody(table);
			div.appendChild(table);
			td.appendChild(div);
			tr.appendChild(td);
			getElement().appendChild(tr);
			String[] tis = encodeToolbar();
			widget = createByJs(this, getElement().getId(), tis, url, tools.toArray(new GridToolItem[] {}));
		}
	}

	private String[] encodeToolbar() {
		String[] ids = new String[tools.size() + 1];
		Element tbTr = DOM.createTR();
		tbTr.setAttribute("height", "1%");
		tbTr.setClassName("ui-state-default ui-border-top");
		Element tbTd = DOM.createTD();
		Element table = DOM.createTable();
		Element tr = DOM.createTR();

		Element td = DOM.createTD();
		Element span = DOM.createSpan();
		span.setClassName("ui-icon ui-cursor-hand " + JqIcon.refresh.getCssClass());
		span.setAttribute("title", "Yenile");
		int tindex = 0;
		td.appendChild(span);
		tr.appendChild(td);
		ids[tindex++] = JsUtil.ensureId(span);

		for (GridToolItem ti : tools) {
			td = DOM.createTD();
			span = DOM.createSpan();
			span.setClassName("ui-icon ui-cursor-hand " + ti.getIcon().getCssClass());
			span.setAttribute("title", ti.getHint());
			if (ti.getCaption() != null)
				span.setInnerText(ti.getCaption());
			td.appendChild(span);
			tr.appendChild(td);
			ids[tindex++] = JsUtil.ensureId(span);
		}
		table.appendChild(tr);
		tbTd.appendChild(table);
		tbTr.appendChild(tbTd);
		getElement().appendChild(tbTr);
		return ids;
	}

	private void encodeBody(final Element table) {
		Element tbody = DOM.createTBody();
		Element tr = DOM.createTR();
		tr.setId(getElement().getId() + "_sample");
		tr.setClassName("ui-cursor-hand");
		tr.getStyle().setDisplay(Display.NONE);
		for (Column column : columns) {
			if (column.isHidden()) {
			} else {
				Element td = DOM.createTD();
				td.setAttribute("data", column.getField());
				if (column instanceof TreeColumn) {
					TreeColumn tc = (TreeColumn) column;
					if (tc.getOnclick() != null) {
						Element span = DOM.createSpan();
						span.setClassName("ui-icon ui-cursor-hand " + tc.getIcon().getCssClass());
						span.setAttribute("onclick_sample", tc.getOnclick());
						td.appendChild(span);
					}
				}
				tr.appendChild(td);
			}
		}
		tbody.appendChild(tr);
		table.appendChild(tbody);
	}

	private void encodeHead(final Element table) {
		Element thead = DOM.createTHead();
		Element tr = DOM.createTR();
		tr.setClassName("ui-widget-header ui-state-default");
		for (Column col : columns) {
			if (col.isHidden()) {

			} else {
				Element th = DOM.createTH();
				th.setClassName("ui-treetable-header");
				th.setInnerText(col.getTitle());
				tr.appendChild(th);
			}
		}
		thead.appendChild(tr);
		table.appendChild(thead);
	}

	private native JavaScriptObject createByJs(TreeTable x, String id, String[] tis, String url, GridToolItem[] tools) /*-{
		var el = new $wnd.JexpUI.TreeTable(id + "_table", {
			sampleRowId : id + "_sample",
			listUrl : url,
			selectionOncomplete : function(data, xhr, status) {
			},
			onprepareData : function(postData) {
			},
			indent : 18
		});
		$wnd.alert(tis.length());
		for (var i = 0; i < tis.length; i++) {
			$wnd
					.$("#" + tis[i])
					.click(
							function() {
								if (i == 0)
									x.@com.javexpress.gwt.library.ui.data.treetable.TreeTable::refresh(Ljava/io/Serializable;)(null);
								else
									tools[i - 1].@com.javexpress.gwt.library.ui.data.GridToolItem::executeHandler(Lcom/google/gwt/user/client/Event;)(event);
							});
		}
		return el;
	}-*/;

	@Override
	protected void onUnload() {
		widget = null;
		listener = null;
		columns = null;
		tools = null;
		if (!Beans.isDesignTime())
			destroyByJs(getElement().getId());
		super.onUnload();
	}

	private native void destroyByJs(String id) /*-{
		$wnd.$("#" + id).empty().off().remove();
	}-*/;

	public void addColumn(final ListColumn column) {
		columns.add(column);
	}

	public void addToolItem(final GridToolItem toolItem) {
		tools.add(toolItem);
	}

	public void refresh() {
		_refresh(widget, null);
	}

	public void refresh(final Serializable rowId) {
		_refresh(widget, rowId);
	}

	private native void _refresh(JavaScriptObject widget, Serializable rowId) /*-{
		widget.refresh(rowId);
	}-*/;

	//---------- EVENTS
	public void fireOnRowSelect(final String rowId, final JavaScriptObject rowData) throws Exception {
		if (listener != null) {
			listener.onGridRowSelect(rowId, true, new JsonMap(rowData));
		}
	}

	public void fireOnRowDoubleClick(final String rowId, final JavaScriptObject rowData) throws Exception {
		if (listener != null) {
			listener.onGridRowDoubleClick(rowId, new JsonMap(rowData));
		}
	}

}