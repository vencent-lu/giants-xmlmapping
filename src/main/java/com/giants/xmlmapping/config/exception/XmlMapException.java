/**
 * 
 */
package com.giants.xmlmapping.config.exception;

/**
 * @author vencent.lu
 *
 */
public abstract class XmlMapException extends Exception {

	private static final long serialVersionUID = 8030006652739205647L;
	
	private Class<?> entityMapClass;

	/**
	 * @param entityMapClass
	 */
	public XmlMapException(Class<?> entityMapClass,String errorMessage) {
		super(errorMessage);
		this.entityMapClass = entityMapClass;
	}

	public Class<?> getEntityMapClass() {
		return entityMapClass;
	}	

}
