package org.springmodules.beans.factory.drivers.xml;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springmodules.beans.factory.drivers.Alias;
import org.springmodules.beans.factory.drivers.Bean;
import org.springmodules.beans.factory.drivers.BeanReference;
import org.springmodules.beans.factory.drivers.Instance;
import org.springmodules.beans.factory.drivers.LiteralList;
import org.springmodules.beans.factory.drivers.LiteralMap;
import org.springmodules.beans.factory.drivers.LiteralProperties;
import org.springmodules.beans.factory.drivers.LiteralValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlApplicationContextUtils {

	private static final String XERCES_XML_SERIALIZER = "org.apache.xml.serialize.XMLSerializer";
	private static final String JAVA_15_XML_SERIALIZER = "com.sun.org.apache.xml.internal.serialize.XMLSerializer";
	private static final String XERCES_DOCUMENT_IMPL = "org.apache.xerces.dom.DocumentImpl";
	private static final String JAVA_15_DOCUMENT_IMPL = "com.sun.org.apache.xerces.internal.dom.DocumentImpl";
	private static final String XERCES_OUTPUT_FORMAT = "org.apache.xml.serialize.OutputFormat";
	private static final String JAVA_15_OUTPUT_FORMAT = "com.sun.org.apache.xml.internal.serialize.OutputFormat";
	
	private XmlApplicationContextUtils() {
		super();
	}

	private interface CallBack {
		public Object execute() throws Exception;
	}
	
	private static Object execute(CallBack callBack) throws BeansException {
		try {
			return callBack.execute();
		} catch (Exception e) {
			Throwable targetException = null;
			if (e instanceof InvocationTargetException) {
				targetException = ((InvocationTargetException)e).getTargetException();
			} else {
				targetException = e;
			}
			throw new BeansException("Error occured", targetException) {};
		}
	}
	
	private static Document getNewDocument() throws BeansException {
		Class documentImplClass = null;
		try {
			if (documentImplClass == null) {
				documentImplClass = ClassUtils.forName(JAVA_15_DOCUMENT_IMPL);
			}
		} catch (ClassNotFoundException e) {
			// ignore.
		}
		try {
			if (documentImplClass == null) {
				documentImplClass = ClassUtils.forName(XERCES_DOCUMENT_IMPL);
			}
		} catch (ClassNotFoundException e) {
			// ignore.
		}
		if (documentImplClass != null) {
			final Class clazz = documentImplClass;
			return (Document)execute(new CallBack() {
				public Object execute() throws Exception {
					return clazz.newInstance();
				};
			});
		} else {
			throw new BeansException("Could not load [" + JAVA_15_DOCUMENT_IMPL + "] or [" + XERCES_DOCUMENT_IMPL + "]!") {};
		}
	}
	
	private static Object getNewOutputFormat() throws BeansException {
		Class outputFormatClass = null;
		try {
			if (outputFormatClass == null) {
				outputFormatClass = ClassUtils.forName(JAVA_15_OUTPUT_FORMAT);
			}
		} catch (ClassNotFoundException e) {
			// ignore.
		}
		try {
			if (outputFormatClass == null) {
				outputFormatClass = ClassUtils.forName(XERCES_OUTPUT_FORMAT);
			}
		} catch (ClassNotFoundException e) {
			// ignore.
		}
		
		if (outputFormatClass != null) {
			final Class clazz = outputFormatClass;
			return execute(new CallBack() {
				public Object execute() throws Exception {
					return clazz.newInstance();
				}
			});
		} else {
			throw new BeansException("Could not load [" + JAVA_15_OUTPUT_FORMAT + "] or [" + XERCES_OUTPUT_FORMAT + "]!") {};
		}
	}
	
	private static Class getXMLSerializerClass() {
		Class xmlSerializerClass = null;
		try {
			if (xmlSerializerClass == null) {
				xmlSerializerClass = ClassUtils.forName(JAVA_15_XML_SERIALIZER);
			}
		} catch (ClassNotFoundException e) {
			// ignore.
		}
		try {
			if (xmlSerializerClass == null) {
				xmlSerializerClass = ClassUtils.forName(XERCES_XML_SERIALIZER);
			} 
		} catch (ClassNotFoundException e) {
			// ignore.
		}
		
		if (xmlSerializerClass != null) {
			return xmlSerializerClass;
		} else {
			throw new BeansException("Could not load [" + JAVA_15_XML_SERIALIZER + "] or [" + XERCES_XML_SERIALIZER + "]!") {};
		}
	}
	
	private static void serialize(Element beans, Writer writer) throws BeansException {
		final Object outputFormat = getNewOutputFormat();
		BeanWrapper outputFormatBeanWrapper = new BeanWrapperImpl(outputFormat);
		outputFormatBeanWrapper.setPropertyValue("method", "XML");
		outputFormatBeanWrapper.setPropertyValue("encoding", "UTF-8");
		outputFormatBeanWrapper.setPropertyValue("indent", "1");
		outputFormatBeanWrapper.setPropertyValue("indenting", "true");
		Method setDocTypeMethod = (Method)execute(new CallBack() {
			public Object execute() throws Exception {
				return outputFormat.getClass().getMethod("setDoctype", new Class[] { String.class, String.class});
			}
		});
		invokeMethod(setDocTypeMethod, outputFormat, new Object[] { "-//SPRING//DTD BEAN//EN", "http://www.springframework.org/dtd/spring-beans.dtd"});
		final Class xmlSerializerClass = getXMLSerializerClass();
		Object xmlSerializer = getNewXMLSerializer(xmlSerializerClass, outputFormat, writer);
		Method asDOMSerializerMethod = (Method)execute(new CallBack() {
			public Object execute() throws Exception {
				return xmlSerializerClass.getMethod("asDOMSerializer", new Class[] {});
			}
		});
		invokeMethod(asDOMSerializerMethod, xmlSerializer, new Object[] {});
		Method serializeMethod = serializeMethod = (Method)execute(new CallBack() {
				public Object execute() throws Exception {
					return xmlSerializerClass.getMethod("serialize", new Class[] { Element.class });
				}
			}); 
		invokeMethod(serializeMethod, xmlSerializer, new Object[] { beans });
	}
	
	private static Object invokeMethod(Method method, Object target, Object[] values) throws BeansException {
		try {
			return method.invoke(target, values);
		} catch (Exception e) {
			Throwable targetException = null;
			if (e instanceof InvocationTargetException) {
				targetException = ((InvocationTargetException)e).getTargetException();
			} else {
				targetException = e;
			}
			throw new BeansException("Error invoking method [" + method + "] on target [" + target + "] with values [" + values + "]!", targetException) {};
		}
	}
	
	private static Object getNewXMLSerializer(final Class clazz, final Object outputformat, final Writer writer) {
		return execute(new CallBack() {
			public Object execute() throws Exception {
				Constructor constructor = clazz.getConstructor(new Class[] { Writer.class, outputformat.getClass()});
				return constructor.newInstance(new Object[] { writer, outputformat });				
			};
		});
	}
	
	public static Resource convert2xml(Collection beanReferences) throws BeansException {
		Document doc = getNewDocument();
		Element beans = doc.createElement("beans");
		doc.appendChild(beans);
		
		for (Iterator iter = beanReferences.iterator(); iter.hasNext();) {
			Object o = iter.next();
			
			if (o instanceof BeanReference) {
				BeanReference beanReference = (BeanReference)o;
				Element bean = createBean(beanReference, doc);
				beans.appendChild(bean);
			} else if (o instanceof Alias) {
				Alias alias = (Alias)o;
				Element aliasElement = createAlias(alias.getReferencedBean().getBeanName(), alias.getAlias(), doc);
				beans.appendChild(aliasElement);
			} else {
				throw new IllegalArgumentException("Type [" + o.getClass().getName() + "] is not supported by this application context driver!");
			}
		}
			
			
		StringWriter sw = new StringWriter();
		serialize(beans, sw);
		
		return new InputStreamResource(new ByteArrayInputStream(sw.getBuffer().toString().getBytes()));
	}
	
	private static Element createAlias(String beanName, String alias, Document doc) {
		Element aliasElement = doc.createElement("alias");
		
		aliasElement.setAttribute("name", beanName);
		aliasElement.setAttribute("alias", alias);
		
		return aliasElement;
	}
	
	private static Element createBean(BeanReference beanReference, Document doc) {
		Element bean = doc.createElement("bean");
		
		bean.setAttribute("id", beanReference.getBeanName());
		populateBean(bean, beanReference.getBean(), doc);
		
		return bean;
	}
	
	private static void populateBean(Element beanElement, Bean bean, Document doc) {
		if (bean.getClazz() != null) {
			beanElement.setAttribute("class", bean.getClazz().getName());
		}
		if (bean.getParent() != null) {
			beanElement.setAttribute("parent", bean.getParent().getBeanName());
		}
		if (bean.isAbstract()) {
			beanElement.setAttribute("abstract", "true");
		}
		if (bean.isLazy()) {
			beanElement.setAttribute("lazy-init", "true");
		}
		if (!bean.isSingleton()) {
			beanElement.setAttribute("singleton", "false");
		}
		if (StringUtils.hasLength(bean.getInitMethod())) {
			beanElement.setAttribute("init-method", bean.getInitMethod());
		}
		if (StringUtils.hasLength(bean.getDestroyMethod())) {
			beanElement.setAttribute("destroy-method", bean.getDestroyMethod());
		}
		if (bean.getDependsOn() != null && bean.getDependsOn().size() > 0) {
			String s = "";
			for (Iterator iter = bean.getDependsOn().iterator(); iter.hasNext();) {
				BeanReference beanReference = (BeanReference)iter.next();
				if (s.length() == 0) {
					s += beanReference.getBeanName();
				} else {
					s += "," + beanReference.getBeanName();
				}
			}
			beanElement.setAttribute("depends-on", s);
		}
		if (StringUtils.hasLength(bean.getAutowire())) {
			beanElement.setAttribute("autowire", bean.getAutowire());
		}
		
		if (StringUtils.hasLength(bean.getDescription())) {
			Element descriptionElement = doc.createElement("description");
			descriptionElement.appendChild(doc.createTextNode(bean.getDescription()));
			beanElement.appendChild(descriptionElement);
		}
		
		int counter = 0;
		for (Iterator iter = bean.getConstructorArguments().iterator(); iter.hasNext();) {
			Instance value = (Instance)iter.next();
			Element constructorArgumentElement = doc.createElement("constructor-arg");
			constructorArgumentElement.setAttribute("index", Integer.toString(counter));
			constructorArgumentElement.appendChild(createValue(value, doc));
			beanElement.appendChild(constructorArgumentElement);
			counter++;
		}
		
		for (Iterator iter = bean.getProperties().keySet().iterator(); iter.hasNext();) {
			String propertyName = (String)iter.next();
			Instance value = (Instance)bean.getProperties().get(propertyName);
			Element property = doc.createElement("property");
			property.setAttribute("name", propertyName);
			
			Element valueElement = createValue(value, doc);
			property.appendChild(valueElement);
			beanElement.appendChild(property);
		}
	}
	
	private static Element createValue(Instance value, Document doc) {
		if (value instanceof LiteralValue) {
			Element valueElement = doc.createElement("value");
			valueElement.appendChild(doc.createTextNode(((LiteralValue)value).getValue()));
			return valueElement;
		} else if (value instanceof LiteralList) {
			LiteralList list = (LiteralList)value;
			Element listElement = doc.createElement("list");
			for (Iterator iter = list.getElements().iterator(); iter.hasNext();) {
				Instance tmpValue = (Instance)iter.next();
				Element tmpElement = createValue(tmpValue, doc);
				listElement.appendChild(tmpElement);
			}
			return listElement;
		} else if (value instanceof LiteralMap) {
			LiteralMap map = (LiteralMap)value;
			Element mapElement = doc.createElement("map");
			for (Iterator iter = map.getMap().keySet().iterator(); iter.hasNext();) {
				LiteralValue key = (LiteralValue)iter.next();
				Instance tmpValue = (Instance)map.getMap().get(key);
				Element entryElement = doc.createElement("entry");
				entryElement.setAttribute("key", key.getValue());
				Element tmpElement = createValue(tmpValue, doc);
				entryElement.appendChild(tmpElement);
				mapElement.appendChild(entryElement);
			}
			return mapElement;
		} else if (value instanceof LiteralProperties) {
			LiteralProperties properties = (LiteralProperties)value;
			Element propertiesElement = doc.createElement("props");
			for (Iterator iter = properties.getProperties().keySet().iterator(); iter.hasNext();) {
				LiteralValue key = (LiteralValue)iter.next();
				LiteralValue tmpValue = (LiteralValue)properties.getProperties().get(key);
				Element propertyElement = doc.createElement("prop");
				propertyElement.setAttribute("key", key.getValue());
				propertyElement.appendChild(doc.createTextNode(tmpValue.getValue()));
				propertiesElement.appendChild(propertyElement);
			}
			return propertiesElement;
		} else if (value instanceof BeanReference) {
			BeanReference beanReference = (BeanReference)value;
			Element referenceElement = doc.createElement("ref");
			referenceElement.setAttribute("bean", beanReference.getBeanName());
			return referenceElement;
		} else if (value instanceof Bean) {
			Bean bean = (Bean)value;
			Element beanElement = doc.createElement("bean");
			populateBean(beanElement, bean, doc);
			return beanElement;
		}
		
		throw new IllegalArgumentException("Could not handle value class [" + value.getClass().getName() + "]!");
	}
}
