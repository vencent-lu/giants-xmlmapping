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
public class XmlDataUndefinedElementException extends XmlDataObjectException {

	private static final long serialVersionUID = -5710727170417348419L;
	
	private String elementName;

	/**
	 * @param mapEntity
	 * @param elementName
	 * @param object
	 */
	public XmlDataUndefinedElementException(XmlMapEntity mapEntity, String elementName,
			Object object) {
		super(mapEntity, object, MessageFormat.format(
				"Element {0} in {1} of undefined!", elementName, mapEntity));
		this.elementName = elementName;
	}

	public String getElementName() {
		return elementName;
	}

}
