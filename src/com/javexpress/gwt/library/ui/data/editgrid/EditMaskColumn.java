package com.javexpress.gwt.library.ui.data.editgrid;

public class EditMaskColumn extends EditColumn {
	
	private String mask;
	private String placeHolder="_";
	
	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}
	
	public String getPlaceHolder() {
		return placeHolder;
	}

	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	/** Designer compatible constructor */
	public EditMaskColumn(final String title, final String field) {
		this(title, null, field, null);
	}

	public EditMaskColumn(final String title, final String field, final String width) {
		this(title, null, field, width);
	}

	public EditMaskColumn(final String title, final String hint, final String field, final String width) {
		super(title, hint, field, ColumnAlign.left, width, EditorType.mask, null);
	}

}