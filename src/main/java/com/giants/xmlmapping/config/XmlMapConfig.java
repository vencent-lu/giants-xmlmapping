/**
 * 
 */
package com.giants.xmlmapping.config;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.giants.common.lang.NamingConventionsUtils;
import com.giants.common.lang.StringUtil;
import com.giants.common.lang.reflect.FieldUtils;
import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlElement;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlIdKey;
import com.giants.xmlmapping.annotation.XmlManyElement;
import com.giants.xmlmapping.config.exception.XmlMapAttributeTypeErrorException;
import com.giants.xmlmapping.config.exception.XmlMapException;
import com.giants.xmlmapping.config.exception.XmlMapUndefinedEntityException;
import com.giants.xmlmapping.config.exception.XmlMapUndefinedException;

/**
 * @author vencent.lu
 *
 */
public class XmlMapConfig implements Serializable {

	private static final long serialVersionUID = 1488380307332115471L;
	
	private Map<String,XmlMapEntity> xmlMapEntitieies;

	/**
	 * @param xmlEntities
	 * @throws XmlMapException 
	 */
	public XmlMapConfig(Class<?>... xmlEntities) throws XmlMapException {
		super();
		if (ArrayUtils.isNotEmpty(xmlEntities)) {
			this.xmlMapEntitieies = new HashMap<String,XmlMapEntity>();
			for (Class<?> entityClass : xmlEntities) {				
				XmlMapEntity mapEntity = this.loadEntity(entityClass);
				this.xmlMapEntitieies.put(mapEntity.getName(), mapEntity);
			}
		}
	}
	
	public XmlMapEntity getXmlMapEntity(String name) {
		if (this.xmlMapEntitieies == null) {
			return null;
		}
		return this.xmlMapEntitieies.get(name);
	}
	
	private XmlMapEntity loadEntity(Class<?> entityClass) throws XmlMapException {
		XmlEntity xmlEntity = entityClass.getAnnotation(XmlEntity.class);
		if (xmlEntity != null) {
			XmlMapEntity mapEntity = new XmlMapEntity();
			mapEntity.setName(StringUtils.isEmpty(xmlEntity.name()) ? NamingConventionsUtils
							.initialsLowercase(entityClass.getSimpleName()) : xmlEntity	.name());
			mapEntity.setEntityClass(entityClass);
			Field[] fields = FieldUtils.getAvailableFields(entityClass);
			for (Field field : fields) {
				this.addMapProperty(mapEntity, field);
			}
			return mapEntity;
		} else {
			throw new XmlMapUndefinedException(entityClass);
		}
	}
	
	private void addMapProperty(XmlMapEntity mapEntity, Field field)
			throws XmlMapException {
		Class<?> entityClass = field.getDeclaringClass();
		XmlIdKey xmlIdKey = field.getAnnotation(XmlIdKey.class);
		if (xmlIdKey != null) {
			if (!field.getType().getPackage().getName().equals("java.lang")) {
				throw new XmlMapAttributeTypeErrorException(
						entityClass, field.getName(), field.getType());
			}
			mapEntity.setMapIdKey(new XmlMapIdKey(
					StringUtils.isEmpty(xmlIdKey.name()) ? field.getName() : xmlIdKey.name(),
					field.getName(),
					field.getType()));
			return;
		}
		XmlAttribute xmlAttribute = field.getAnnotation(XmlAttribute.class);
		if (xmlAttribute != null) {
			if (!field.getType().isPrimitive() && !field.getType().getPackage().getName().equals("java.lang")) {
				throw new XmlMapAttributeTypeErrorException(
						entityClass, field.getName(), field.getType());
			}
			String fieldName = field.getName();
			if (fieldName.startsWith("is") && field.getType() == boolean.class) {
				fieldName = StringUtil.capitalizeLower(fieldName.replaceFirst("is", ""));
			} 
			mapEntity.addMapAttribute(new XmlMapAttribute(
					StringUtils.isEmpty(xmlAttribute.name()) ? fieldName : xmlAttribute.name(),
					fieldName,
					field.getType()));
			return;
		}
		XmlElement xmlElement = field.getAnnotation(XmlElement.class);
		if (xmlElement != null) {
			if (field.getType() != entityClass) {
				mapEntity.addMapElement(new XmlMapElement(this.loadEntity(field
						.getType()), field.getName()));
			} else {
				mapEntity.addMapElement(new XmlMapElement(mapEntity, field
						.getName()));
			}
			return;
		}
		XmlManyElement xmlManyElement = field
				.getAnnotation(XmlManyElement.class);
		if (xmlManyElement != null) {
			Type fieldType = field.getGenericType();
			Class<?> filedEntityClass = null;
			if (fieldType instanceof ParameterizedType) {
				filedEntityClass = (Class<?>) ((ParameterizedType) fieldType).getActualTypeArguments()[0];
			} else {
				if (xmlManyElement.elementClass().equals(Object.class)) {
					throw new XmlMapUndefinedEntityException(entityClass,field.getName());
				}
				filedEntityClass = xmlManyElement.elementClass();
			}
			
			if (filedEntityClass != entityClass) {
				mapEntity.addMapElement(new XmlMapManyElement(this
						.loadEntity(filedEntityClass), field.getName(), field
						.getType()));
			} else {
				mapEntity.addMapElement(new XmlMapManyElement(mapEntity, field
						.getName(), field.getType()));
			}
		}
	}
	
}
