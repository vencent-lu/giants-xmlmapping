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
public abstract class XmlDataObjectException extends XmlDataException {

	private static final long serialVersionUID = 7042604333904141972L;
	
	private XmlMapEntity mapEntity;
	private Object object;

	/**
	 *
	 * @param mapEntity
	 * @param object
	 * @param message
	 */
	public XmlDataObjectException(XmlMapEntity mapEntity, Object object, String message) {
		super(mapEntity.getName(), MessageFormat.format("{0} XML elements are as follows:\n{1}", message,object.toString()));
		this.mapEntity = mapEntity;
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

	public XmlMapEntity getMapEntity() {
		return mapEntity;
	}

}
