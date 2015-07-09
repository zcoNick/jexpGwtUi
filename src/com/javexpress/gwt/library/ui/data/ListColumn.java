package com.javexpress.gwt.library.ui.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.javexpress.gwt.library.ui.ClientContext;

public class ListColumn extends Column {

	private Formatter					formatter;
	private SummaryType					summaryType;
	private String						summaryTemplate;
	private boolean						sortable;
	private boolean						sorted;
	private boolean						sortDesc;
	private boolean						groupable;
	private Map<String, Serializable>	values;

	public static enum Formatter {
		decimal, currency, date, bool, number, map, timestamp, link, checkbox, email, percentBar;
	}

	public static enum SummaryType {
		min, max, avg, sum, count
	}

	public ListColumn(final String title, final String field, final ColumnAlign align, final String width, final boolean sortable, final Formatter formatter) {
		this(title, field, align, width, sortable, false, formatter, null);
	}

	public ListColumn(final String title, final String field, final ColumnAlign align, final String width, final boolean sortable, final boolean hidden, final Formatter formatter, final SummaryType summaryType) {
		super(title, field, align, width, hidden, false);
		if (formatter == Formatter.timestamp)
			setWidth("200");
		this.formatter = formatter;
		this.summaryType = summaryType;
		this.sortable = sortable;
	}

	/** Designer compatible constructor */
	public ListColumn(final String title, final String field, final boolean sortable) {
		this(title, field, null, null, sortable, false, null, null);
	}

	public ListColumn(final String title, final String field, final boolean sortable, final Formatter formatter, final SummaryType summaryType) {
		this(title, field, null, null, sortable, false, formatter, summaryType);
	}

	public ListColumn(final String title, final String field, final boolean sortable, final Formatter formatter) {
		this(title, field, null, null, sortable, false, formatter, null);
	}

	@Deprecated
	public ListColumn(final String title, final String field, final String width, final boolean sortable) {
		this(title, field, null, width, sortable, false, null, null);
	}

	public ListColumn(final String title, final String field, final String width, final boolean sortable, final Formatter formatter, final SummaryType summary) {
		this(title, field, null, width, sortable, false, formatter, summary);
	}

	public ListColumn(final String title, final String field, final ColumnAlign align, final boolean sortable) {
		this(title, field, align, null, sortable);
	}

	public ListColumn(final String title, final String field, final ColumnAlign align, final String width, final boolean sortable) {
		this(title, field, align, width, sortable, null);
	}

	public Formatter getFormatter() {
		return formatter;
	}

	public void setFormatter(final Formatter formatter) {
		this.formatter = formatter;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(final boolean sortable) {
		this.sortable = sortable;
	}

	public SummaryType getSummaryType() {
		return summaryType;
	}

	public ListColumn setSummaryType(final SummaryType summaryType) {
		this.summaryType = summaryType;
		return this;
	}

	public String getSummaryTemplate() {
		if (this.summaryTemplate != null)
			return this.summaryTemplate;
		if (summaryType != null)
			switch (summaryType) {
				case count:
					return "<b>{0} " + ClientContext.nlsCommon.gridOzet_Adet() + "</b>";
				case avg:
					return "<b>" + ClientContext.nlsCommon.gridOzet_Ortalama() + ": {0}</b>";
				case max:
					return "<b>" + ClientContext.nlsCommon.gridOzet_Max() + ": {0}</b>";
				case min:
					return "<b>" + ClientContext.nlsCommon.gridOzet_Min() + ": {0}</b>";
				case sum:
					return "<b>{0}</b>";
			}
		return null;
	}

	/*
	 * <b>{0}</b> şeklinde
	 */
	public void setSummaryTemplate(String summaryTemplate) {
		this.summaryTemplate = summaryTemplate;
	}

	public ListColumn setMap(final Map<? extends Serializable, ? extends Serializable> asMap) {
		return setMap(asMap, null);
	}

	public ListColumn setMap(final Map<? extends Serializable, ? extends Serializable> asMap, ConstantsWithLookup nls) {
		return setMap(false, asMap, nls);
	}

	public ListColumn setMap(boolean useEmptyItem, final Map<? extends Serializable, ? extends Serializable> asMap, ConstantsWithLookup nls) {
		formatter = Formatter.map;
		values = new HashMap<String, Serializable>();
		if (useEmptyItem)
			values.put("", "");
		for (Serializable key : asMap.keySet()) {
			if (nls == null)
				values.put(key.toString(), asMap.get(key));
			else {
				String constant = asMap.get(key).toString();
				if (constant.startsWith("@")) {
					try {
						String nlsValue = nls.getString(constant.substring(1));
						values.put(key.toString(), nlsValue);
					} catch (Exception ex) {
						values.put(key.toString(), constant);
					}
				} else
					values.put(key.toString(), asMap.get(key));
			}
		}
		return this;
	}

	public Serializable getMapValue(final Serializable key) {
		return values.get(key);
	}

	public ListColumn sorted(final boolean desc) {
		sorted = true;
		sortDesc = desc;
		return this;
	}

	public boolean isSorted() {
		return sorted;
	}

	public boolean isSortDesc() {
		return sortDesc;
	}

	public Set<String> getMapKeys() {
		return values.keySet();
	}

	public ListColumn freeze() {
		setFrozen(true);
		return this;
	}

	public boolean isGroupable() {
		return groupable;
	}

	public ListColumn setGroupable(boolean groupable) {
		this.groupable = groupable;
		return this;
	}

}