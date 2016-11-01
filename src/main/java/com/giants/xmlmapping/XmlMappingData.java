/**
 * 
 */
package com.giants.xmlmapping;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.giants.common.beanutils.BeanUtils;
import com.giants.common.lang.NamingConventionsUtils;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.config.XmlMapAttribute;
import com.giants.xmlmapping.config.XmlMapConfig;
import com.giants.xmlmapping.config.XmlMapElement;
import com.giants.xmlmapping.config.XmlMapEntity;
import com.giants.xmlmapping.config.XmlMapManyElement;
import com.giants.xmlmapping.config.exception.XmlMapException;
import com.giants.xmlmapping.config.exception.XmlMapUndefinedException;
import com.giants.xmlmapping.exception.XMLConfigParseException;
import com.giants.xmlmapping.exception.XMLFileParseException;
import com.giants.xmlmapping.exception.XMLParseException;
import com.giants.xmlmapping.exception.XmlDataException;
import com.giants.xmlmapping.exception.XmlDataIdKeyIsEmptyException;
import com.giants.xmlmapping.exception.XmlDataNotFindEntityException;
import com.giants.xmlmapping.exception.XmlDataUndefinedAttributeException;
import com.giants.xmlmapping.exception.XmlDataUndefinedElementException;

/**
 * @author vencent.lu
 *
 */
public class XmlMappingData implements Serializable {

	private static final long serialVersionUID = 7945536350728370638L;
	
	private SAXReader xmlReader = new SAXReader();
	private OutputFormat outputFormat = new OutputFormat("    ");
	
	private XmlMapConfig xmlMapConfig;
	private Map<Class<?>,XmlDataModule<?>> dataObjectModule;
	private List<String> xmlFilePathes = new ArrayList<String>();
	
	/**
	 * @param xmlMapConfig
	 */
	public XmlMappingData(XmlMapConfig xmlMapConfig) {
		super();
		this.xmlMapConfig = xmlMapConfig;
	}
	
	/**
	 * @param xmlMapConfig
	 * @param xmlFilePathes
	 * @throws XMLParseException 
	 * @throws XmlDataException 
	 */
	public XmlMappingData(XmlMapConfig xmlMapConfig, String... xmlFilePathes)
			throws XmlDataException, XMLParseException {
		super();
		this.xmlMapConfig = xmlMapConfig;
		this.loadXmls(xmlFilePathes);
	}
	
	/**
	 * @param xmlMapConfig
	 * @param xmlInputStream
	 * @throws XmlDataException
	 * @throws XMLParseException
	 */
	public XmlMappingData(XmlMapConfig xmlMapConfig, InputStream xmlInputStream)
			throws XmlDataException, XMLParseException {
		super();
		this.xmlMapConfig = xmlMapConfig;
		this.loadXml(xmlInputStream);
	}
	
	/**
	 * @param xmlEntities
	 * @throws XmlMapException 
	 */
	public XmlMappingData(Class<?>... xmlEntities) throws XmlMapException {
		super();
		this.xmlMapConfig = new XmlMapConfig(xmlEntities);
	}
	
	/**
	 * @param xmlEntitie
	 * @param xmlInputStream
	 * @throws XmlMapException
	 * @throws XmlDataException
	 * @throws XMLParseException
	 */
	public XmlMappingData(Class<?> xmlEntitie, InputStream xmlInputStream)
			throws XmlMapException, XmlDataException, XMLParseException {
		super();
		this.xmlMapConfig = new XmlMapConfig(xmlEntitie);
		this.loadXml(xmlInputStream);
	}
	
	/**
	 * @param xmlEntities
	 * @param xmlFilePathes
	 * @throws XmlMapException 
	 * @throws XMLParseException 
	 * @throws XmlDataException 
	 */
	public XmlMappingData(Class<?>[] xmlEntities, String... xmlFilePathes)
			throws XmlMapException, XmlDataException, XMLParseException {
		super();
		this.xmlMapConfig = new XmlMapConfig(xmlEntities);
		this.loadXmls(xmlFilePathes);
	}
	
	/**
	 * @param xmlEntities
	 * @param xmlInputStream
	 * @throws XmlMapException
	 * @throws XmlDataException
	 * @throws XMLParseException
	 */
	public XmlMappingData(Class<?>[] xmlEntities, InputStream xmlInputStream)
			throws XmlMapException, XmlDataException, XMLParseException {
		super();
		this.xmlMapConfig = new XmlMapConfig(xmlEntities);
		this.loadXml(xmlInputStream);
	}
	
