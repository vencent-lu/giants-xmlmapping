/**
 * 
 */
package com.giants.xmlmapping.config;

import com.giants.xmlmapping.config.exception.XmlMapAttributeTypeErrorException;

/**
 * @author vencent.lu
 *
 */
public class XmlMapAttribute extends XmlMapItem {

	private static final long serialVersionUID = 8767645783888957776L;
	
	private Class<?> typeClass;
		
	/**
	 * @param name
	 * @param fieldName
	 * @param typeClass
	 * @throws XmlMapAttributeTypeErrorException 
	 */
	public XmlMapAttribute(String name, String fieldName, Class<?> typeClass)
			throws XmlMapAttributeTypeErrorException {
		super(name,fieldName);
		this.typeClass = typeClass;
	}

	public Class<?> getTypeClass() {
		return typeClass;
	}

	@Override
	public String toString() {
		return "XmlMapAttribute [typeClass=" + typeClass + ", name="
				+ getName() + ", fieldName=" + getFieldName() + "]";
	}
		
}
