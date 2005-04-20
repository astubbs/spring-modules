
package org.springmodules.workflow.osworkflow.support;

import java.util.Map;

import junit.framework.TestCase;

/**
 * @author robh
 */
public class WorkflowContextManagerTests extends TestCase {

	public void setUp() {
		WorkflowContextManager.clearContext();
	}
	public void testBindAndUnbind() {
		Object key1 = new Object();
		Object key2 = new Object();
		Object val1 = new Object();
		Object val2 = new Object();

		WorkflowContextManager.bindResource(key1, val1);
		WorkflowContextManager.bindResource(key2, val2);

		assertEquals(val1, WorkflowContextManager.getResource(key1));
		assertEquals(val2, WorkflowContextManager.getResource(key2));
	}

	public void getHasResource() {
		WorkflowContextManager.bindResource("foo", new Object());
		assertTrue(WorkflowContextManager.hasResource("foo"));
		assertFalse(WorkflowContextManager.hasResource(new Object()));
	}

	public void testGetResourcesAsMap() {
		WorkflowContextManager.bindResource("foo", "bar");
		WorkflowContextManager.bindResource("bar", "foo");

		Map map = WorkflowContextManager.getResourcesAsMap();
		assertEquals(2, map.size());

		assertEquals("bar", map.get("foo"));
		assertEquals("foo", map.get("bar"));

		try {
			map.put("something", "something");
			fail("Map should be unmodifiable");
		}
		catch (UnsupportedOperationException ex) {
			// success
		}
	}

	public void testClearContext() {
		WorkflowContextManager.bindResource("foo", "bar");
		WorkflowContextManager.clearContext();
		assertEquals(0, WorkflowContextManager.getResourcesAsMap().size());
	}

	public void testThreadedAccess() throws InterruptedException {
		final Object key = new Object();

		final Object val1 = new Object();
		final Object val2 = new Object();

		WorkflowContextManager.bindResource(key, val1);
		assertEquals(val1, WorkflowContextManager.getResource(key));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				WorkflowContextManager.bindResource(key, val2);
				assertEquals(val2, WorkflowContextManager.getResource(key));
			}
		});

		thread.start();
		thread.join();
		assertEquals(val1, WorkflowContextManager.getResource(key));
	}
}