	public void setOutputNewlines(boolean newLines) {
		this.outputFormat.setNewlines(newLines);
	}
	
	public void setOutputEncoding(String encoding) {
		this.outputFormat.setEncoding(encoding);
	}
	
	@SuppressWarnings("unchecked")
	public <T> XmlDataModule<T> getDataModule(Class<T> moduleClass) {
		if (this.dataObjectModule == null) {
			return null;
		}
		return (XmlDataModule<T>) this.dataObjectModule.get(moduleClass);
	}
	
	private <T> void addDataModule(Class<T> moduleClass) {
		if (this.dataObjectModule == null) {
			this.dataObjectModule = new HashMap<Class<?>,XmlDataModule<?>>();
		}
		this.dataObjectModule.put(moduleClass, new XmlDataModule<T>());
	}
	
	private void loadRoot(Element root) throws XmlDataException,
			XMLParseException {
		XmlMapEntity mapEntity = this.xmlMapConfig.getXmlMapEntity(root.getName());
		if (mapEntity == null) {
			throw new XmlDataNotFindEntityException(root.getName());
		}
		if (this.getDataModule(mapEntity.getEntityClass()) == null) {
			this.addDataModule(mapEntity.getEntityClass());
		}
		Object dataObj = this.loadElement(mapEntity, root);
		try {
			if (mapEntity.getMapIdKey() != null) {
				this.getDataModule(mapEntity.getEntityClass()).insert(
						(Serializable) PropertyUtils.getProperty(
								dataObj, mapEntity.getMapIdKey()
										.getFieldName()), dataObj);
			} else {
				this.getDataModule(mapEntity.getEntityClass()).insert(
						dataObj);
			}
			
		} catch (Exception e) {
			throw new XMLConfigParseException(root.asXML(), mapEntity.getEntityClass(), e);
		}
	}
	
	public void loadXml(InputStream xmlInputStream) throws XmlDataException,
			XMLParseException {
		if (xmlInputStream != null) {
			Element root;
			try {
				root = this.xmlReader.read(xmlInputStream).getRootElement();
			} catch (DocumentException e) {
				throw new XMLFileParseException(xmlInputStream,e);
			}
			this.loadRoot(root);
		}
	}
	
	public void loadXmls(String... xmlFilePathes) throws XmlDataException,
			XMLParseException {
		if (ArrayUtils.isNotEmpty(xmlFilePathes)) {
			for (String xmlFilePath : xmlFilePathes) {
				try {
					Enumeration<URL> urlEnumer = this.getClass().getClassLoader()
							.getResources(xmlFilePath);
					while(urlEnumer.hasMoreElements()) {
						Element root;				
						try {
							root = this.xmlReader.read(
									urlEnumer.nextElement().openStream())
									.getRootElement();
						} catch (DocumentException e) {
							throw new XMLFileParseException(xmlFilePath, e);
						}
						this.loadRoot(root);
						this.xmlFilePathes.add(xmlFilePath);
					}
				} catch (IOException e) {
					throw new XMLFileParseException(xmlFilePath,e);
				}				
			}
		}
	}
	
	private Element buildElement(XmlMapEntity xmlMapEntity, Object object)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, XmlDataIdKeyIsEmptyException {
		Element element = DocumentHelper.createElement(xmlMapEntity.getName());
		if (xmlMapEntity.getMapIdKey() != null) {
			Object IdKeyObj = BeanUtils.getPropertyValue(object, xmlMapEntity.getMapIdKey().getFieldName());
			if (IdKeyObj == null) {
				throw new XmlDataIdKeyIsEmptyException(xmlMapEntity, object);
			}
			element.addAttribute(xmlMapEntity.getMapIdKey().getName(), IdKeyObj.toString());
		}
		
		Collection<XmlMapAttribute> xmlMapAttributes = xmlMapEntity.getAllMapAttribute();
		if (CollectionUtils.isNotEmpty(xmlMapAttributes)) {
			for (XmlMapAttribute xmlMapAttribute : xmlMapAttributes) {
				Object attObj = BeanUtils.getPropertyValue(object, xmlMapAttribute.getFieldName());
				if (attObj != null) {
					element.addAttribute(xmlMapAttribute.getName(), attObj.toString());
				}					
			}
		}
		
		Collection<XmlMapElement> xmlMapElements = xmlMapEntity.getAllMapElement();
		if (CollectionUtils.isNotEmpty(xmlMapElements)) {
			for (XmlMapElement xmlMapElement : xmlMapElements) {
				Object elementObj = BeanUtils.getPropertyValue(object, xmlMapElement.getFieldName());
				if (elementObj != null) {					
					XmlMapEntity mapEntity = xmlMapElement.getMapEntity();
					if (!(elementObj instanceof Collection<?>)) {
						element.add(this.buildElement(mapEntity, elementObj));
					} else {
						Collection<?> collectionObj = (Collection<?>)elementObj;
						Iterator<?> it = collectionObj.iterator();
						while(it.hasNext()) {
							element.add(this.buildElement(mapEntity, it.next()));
						}
					}
				}
			}
		}
		
		return element;
	}
	
