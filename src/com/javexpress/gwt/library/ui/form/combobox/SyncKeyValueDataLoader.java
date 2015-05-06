package com.javexpress.gwt.library.ui.form.combobox;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.javexpress.gwt.library.ui.js.JsUtil;
import com.javexpress.gwt.library.ui.js.SyncRequestBuilder;
import com.javexpress.gwt.library.ui.js.SyncRequestCallback;

public class SyncKeyValueDataLoader implements Command {

	private final Long							moduleId;
	private final ServiceDefTarget				serviceDefTarget;
	private final Map<IKeyValueList, String>	comboKey	= new LinkedHashMap<IKeyValueList, String>();

	public SyncKeyValueDataLoader(Long moduleId) {
		this.moduleId = moduleId;
		this.serviceDefTarget = GwtBootstrapApplication.getModuleServiceTarget(moduleId);
	}

	public void add(IKeyValueList combo, Long pmoduleId, String key) {
		add(combo, pmoduleId, key, null);
	}

	public void add(IKeyValueList combo, Long pmoduleId, Enum key, String params) {
		add(combo, pmoduleId, key.toString(), params);
	}

	public void add(IKeyValueList combo, Long pmoduleId, String key, String params) {
		if (key.indexOf("/") == -1)
			key = (pmoduleId != null ? pmoduleId : moduleId) + "/" + key;
		if (JsUtil.isNotEmpty(params) && key.indexOf("?") == -1)
			key += "?" + params;
		comboKey.put(combo, key);
	}

	public void add(IKeyValueList combo, String key) {
		add(combo, moduleId, key);
	}

	public void add(IKeyValueList combo, Enum key) {
		add(combo, moduleId, key.toString());
	}

	public void add(IKeyValueList combo, Enum key, String params) {
		add(combo, moduleId, key.toString(), params);
	}

	public void add(IKeyValueList combo, Long moduleId, Enum key) {
		add(combo, moduleId, key.toString());
	}

	@Override
	public void execute() {
		if (comboKey.isEmpty())
			return;
		StringBuilder rd = new StringBuilder();
		for (String k : comboKey.values())
			if (rd.indexOf("~" + k + "~") == -1)
				rd.append(k).append("~");
		SyncRequestBuilder r = new SyncRequestBuilder(RequestBuilder.POST, serviceDefTarget.getServiceEntryPoint() + "?jexp.op.kvdl");
		r.sendRequest(rd.toString(), new SyncRequestCallback() {
			@Override
			public void onSuccess(String textStatus, String response) {
				JSONValue v = JSONParser.parseStrict(response);
				JSONObject json = v.isObject();
				for (IKeyValueList combo : comboKey.keySet()) {
					String k = comboKey.get(combo);
					JSONValue va = json.get(k);
					if (va != null) {
						JSONObject grp = va.isObject();
						for (String ig : grp.keySet()) {
							JSONObject itm = grp.get(ig).isObject();
							combo.setKeyValueDataItems(itm);
						}
					}
				}
			}

			@Override
			public void onError(String textStatus, String errorThrown) {
				JsUtil.handleError("kvdl", new Exception(errorThrown));
			}
		});
	}

	public Long getModuleId() {
		return moduleId;
	}

}