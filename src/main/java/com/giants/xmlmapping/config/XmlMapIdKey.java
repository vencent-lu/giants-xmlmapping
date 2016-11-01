/**
 * 
 */
package com.giants.xmlmapping.config;

import com.giants.xmlmapping.config.exception.XmlMapAttributeTypeErrorException;

/**
 * @author vencent.lu
 *
 */
public class XmlMapIdKey extends XmlMapAttribute {

	private static final long serialVersionUID = -1265840169006198167L;

	/**
	 * @param name
	 * @param fieldName
	 * @param typeClass
	 * @throws XmlMapAttributeTypeErrorException
	 */
	public XmlMapIdKey(String name, String fieldName, Class<?> typeClass)
			throws XmlMapAttributeTypeErrorException {
		super(name, fieldName, typeClass);
	}

	@Override
	public String toString() {
		return "XmlMapIdKey [typeClass=" + getTypeClass() + ", name="
				+ getName() + ", fieldName=" + getFieldName() + "]";
	}

}
