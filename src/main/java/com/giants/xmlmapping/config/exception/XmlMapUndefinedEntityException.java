/**
 * 
 */
package com.giants.xmlmapping.config.exception;

import java.text.MessageFormat;

/**
 * @author vencent.lu
 *
 */
public class XmlMapUndefinedEntityException extends XmlMapException {

	private static final long serialVersionUID = -6595521632761782220L;
	
	private String fieldName;

	/**
	 * @param entityMapClass
	 * @param fieldName
	 */
	public XmlMapUndefinedEntityException(Class<?> entityMapClass,
			String fieldName) {
		super(entityMapClass, MessageFormat.format("{0} undefined {1} mapping entity!", entityMapClass,fieldName));
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

}
