/**
 * 
 */
package com.giants.xmlmapping.config;

import java.io.Serializable;

/**
 * @author vencent.lu
 *
 */
public abstract class XmlMapItem implements Serializable {

	private static final long serialVersionUID = 6906139484534051538L;
	
	private String name;
	private String fieldName;
	
	/**
	 * @param name
	 * @param fieldName
	 */
	public XmlMapItem(String name, String fieldName) {
		super();
		this.name = name;
		this.fieldName = fieldName;
	}
	
	public String getName() {
		return name;
	}

	public String getFieldName() {
		return fieldName;
	}
	
}
