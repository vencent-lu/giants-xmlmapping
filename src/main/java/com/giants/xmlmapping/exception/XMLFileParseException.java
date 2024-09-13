/**
 * 
 */
package com.giants.xmlmapping.exception;

import java.text.MessageFormat;

/**
 * @author vencent.lu
 *
 */
public class XMLFileParseException extends XMLParseException {

	private static final long serialVersionUID = 5599549569823912346L;
	
	private Object xmlFile;

	/**
	 *
	 * @param xmlFile
	 * @param e
	 */
	public XMLFileParseException(Object xmlFile,Throwable e) {
		super(MessageFormat.format("Read xml document:{0} error!",
				xmlFile), e);
		this.xmlFile = xmlFile;
	}

	public Object getXmlFile() {
		return xmlFile;
	}

}
