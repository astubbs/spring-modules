
package org.springmodules.workflow.osworkflow;

import junit.framework.TestCase;

/**
 * @author robh
 */
public class ThreadLocalWorkflowContextManagerTests extends TestCase {

	private WorkflowContextManager manager = new ThreadLocalWorkflowContextManager();

	public void setUp() {
		this.manager.clear();
	}

	public void testGetAndSetCaller() throws Exception {
		final String caller = "robh";
		final String innerCaller = "janm";

		this.manager.setCaller(caller);
		assertEquals("Invalid caller", caller, this.manager.getCaller());

		Thread thread = new Thread(new Runnable() {
			public void run() {
				ThreadLocalWorkflowContextManagerTests.this.manager.setCaller(innerCaller);
				assertEquals("Invalid caller", innerCaller, ThreadLocalWorkflowContextManagerTests.this.manager.getCaller());
			}
		});

		thread.start();
		thread.join();

		assertEquals("Caller changed on spawned thread", caller, this.manager.getCaller());
	}

	public void testGetCallerWithNoContext() {
		try {
			this.manager.getCaller();
			fail("Should not be able to get caller with it being set");
		}
		catch (InvalidWorkflowContextStateException ex) {
			// success
		}
	}

	public void testGetInstanceIdWithNoContext() {
		try {
			this.manager.getInstanceId();
			fail("Should not be able to get instance Id with it being set");
		}
		catch (InvalidWorkflowContextStateException ex) {
			// success
		}
	}

	public void testHasInstanceId() throws Exception {
		assertFalse(this.manager.isInstanceIdBound());
		this.manager.setInstanceId(1);
		assertTrue(this.manager.isInstanceIdBound());
	}

	public void testGetAndSetInstanceId() throws Exception {
		final long instanceId = 1L;
		final long innerInstanceId = 2L;

		this.manager.setInstanceId(instanceId);
		assertEquals("Invalid instanceId", instanceId, this.manager.getInstanceId());

		Thread thread = new Thread(new Runnable() {
			public void run() {
				ThreadLocalWorkflowContextManagerTests.this.manager.setInstanceId(innerInstanceId);
				assertEquals("Invalid instanceId", innerInstanceId, ThreadLocalWorkflowContextManagerTests.this.manager.getInstanceId());
			}
		});

		thread.start();
		thread.join();

		assertEquals("Instance ID changed on spawned thread", instanceId, this.manager.getInstanceId());
	}
}
