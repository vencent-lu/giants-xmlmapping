/**
 * 
 */
package com.giants.xmlmapping.config;

/**
 * @author vencent.lu
 *
 */
public class XmlMapManyElement extends XmlMapElement {

	private static final long serialVersionUID = 1989902781592134795L;
	
	private Class<?> collectionTypeClass;
	
	/**
	 * @param mapEntity
	 * @param fieldName
	 * @param collectionTypeClass
	 */
	public XmlMapManyElement(XmlMapEntity mapEntity, String fieldName,
			Class<?> collectionTypeClass) {
		super(mapEntity, fieldName);
		this.collectionTypeClass = collectionTypeClass;
	}

	public Class<?> getCollectionTypeClass() {
		return collectionTypeClass;
	}

	@Override
	public String toString() {
		return "XmlMapManyElement [collectionTypeClass=" + collectionTypeClass
				+ ", mapEntity()=" + getMapEntity() + ", name()="
				+ getName() + ", fieldName()=" + getFieldName() + "]";
	}

}
