package org.springmodules.orm.db4o;

import java.io.IOException;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.Db4oDatabase;
import com.db4o.ext.Db4oUUID;
import com.db4o.ext.ExtClient;
import com.db4o.ext.ObjectInfo;
import com.db4o.ext.ObjectNotStorableException;
import com.db4o.ext.StoredClass;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.db4o.reflect.ReflectClass;
import com.db4o.reflect.generic.GenericReflector;
import com.db4o.reflect.jdk.JdkClass;
import com.db4o.reflect.jdk.JdkReflector;
import com.db4o.replication.ReplicationConflictHandler;
import com.db4o.replication.ReplicationProcess;
import com.db4o.types.Db4oCollections;

/**
 * Db4o Template tests.
 * 
 * @author Costin Leau
 *
 */
public class Db4oTemplateTests extends TestCase {
	private MockControl containerControl, objectSetControl;
	private ExtClient container;
	private ObjectSet set;

	private Db4oTemplate template;

	protected void setUp() throws Exception {
		super.setUp();
		containerControl = MockControl.createControl(ExtClient.class);
		container = (ExtClient) containerControl.getMock();
		objectSetControl = MockControl.createControl(ObjectSet.class);
		set = (ObjectSet) objectSetControl.getMock();

		template = new Db4oTemplate(container);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		try {
			containerControl.verify();
			objectSetControl.verify();
		}
		catch (IllegalStateException ex) {
			// ignore: test method didn't call replay
		}
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.execute(Db4oCallback)'
	 */
	public void testExecuteDb4oCallback() {
		final Object result = new Object();

		containerControl.replay();
		// callback for unproxied container
		Db4oCallback action = new Db4oCallback() {
			public Object doInDb4o(ObjectContainer cont) throws RuntimeException {
				assertEquals(container, cont);
				// compare proxy / native
				return result;
			}
		};

		// callback for proxied callback
		Db4oCallback proxiedAction = new Db4oCallback() {
			public Object doInDb4o(ObjectContainer cont) throws RuntimeException {
				// compare proxy / native
				assertFalse(container == cont);
				assertFalse(container.hashCode() == cont.hashCode());
				// if not proxied, mock control will throw an error
				assertFalse(cont.close());
				return result;
			}
		};

		// compare results
		assertSame(result, template.execute(action, true));
		assertSame(result, template.execute(proxiedAction, false));
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.activate(Object, int)'
	 */
	public void testActivate() {
		Object obj = new Object();
		int depth = 10;
		container.activate(obj, depth);
		containerControl.replay();
		template.activate(obj, depth);
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.deactivate(Object, int)'
	 */
	public void testDeactivate() {
		Object obj = new Object();
		int depth = 10;
		container.deactivate(obj, depth);
		containerControl.replay();
		template.deactivate(obj, depth);
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.delete(Object)'
	 */
	public void testDelete() {
		Object obj = new Object();
		container.delete(obj);
		containerControl.replay();
		template.delete(obj);
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.get(Object)'
	 */
	public void testGet() {
		Object obj = new Object();
		objectSetControl.replay();
		containerControl.expectAndReturn(container.get(obj), set);
		containerControl.replay();

		assertSame(set, template.get(obj));
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.query()'
	 */
	public void testQuery() {

		MockControl queryCtrl = MockControl.createControl(Query.class);
		Query query = (Query) queryCtrl.getMock();
		queryCtrl.replay();

		containerControl.expectAndReturn(container.query(), query);
		containerControl.replay();

		assertSame(query, template.query());
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.query(Predicate)'
	 */
	public void testQueryPredicate() {
		Predicate predicate = new Predicate() {
			public boolean match(Object candidate) {
				return false;
			};
		};

		objectSetControl.replay();
		containerControl.expectAndReturn(container.query(predicate), set);
		containerControl.replay();

		assertSame(set, template.query(predicate));
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.set(Object)'
	 */
	public void testSetObject() {
		Object obj = new Object();
		container.set(obj);
		containerControl.replay();

		template.set(obj);
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.backup(String)'
	 */
	public void testBackup() throws IOException {
		String backup = new String();
		container.backup(backup);
		containerControl.replay();

		template.backup(backup);

	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.bind(Object, long)'
	 */
	public void testBind() {
		Object obj = new Object();
		long id = 1234l;

		container.bind(obj, id);
		containerControl.replay();

		template.bind(obj, id);
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.collections()'
	 */
	public void testCollections() {
		MockControl colCtrl = MockControl.createControl(Db4oCollections.class);
		Db4oCollections col = (Db4oCollections) colCtrl.getMock();

		containerControl.expectAndReturn(container.collections(), col);
		containerControl.replay();
		colCtrl.replay();

		assertSame(col, template.collections());
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.getByID(long)'
	 */
	public void testGetByID() {
		Object result = new Object();
		long id = 1234l;

		containerControl.expectAndReturn(container.getByID(id), result);
		containerControl.replay();

		assertSame(result, template.getByID(id));
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.getByUUID(Db4oUUID)'
	 */
	public void testGetByUUID() {
		long id = 1234l;
		Db4oUUID uuid = new Db4oUUID(id, new byte[] {});
		Object result = new Object();

		containerControl.expectAndReturn(container.getByUUID(uuid), result);
		containerControl.replay();

		assertSame(result, template.getByUUID(uuid));

	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.getID(Object)'
	 */
	public void testGetID() {
		long id = 1234l;
		Object result = new Object();

		containerControl.expectAndReturn(container.getByID(id), result);
		containerControl.replay();

		assertSame(result, template.getByID(id));

	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.getObjectInfo(Object)'
	 */
	public void testGetObjectInfo() {

		MockControl infoCtrl = MockControl.createControl(ObjectInfo.class);
		ObjectInfo info = (ObjectInfo) infoCtrl.getMock();

		Object obj = new Object();

		containerControl.expectAndReturn(container.getObjectInfo(obj), info);
		containerControl.replay();
		infoCtrl.replay();

		assertSame(info, template.getObjectInfo(obj));
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.identity()'
	 */
	public void testIdentity() {
		Db4oDatabase result = new Db4oDatabase();

		containerControl.expectAndReturn(container.identity(), result);
		containerControl.replay();
		assertSame(result, template.identity());
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.isActive(Object)'
	 */
	public void testIsActive() {
		boolean result = false;
		Object obj = new Object();

		containerControl.expectAndReturn(container.isActive(obj), result);
		containerControl.replay();
		assertFalse(template.isActive(obj));

	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.isCached(long)'
	 */
	public void testIsCached() {
		boolean result = false;
		long id = 12345l;

		containerControl.expectAndReturn(container.isCached(id), result);
		containerControl.replay();
		assertFalse(template.isCached(id));
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.isClosed()'
	 */
	public void testIsClosed() {
		boolean result = false;

		containerControl.expectAndReturn(container.isClosed(), result);
		containerControl.replay();
		assertFalse(template.isClosed());
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.isStored(Object)'
	 */
	public void testIsStored() {
		boolean result = false;
		Object obj = new Object();

		containerControl.expectAndReturn(container.isStored(obj), result);
		containerControl.replay();
		assertFalse(template.isStored(obj));
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.knownClasses()'
	 */
	public void testKnownClasses() {
		ReflectClass[] result = new ReflectClass[] {};

		containerControl.expectAndReturn(container.knownClasses(), result);
		containerControl.replay();
		assertSame(result, template.knownClasses());
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.lock()'
	 */
	public void testLock() {
		Object lock = new Object();

		containerControl.expectAndReturn(container.lock(), lock);
		containerControl.replay();
		assertSame(lock, template.lock());
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.migrateFrom(ObjectContainer)'
	 */
	public void testMigrateFrom() {
		MockControl objCtrl = MockControl.createControl(ObjectContainer.class);
		ObjectContainer obj = (ObjectContainer) objCtrl.getMock();

		container.migrateFrom(obj);
		containerControl.replay();
		template.migrateFrom(obj);
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.peekPersisted(Object, int, boolean)'
	 */
	public void testPeekPersisted() {
		Object obj = new Object();
		int depth = 123;
		boolean committed = false;

		Object result = new Object();

		containerControl.expectAndReturn(container.peekPersisted(obj, depth, committed), result);
		containerControl.replay();
		assertSame(result, template.peekPersisted(obj, depth, committed));
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.purge()'
	 */
	public void testPurge() {
		container.purge();
		containerControl.replay();
		template.purge();
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.purge(Object)'
	 */
	public void testPurgeObject() {
		Object obj = new Object();

		container.purge(obj);
		containerControl.replay();
		template.purge(obj);
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.reflector()'
	 */
	public void testReflector() {
		MockControl refCtrl = MockClassControl.createControl(GenericReflector.class);
		GenericReflector reflector = (GenericReflector) refCtrl.getMock();
		refCtrl.replay();
		
		containerControl.expectAndReturn(container.reflector(), reflector);
		containerControl.replay();
		assertSame(reflector, template.reflector());
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.refresh(Object, int)'
	 */
	public void testRefresh() {
		Object obj = new Object();
		int depth = 1234;
		
		container.refresh(obj, depth);
		containerControl.replay();
		template.refresh(obj, depth);
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.releaseSemaphore(String)'
	 */
	public void testReleaseSemaphore() {
		String name = "";
		
		container.releaseSemaphore(name);
		containerControl.replay();
		template.releaseSemaphore(name);
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.replicationBegin(ObjectContainer, ReplicationConflictHandler)'
	 */
	public void testReplicationBegin() {
		MockControl processCtrl = MockControl.createControl(ReplicationProcess.class);
		MockControl handlerCtrl = MockControl.createControl(ReplicationConflictHandler.class);
		ReplicationProcess process = (ReplicationProcess)processCtrl.getMock(); 
	    ReplicationConflictHandler handler = (ReplicationConflictHandler) handlerCtrl.getMock();
		MockControl peerControl = MockControl.createControl(ObjectContainer.class);
		ObjectContainer peer = (ObjectContainer) peerControl.getMock();
		processCtrl.replay(); handlerCtrl.replay(); peerControl.replay();
		
		containerControl.expectAndReturn(container.replicationBegin(peer, handler), process);
		containerControl.replay();
		
		assertSame(process, template.replicationBegin(peer, handler));
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.set(Object, int)'
	 */
	public void testSetObjectInt() {
		Object obj = new Object();
		int depth = 123;

		container.set(obj, depth);
		containerControl.replay();
		template.set(obj, depth);
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.setSemaphore(String, int)'
	 */
	public void testSetSemaphore() {
		boolean result = false;
		String name = "";
		int wait = 123;
		
		containerControl.expectAndReturn(container.setSemaphore(name, wait), result);
		containerControl.replay();
		assertFalse(template.setSemaphore(name, wait));

	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.storedClass(Object)'
	 */
	public void testStoredClass() {
		MockControl classCtrl = MockControl.createControl(StoredClass.class);
		StoredClass clazz = (StoredClass) classCtrl.getMock();
		
		Object obj = new Object();
		 
		containerControl.expectAndReturn(container.storedClass(obj), clazz);
		containerControl.replay();
		assertSame(clazz, template.storedClass(obj));
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.storedClasses()'
	 */
	public void testStoredClasses() {
		StoredClass[] result = new StoredClass[] {};
		
		Object obj = new Object();
		 
		containerControl.expectAndReturn(container.storedClasses(), result);
		containerControl.replay();
		assertSame(result, template.storedClasses());
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.version()'
	 */
	public void testVersion() {
		long result = 1234;
		
		containerControl.expectAndReturn(container.version(), result);
		containerControl.replay();
		assertEquals(result, template.version());
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.switchToFile(String)'
	 */
	public void testSwitchToFile() {
		String file = "";
		
		container.switchToFile(file);
		containerControl.replay();
		template.switchToFile(file);
	}

	/*
	 * Test method for 'org.db4ospring.Db4oTemplate.switchToMainFile()'
	 */
	public void testSwitchToMainFile() {
		container.switchToMainFile();
		containerControl.replay();	
		template.switchToMainFile();
	}

	/*
	 * Test method for 'org.db4ospring.Db4oAccessor.afterPropertiesSet()'
	 */
	public void testAfterPropertiesSet() {
		try {
			template.setObjectContainer(null);
			template.afterPropertiesSet();
			fail("expected illegal argument exception");
		}
		catch (RuntimeException e) {
			// it's okay. it's expected exception
		}
	}

	/*
	 * Test method for 'org.db4ospring.Db4oAccessor.convertDb4oAccessException(Exception)'
	 */
	public void testConvertDb4oAccessException() {

		try {
			createTemplate().execute(new Db4oCallback() {
				public Object doInDb4o(ObjectContainer container) {
					throw new DatabaseFileLockedException();
				}
			});
			fail("Should have thrown DataAccessResourceFailureException");
		}
		catch (DataAccessResourceFailureException ex) {
			// expected
		}
		try {
			createTemplate().execute(new Db4oCallback() {
				public Object doInDb4o(ObjectContainer container) {
					ReflectClass refClass = new JdkClass(new JdkReflector(getClass().getClassLoader()),
							this.getClass());
					throw new ObjectNotStorableException(refClass);
				}
			});
			fail("Should have thrown InvalidDataAccessApiUsageException");
		}
		catch (InvalidDataAccessApiUsageException ex) {
			// expected
		}

	}

	private Db4oOperations createTemplate() {
		containerControl.reset();

		Db4oTemplate tmpl = new Db4oTemplate(container);
		containerControl.replay();
		return tmpl;
	}
}
