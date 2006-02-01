package org.springmodules.lucene.index.support.handler.object;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.springframework.aop.support.AopUtils;

/**
 * 
 * @author Thierry Templier
 */
public abstract class AbstractAttributeObjectDocumentHandler extends AbstractObjectDocumentHandler {

	public final static Object INDEXABLE_CLASS = new Object();
	public final static Object NON_INDEXABLE_CLASS = new Object();

	public static final Object KEYWORD = "keyword";

	public static final Object TEXT = "text";

	protected Map cache = new HashMap();

	protected Map indexableClasses = new HashMap();

	/**
	 * Determine a cache key for the given method and target class.
	 * Must not produce same key for overloaded methods.
	 * Must produce same key for different instances of the same method.
	 * @param method the method
	 * @param targetClass the target class (may be <code>null</code>)
	 * @return the cache key
	 */
	protected Object getCacheKey(Method method, Class targetClass) {
		// TODO this works fine, but could consider making it faster in future:
		// Method.toString() is relatively (although not disastrously) slow.
		return targetClass + "." + method;
	}
	
	/**
	 * Returns all the attributes found for the given method.
	 */
	protected abstract Collection findAllAttributes(Method method);

	/**
	 * Returns all the attributes found for the given class.
	 */
	protected abstract Collection findAllAttributes(Class clazz);

	/**
	 * Same return as getIndexAttribute method, but doesn't cache the result.
	 * getIndexAttribute is a caching decorator for this method.
	 */
	private IndexAttribute computeIndexAttribute(Method method, Class targetClass) {
		// The method may be on an interface, but we need attributes from the target class.
		// The AopUtils class provides a convenience method for this. If the target class
		// is null, the method will be unchanged.
		Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
		
		// First try is the method in the target class.
		IndexAttribute indexAtt = findIndexAttribute(findAllAttributes(specificMethod));
		if (indexAtt != null) {
			return indexAtt;
		}

		// Second try is the transaction attribute on the target class.
		indexAtt = findIndexAttribute(findAllAttributes(specificMethod.getDeclaringClass()));
		if (indexAtt != null) {
			return indexAtt;
		}

		if (specificMethod != method ) {
			// Fallback is to look at the original method.
			indexAtt = findIndexAttribute(findAllAttributes(method));
			if (indexAtt != null) {
				return indexAtt;
			}
			// Last fallback is the class of the original method.
			return findIndexAttribute(findAllAttributes(method.getDeclaringClass()));
		}
		return null;
	}

	public final IndexAttribute getIndexAttribute(Method method, Class targetClass) {
		// First, see if we have a cached value.
		Object cacheKey = getCacheKey(method, targetClass);
		Object cached = this.cache.get(cacheKey);
		if (cached != null) {
			// Value will either be canonical value indicating there is no transaction attribute,
			// or an actual transaction attribute.
			if (cached == NON_INDEXABLE_CLASS) {
				return null;
			}
			else {
				return (IndexAttribute) cached;
			}
		}
		else {
			// We need to work it out.
			IndexAttribute indexAtt = computeIndexAttribute(method, targetClass);
			// Put it in the cache.
			if (indexAtt == null) {
				this.cache.put(cacheKey, NON_INDEXABLE_CLASS);
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Adding index method [" + method.getName() + "] with attribute [" + indexAtt + "]");
				}
				this.cache.put(cacheKey, indexAtt);
			}
			return indexAtt;
		}
	}

	private Field createField(String fieldName, IndexAttribute indexAtt, Object value) {
		if( KEYWORD.equals(indexAtt.getType()) ) {
			return Field.Keyword(fieldName,String.valueOf(value));
		} else if( TEXT.equals(indexAtt.getType()) ) {
			return Field.Text(fieldName,String.valueOf(value));
		} else {
			return Field.Text(fieldName,String.valueOf(value));
		}
	}

	protected Document doGetDocument(Map description, Object object) throws Exception {
		Class clazz=object.getClass();
		Method[] methods=clazz.getDeclaredMethods();

		Document document = new Document();
		document.add(Field.Keyword("class",clazz.getCanonicalName()));
		for(int cpt=0;cpt<methods.length;cpt++) {
			String name=methods[cpt].getName();
			IndexAttribute indexAtt=computeIndexAttribute(methods[cpt], clazz);
			if( indexAtt==null || indexAtt.isExcluded() ) {
				continue;
			}
			if( name.startsWith(PREFIX_ACCESSOR) ) {
				String fieldName=null;
				if( !("".equals(indexAtt.getName())) ) {
					fieldName=indexAtt.getName();
				} else {
					fieldName=constructFieldName(name);
				}
				Object value=methods[cpt].invoke(object,new Object[] {});
				document.add(createField(fieldName, indexAtt, value));
			}
		}
		return document;
	}

	/**
	 * Return the indexable attribute, given this set of attributes
	 * attached to a method or class. Overrides method from parent class.
	 * This version actually converts JDK 5.0+ Annotations to the Spring
	 * classes. Returns null if it's not indexable.
	 * @param atts attributes attached to a method or class. May
	 * be <code>null</code>, in which case a null IndexAttribute will be returned.
	 * @return IndexAttribute configured indexable attribute,
	 * or <code>null</code> if none was found
	 */
	protected abstract IndexAttribute findIndexAttribute(Collection atts);

	protected abstract Object findIndexClassProperty(Class clazz);
	
	public boolean supports(Class clazz) {
		Object indexableClass=indexableClasses.get(clazz);
		if( indexableClass!=null && indexableClass==INDEXABLE_CLASS ) {
			return true;
		}
		indexableClass=findIndexClassProperty(clazz);
		indexableClasses.put(clazz,indexableClass);
		return (indexableClass==INDEXABLE_CLASS);
	}

}