	private Document buildDocument(Class<?> moduleClass)
			throws XmlDataIdKeyIsEmptyException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		XmlDataModule<?> dataModule = this.getDataModule(moduleClass);
		Document document = DocumentHelper.createDocument();
		if (dataModule != null) {
			Object object = dataModule.get();
			
			if (object != null) {
				XmlEntity xmlEntity = moduleClass.getAnnotation(XmlEntity.class);
				String name = StringUtils.isEmpty(xmlEntity.name()) ? NamingConventionsUtils
						.initialsLowercase(moduleClass.getSimpleName()) : xmlEntity	.name();
				XmlMapEntity xmlMapEntity = this.xmlMapConfig.getXmlMapEntity(name);
				document.setRootElement(this.buildElement(xmlMapEntity, object));
			}
		}		
		return document;
	}
	
	public void writeXml(Class<?> moduleClass, OutputStream outputStream)
			throws XmlDataIdKeyIsEmptyException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, IOException {
		Document doc = this.buildDocument(moduleClass);
		XMLWriter xmlWriter = new XMLWriter(outputStream, this.outputFormat);
		xmlWriter.write(doc);
		xmlWriter.close();
	}
	
	public void writeXml(Class<?> moduleClass, String outFilePath)
			throws XmlDataIdKeyIsEmptyException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			FileNotFoundException, IOException {
		this.writeXml(moduleClass, new FileOutputStream(outFilePath));
	}
	
	public String buildXmlString(Class<?> moduleClass)
			throws XmlDataIdKeyIsEmptyException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, IOException {
		Document doc = this.buildDocument(moduleClass);
		StringWriter out = new StringWriter();
		XMLWriter xmlWriter = new XMLWriter(out, this.outputFormat);
		xmlWriter.write(doc);
		xmlWriter.flush();
		return out.toString();
	}
	
	public void loadObject(Object object) throws XmlMapUndefinedException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Class<?> moduleClass = object.getClass();
		XmlEntity xmlEntity = moduleClass.getAnnotation(XmlEntity.class);
		String name = StringUtils.isEmpty(xmlEntity.name()) ? NamingConventionsUtils
				.initialsLowercase(moduleClass.getSimpleName()) : xmlEntity
				.name();
		XmlMapEntity xmlMapEntity = this.xmlMapConfig.getXmlMapEntity(name);
		
		if (xmlMapEntity == null) {
			throw new XmlMapUndefinedException(moduleClass);
		}
		
		if (this.getDataModule(moduleClass) == null) {
			this.addDataModule(moduleClass);
		}

		if (xmlMapEntity.getMapIdKey() != null) {
			this.getDataModule(moduleClass).insert((Serializable) PropertyUtils.getProperty(object,
					xmlMapEntity.getMapIdKey().getFieldName()), object);
		} else {
			this.getDataModule(moduleClass).insert(object);
		}
	}
	
	public void clearObjects(Class<?> moduleClass) {
		this.getDataModule(moduleClass).clear();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object loadElement(XmlMapEntity mapEntity, Element element)
			throws XmlDataException, XMLParseException {
		Object object;
		try {
			object = mapEntity.getEntityClass().getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new XMLConfigParseException(element.asXML(),mapEntity.getEntityClass(), e);
		}
		if (mapEntity.getMapIdKey() != null) {
			String idKeyStr = element.attributeValue(mapEntity.getMapIdKey().getName());
			idKeyStr = StringUtils.isEmpty(idKeyStr) ? element.elementTextTrim(mapEntity.getMapIdKey().getName()) : idKeyStr;
			if (StringUtils.isEmpty(idKeyStr)) {
				throw new XmlDataIdKeyIsEmptyException(mapEntity, element.asXML());
			}
			try {
				BeanUtils.copyProperty(object, mapEntity.getMapIdKey()
						.getFieldName(), this.instance(mapEntity.getMapIdKey()
						.getTypeClass(), idKeyStr));
			} catch (Exception e) {
				throw new XMLConfigParseException(element.asXML(), mapEntity.getEntityClass(), e);
			}
		}		
		
		for (Attribute attribute : (List<Attribute>) element.attributes()) {
			if (mapEntity.getMapIdKey() == null
					|| !attribute.getName().equals(
							mapEntity.getMapIdKey().getName())) {
				XmlMapAttribute mapAttribute = mapEntity
						.getMapAttribute(attribute.getName());
				if (mapAttribute == null) {
					throw new XmlDataUndefinedAttributeException(mapEntity,
							attribute.getName(), element.asXML());
				}
				try {
					BeanUtils.copyProperty(object, mapAttribute.getFieldName(),
							this.instance(mapAttribute.getTypeClass(),
									attribute.getValue()));
				} catch (Exception e) {
					throw new XMLConfigParseException(element.asXML(),
							mapEntity.getEntityClass(), e);
				}
			}
		}
		
		Map<XmlMapManyElement, Collection> collectionObject = new HashMap<XmlMapManyElement, Collection>();
		for (Element entiry : (List<Element>)element.elements()) {
			String text = entiry.getTextTrim();
			if (StringUtils.isEmpty(text)){
				XmlMapElement mapElement = mapEntity.getMapElement(entiry.getName());
				if (mapElement == null) {
					throw new XmlDataUndefinedElementException(mapEntity,
							entiry.getName(), entiry.asXML());
				}			
				if (mapElement instanceof XmlMapManyElement) {
					XmlMapManyElement mapManyElement = (XmlMapManyElement)mapElement;
					if (collectionObject.get(mapManyElement) == null) {
						collectionObject.put(mapManyElement, this
								.instanceCollection(mapManyElement
										.getCollectionTypeClass(), mapManyElement
										.getMapEntity().getEntityClass()));
					}
					collectionObject.get(mapManyElement)
							.add(this.loadElement(mapManyElement.getMapEntity(),entiry));
				} else {
					try {
						BeanUtils.copyProperty(object, mapElement.getFieldName(),
								this.loadElement(mapElement.getMapEntity(), entiry));
					} catch (Exception e) {
						throw new XMLConfigParseException(element.asXML(), mapEntity.getEntityClass(), e);
					}
				}
			} else {
				if (!entiry.getName().equals(
						mapEntity.getMapIdKey().getName())) {
					XmlMapAttribute mapAttribute = mapEntity.getMapAttribute(entiry.getName());
					if (mapAttribute == null) {
						throw new XmlDataUndefinedAttributeException(
								mapEntity, entiry.getName(),
								element.asXML());
					}
					try {
						BeanUtils.copyProperty(object, mapAttribute
								.getFieldName(), this.instance(
								mapAttribute.getTypeClass(), text));
					} catch (Exception e) {
						throw new XMLConfigParseException(element.asXML(), mapEntity.getEntityClass(), e);
					}
				}
			}			
		}
		
		if (MapUtils.isNotEmpty(collectionObject)) {
			Iterator<Entry<XmlMapManyElement, Collection>> it = collectionObject.entrySet().iterator();
			while (it.hasNext()) {
				Entry<XmlMapManyElement, Collection> entry = it.next();
				try {
					BeanUtils.copyProperty(object, entry.getKey().getFieldName(), entry.getValue());
				} catch (Exception e) {
					throw new XMLConfigParseException(element.asXML(), mapEntity.getEntityClass(), e);
				}
			}
		}
		
		return object;
	}
	
	private <T> Collection<T> instanceCollection(Class<?> CollectionType, Class<T> elementType) {
		if (CollectionType.equals(List.class)) {
			return new ArrayList<T>();
		}
		if (CollectionType.equals(Set.class)) {
			return new HashSet<T>();
		}
		return null;
	}
	
	private Object instance(Class<?> type, String value)
			throws ClassNotFoundException {
		if (type == String.class) {
			return value;
		}
		if (type == Integer.class || type == int.class) {
			return Integer.valueOf(value);
		}
		if (type == Boolean.class || type == boolean.class) {
			return Boolean.valueOf(value);
		}
		if (type == Long.class || type == long.class) {
			return Long.valueOf(value);
		}
		if (type == Short.class || type == short.class) {
			return Short.valueOf(value);
		}
		if (type == Double.class || type == double.class) {
			return Double.valueOf(value);
		}
		if (type == Float.class || type == float.class) {
			return Float.valueOf(value);
		}
		if (type == Byte.class || type == byte.class) {
			return Byte.valueOf(value);
		}
		if (type.equals(Class.class)) {
			return Class.forName(value);
		}
		return null;
	}

}
