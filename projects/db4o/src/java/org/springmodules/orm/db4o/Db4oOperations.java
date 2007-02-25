package org.springmodules.orm.db4o;

import org.springframework.dao.DataAccessException;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.Db4oDatabase;
import com.db4o.ext.Db4oUUID;
import com.db4o.ext.ObjectInfo;
import com.db4o.ext.StoredClass;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.db4o.reflect.ReflectClass;
import com.db4o.reflect.generic.GenericReflector;
import com.db4o.replication.ReplicationConflictHandler;
import com.db4o.replication.ReplicationProcess;
import com.db4o.types.Db4oCollections;

public interface Db4oOperations {

	public Object execute(Db4oCallback callback) throws DataAccessException;

	public Object execute(Db4oCallback callback, boolean exposeNativeContainer) throws DataAccessException;

	//
	// ObjectContainer interface methods 
	//
	public void activate(final java.lang.Object obj, final int depth);

	public void deactivate(final java.lang.Object obj, final int depth);

	public void delete(final java.lang.Object obj);

	public ObjectSet get(final java.lang.Object template);

	public Query query();

	public ObjectSet query(final Predicate predicate);

	public void set(final Object obj);

	public void backup(final java.lang.String path);

	public void bind(final java.lang.Object obj, final long id);

	public Db4oCollections collections();

	public Object getByID(final long ID);

	public Object getByUUID(final Db4oUUID uuid);

	public long getID(final java.lang.Object obj);

	public ObjectInfo getObjectInfo(final java.lang.Object obj);

	public Db4oDatabase identity();

	public boolean isActive(final java.lang.Object obj);

	public boolean isCached(final long ID);

	public boolean isClosed();

	public boolean isStored(final java.lang.Object obj);

	public ReflectClass[] knownClasses();

	public Object lock();

	public void migrateFrom(final ObjectContainer objectContainer);

	public Object peekPersisted(final java.lang.Object object, final int depth, final boolean committed);

	public void purge();

	public void purge(final java.lang.Object obj);

	public GenericReflector reflector();

	public void refresh(final java.lang.Object obj, final int depth);

	public void releaseSemaphore(final java.lang.String name);

	public ReplicationProcess replicationBegin(final ObjectContainer peerB,
			final ReplicationConflictHandler conflictHandler);

	public void set(final java.lang.Object obj, final int depth);

	public boolean setSemaphore(final java.lang.String name, final int waitForAvailability);

	public StoredClass storedClass(final java.lang.Object clazz);

	public StoredClass[] storedClasses();

	public long version();

	public void switchToFile(final String fileName);

	public void switchToMainFile();

}