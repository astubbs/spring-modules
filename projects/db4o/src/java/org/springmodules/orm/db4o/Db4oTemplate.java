/**
 * Created on Nov 5, 2005
 *
 * $Id: Db4oTemplate.java,v 1.2 2007/02/27 12:12:59 costin Exp $
 * $Revision: 1.2 $
 */
package org.springmodules.orm.db4o;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.dao.DataAccessException;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.Db4oDatabase;
import com.db4o.ext.Db4oException;
import com.db4o.ext.Db4oUUID;
import com.db4o.ext.ExtClient;
import com.db4o.ext.ExtObjectContainer;
import com.db4o.ext.ObjectInfo;
import com.db4o.ext.StoredClass;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.db4o.reflect.ReflectClass;
import com.db4o.reflect.generic.GenericReflector;
import com.db4o.replication.ReplicationConflictHandler;
import com.db4o.replication.ReplicationProcess;
import com.db4o.types.Db4oCollections;

/**
 * Db4o template.
 * 
 * @author Costin Leau
 * 
 */
public class Db4oTemplate extends Db4oAccessor implements Db4oOperations {

	private boolean exposeNativeContainer = false;

	public Db4oTemplate() {
	}

	public Db4oTemplate(ObjectContainer container) {
		setObjectContainer(container);
		afterPropertiesSet();
	}

	/**
	 * @see org.db4ospring.Db4oOperations#execute(org.db4ospring.Db4oCallback)
	 */
	public Object execute(Db4oCallback callback) throws DataAccessException {
		return execute(callback, isExposeNativeContainer());
	}

	/**
	 * @see org.db4ospring.Db4oOperations#execute(org.db4ospring.Db4oCallback,
	 * boolean)
	 */
	public Object execute(Db4oCallback callback, boolean exposeNativeContainer) throws DataAccessException {
		ObjectContainer cont = getObjectContainer();
		try {
			ObjectContainer container = (exposeNativeContainer ? cont : createContainerProxy(cont));
			Object result = callback.doInDb4o(container);
			// check out caching/query support
			return result;
		}
		catch (Db4oException ex) {
			throw convertDb4oAccessException(ex);
		}
		catch (RuntimeException ex) {
			RuntimeException convEx = convertDb4oAccessException(ex);
			// it's user specific
			if (convEx instanceof Db4oSystemException)
				throw ex;
			// it's a converted exception
			throw convEx;
		}
	}

