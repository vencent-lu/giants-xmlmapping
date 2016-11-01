/**
 * 
 */
package com.giants.xmlmapping.exception;

import java.text.MessageFormat;

/**
 * @author vencent.lu
 *
 */
public class XmlDataNotFindEntityException extends XmlDataException {

	private static final long serialVersionUID = -7374012679799447006L;

	/**
	 * @param entityName
	 */
	public XmlDataNotFindEntityException(String entityName) {
		super(entityName, MessageFormat.format("Can't find Entity[name='{0}']", entityName));
	}

}
