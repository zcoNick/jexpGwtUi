package com.javexpress.gwt.library.ui.form.filterpanel;

import java.io.Serializable;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.javexpress.application.model.item.DatasetFilterDescriptor;
import com.javexpress.gwt.library.ui.ClientModule;
import com.javexpress.gwt.library.ui.form.IFormFactory;
import com.javexpress.gwt.library.ui.form.IUIComposite;
import com.javexpress.gwt.library.ui.form.combobox.IListItemBox;
import com.javexpress.gwt.library.ui.form.combobox.KeyValueDataLoader;

public class BaseFilterLabelAndControlDataResolver implements IFilterLabelAndControlDataResolver {

	private ClientModule		module;
	private IUIComposite				form;
	private KeyValueDataLoader	keyValueDataLoader;

	public KeyValueDataLoader getKeyValueDataLoader() {
		if (keyValueDataLoader == null)
			keyValueDataLoader = module.createKeyValueDataLoader(form);
		return keyValueDataLoader;
	}

	public BaseFilterLabelAndControlDataResolver(ClientModule module, IUIComposite form) {
		this.module = module;
		this.form = form;
	}

	@Override
	public String handleFilterLabelNls(String constant) {
		if (constant.startsWith("@"))
			return IFormFactory.nlsCommon.getString(constant.substring(1));
		return module.getNls().getString(constant);
	}

	@Override
	public boolean handleFilterControlData(Widget widget, DatasetFilterDescriptor fd) {
		if (widget instanceof IListItemBox) {
			IListItemBox combo = (IListItemBox) widget;
			if (fd.getControlData().startsWith("@enum:")) {
				String enumName = fd.getControlData().substring(6);
				Map<? extends Serializable, ? extends Serializable> map = module.getEnums().getAsMap(enumName);
				if (map != null) {
					combo.setItemsNls(map, module.getNls());
					return true;
				}
			} else if (fd.getControlData().startsWith("@combo:")) {
				String moduleAndcode = fd.getControlData().substring(7);
				if (moduleAndcode.indexOf("/") == -1) {
					getKeyValueDataLoader().add(combo, module.getModuleId(), moduleAndcode);
				} else
					getKeyValueDataLoader().add(combo, moduleAndcode);
			} else if (fd.getControlData().startsWith("@code:")) {
				String moduleAndcode = fd.getControlData().substring(6);
				getKeyValueDataLoader().add(combo, "@code:" + moduleAndcode);
			}
		}
		return false;
	}

	public void onUnload() {
		module = null;
		keyValueDataLoader = null;
		form = null;
	}

}