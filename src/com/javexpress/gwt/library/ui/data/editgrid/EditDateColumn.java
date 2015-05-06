package com.javexpress.gwt.library.ui.data.editgrid;

import com.javexpress.gwt.library.shared.model.JexpGwtUser;
import com.javexpress.gwt.library.ui.data.ListColumn.SummaryType;

public class EditDateColumn extends EditColumn {

	public EditDateColumn(final String title, final String field, final String width) {
		this(title, null, field, width, null);
	}

	/** Designer compatible constructor */
	public EditDateColumn(final String title, final String field) {
		this(title, null, field, "25", null);
	}

	public EditDateColumn(final String title, final String field, final String width, final SummaryType summaryType) {
		this(title, null, field, width, summaryType);
	}

	public EditDateColumn(final String title, final String hint, final String field, final String width, final SummaryType summaryType) {
		super(title, hint, field, ColumnAlign.left, width, EditorType.date, summaryType);
	}

	public String getMatchingFormat() {
		String fmt = JexpGwtUser.getDateFormat();
		return fmt.replaceFirst("MM", "mm");
	}

	public String getInputFormat() {
		String fmt = JexpGwtUser.getDateFormat();
		fmt = fmt.replaceFirst("dd", "d");
		fmt = fmt.replaceFirst("mm", "m");
		fmt = fmt.replaceFirst("MM", "m");
		fmt = fmt.replaceFirst("yyyy", "y");
		fmt = fmt.replaceFirst("yy", "y");
		return fmt;
	}

}