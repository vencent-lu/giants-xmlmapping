/**
 * 
 */
package com.giants.xmlmapping.config.exception;

import java.text.MessageFormat;

/**
 * @author vencent.lu
 *
 */
public class XmlMapUndefinedException extends XmlMapException {

	private static final long serialVersionUID = -7407144028343028898L;

	/**
	 * @param entityMapClass
	 */
	public XmlMapUndefinedException(Class<?> entityMapClass) {
		super(entityMapClass, MessageFormat.format("{0} undefined XmlEntity!", entityMapClass));
	}

}