	//
	// ObjectContainer interface methods
	//
	/**
	 * @see org.db4ospring.Db4oOperations#activate(java.lang.Object, int)
	 */
	public void activate(final java.lang.Object obj, final int depth) {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				container.activate(obj, depth);
				return null;
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#deactivate(java.lang.Object, int)
	 */
	public void deactivate(final java.lang.Object obj, final int depth) {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				container.deactivate(obj, depth);
				return null;
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#delete(java.lang.Object)
	 */
	public void delete(final java.lang.Object obj) {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				container.delete(obj);
				return null;
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#get(java.lang.Object)
	 */
	public ObjectSet get(final java.lang.Object template) {
		return (ObjectSet) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return container.get(template);
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#query()
	 */
	public Query query() {
		return (Query) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return container.query();
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#query(com.db4o.query.Predicate)
	 */
	public ObjectSet query(final Predicate predicate) {
		return (ObjectSet) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return container.query(predicate);
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#set(java.lang.Object)
	 */
	public void set(final Object obj) {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				container.set(obj);
				return null;
			}
		}, true);
	}

	//
	// ExtObjectContainer interface
	//

	/**
	 * @see org.db4ospring.Db4oOperations#backup(java.lang.String)
	 */
	public void backup(final java.lang.String path) {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				// special hack
				// the IOException appears out of the blue inside the API which
				// has only RuntimeExceptions
				try {
					((ExtObjectContainer) container).backup(path);
					return null;
				}
				catch (IOException e) {
					throw convertDb4oAccessException(e);
				}
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#bind(java.lang.Object, long)
	 */
	public void bind(final java.lang.Object obj, final long id) {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				((ExtObjectContainer) container).bind(obj, id);
				return null;
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#collections()
	 */
	public Db4oCollections collections() {
		return (Db4oCollections) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return ((ExtObjectContainer) container).collections();
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#getByID(long)
	 */
	public Object getByID(final long ID) {
		return execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return ((ExtObjectContainer) container).getByID(ID);
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#getByUUID(com.db4o.ext.Db4oUUID)
	 */
	public Object getByUUID(final Db4oUUID uuid) {
		return execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return ((ExtObjectContainer) container).getByUUID(uuid);
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#getID(java.lang.Object)
	 */
	public long getID(final java.lang.Object obj) {
		return ((Long) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return new Long(((ExtObjectContainer) container).getID(obj));
			}
		}, true)).longValue();
	}

	/**
	 * @see org.db4ospring.Db4oOperations#getObjectInfo(java.lang.Object)
	 */
	public ObjectInfo getObjectInfo(final java.lang.Object obj) {
		return (ObjectInfo) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return ((ExtObjectContainer) container).getObjectInfo(obj);
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#identity()
	 */
	public Db4oDatabase identity() {
		return (Db4oDatabase) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return ((ExtObjectContainer) container).identity();
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#isActive(java.lang.Object)
	 */
	public boolean isActive(final java.lang.Object obj) {
		return ((Boolean) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return new Boolean(((ExtObjectContainer) container).isActive(obj));
			}
		}, true)).booleanValue();
	}

	/**
	 * @see org.db4ospring.Db4oOperations#isCached(long)
	 */
	public boolean isCached(final long ID) {
		return ((Boolean) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return new Boolean(((ExtObjectContainer) container).isCached(ID));
			}
		}, true)).booleanValue();
	}

	/**
	 * @see org.db4ospring.Db4oOperations#isClosed()
	 */
	public boolean isClosed() {
		return ((Boolean) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return new Boolean(((ExtObjectContainer) container).isClosed());
			}
		}, true)).booleanValue();
	}

	/**
	 * @see org.db4ospring.Db4oOperations#isStored(java.lang.Object)
	 */
	public boolean isStored(final java.lang.Object obj) {
		return ((Boolean) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return new Boolean(((ExtObjectContainer) container).isStored(obj));
			}
		}, true)).booleanValue();
	}

	/**
	 * @see org.db4ospring.Db4oOperations#knownClasses()
	 */
	public ReflectClass[] knownClasses() {
		return (ReflectClass[]) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return ((ExtObjectContainer) container).knownClasses();
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#lock()
	 */
	public Object lock() {
		return execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return ((ExtObjectContainer) container).lock();
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#migrateFrom(com.db4o.ObjectContainer)
	 */
	public void migrateFrom(final ObjectContainer objectContainer) {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				((ExtObjectContainer) container).migrateFrom(objectContainer);
				return null;
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#peekPersisted(java.lang.Object, int,
	 * boolean)
	 */
	public Object peekPersisted(final java.lang.Object object, final int depth, final boolean committed) {
		return execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return ((ExtObjectContainer) container).peekPersisted(object, depth, committed);
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#purge()
	 */
	public void purge() {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				((ExtObjectContainer) container).purge();
				return null;
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#purge(java.lang.Object)
	 */
	public void purge(final java.lang.Object obj) {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				((ExtObjectContainer) container).purge(obj);
				return null;
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#reflector()
	 */
	public GenericReflector reflector() {
		return (GenericReflector) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return ((ExtObjectContainer) container).reflector();
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#refresh(java.lang.Object, int)
	 */
	public void refresh(final java.lang.Object obj, final int depth) {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				((ExtObjectContainer) container).refresh(obj, depth);
				return null;
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#releaseSemaphore(java.lang.String)
	 */
	public void releaseSemaphore(final java.lang.String name) {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				((ExtObjectContainer) container).releaseSemaphore(name);
				return null;
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#replicationBegin(com.db4o.ObjectContainer,
	 * com.db4o.replication.ReplicationConflictHandler)
	 */
	public ReplicationProcess replicationBegin(final ObjectContainer peerB,
			final ReplicationConflictHandler conflictHandler) {
		return (ReplicationProcess) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return ((ExtObjectContainer) container).replicationBegin(peerB, conflictHandler);
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#set(java.lang.Object, int)
	 */
	public void set(final java.lang.Object obj, final int depth) {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				((ExtObjectContainer) container).set(obj, depth);
				return null;
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#setSemaphore(java.lang.String, int)
	 */
	public boolean setSemaphore(final java.lang.String name, final int waitForAvailability) {
		return ((Boolean) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return new Boolean(((ExtObjectContainer) container).setSemaphore(name, waitForAvailability));
			}
		}, true)).booleanValue();
	}

	/**
	 * @see org.db4ospring.Db4oOperations#storedClass(java.lang.Object)
	 */
	public StoredClass storedClass(final java.lang.Object clazz) {
		return (StoredClass) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return ((ExtObjectContainer) container).storedClass(clazz);
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#storedClasses()
	 */
	public StoredClass[] storedClasses() {
		return (StoredClass[]) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return ((ExtObjectContainer) container).storedClasses();
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#version()
	 */
	public long version() {
		return ((Long) execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				return new Long(((ExtObjectContainer) container).version());
			}
		}, true)).longValue();
	}

	//
	// Ext Client interface
	//

	/**
	 * @see org.db4ospring.Db4oOperations#switchToFile(java.lang.String)
	 */
	public void switchToFile(final String fileName) {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				((ExtClient) container).switchToFile(fileName);
				return null;
			}
		}, true);
	}

	/**
	 * @see org.db4ospring.Db4oOperations#switchToMainFile()
	 */
	public void switchToMainFile() {
		execute(new Db4oCallback() {
			public Object doInDb4o(ObjectContainer container) throws RuntimeException {
				((ExtClient) container).switchToMainFile();
				return null;
			}
		}, true);
	}

	/**
	 * Create a close-suppressing proxy for the given object container.
	 * 
	 * @param container the Db4o ObjectContainer to create a proxy for
	 * @return the ObjectContainer proxy
	 * 
	 * @see com.db4o.ObjectContainer#close()
	 */
	protected ObjectContainer createContainerProxy(ObjectContainer container) {
		Class intrf = ExtObjectContainer.class;

		// the documentation states that the container always implements
		// the following interface but we do this here just to make sure

		if (container instanceof ExtClient)
			intrf = ExtClient.class;

		return (ObjectContainer) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { intrf },
				new CloseSuppressingInvocationHandler(container));
	}

	/**
	 * Invocation handler that suppresses close calls on Object Container.
	 * 
	 * @see com.db4o.ObjectContainer#close()
	 */
	private class CloseSuppressingInvocationHandler implements InvocationHandler {

		private final ObjectContainer target;

		public CloseSuppressingInvocationHandler(ObjectContainer target) {
			this.target = target;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// Invocation on ObjectContainer interface (or vendor-specific
			// extension) coming in...

			if (method.getName().equals("equals")) {
				// Only consider equal when proxies are identical.
				return (proxy == args[0] ? Boolean.TRUE : Boolean.FALSE);
			}
			else if (method.getName().equals("hashCode")) {
				// Use hashCode of session proxy.
				return new Integer(hashCode());
			}
			else if (method.getName().equals("close")) {

				// Handle close method: suppress, not valid.
				// tell the truth when returning the value.
				return Boolean.FALSE;
			}

			// Invoke method on target Session.
			try {
				Object retVal = method.invoke(this.target, args);

				// TODO: watch out for Query returned
				/*
				 * if (retVal instanceof Query) { prepareQuery(((Query)
				 * retVal)); }
				 */
				return retVal;
			}
			catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			}
		}
	}

	/**
	 * @return Returns the exposeNativeContainer.
	 */
	public boolean isExposeNativeContainer() {
		return exposeNativeContainer;
	}

	/**
	 * @param exposeNativeContainer The exposeNativeContainer to set.
	 */
	public void setExposeNativeContainer(boolean exposeNativeContainer) {
		this.exposeNativeContainer = exposeNativeContainer;
	}

}
