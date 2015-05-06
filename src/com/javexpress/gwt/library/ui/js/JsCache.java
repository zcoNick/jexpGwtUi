package com.javexpress.gwt.library.ui.js;

import java.util.HashMap;
import java.util.Map;

public class JsCache<K,V> {
	
	private int maxCapacity;
	private Map<K,V> map = new HashMap<K, V>();
	
	public JsCache(final int maxCapacity) {
		this.maxCapacity = maxCapacity; 
	}
	
	public void put(final K key, final V value) {
		if (map.size()==maxCapacity)
			map.remove(map.keySet().iterator().next());
		map.put(key, value);
	}

	public V get(final K key) {
		return map.get(key);
	}
	
}