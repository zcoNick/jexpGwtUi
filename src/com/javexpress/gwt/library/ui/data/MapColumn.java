package com.javexpress.gwt.library.ui.data;

import java.io.Serializable;
import java.util.Map;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.javexpress.application.model.item.ModuleEnumItems;
import com.javexpress.gwt.fw.client.GwtBootstrapApplication;

public class MapColumn extends ListColumn {

	public MapColumn(String title, String field, boolean sortable, final ModuleEnumItems<? extends Serializable> moduleEnumItems) {
		this(title, field, null, sortable, moduleEnumItems, GwtBootstrapApplication.getModuleNls(moduleEnumItems.getModuleId()));
	}

	/** Designer compatible constructor */
	public MapColumn(String title, String field, boolean sortable) {
		this(title, field, null, sortable, null, null);
	}

	public MapColumn(String title, String field, String width, boolean sortable, final ModuleEnumItems<? extends Serializable> moduleEnumItems) {
		this(title, field, width, sortable, moduleEnumItems, GwtBootstrapApplication.getModuleNls(moduleEnumItems.getModuleId()));
	}

	public MapColumn(String title, String field, boolean sortable, final Map<? extends Serializable, ? extends Serializable> asMap) {
		this(title, field, null, sortable, asMap, null);
	}

	public MapColumn(String title, String field, final Map<? extends Serializable, ? extends Serializable> asMap, ConstantsWithLookup nls) {
		this(title, field, null, true, asMap, nls);
	}

	public MapColumn(String title, String field, boolean sortable, final Map<? extends Serializable, ? extends Serializable> asMap, ConstantsWithLookup nls) {
		this(title, field, null, sortable, asMap, nls);
	}

	public void setItemsEnum(final ModuleEnumItems<? extends Serializable> moduleEnumItems) {
		setMap(moduleEnumItems, GwtBootstrapApplication.getModuleNls(moduleEnumItems.getModuleId()));
	}

	public MapColumn(String title, String field, String width, boolean sortable, final Map<? extends Serializable, ? extends Serializable> asMap, ConstantsWithLookup nls) {
		super(title, field, sortable, Formatter.map);
		if (width != null)
			setWidth(width);
		if (asMap != null)
			setMap(asMap, nls);
	}

}