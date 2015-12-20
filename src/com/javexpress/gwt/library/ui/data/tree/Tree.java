package com.javexpress.gwt.library.ui.data.tree;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.javexpress.gwt.library.shared.model.WidgetConst;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class Tree extends SimplePanel {

	private Map<String, TreeItem>						valMap;
	private final com.google.gwt.user.client.ui.Tree	tree;
	private String										pathSeparator	= ".";

	public String getPathSeparator() {
		return pathSeparator;
	}

	public void setPathSeparator(String pathSeparator) {
		this.pathSeparator = pathSeparator;
	}

	public Tree(Widget parent, String id) {
		this(parent, id, false);
	}

	public Tree(Widget parent, String id, boolean fitToParent) {
		super(new com.google.gwt.user.client.ui.Tree());
		JsUtil.ensureId(parent, this, WidgetConst.TREE_PREFIX, id);
		addStyleName("jexpTree");

		tree = (com.google.gwt.user.client.ui.Tree) getWidget();
		tree.setAnimationEnabled(true);

		if (fitToParent) {
			setWidth("100%");
			setHeight("100%");
		}

		valMap = new HashMap<String, TreeItem>();
	}

	public TreeItem addItem(String label, String val) {
		int pos = val.lastIndexOf(pathSeparator);
		TreeItem parent = pos == -1 ? null : getItem(val.substring(0, pos));
		TreeItem ti = new TreeItem();
		ti.setText(label);
		ti.getElement().setAttribute("v", val);
		if (parent == null) {
			tree.addItem(ti);
		} else
			parent.addItem(ti);
		valMap.put(val, ti);
		return ti;
	}

	protected void removeItem(TreeItem item) {
		tree.removeItem(item);
		valMap.remove(item.getElement().getAttribute("v"));
	}

	public void removeItem(String val) {
		removeItem(getItem(val));
	}

	protected TreeItem getItem(String val) {
		return valMap.get(val);
	}

	public void clearItems() {
		tree.clear();
		valMap.clear();
	}

	@Override
	protected void onUnload() {
		valMap = null;
		super.onUnload();
	}

	public String getSelectedItemValue() {
		return tree.getSelectedItem().getElement().getAttribute("v");
	}

	public String getItemText(String val) {
		return getItem(val).getText();
	}

	public void addSelectionHandler(SelectionHandler<TreeItem> handler) {
		tree.addSelectionHandler(handler);
	}

}