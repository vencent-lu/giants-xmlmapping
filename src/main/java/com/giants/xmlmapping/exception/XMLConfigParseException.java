/**
 * 
 */
package com.giants.xmlmapping.exception;

import java.text.MessageFormat;

/**
 * @author vencent.lu
 *
 */
public class XMLConfigParseException extends XMLParseException {

	private static final long serialVersionUID = 1642629561499407908L;
	
	private String elementXml;
	private Class<?> xmlMapConfigClass;

	/**
	 *
	 * @param elementXml
	 * @param xmlMapConfigClass
	 * @param e
	 */
	public XMLConfigParseException(String elementXml,
			Class<?> xmlMapConfigClass, Throwable e) {
		super(
				MessageFormat
						.format("XmlMapEntity config error! Details as follows:\n xml element:{0} \n xml map config class:{1}",
								elementXml, xmlMapConfigClass), e);
		this.elementXml = elementXml;
		this.xmlMapConfigClass = xmlMapConfigClass;
	}

	public String getElementXml() {
		return elementXml;
	}

	public Class<?> getXmlMapConfigClass() {
		return xmlMapConfigClass;
	}

}
