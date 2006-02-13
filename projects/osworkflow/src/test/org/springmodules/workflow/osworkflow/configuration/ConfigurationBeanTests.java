package org.springmodules.workflow.osworkflow.configuration;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.springframework.util.ClassUtils;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.StoreException;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.query.WorkflowExpressionQuery;
import com.opensymphony.workflow.query.WorkflowQuery;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowStore;
import com.opensymphony.workflow.spi.memory.MemoryWorkflowStore;
import com.opensymphony.workflow.util.DefaultVariableResolver;
import com.opensymphony.workflow.util.VariableResolver;

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
		args.put("foo", "bar");
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

	public void testNullAndEmptyPersistenceArgs() throws Exception {
		try {
			new ConfigurationBean().setPersistenceArgs(null);
			fail("Should not be able to set persistenceArgs to null");
		}
		catch (IllegalArgumentException ex) {
			// success
		}

		try {
			new ConfigurationBean().setPersistenceArgs(new HashMap());
			fail("Should not be able to set persistenceArgs to an empty Map");
		}
		catch (IllegalArgumentException ex) {
			// success
		}
	}

	public void testLoadWorkflows() throws Exception {
		Properties locations = new Properties();
		locations.put("foo", ClassUtils.classPackageAsResourcePath(getClass()) + "/fooFlow.xml");
		locations.put("bar", ClassUtils.classPackageAsResourcePath(getClass()) + "/barFlow.xml");

		ConfigurationBean bean = new ConfigurationBean();
		bean.setWorkflowLocations(locations);

		String[] names = bean.getWorkflowNames();
		Arrays.sort(names);
		assertTrue(Arrays.binarySearch(names, "foo") > -1);
		assertTrue(Arrays.binarySearch(names, "bar") > -1);

		WorkflowDescriptor wd = bean.getWorkflow("foo");
		assertNotNull("Workflow descriptor should not be null", wd);

	}

	public void testGetNonExistentWorkflowDescriptor() throws Exception {
		try {
			new ConfigurationBean().getWorkflow("foo");
			fail("Accessing a non-existent workflow should throw FactoryException");
		}
		catch (FactoryException ex) {
			// success
		}
	}

	public void testRemoveWorkflowIsUnsupported() throws Exception {
		try {
			new ConfigurationBean().removeWorkflow("foo");
			fail("removeWorkflow should throw UnsupportedOperationException");
		}
		catch (UnsupportedOperationException ex) {
			// success
		}
	}
	
	public void testVariableResolver() throws Exception {
		ConfigurationBean cfg = new ConfigurationBean();
		VariableResolver defaultResolver = cfg.getVariableResolver();
		assertNotNull(defaultResolver);
		VariableResolver userResolver = new DefaultVariableResolver();
		cfg.setVariableResolver(userResolver);
		assertSame(userResolver, cfg.getVariableResolver());
		cfg.setVariableResolver(null);
		assertSame(defaultResolver, cfg.getVariableResolver());
	}
	
	public void testUserDefinedWorkflowStore() throws Exception {
		MockWorkflowStore mockStore = new MockWorkflowStore();
		// do smth on the store to make sure it's our and not overwritten
		Map marker = new HashMap();
		marker.put("foo", "bar");
		mockStore.init(marker);

		ConfigurationBean cfg = new ConfigurationBean();
		cfg.setWorkflowStore(mockStore);

		MockWorkflowStore store = (MockWorkflowStore) cfg.getWorkflowStore();
		assertSame("Stores are not the same", mockStore, store);
		assertSame("Persistence args not carried to WorkflowStore", marker, store.getArgs());
	}

	public static class MockWorkflowStore implements WorkflowStore {

		private Map args;

		public void setEntryState(long entryId, int state) throws StoreException {
		}

		public PropertySet getPropertySet(long entryId) throws StoreException {
			return null;
		}

		public Step createCurrentStep(long entryId, int stepId, String owner, Date startDate, Date dueDate,
				String status, long[] previousIds) throws StoreException {
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

		public Step markFinished(Step step, int actionId, Date finishDate, String status, String caller)
				throws StoreException {
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
