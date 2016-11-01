/**
 * 
 */
package com.giants.xmlmapping;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * @author vencent.lu
 *
 */
public class XmlDataModule<T> implements Serializable {

	private static final long serialVersionUID = 8384748949609831664L;
	
	private Map<Serializable,T> dataObjectMap;
	
	public T get() {
		if (MapUtils.isEmpty(this.dataObjectMap)) {
			return null;
		}
		return this.dataObjectMap.values().iterator().next();
	}
	
	public T get(Serializable id) {
		if (this.dataObjectMap == null) {
			return null;
		}
		return this.dataObjectMap.get(id);
	}
	
	public Collection<T> getAll() {
		if (this.dataObjectMap == null) {
			return null;
		}
		return this.dataObjectMap.values();
	}
	
	public Boolean isNotEmpty() {
		if (this.dataObjectMap == null) {
			return false;
		}
		return MapUtils.isNotEmpty(this.dataObjectMap);
	}

	@SuppressWarnings("unchecked")
	protected Object insert(Serializable id, Object object) {
		if (this.dataObjectMap == null) {
			this.dataObjectMap = new HashMap<Serializable,T>();
		}
		this.dataObjectMap.put(id, (T)object);
		return object;
	}
	
	@SuppressWarnings("unchecked")
	protected Object insert(Object object) {
		if (this.dataObjectMap == null) {
			this.dataObjectMap = new HashMap<Serializable,T>();
		}
		this.dataObjectMap.put(object.getClass(), (T)object);
		return object;
	}
	
	protected void clear() {
		this.dataObjectMap.clear();
	}

}
