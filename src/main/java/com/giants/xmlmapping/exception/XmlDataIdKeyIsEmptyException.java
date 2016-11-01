/**
 * 
 */
package com.giants.xmlmapping.exception;

import java.text.MessageFormat;

import com.giants.xmlmapping.config.XmlMapEntity;
import com.giants.xmlmapping.config.XmlMapIdKey;

/**
 * @author vencent.lu
 *
 */
public class XmlDataIdKeyIsEmptyException extends XmlDataObjectException {

	private static final long serialVersionUID = 7191396928905903182L;
	
	private XmlMapIdKey mapIdKey;

	/**
	 * @param mapEntity
	 * @param objectXml
	 * @param idKeyName
	 */
	public XmlDataIdKeyIsEmptyException(XmlMapEntity mapEntity, Object object) {
		super(mapEntity, object, MessageFormat.format(
				"{0} primary key {1} value is empty!", mapEntity,
				mapEntity.getMapIdKey()));
		this.mapIdKey = mapEntity.getMapIdKey();
	}

	public XmlMapIdKey getMapIdKey() {
		return mapIdKey;
	}

}
