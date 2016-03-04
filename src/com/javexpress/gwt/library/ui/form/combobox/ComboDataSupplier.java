package com.javexpress.gwt.library.ui.form.combobox;

import java.io.Serializable;
import java.util.List;

public abstract class ComboDataSupplier<T, V extends Serializable, L extends Serializable> extends LazyDataSupplier<T, V, L> {

	private ListBoxBase[]	combo;
	private boolean			enableOnComplete;

	@Override
	protected void fillItems(List<T> result) {
		for (ListBoxBase cmb : combo) {
			if (!cmb.isRequired())
				cmb.addItem("", "");
			for (T t : result)
				cmb.addItem(getLabel(t), getValue(t), getData(t), getHint(t));
			if (enableOnComplete)
				cmb.setEnabled(true);
		}
	}

	public void fillItems(final ListBoxBase... comboBox) {
		fillItems(false, comboBox);
	}

	public void fillItems(boolean enableOnComplete, final ListBoxBase... comboBox) {
		this.combo = comboBox;
		this.enableOnComplete = enableOnComplete;
		loadData(this);
	}

}