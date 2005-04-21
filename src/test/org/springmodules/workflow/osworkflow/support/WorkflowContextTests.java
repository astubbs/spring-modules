
package org.springmodules.workflow.osworkflow.support;

import junit.framework.TestCase;
import org.springmodules.workflow.osworkflow.OsWorkflowTemplate;

/**
 * @author robh
 */
public class WorkflowContextTests extends TestCase {

	public void setUp() {
		WorkflowContext.clear();
	}

	public void testGetAndSetCaller() throws Exception {
		final String caller = "robh";
		final String innerCaller = "janm";

		WorkflowContext.setCaller(caller);
		assertEquals("Invalid caller", caller, WorkflowContext.getCaller());

		Thread thread = new Thread(new Runnable() {
			public void run() {
				WorkflowContext.setCaller(innerCaller);
				assertEquals("Invalid caller", innerCaller, WorkflowContext.getCaller());
			}
		});

		thread.start();
		thread.join();

		assertEquals("Caller changed on spawned thread", caller, WorkflowContext.getCaller());
	}

	public void testGetCallerWithNoContext() {
		try {
			WorkflowContext.getCaller();
			fail("Should not be able to get caller with it being set");
		}
		catch (InvalidWorkflowContextStateException ex) {
			// success
		}
	}

	public void testGetAndSetInstanceId() throws Exception {
		final long instanceId = 1L;
		final long innerInstanceId = 2L;

		WorkflowContext.setInstanceId(instanceId);
		assertEquals("Invalid instanceId", instanceId, WorkflowContext.getInstanceId());

		Thread thread = new Thread(new Runnable() {
			public void run() {
				WorkflowContext.setInstanceId(innerInstanceId);
				assertEquals("Invalid instanceId", innerInstanceId, WorkflowContext.getInstanceId());
			}
		});

		thread.start();
		thread.join();

		assertEquals("Instance ID changed on spawned thread", instanceId, WorkflowContext.getInstanceId());
	}
}
