
package org.springmodules.workflow.osworkflow.configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.query.WorkflowExpressionQuery;
import com.opensymphony.workflow.query.WorkflowQuery;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowStore;
import com.opensymphony.workflow.spi.memory.MemoryWorkflowStore;
import junit.framework.TestCase;

/**
 * @author robh
 */
public class ConfigurationBeanTests extends TestCase {

	public void testGetDefaultStore() throws Exception {
		ConfigurationBean bean = new ConfigurationBean();
		WorkflowStore store = bean.getWorkflowStore();

		assertNotNull("Default WorkflowStore should not be null", store);
		assertEquals("Incorrect default store type", MemoryWorkflowStore.class, store.getClass());
	}

	public void testGetCustomStore() throws Exception {
		ConfigurationBean bean = new ConfigurationBean();
		bean.setPersistence(MockWorkflowStore.class.getName());

		WorkflowStore store = bean.getWorkflowStore();

		assertNotNull("Custom store should not be null", store);
		assertEquals("Incorrect store type", MockWorkflowStore.class, store.getClass());
	}

	public void testWithPersistenceArgs() throws Exception {
		ConfigurationBean bean = new ConfigurationBean();
		bean.setPersistence(MockWorkflowStore.class.getName());

		Map args = new HashMap();
		bean.setPersistenceArgs(args);

		MockWorkflowStore store = (MockWorkflowStore) bean.getWorkflowStore();

		assertSame("Persistence args not carried to WorkflowStore", args, store.getArgs());
	}

	public void testNullWorkflowLocations() throws Exception {
		try {
			new ConfigurationBean().setWorkflowLocations(null);
			fail("Should not be able to set workflowLocations to null");
		}
		catch (IllegalArgumentException e) {
			// success
		}
	}

	public static class MockWorkflowStore implements WorkflowStore {

		private Map args;

		public void setEntryState(long entryId, int state) throws StoreException {
		}

		public PropertySet getPropertySet(long entryId) throws StoreException {
			return null;
		}

		public Step createCurrentStep(long entryId, int stepId, String owner, Date startDate, Date dueDate, String status, long[] previousIds) throws StoreException {
			return null;
		}

		public WorkflowEntry createEntry(String workflowName) throws StoreException {
			return null;
		}

		public List findCurrentSteps(long entryId) throws StoreException {
			return null;
		}

		public WorkflowEntry findEntry(long entryId) throws StoreException {
			return null;
		}

		public List findHistorySteps(long entryId) throws StoreException {
			return null;
		}

		public void init(Map props) throws StoreException {
			this.args = props;
		}

		public Map getArgs() {
			return this.args;
		}

		public Step markFinished(Step step, int actionId, Date finishDate, String status, String caller) throws StoreException {
			return null;
		}

		public void moveToHistory(Step step) throws StoreException {
		}

		public List query(WorkflowQuery query) throws StoreException {
			return null;
		}

		public List query(WorkflowExpressionQuery query) throws StoreException {
			return null;
		}
	}

}
