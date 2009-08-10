package org.springmodules.lucene.index.resource;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmodules.lucene.index.LuceneIndexingException;
import org.springmodules.lucene.index.factory.MockIndexFactory;

public class ResourceInterceptorTests extends TestCase {

	private static final String BEAN_ID = "test";
	private static final String INDEX_FACTORY_ID = "indexFactory";

	protected String getConfigLocation() {
		return "/org/springmodules/lucene/index/resource/applicationContext.xml";
	}

	public void testInterceptorWriterSuccess() throws Exception {
		System.out.println("1");
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext(getConfigLocation());
			IndexDao test = (IndexDao)context.getBean(BEAN_ID);
			MockIndexFactory mockIndexFactory = (MockIndexFactory)context.getBean(INDEX_FACTORY_ID);

			test.myMethod1();

			assertEquals(mockIndexFactory.getCallNumberWriter(), 1);
			assertEquals(mockIndexFactory.getCallNumberReader(), 0);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void testInterceptorWriterError() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(getConfigLocation());
		IndexDao test = (IndexDao)context.getBean(BEAN_ID);
		MockIndexFactory mockIndexFactory = (MockIndexFactory)context.getBean(INDEX_FACTORY_ID);

		try {
			test.myMethod2();
			fail();
		} catch(LuceneIndexingException ex) { }

		assertEquals(mockIndexFactory.getCallNumberWriter(), 1);
		assertEquals(mockIndexFactory.getCallNumberReader(), 0);
	}

	public void testInterceptorReaderSuccess() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(getConfigLocation());
		IndexDao test = (IndexDao)context.getBean(BEAN_ID);
		MockIndexFactory mockIndexFactory = (MockIndexFactory)context.getBean(INDEX_FACTORY_ID);

		test.myMethod3();

		assertEquals(mockIndexFactory.getCallNumberWriter(), 0);
		assertEquals(mockIndexFactory.getCallNumberReader(), 1);
	}

	public void testInterceptorReaderError() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(getConfigLocation());
		IndexDao test = (IndexDao)context.getBean(BEAN_ID);
		MockIndexFactory mockIndexFactory = (MockIndexFactory)context.getBean(INDEX_FACTORY_ID);

		try {
			test.myMethod4();
			fail();
		} catch(LuceneIndexingException ex) { }

		assertEquals(mockIndexFactory.getCallNumberWriter(), 0);
		assertEquals(mockIndexFactory.getCallNumberReader(), 1);
	}

}
