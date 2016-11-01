/**
 * 
 */
package com.giants.xmlmapping.config;

/**
 * @author vencent.lu
 *
 */
public class XmlMapElement extends XmlMapItem {

	private static final long serialVersionUID = 4591333660048436762L;
	
	private XmlMapEntity mapEntity;
	
	/**
	 * @param mapEntity
	 * @param fieldName
	 */
	public XmlMapElement(XmlMapEntity mapEntity, String fieldName) {
		super(mapEntity.getName(), fieldName);
		this.mapEntity = mapEntity;
	}
	
	public XmlMapEntity getMapEntity() {
		return mapEntity;
	}

	@Override
	public String toString() {
		return "XmlMapElement [mapEntity=" + mapEntity + ", name="
				+ getName() + ", fieldName=" + getFieldName() + "]";
	}

}
