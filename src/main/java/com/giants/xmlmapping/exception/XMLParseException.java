package com.giants.xmlmapping.exception;

/**
 * @author vencent.lu
 *
 */
public abstract class XMLParseException extends Exception {

	private static final long serialVersionUID = 2907023560218097771L;

	/**
	 * @param message
	 * @param e
	 */
	public XMLParseException(String message, Throwable e) {
		super(message, e);
		// TODO Auto-generated constructor stub
	}
	
	/*private String xmlFilePath;

	public XMLParseException(String xmlFilePath, String message) {
		super(message);
		this.xmlFilePath = xmlFilePath;
	}

	public String getXmlFilePath() {
		return xmlFilePath;
	}*/

}
