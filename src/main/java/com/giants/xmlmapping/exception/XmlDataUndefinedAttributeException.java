/**
 * 
 */
package com.giants.xmlmapping.exception;

import java.text.MessageFormat;

import com.giants.xmlmapping.config.XmlMapEntity;

/**
 * @author vencent.lu
 *
 */
public class XmlDataUndefinedAttributeException extends XmlDataObjectException {

	private static final long serialVersionUID = 1107011226756957124L;
	
	private String attributeName;

	/**
	 *
	 * @param mapEntity
	 * @param attributeName
	 * @param object
	 */
	public XmlDataUndefinedAttributeException(XmlMapEntity mapEntity,
			String attributeName, Object object) {
		super(mapEntity, object, MessageFormat.format(
				"Attribute {0} in {1} of undefined!", attributeName, mapEntity));
		this.attributeName = attributeName;
	}

	public String getAttributeName() {
		return attributeName;
	}

}
