package org.springmodules.lucene.index.resource;

import java.lang.reflect.Method;
import java.util.Properties;

import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class NameMatchResourceAttributeSourceTests extends TestCase {
	private static final String TEST_METHOD_NAME = "myMethod1";
	public static final String INDEX_READER_OPEN = "INDEX_READER_OPEN";
	public static final String INDEX_WRITER_OPEN = "INDEX_WRITER_OPEN";
	public static final String WRITE_INDEX_READER_AUTHORIZED = "WRITE_INDEX_READER_AUTHORIZED";
	public static final String WRITE_INDEX_WRITER_AUTHORIZED = "WRITE_INDEX_WRITER_AUTHORIZED";

	public void testResourceAttributeSourceNoConfiguration() throws Exception {
		NameMatchResourceAttributeSource source = new NameMatchResourceAttributeSource();
		
		Properties properties = new Properties();
		source.setProperties(properties);
		
		Method method = ClassUtils.getMethodIfAvailable(IndexDaoImpl.class, TEST_METHOD_NAME, new Class[] {});
		ResourceAttribute attr = source.getResourceAttribute(method, IndexDaoImpl.class);
		assertNull(attr);
	}

	public void testResourceAttributeSourceEmptyConfiguration() throws Exception {
		NameMatchResourceAttributeSource source = new NameMatchResourceAttributeSource();
		
		Properties properties = new Properties();
		properties.setProperty(TEST_METHOD_NAME, "");
		source.setProperties(properties);
		
		Method method = ClassUtils.getMethodIfAvailable(IndexDaoImpl.class, TEST_METHOD_NAME, new Class[] {});
		ResourceAttribute attr = source.getResourceAttribute(method, IndexDaoImpl.class);
		assertNull(attr);
	}

	public void testResourceAttributeSourceIndexReaderConfiguration() throws Exception {
		NameMatchResourceAttributeSource source = new NameMatchResourceAttributeSource();
		
		Properties properties = new Properties();
		properties.setProperty(TEST_METHOD_NAME, "INDEX_READER_OPEN, WRITE_INDEX_READER_AUTHORIZED");
		source.setProperties(properties);
		
		Method method = ClassUtils.getMethodIfAvailable(IndexDaoImpl.class, TEST_METHOD_NAME, new Class[] {});
		ResourceAttribute attr = source.getResourceAttribute(method, IndexDaoImpl.class);
		assertNotNull(attr);
		assertTrue(attr.isIndexReaderOpen());
		assertTrue(attr.isIndexReaderOpen());
		assertFalse(attr.isIndexWriterOpen());
		assertFalse(attr.isWriteOperationsForIndexWriterAuthorized());
	}

	public void testResourceAttributeSourceIndexWriterConfiguration() throws Exception {
		NameMatchResourceAttributeSource source = new NameMatchResourceAttributeSource();
		
		Properties properties = new Properties();
		properties.setProperty(TEST_METHOD_NAME, "INDEX_WRITER_OPEN, WRITE_INDEX_WRITER_AUTHORIZED");
		source.setProperties(properties);
		
		Method method = ClassUtils.getMethodIfAvailable(IndexDaoImpl.class, TEST_METHOD_NAME, new Class[] {});
		ResourceAttribute attr = source.getResourceAttribute(method, IndexDaoImpl.class);
		assertNotNull(attr);
		assertFalse(attr.isIndexReaderOpen());
		assertFalse(attr.isIndexReaderOpen());
		assertTrue(attr.isIndexWriterOpen());
		assertTrue(attr.isWriteOperationsForIndexWriterAuthorized());
	}
}
