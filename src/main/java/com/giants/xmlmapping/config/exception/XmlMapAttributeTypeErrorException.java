/**
 * 
 */
package com.giants.xmlmapping.config.exception;

import java.text.MessageFormat;

/**
 * @author vencent.lu
 *
 */
public class XmlMapAttributeTypeErrorException extends XmlMapException {

	private static final long serialVersionUID = 7812782557870172468L;
	
	private String fieldName;
	private Class<?> type;
	/**
	 * @param entityMapClass
	 * @param fieldName
	 * @param type
	 */
	public XmlMapAttributeTypeErrorException(Class<?> entityMapClass,
			String fieldName, Class<?> type) {
		super(entityMapClass, MessageFormat.format(
				"{0} of the {1} does not support {3}, Please use base type!",
				entityMapClass, fieldName, type));
		this.fieldName = fieldName;
		this.type = type;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public Class<?> getType() {
		return type;
	}	

}
