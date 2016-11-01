/**
 * 
 */
package com.giants.xmlmapping.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vencent.lu
 *
 */
public class XmlMapEntity implements Serializable {

	private static final long serialVersionUID = -4539835377249700258L;
	
	private String name;
	private Class<?> entityClass;
	private XmlMapIdKey mapIdKey;
	private Map<String,XmlMapAttribute> mapAttributes;
	private Map<String,XmlMapElement> mapElements;
	
	public void addMapAttribute(XmlMapAttribute mapAttribute) {
		if (this.mapAttributes == null) {
			this.mapAttributes = new HashMap<String,XmlMapAttribute>();
		}
		this.mapAttributes.put(mapAttribute.getName(), mapAttribute);
	}
	
	public XmlMapAttribute getMapAttribute(String name) {
		if (this.mapAttributes == null) {
			return null;
		}
		return this.mapAttributes.get(name);
	}
	
	public Collection<XmlMapAttribute> getAllMapAttribute() {
		if (this.mapAttributes == null) {
			return null;
		}
		return this.mapAttributes.values();
	}
	
	public void addMapElement(XmlMapElement mapElement) {
		if (this.mapElements == null) {
			this.mapElements = new HashMap<String,XmlMapElement>();
		}
		this.mapElements.put(mapElement.getName(), mapElement);
	}
	
	public XmlMapElement getMapElement(String name) {
		if (this.mapElements == null) {
			return null;
		}
		return this.mapElements.get(name);
	}
	
	public Collection<XmlMapElement> getAllMapElement() {
		if (this.mapElements == null) {
			return null;
		}
		return this.mapElements.values();
	} 
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Class<?> getEntityClass() {
		return entityClass;
	}
	
	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public XmlMapIdKey getMapIdKey() {
		return mapIdKey;
	}

	public void setMapIdKey(XmlMapIdKey mapIdKey) {
		this.mapIdKey = mapIdKey;
	}

	@Override
	public String toString() {
		return "XmlMapEntity [name=" + name + ", entityClass=" + entityClass
				+ "]";
	}
	
}