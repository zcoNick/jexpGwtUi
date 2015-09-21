package com.javexpress.gwt.library.ui.data.jqgrid;

import java.io.Serializable;

import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.IJsonServicePoint;
import com.javexpress.gwt.library.ui.data.ListColumn;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class JqTree<T extends Serializable> extends JqGrid<T> {

	/** Designer compatible constructor */
	public JqTree(final Widget parent, String id, boolean fitToParent, String keyColumnName, String header) {
		this(parent, id, fitToParent, null, keyColumnName, true, header);
	}

	private JqTree(final Widget parent, final String id, boolean fitToParent, final IJsonServicePoint servicePoint, String keyColumnName, final boolean autoLoad, String header) {
		super(parent, id, fitToParent, servicePoint, keyColumnName, autoLoad);
		options.set("treeGrid", true);
		options.set("treeGridModel", "adjacency");
		options.clear("gridview");
		options.clear("scroll");
		setPaging(false);
		if (JsUtil.USE_BOOTSTRAP) {
			JsonMap ti = new JsonMap();
			ti.set("plus", "fa fa-plus-square-o");
			ti.set("minus", "fa fa-minus-square-o");
			ti.set("leaf", "fa fa-angle-right");
			options.put("treeIcons", ti);
		}
	}

	public void setExpandColumn(final String expandColumn) {
		options.set("ExpandColumn", expandColumn);
	}

	@Override
	public void addColumn(final ListColumn column) {
		super.addColumn(column);
		if (column instanceof ExpandColumn)
			setExpandColumn(column.getField());
	}

	@Override
	public void setHeight(String height) {
		options.setInt("height", Integer.valueOf(height.replaceFirst("px", "")));
	}

}