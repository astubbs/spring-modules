/*
 * Copyright 2002-2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.lucene.index.document.handler.object;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springmodules.lucene.util.IOUtils;

/**
 * Implementation of the DocumentHandler interface which allows to index an
 * object based on a configuration in a properties file.
 * 
 * In order to make participate a bean in the indexing, the following line must
 * be present in the properties file:
 *  MyClass.indexable=true
 * 
 * Then, for each field of the class, the different properties must be set:
 *  MyClass.field.myField.indexable=true
 *  MyClass.field.myField.type=Text
 *  MyClass.field.myField.indexable=true
 * 
 * @author Thierry Templier
 */
public class PropertiesDocumentHandler extends AbstractAttributeObjectDocumentHandler implements InitializingBean {

	private static final String ELEMENT_SEPARATOR = ".";
	private static final String FIELD_SEPARATOR = "field";
	private static final String FIELD_ELEMENT_SEPARATOR = ".field.";
	private Properties properties;
	private Resource propertiesFileName;
	
	private void loadPropertiesIfNecessary() {
		if( properties==null ) {
			properties=IOUtils.loadPropertiesFromResource(propertiesFileName);
		}
	}
	
	/**
	 * Returns all the properties found for the given method.
	 */
	protected Collection findAllAttributes(Method method) {
		loadPropertiesIfNecessary();
		Collection attributes=new ArrayList();
		Set keys=properties.keySet();
		for(Iterator i=keys.iterator();i.hasNext();) {
			String key=(String)i.next();
			String fieldName=method.getName();
			if( fieldName.startsWith(PREFIX_ACCESSOR) ) {
				fieldName=constructFieldName(fieldName);
			}
			if( key.startsWith(method.getDeclaringClass().getName()) 
					&& key.indexOf(ELEMENT_SEPARATOR+fieldName+ELEMENT_SEPARATOR)!=-1 ) {
				String value=(String)properties.get(key);
				attributes.add(new AttributeData(key,value));
			}
		}
		return attributes;
	}

	/**
	 * Returns all the properties found for the given class.
	 */
	protected Collection findAllAttributes(Class clazz) {
		loadPropertiesIfNecessary();
		Collection attributes=new ArrayList();
		Set keys=properties.keySet();
		for(Iterator i=keys.iterator();i.hasNext();) {
			String key=(String)i.next();
			if( key.startsWith(clazz.getName())
					&& key.indexOf(FIELD_ELEMENT_SEPARATOR)==-1 ) {
				String value=(String)properties.get(key);
				attributes.add(new AttributeData(key,value));
			}
		}
		return attributes;
	}

	protected IndexAttribute findIndexAttribute(Collection atts) {
		if (atts == null) {
			return null;
		}

		Map attsMap=new HashMap();
		for (Iterator i=atts.iterator();i.hasNext();) {
			AttributeData data=(AttributeData)i.next();
			String attributeName=data.getAttributeName();
			int indice=-1;
			if( (indice=attributeName.indexOf(FIELD_SEPARATOR))!=-1 ) {
				attributeName=attributeName.substring(indice+7);
				if( (indice=attributeName.indexOf(ELEMENT_SEPARATOR))!=-1 ) {
					attributeName=attributeName.substring(indice+1);
				}
			}
			attsMap.put(attributeName,data.getAttributeValue());
		}
		
		if( attsMap.size()==0 ) {
			return null;
		} else {
			DefaultIndexAttribute dia = new DefaultIndexAttribute();
			String name=(String)attsMap.get("name");
			if( name==null ) {
				name="";
			}
			dia.setName(name);
			String type=(String)attsMap.get("type");
			if( type==null ) {
				type="Text";
			}
			if( type!=null ) {
				type=type.toLowerCase();
			}
			dia.setType(type);
			String exclude=(String)attsMap.get("exclude");
			if( exclude!=null ) {
				dia.setExcluded(exclude.equals("true"));
			}

			return dia;
		}
	}

	protected Object findIndexClassProperty(Class clazz) {
		loadPropertiesIfNecessary();
		String value=(String)properties.get(clazz.getName()+".indexable");
		if( value!=null && "true".equals(value) ) {
			return INDEXABLE_CLASS;
		} else {
			return NON_INDEXABLE_CLASS;
		}
	}

	public Resource getPropertiesFileName() {
		return propertiesFileName;
	}

	public void setPropertiesFileName(Resource propertiesFileName) {
		this.propertiesFileName = propertiesFileName;
	}

	private class AttributeData {
		private String attributeName;
		private String attributeValue;

		public AttributeData(String attributeName, String attributeValue) {
			this.attributeName=attributeName;
			this.attributeValue=attributeValue;
		}
		
		public String getAttributeName() {
			return attributeName;
		}

		public void setAttributeName(String attributeName) {
			this.attributeName = attributeName;
		}

		public String getAttributeValue() {
			return attributeValue;
		}

		public void setAttributeValue(String attributeValue) {
			this.attributeValue = attributeValue;
		}
	}

	public void afterPropertiesSet() throws Exception {
        if (propertiesFileName == null) {
            throw new BeanInitializationException(
                    "Must specify a name of a property file");
        }
	}
}
