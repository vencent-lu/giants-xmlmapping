/**
 * 
 */
package com.giants.xmlmapping.exception;

/**
 * @author vencent.lu
 *
 */
public abstract class XmlDataException extends Exception {

	private static final long serialVersionUID = 564806345086798145L;
	
	private String entityName;

	/**
	 * @param message
	 * @param entityName
	 */
	public XmlDataException(String entityName,String message) {
		super(message);
		this.entityName = entityName;
	}

	public String getEntityName() {
		return entityName;
	}

}
