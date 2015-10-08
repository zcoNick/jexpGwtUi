package com.javexpress.gwt.library.ui.form.combobox;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.javexpress.gwt.library.ui.js.JsUtil;

public class KeyValueDataLoader implements Command {

	private final long							moduleId;
	private final ServiceDefTarget				serviceDefTarget;
	protected final Map<IKeyValueList, String>	comboKey	= new LinkedHashMap<IKeyValueList, String>();
	private IKeyValueDataLoaderListener			listener;

	public IKeyValueDataLoaderListener getListener() {
		return listener;
	}

	public void setListener(IKeyValueDataLoaderListener listener) {
		this.listener = listener;
	}

	public KeyValueDataLoader(long moduleId, ServiceDefTarget serviceDefTarget) {
		this.moduleId = moduleId;
		this.serviceDefTarget = serviceDefTarget;
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
		final Set<IKeyValueList> set = comboKey.keySet();
		if (listener != null)
			listener.onStartLoadingKeys(set);
		//Fired from Form.onLoad
		StringBuilder rd = new StringBuilder();
		for (String k : comboKey.values())
			if (rd.indexOf("~" + k + "~") == -1)
				rd.append(k).append("~");
		try {
			RequestBuilder r = new RequestBuilder(RequestBuilder.POST, serviceDefTarget.getServiceEntryPoint() + "?jexp.op.kvdl");
			r.sendRequest(rd.toString(), new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 901)
						return;
					JSONValue v = JSONParser.parseStrict(response.getText());
					JSONObject json = v.isObject();
					for (IKeyValueList combo : set) {
						String k = comboKey.get(combo);
						if (k.indexOf("?") > -1)
							k = k.substring(0, k.indexOf("?"));
						JSONValue va = json.get(k);
						JSONObject grp = va != null ? va.isObject() : null;
						if (grp != null && grp.size() > 0) {
							for (String ig : grp.keySet()) {
								JSONObject itm = grp.get(ig).isObject();
								combo.setKeyValueDataItems(itm);
							}
						} else
							combo.setKeyValueDataItems(null);
						if (listener != null)
							listener.onLoadedKeyValues(k, grp);
					}
					if (listener != null)
						listener.onKeysCompleted(set);
					comboKey.clear();
				}

				@Override
				public void onError(Request request, Throwable exception) {
					JsUtil.handleError("kvdl", exception);
				}
			});
		} catch (RequestException e) {
			JsUtil.handleError("kvdl", e);
		}
	}

	public Long getModuleId() {
		return moduleId;
	}

}