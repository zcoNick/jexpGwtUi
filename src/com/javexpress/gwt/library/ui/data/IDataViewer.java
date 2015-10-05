package com.javexpress.gwt.library.ui.data;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.IsWidget;
import com.javexpress.gwt.library.shared.model.IJsonServicePoint;
import com.javexpress.gwt.library.ui.IJexpWidget;
import com.javexpress.gwt.library.ui.data.slickgrid.GroupingDefinition;
import com.javexpress.gwt.library.ui.form.ISizeAwareWidget;
import com.javexpress.gwt.library.ui.js.JsonMap;
import com.javexpress.gwt.library.ui.menu.IHasPopupMenu;

public interface IDataViewer extends IJexpWidget, ISizeAwareWidget, IDataChangeListener, IHasPopupMenu, IsWidget, Focusable {

	void loadData();

	void refresh();

	void addToolItem(GridToolItem gridToolItem);

	String getSelectedId();

	void addColumn(ListColumn column);

	void setListener(IGridListener listener);

	void setFitColumns(boolean value);

	void clearSelection();

	void setListing(IJsonServicePoint serviceMethod);

	void setPaging(boolean dataPaging);

	void setAutoLoad(boolean autoLoad);

	List<String> getSelectedIds();

	void setSelectedIds(List<? extends Serializable> values, boolean fireOnSelect);

	JsonMap getSelectedData();

	void setMultiSelect(boolean value);

	Serializable getWidgetData();

	void setWidgetData(Serializable widgetData);

	void clearData();

	void setKeyColumnName(String keyColumnName);

	void addStyleName(String cssClasses);

	void setHeight(String height);

	void setMaxHeight(int maxHeight);

	void performAutoSizeColumns();

	void setDataExportOptions(boolean useForeignKeysAsVariable);

	void addGrouping(GroupingDefinition groupingItem);

	void applyGrouping();

}