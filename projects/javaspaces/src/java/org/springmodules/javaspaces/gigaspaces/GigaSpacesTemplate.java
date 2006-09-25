/*
* Copyright 2006 GigaSpaces, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.springmodules.javaspaces.gigaspaces;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;

import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import org.springmodules.javaspaces.JavaSpaceCallback;
import org.springmodules.javaspaces.JavaSpaceListener;
import org.springmodules.javaspaces.JavaSpaceTemplate;
import org.springmodules.javaspaces.gigaspaces.exception.GigaSpacesException;

import com.j_spaces.core.IJSpace;
import com.j_spaces.core.IJSpaceContainer;

import com.j_spaces.core.client.NotifyDelegator;
import com.j_spaces.core.client.SpaceURL;
/**
 * <p>
 * Helper object offered to the use of application developers who need to
 * perform operations in space. The template exposes all the operations offered
 * by the IJSpace, wrapping them with additional exception conversion logic, and
 * one general purpose execute() method, accepting a JavaSpaceCallback
 * implementation, where application logic can be implemented by the application
 * developer.
 * </p>
 *
 * @author Lior Ben Yizhak
 * @version 5.0
 * @Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * @Company:      Gigaspaces Technologies
 */

public class GigaSpacesTemplate
extends JavaSpaceTemplate
{

	/**
	 * Empty constructor
	 *
	 */
	public GigaSpacesTemplate()
	{
		super();
	}

	/**
	 * Constructor Gets a GigaSpaces space proxy and sets it into the template.
	 * @param space
	 */
	public GigaSpacesTemplate(IJSpace space)
	{
		super();
		setSpace(space);
	}

	/**
	 * Override the JavaSpaceTemplate method in order not to
	 * throw exception if space instance is null.
	 *@Override
	 */
	public void afterPropertiesSet()
    {
    }

	/**
	 * Checks if the space instance is null before executing
	 * @param jsc the javaspaces callback which wraps the javaspace executed method.
	 * @return the wrapped executed javaspace method result
	 */
	public Object execute(JavaSpaceCallback jsc)
    {
        if(getSpace() == null){
        	throw new GigaSpacesException("The space property is required and can not be null.");
        }
		return super.execute(jsc);
    }


	// Standard JavaSpace API calls


	/**
	  * The process of serializing an object for transmission to a JavaSpaces
	  * service will be identical if the same object is used twice. This is most
	  * likely to be an issue with templates that are used repeatedly to search
	  * for entries with read or take. The client-side implementations of read
	  * and take cannot reasonably avoid this duplicated effort, since they have
	  * no efficient way of checking whether the same template is being used
	  * without intervening modification. The snapshot method gives the
	  * JavaSpaces service implementor a way to reduce the impact of repeated use
	  * of the same object. Invoking snapshot with an Object will return another
	  * object that contains a snapshot of the original . Using the
	  * returned snapshot object is equivalent to using the unmodified original
	  * object in all operations on the same JavaSpaces service. Modifications to
	  * the original object will not affect the snapshot. You can snapshot a null
	  * template; snapshot may or may not return null given a null template. The
	  * object returned from snapshot will be guaranteed equivalent to the
	  * original unmodified object only when used with the space. Using the
	  * snapshot with any other JavaSpaces service will generate an
	  * IllegalArgumentException unless the other space can use it because of
	  * knowledge about the JavaSpaces service that generated the snapshot. The
	  * snapshot will be a different object from the original, may or may not
	  * have the same hash code, and equals may or may not return true when
	  * invoked with the original object, even if the original object is
	  * unmodified. A snapshot is guaranteed to work only within the virtual
	  * machine in which it was generated. If a snapshot is passed to another
	  * virtual machine (for example, in a parameter of an RMI call), using
	  * it--even with the same JavaSpaces service--may generate an
	  * IllegalArgumentException.
	  * @see {@link http://java.sun.com/products/jini/2.0/doc/api/net/jini/space/JavaSpace.html#snapshot(net.jini.core.entry.Entry)}
	  *
	  * @param object the object to take a snapshot of.
	  * @return a snapshot of the object.
	  */
	public Object snapshot(final Object object)
	{
		return (Object) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException,
			TransactionException, UnusableEntryException, InterruptedException {
				return ((IJSpace)js).snapshot(object);
			}
		});
	}

	/**
	 * Take any matching entry from the space, blocking until one exists. Return
	 * null if the timeout expires.
	 *
	 * @param object The template used for matching. Matching is done against
	 *            tmpl with null fields being wildcards ("match anything") other
	 *            fields being values ("match exactly on the serialized form").
	 * @param timeout How long the client is willing to wait for a
	 *            transactionally proper matching entry. A timeout of NO_WAIT
	 *            means to wait no time at all; this is equivalent to a wait of
	 *            zero.
	 * @return a  from the space
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#take(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,long)}
	 */
	public Object take(final Object template,final long millis)
	{
		return (Object) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException,
			TransactionException, UnusableEntryException, InterruptedException {
				return ((IJSpace)js).take(template,tx, millis);
			}
		});
	}

	/**
	 * Take if exists any matching entry from the space, blocking until one
	 * exists. Return null if the timeout expires.
	 *
	 * @param object The template used for matching. Matching is done against
	 *            tmpl with null fields being wildcards ("match anything") other
	 *            fields being values ("match exactly on the serialized form").
	 * @param timeout How long the client is willing to wait for a
	 *            transactionally proper matching entry. A timeout of NO_WAIT
	 *            means to wait no time at all; this is equivalent to a wait of
	 *            zero.
	 * @return object from the space.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#takeIfExists(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,long)}
	 */
	public Object takeIfExists(final Object template,final long millis)
	{
		return (Object) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException,
			TransactionException, UnusableEntryException, InterruptedException {
				return ((IJSpace)js).takeIfExists(template,tx, millis);
			}
		});
	}


	/**
	 * Write a new object to the space.
	 * If the object has a primary key and isn't auto-generated,
	 * it will call to the update(Object updatedObject, Transaction transaction,
	 * long lease, long timeout, int updateModifiers) method with
	 * UpdateModifiers.UPDATE_OR_WRITE.
	 * @param object
	 *            the object to write
	 * @param lease
	 *            the requested lease time, in milliseconds
	 * @return an object for the entry that was written to the space
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#write(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,long)}
	 */
	public Lease write(final Object pojo, final long lease)
	{
		return (Lease) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException,
			TransactionException, UnusableEntryException, InterruptedException {
				return ((IJSpace)js).write(pojo,tx, lease);
			}
		});
	}

	/**
	 * Write a new object to the space.
	 * If the object has a primary key and isn't auto-generated,
	 * it will call to the update(Object updatedObject, Transaction transaction,
	 * long lease, long timeout, int updateModifiers) method with
	 * UpdateModifiers.UPDATE_OR_WRITE.
	 * @param object
	 *            the object to write
	 * @param lease
	 *            the requested lease time, in milliseconds
	 * @param modifiers the modifiers
	 * @return an object for the entry that was written to the space
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#write(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,long,int)}
	 */
	public Lease write(final Object pojo, final long lease, final int modifiers)
	{
		return (Lease) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException,
			TransactionException, UnusableEntryException, InterruptedException {
				return ((IJSpace)js).write(pojo,tx, lease, Long.MAX_VALUE, modifiers);
			}
		});
	}


	/**
	 * Write a new object to the space.
	 * If the object has a primary key and isn't auto-generated,
	 * it will call to the update(Object updatedObject, Transaction transaction,
	 * long lease, long timeout, int updateModifiers) method with
	 * UpdateModifiers.UPDATE_OR_WRITE and lease Long.MAX_VALUE.
	 * @param object
	 *            the object to write
	 * @return an object for the entry that was written to the space
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#write(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,long)}
	 */
	public Lease write(final Object pojo)
	{
		return write( pojo, Long.MAX_VALUE);
	}

	/**
	 * Counts the number of objects that match the specified template and
	 * the specified transaction.
	 * @param template the template to use for matching.
	 * @return the number of entries that match the specified template and
	 * the specified transaction.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#count(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction)}
	 */
	public int count( Entry entry){
		return count((Object)entry);
	}

	/**
	 * Counts the number of objects that match the specified template and
	 * the specified transaction.
	 * @param template the template to use for matching.
	 * @return the number of entries that match the specified template and
	 * the specified transaction.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#count(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction)}
	 */
	public int count(final Object object)
	{
		return ((Integer) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException,
			TransactionException, UnusableEntryException, InterruptedException {
				return new Integer(((IJSpace)js).count(object, tx));
			}
		})).intValue();
	}

	/**
	 * Removes the entries that match the specified template and
	 *  the specified transaction from this space.
	 * If the clear operation conducted without transaction (null as value)
	 * it will clear all entries that are not under transaction.
	 * Therefor entries under transaction would not be removed from the space.
	 *
	 * The clear operation supports inheritance, therefor template class matching objects
	 * and its sub classes matching objects are part of the candidates population
	 * to be removed from the space.
	 * You can in fact clean all space objects (that are not under transaction) by calling:
	 *
	 * Notice: The clear operation does not remove notify templates i.e. registration for notifications.
	 * @param template the template to use for matching.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#clear(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction)}
	 */
	public void clear(final Entry entry)
	{
		clear((Object)entry);
	}


	/**
	 * Removes the entries that match the specified template and
	 *  the specified transaction from this space.
	 * If the clear operation conducted without transaction (null as value)
	 * it will clear all entries that are not under transaction.
	 * Therefor entries under transaction would not be removed from the space.
	 *
	 * The clear operation supports inheritance, therefor template class matching objects
	 * and its sub classes matching objects are part of the candidates population
	 * to be removed from the space.
	 * You can in fact clean all space objects (that are not under transaction) by calling:
	 *
	 * Notice: The clear operation does not remove notify templates i.e. registration for notifications.
	 * @param template the template to use for matching.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#clear(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction)}
	 */
	public void clear(final Object pojo)
	{
		execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException,
			TransactionException, UnusableEntryException, InterruptedException {
				((IJSpace)js).clear(pojo, tx);
				return null;
			}
		});
	}

		/**
		 * Cleans this space. The side-effects of cleaning the space are:
		 * <ul>
		 * <li>all entries and templates are deleted.</li>
		 * <li>all storage adapter contexts are closed.</li>
		 * <li>all engine threads are terminated.</li>
		 * <li>the engine re-initializes itself.</li>
		 * </ul>
		 *
		 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#clean()}
		 */
	public void clean()
	{
		execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException,
			TransactionException, UnusableEntryException, InterruptedException {
				((IJSpace)js).clean();
				return null;
			}
		});
	}

	/**
	 * Returns the name of this space.
	 * @return the name of this space.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#getName()}
	 */
	public String getName()
	{
		return (String) execute(new JavaSpaceCallback()
		{
			public Object doInSpace(JavaSpace js, Transaction tx)
					throws RemoteException
			{
				return ((IJSpace) js).getName();
			}
		});
	}

	/**
	 * Returns the {@link SpaceURL} instance which was used to initialize the space.
	 * <p>
	 * Notice: The {@link SpaceURL} object contains information on the space and container
	 * configuration/setup such as space url used, space/container/cluster schema used
	 * and other attributes.<p>
	 * The {@link IJSpaceContainer} keeps also its reference of the SpaceURL which launched the container.
	 * @return {@link SpaceURL} which initialized that specific space instance.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#getUrl()}
	 **/
	public SpaceURL getUrl()
	{
		return (SpaceURL) execute(new JavaSpaceCallback()
		{
			public Object doInSpace(JavaSpace js, Transaction tx)
					throws RemoteException
			{
				return ((IJSpace) js).getURL();
			}
		});
	}

	/**
	 * Returns the admin object to the remote part of this space.
	 *
	 * @return the admin object to the remote part of this space.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#getAdmin()}
	 */
	public Object getAdmin()
	{
		return execute(new JavaSpaceCallback()
		{
			public Object doInSpace(JavaSpace js, Transaction tx)
					throws RemoteException
			{
				return ((IJSpace) js).getAdmin();
			}
		});
	}

	/**
	 * Writes the specified entries to this space.
	 *
	 * @param entries the entries to write.
	 * @param lease the requested lease time, in milliseconds
	 * @return the leases for the written entries.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#writeMultiple(net.jini.core.entry.Entry[],net.jini.core.transaction.Transaction,long)}
	 */
	public Lease[] writeMultiple(Entry[] entries, long lease)
	{
		return writeMultiple((Object[]) entries, lease);
	}

	/**
	 * Writes the specified entries to this space.
	 *
	 * @param entries the entries to write.
	 * @param lease the requested lease time, in milliseconds
	 * @return the leases for the written entries.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#writeMultiple(net.jini.core.entry.Entry[],net.jini.core.transaction.Transaction,long)}
	 */
	public Lease[] writeMultiple( final Object[] pojos, final long lease)
	{
		return (Lease[]) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException{
				return ((IJSpace)js).writeMultiple(pojos, tx, lease);
			}
		});
	}

	/**
	 * Reads all the entries matching the specified template from this space.
	 *
	 * @param entries the templates
	 * @param maxEntries a limit on the number of entries to be read. Use
	 *            Integer.MAX_VALUE for the maximum value
	 * @return array of entry object
 	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#readMultiple(net.jini.core.entry.Entry[],net.jini.core.transaction.Transaction,int)}
	 */
	public Entry[] readMultiple(Entry entry, int maxEntries)
	{
		return  (Entry[])readMultiple((Object)entry, maxEntries);
	}


	/**
	 * Reads all the entries matching the specified template from this space.
	 *
	 * @param POJOs the templates
	 * @param maxEntries a limit on the number of entries to be read. Use
	 *            Integer.MAX_VALUE for the maximum value
	 * @return array of entry object
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#readMultiple(net.jini.core.entry.Entry[],net.jini.core.transaction.Transaction,int)}
	 */
	public Object[] readMultiple(final Object pojo, final int maxEntries)
	{
		return (Object[]) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException, UnusableEntryException{
				return ((IJSpace)js).readMultiple(pojo, tx, maxEntries);
			}
		});
	}

	/**
	 * Takes all the entries matching the specified template from this space.
	 *
	 * @param entry the template to use for matching.
	 * @param a limit on the number of entries to be taken. Use
	 *            Integer.MAX_VALUE for the maximum value.
	 * @return an array of entries that match the template, or empty array in
	 *         case if no suitable entry was found.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#takeMultiple(net.jini.core.entry.Entry[],net.jini.core.transaction.Transaction,int)}
	 */
	public Entry[] takeMultiple(Entry entry, int maxEntries)
	{
		return (Entry[])takeMultiple((Object)entry, maxEntries);
	}


	/**
	 * Takes all the entries matching the specified template from this space.
	 *
	 * @param POJO the template to use for matching.
	 * @param a limit on the number of entries to be taken. Use
	 *            Integer.MAX_VALUE for the maximum value.
	 * @return an array of POJOs that match the template, or empty array in case
	 *         if no suitable entry was found.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#takeMultiple(net.jini.core.entry.Entry[],net.jini.core.transaction.Transaction,int)}
	 */
	public Object[] takeMultiple(final Object pojo, final int maxEntries)
	{
		return (Object[]) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException, UnusableEntryException{
				return ((IJSpace)js).takeMultiple(pojo, tx, maxEntries);
			}
		});
	}

	/**
	 * Checks whether the space is alive and accessible.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#ping()}
	 */
	public void ping()
	{
		execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				((IJSpace)js).ping();
				return null;
			}
		});

	}

	/**
	 * Updates the first entry matching the specified template, if found and
	 * there is no transaction conflict.
	 *
	 * @param newEntry the new value of the entry, must contain the UID.
	 * @param lease The lease time of the updated entry, 0 means retain the
	 *            original lease
	 * @param timeout "IfExists" timeout
	 * @return the entry
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#update(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,long,long)}
	 */
	public Entry update(Entry newEntry, long lease, long timeout)
	{
		return (Entry) update((Object)newEntry, lease, timeout);
	}

	/**
	 * Updates the first entry matching the specified template, if found and
	 * there is no transaction conflict.
	 *
	 * @param newPojo the new value of the POJO, must contain the primary key.
	 * @param lease The lease time of the updated entry, 0 means retain the
	 *            original lease
	 * @param timeout "IfExists" timeout
	 * @return the POJO
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#update(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,long,long)}
	 */
	public Object update(final Object newPojo, final long lease, final long timeout)
	{
		return (Object) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, InterruptedException, TransactionException, UnusableEntryException{
				return ((IJSpace)js).update(newPojo, tx, lease, timeout);

			}
		});
	}


	/**
	 * Updates the first entry matching the specified template, if found and
	 * there is no transaction conflict.
	 *
	 * @param newEntry the new value of the entry, must contain the UID.
	 * @param lease The lease time of the updated entry, 0 means retain the
	 *            original lease
	 * @param timeout "IfExists" timeout
	 * @param updateModifiers - operation modifiers, values from UpdateModifiers
	 * @return the entry
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#update(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,long,long,int)}
	 */
	public Entry update(Entry newEntry, long lease, long timeout, int updateModifiers)
	{
		return (Entry)update((Object) newEntry, lease, timeout, updateModifiers);
	}

	/**
	 * Updates the first entry matching the specified template, if found and
	 * there is no transaction conflict.
	 *
	 * @param newPojo the new value of the pojo, must contain the primary key.
	 * @param lease The lease time of the updated entry, 0 means retain the
	 *            original lease
	 * @param timeout "IfExists" timeout
	 * @param updateModifiers - operation modifiers, values from UpdateModifiers
	 * @return the POJO
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#update(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,long,long,int)}
	 */
	public Object update(final Object newEntry, final long lease, final long timeout, final int updateModifiers)
	{
		return (Object) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, InterruptedException, TransactionException, UnusableEntryException{
				return ((IJSpace)js).update(	newEntry,
				                            	tx,
				                            	lease,
				                            	timeout,
				                            	updateModifiers);

			}
		});
	}


	/**
	 * Updates a group of entries. The entries with the new values must contain
	 * the UIDs and (optionally) versionIDs
	 *
	 * @param entries the array of entries containing the new values , each
	 *            entry must contain its UID.
	 * @param leases The lease time of the updated entries, 0 means retain the
	 *            original lease
	 * @return array of objects which correspond to the input entries array. An
	 *         object can be - an Entry, if the update was successful - null if
	 *         no update performed for the corresponding input - an Exception
	 *         object, in case of an exception
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#writeMultiple(net.jini.core.entry.Entry[],net.jini.core.transaction.Transaction,long)}
	 */
	public Object[] updateMultiple(Entry[] entries, long[] leases)
	{
		return updateMultiple((Object[])entries, leases);
	}
	/**
	 * Updates a group of POJO's. The POJO's with the new values must contain
	 * the pks and (optionally) versionIDs
	 *
	 * @param POJOs the array of POJO's containing the new values , each POJO
	 *            must contain its primark key value.
	 * @param leases The lease time of the updated entries, 0 means retain the
	 *            original lease
	 * @return array of objects which correspond to the input entries array. An
	 *         object can be - an Entry, if the update was successful - null if
	 *         no update performed for the corresponding input - an Exception
	 *         object, in case of an exception
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#writeMultiple(net.jini.core.entry.Entry[],net.jini.core.transaction.Transaction,long)}
	 */
	public Object[] updateMultiple(final Object[] pojos, final long[] leases)
	{
		return (Object[]) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, InterruptedException, TransactionException, UnusableEntryException{
				return ((IJSpace)js).updateMultiple(pojos, tx, leases);
			}
		});
	}

	/**
	 * Updates a group of entries. The entries with the new values must contain
	 * the UIDs and (optionally) versionIDs
	 *
	 * @param entries the array of entries containing the new values , each
	 *            entry must contain its UID.
	 * @param leases The lease time of the updated entries, 0 means retain the
	 *            original lease
	 * @param updateModifiers - operation modifiers, values from UpdateModifiers
	 * @return array of objects which correspond to the input entries array. An
	 *         object can be - an Entry, if the update was successful - null if
	 *         no update performed for the corresponding input - an Exception
	 *         object, in case of an exception
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#writeMultiple(net.jini.core.entry.Entry[],net.jini.core.transaction.Transaction,long,int)}
	 */
	public Object[] updateMultiple(Entry[] entries, long[] leases, int updateModifiers)
	{
		return updateMultiple((Object[])entries, leases, updateModifiers);
	}
	/**
	 * Updates a group of POJO's. The POJO's with the new values must contain
	 * the pks and (optionally) versionIDs
	 *
	 * @param POJOs the array of POJO's containing the new values , each POJO
	 *            must contain its primark key.
	 * @param leases The lease time of the updated entries, 0 means retain the
	 *            original lease
	 * @param updateModifiers - operation modifiers, values from UpdateModifiers
	 * @return array of objects which correspond to the input entries array. An
	 *         object can be - an Entry, if the update was successful - null if
	 *         no update performed for the corresponding input - an Exception
	 *         object, in case of an exception
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#writeMultiple(net.jini.core.entry.Entry[],net.jini.core.transaction.Transaction,long,int)}
	 */
	public Object[] updateMultiple(final Object[] pojos, final long[] leases, final int updateModifiers)
	{
		return (Object[]) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, InterruptedException, TransactionException, UnusableEntryException{
				return ((IJSpace)js).updateMultiple(	pojos,
				                                    	tx,
				                                    	leases,
				                                    	updateModifiers);
			}
		});
	}


	/**
	 * Returns an indication : is this space secured. If for this space defined
	 * Security Filter, the space will be secured.
	 *
	 * @return boolean true if this space secured, otherwise false.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#isSecured()}
	 */
	public boolean isSecured()
	{
		return ((Boolean) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				return new Boolean(((IJSpace)js).isSecured());
			}
		})).booleanValue();
	}

	/**
	 * Drops all Class's entries and all its templates from the space. Calling
	 * this method will remove all internal meta data related to this class
	 * stored in the space. When using persistent spaced the relevant RDBMS
	 * table will be dropped. It is the caller responsibility to ensure that no
	 * entries from this class are written to the space while this method is
	 * called. This method is protected through the space Default Security
	 * Filter. Admin permissions required to execute this request successfully.
	 *
	 * @pram className - name of class to delete.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#dropClass(java.lang.String)}
	 */
	public void dropClass(final String className)
	{
		execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				try
				{
					((IJSpace)js).dropClass(className);
				}
				catch (Exception e)
				{
					throw new GigaSpacesException("Failed to drop class",e);
				}
				return null;
			}
		});
	}

	/**
	 * Checks whether proxy is connected to embedded or remote space.
	 * @return Returns true if the proxy embedded, otherwise false.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#isEmbedded()}
	 */
	public boolean isEmbedded()
	{
		return ((Boolean) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				 return new Boolean(((IJSpace)js).isEmbedded());
			}
		})).booleanValue();
	}

	/**
	 * Enable/Disable Optimistic Lock protocol.
	 * @param enable true enable Optimistic Lock protocol, false disable.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#isOptimisticLocking(boolean)}
	 */
	public void setOptimisticLocking(final boolean enable)
	{
		execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				((IJSpace)js).setOptimisticLocking(enable);
				return null;
			}
		});

	}

	/**
	 * Returns status of Optimistic Lock protocol.
	 * @return true Optimistic Lock enabled, otherwise false.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#isOptimisticLockingEnabled()}
	 */
	public boolean isOptimisticLockingEnabled()
	{
		return ((Boolean) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				return new Boolean(((IJSpace)js).isOptimisticLockingEnabled());
			}
		})).booleanValue();
	}

	/**
	 * Sets FIFO mode for proxy. Every operation that come through the proxy will be FIFO enabled. The other way to set FIFO enabled using SpaceFinder: SpaceFinder.find("jini://localhost/myContainerName/JavaSpaces?fifo=true") If this mode enabled, you don't need to use com.j_spaces.core.client.MetaDataEntry.setFifo(boolean) for every entry/template. All entries and templates will be FIFO enabled.
	 * @param enable if true this proxy FIFO enabled, otherwise false.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#setFifo(boolean)}
	 */
	public void setFifo(final boolean enable)
	{
		execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				((IJSpace)js).setFifo(enable);
				return null;
			}
		});

	}

	/**
	 * Returns true if this proxy FIFO enabled, otherwise false.
	 * @return Returns true if this proxy FIFO enabled, otherwise false.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#isFifo()}
	 */
	public boolean isFifo()
	{
		return ((Boolean) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				return new Boolean(((IJSpace)js).isFifo());
			}
		})).booleanValue();
	}

	/**
	 * Set noWriteLease mode enabled
	 * @param enable noWriteLeaseMode - write mode.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#setNOWriteLeaseMode(boolean)}
	 */
	public void setNOWriteLeaseMode(final boolean enable)
	{
		execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				((IJSpace)js).setNOWriteLeaseMode(enable);
				return null;
			}
		});
	}

	/**
	 * Checks the write mode.
	 * @return true if do not return Lease object after write, otherwise false.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#isNOWriteLeaseMode()}
	 */
	public boolean isNOWriteLeaseMode()
	{
		return ((Boolean) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				return new Boolean(((IJSpace)js).isNOWriteLeaseMode());
			}
		})).booleanValue();
	}


	/**
	 * Sets the update mode modifiers for proxy level.
	 * @param newModifiers new values for modifiers Values are defined in UpdateModifiers
	 * @return old value of modifiers.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#setUpdateModifiers(int)}
	 */
	public int setUpdateModifiers(final int newModifiers)
	{
		return ((Integer) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				return new Integer(((IJSpace)js).setUpdateModifiers(newModifiers));
			}
		})).intValue();
	}

	/**
	 * Gets the proxyUpdateModifiers. Values are defined in UpdateModifiers
	 * class. Default value: UPDATE_ONLY.
	 *
	 * @return value of modifiers.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#getUpdateModifiers()}
	 */
	public int getUpdateModifiers()
	{
		return ((Integer) execute(new JavaSpaceCallback()
		{
			public Object doInSpace(JavaSpace js, Transaction tx)
					throws RemoteException
			{
				return new Integer(((IJSpace) js).getUpdateModifiers());
			}
		})).intValue();
	}

	/**
	 * Sets the read-take mode modifiers for proxy level.
	 * @param newModifiers new values for modifiers Values are defined in ReadTakeModifiers
	 * @return old value of modifiers.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#setReadTakeModifiers(int)}
	 */
	public int setReadTakeModifiers(final int newModifiers)
	{
		return ((Integer) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				return new Integer(((IJSpace)js).setReadTakeModifiers(newModifiers));
			}
		})).intValue();
	}

	/**
	 * Gets the proxyReadTakeModifiers. values are from ReadTakeModifiers.
	 *
	 * @return value of modifiers.
	 * @see {@link http://gigaspaces.com/docs/JavaDoc/com/j_spaces/core/IJSpace.html#getReadTakeModifiers()}
	 */
	public int getReadTakeModifiers()
	{
		return ((Integer) execute(new JavaSpaceCallback()
		{
			public Object doInSpace(JavaSpace js, Transaction tx)
					throws RemoteException
			{
				return new Integer((((IJSpace) js).getReadTakeModifiers()));
			}
		})).intValue();
	}


	  /**
		 * Read any matching object from the space, blocking until one exists.
		 * Return null if the timeout expires.
		 *
		 * @param The template used for matching. Matching is done against
		 *           template with null fields being wildcards ("match anything")
		 *           other fields being values ("match exactly on the serialized
		 *           form").
		 * @param lease the requested lease time, in milliseconds
		 * @return a object for the entry that was written to the space
		 * @see {@link http://java.sun.com/products/jini/2.0/doc/api/net/jini/space/JavaSpace.html#read(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,long)}
		 */
	public Object read(final Object pojo, final long lease)
	{
		return (Object) execute(new JavaSpaceCallback()
		{
			public Object doInSpace(JavaSpace js, Transaction tx)
					throws RemoteException, TransactionException,
					UnusableEntryException, InterruptedException
			{
				return ((IJSpace) js).read(pojo, tx, lease);
			}
		});
	}

	/**
	 * Read if exists any matching object from the space, blocking until one
	 * exists. Return null if the timeout expires.
	 *
	 * @param object The template used for matching. Matching is done against
	 *           template with null fields being wildcards ("match anything")
	 *           other fields being values ("match exactly on the serialized
	 *           form").
	 * @param timeout How long the client is willing to wait for a
	 *           transactionally proper matching entry. A timeout of NO_WAIT
	 *           means to wait no time at all; this is equivalent to a wait of
	 *           zero.
	 * @return a copy of the entry read from the space
	 * @see {@link http://java.sun.com/products/jini/2.0/doc/api/net/jini/space/JavaSpace.html#readIfExists(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,long)}
	 */
	public Object readIfExists(final Object pojo, final long lease)
	{
		return (Object) execute(new JavaSpaceCallback()
		{
			public Object doInSpace(JavaSpace js, Transaction tx)
					throws RemoteException, TransactionException,
					UnusableEntryException, InterruptedException
			{
				return ((IJSpace) js).readIfExists(pojo, tx, lease);
			}
		});
	}

	/**
	 * When Pojo's are written that match this template notify the given listener with a RemoteEvent that includes the handback object. Matching is done as for read.
	 * using  NotifyDelegator for notify
	 * @param listener -The remote event listener to notify.
	 * @param template - The template used for matching. Matching is done against tmpl with null fields being
	 * @param txn - The transaction (if any) under which to work.
	 * @param handback - An object to send to the listener as part of the event notification.
	 * @param fifoEnabled - enables/disables ordered notifications.
	 * @param lease - the requested lease time, in milliseconds
	 * @param notifyMask -Available Notify Types: NotifyModifiers.NOTIFY_WRITE NotifyModifiers.NOTIFY_UPDATE NotifyModifiers.NOTIFY_TAKE NotifyModifiers.NOTIFY_LEASE_EXPIRATION NotifyModifiers.NOTIFY_ALL NotifyDelegator nd = new NotifyDelegator(space, template, transaction, listener, Lease.FOREVER, handback, true, NotifyModifiers.NOTIFY_WRITE | NotifyModifiers.NOTIFY_TAKE
	 * @return NotifyDelegator the NotifyDelegator
	 * @see {@link http://java.sun.com/products/jini/2.0/doc/api/net/jini/space/JavaSpace.html#addNotifyDelegatorListener(}
	 */
	public NotifyDelegator addNotifyDelegatorListener(RemoteEventListener listener,Entry template, MarshalledObject handback,boolean  fifoEnabled,long lease, int notifyMask)
	{
		JavaSpaceListener javaSpaceListener = new JavaSpaceListener();
		javaSpaceListener.setHandback(handback);
		javaSpaceListener.setLease(lease);
		javaSpaceListener.setListener(listener);
		javaSpaceListener.setTemplate(template);
		return addNotifyDelegatorListener(javaSpaceListener, fifoEnabled, notifyMask);
	}
	/**
	 * When Pojo's are written that match this template notify the given listener with a RemoteEvent that includes the handback object. Matching is done as for read.
	 * using  NotifyDelegator for notify
	 * @param listener -The remote event listener to notify.
	 * @param template - The template used for matching. Matching is done against tmpl with null fields being
	 * @param txn - The transaction (if any) under which to work.
	 * @param handback - An object to send to the listener as part of the event notification.
	 * @param fifoEnabled - enables/disables ordered notifications.
	 * @param lease - the requested lease time, in milliseconds
	 * @param notifyMask -Available Notify Types: NotifyModifiers.NOTIFY_WRITE NotifyModifiers.NOTIFY_UPDATE NotifyModifiers.NOTIFY_TAKE NotifyModifiers.NOTIFY_LEASE_EXPIRATION NotifyModifiers.NOTIFY_ALL NotifyDelegator nd = new NotifyDelegator(space, template, transaction, listener, Lease.FOREVER, handback, true, NotifyModifiers.NOTIFY_WRITE | NotifyModifiers.NOTIFY_TAKE
	 * @return NotifyDelegator the NotifyDelegator
	 * @see {@link http://java.sun.com/products/jini/2.0/doc/api/net/jini/space/JavaSpace.html#addNotifyDelegatorListener(}
	 */
	public NotifyDelegator addNotifyDelegatorListener(final RemoteEventListener listener,final Object template, final MarshalledObject nandback,final boolean  fifoEnabled,final long lease, final int notifyMask)
	{
		return (NotifyDelegator) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException{
				return new NotifyDelegator((IJSpace)js, template , tx, listener, lease, nandback , fifoEnabled, notifyMask);
			}
		});
	}

	/**
	 * When entries are written that match this template notify the given listener with a RemoteEvent that includes the handback object. Matching is done as for read.
	 * using  NotifyDelegator for notify
	 * @param javaSpaceListener - the javaSpaceListener.
	 * @param fifoEnabled - enables/disables ordered notifications.
	 * @param lease - the requested lease time, in milliseconds
	 * @param notifyMask -Available Notify Types: NotifyModifiers.NOTIFY_WRITE NotifyModifiers.NOTIFY_UPDATE NotifyModifiers.NOTIFY_TAKE NotifyModifiers.NOTIFY_LEASE_EXPIRATION NotifyModifiers.NOTIFY_ALL NotifyDelegator nd = new NotifyDelegator(space, template, transaction, listener, Lease.FOREVER, handback, true, NotifyModifiers.NOTIFY_WRITE | NotifyModifiers.NOTIFY_TAKE
	 * @return NotifyDelegator the NotifyDelegator
	 * @see {@link http://java.sun.com/products/jini/2.0/doc/api/net/jini/space/JavaSpace.html#addNotifyDelegatorListener(}
	 */
	public NotifyDelegator addNotifyDelegatorListener(final JavaSpaceListener javaSpaceListener, final boolean fifoEnabled,final int notifyMask){
		return (NotifyDelegator) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException{
				return new NotifyDelegator((IJSpace)js, javaSpaceListener.getTemplate() , tx, javaSpaceListener.getListener(), javaSpaceListener.getLease(), javaSpaceListener.getHandback(), fifoEnabled, notifyMask);

			}
		});
	}

	/**
	 * When Pojo's are written that match this template notify the given listener with a RemoteEvent that includes the handback object. Matching is done as for read.
	 * @param templatePojo - The template used for matching. Matching is done against tmpl with null fields being
	 * @param listener -The remote event listener to notify.
	 * @param millis - the requested lease time, in milliseconds
	 * @param handback - An object to send to the listener as part of the event notification.
	 * @param txn - The transaction (if any) under which to work.
	 * @return EventRegistration - the event registration to the the registrant
	 * @see {@link http://java.sun.com/products/jini/2.0/doc/api/net/jini/space/JavaSpace.html#notify(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,net.jini.core.event.RemoteEventListener,long,java.rmi.MarshalledObject()}
	 */
	public EventRegistration notify(final Object templatePojo, final RemoteEventListener listener,
			final long millis, final MarshalledObject handback, final Transaction tx) {
		return (EventRegistration) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException{
				return ((IJSpace)js).notify(templatePojo, tx, listener, millis, handback);
			}
		});
	}

	/**
	 * When entries are written that match this template notify the given listener with a RemoteEvent that includes the handback object. Matching is done as for read.
	 * @param entry - The template used for matching. Matching is done against tmpl with null fields being
	 * @param listener -The remote event listener to notify.
	 * @param millis - the requested lease time, in milliseconds
	 * @param handback - An object to send to the listener as part of the event notification.
	 * @param txn - The transaction (if any) under which to work.
	 * @return EventRegistration - the event registration to the the registrant
	 * @see {@link http://java.sun.com/products/jini/2.0/doc/api/net/jini/space/JavaSpace.html#notify(net.jini.core.entry.Entry,net.jini.core.transaction.Transaction,net.jini.core.event.RemoteEventListener,long,java.rmi.MarshalledObject()}
	 */
	public EventRegistration notify( Entry template, RemoteEventListener listener,
			long millis, MarshalledObject handback, Transaction tx) {
		{
			return  notify( (Object)template, listener,millis, handback, tx);
		}
	}
}
