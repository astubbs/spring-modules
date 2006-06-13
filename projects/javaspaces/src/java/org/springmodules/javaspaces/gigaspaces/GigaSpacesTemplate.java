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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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

import com.gigaspaces.converter.ConverterTracer;
import com.gigaspaces.converter.IPojoToEntryConverter;
import com.gigaspaces.converter.pojo.Pojo2ExternalEntryConverter;
import org.springmodules.javaspaces.gigaspaces.exception.GigaSpacesException;
import com.j_spaces.core.IJSpace;
import com.j_spaces.core.Constants.Space;
import com.j_spaces.core.client.NotifyDelegator;


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
 * @author Zvika Markfeld
 * @version 5.0
 * @Copyright:    Copyright 2006 GigaSpaces Technologies. All rights reserved.
 * @Company:      Gigaspaces Technologies
 */
public class GigaSpacesTemplate
extends JavaSpaceTemplate
{
	private IPojoToEntryConverter converter = new Pojo2ExternalEntryConverter(true);
	private static String GS_MAPPING  = "/config/mapping";
	public GigaSpacesTemplate()
	{
		super();
		try
		{
			((Pojo2ExternalEntryConverter)converter).setMappingDirectoryLocations(new String[]{GS_MAPPING});
		}
		catch (Exception e)
		{
			if(SpringTracer.getLogger().isLoggable(Level.WARNING)){
				SpringTracer.getLogger()
						.log(	Level.WARNING,
								"Failed to locate folder '"+GS_MAPPING+"'  to load gs xml's files.");
			}
		}
	}

	/**
	 * Set GigaSpaces mapping resources to be found in the class path, like
	 * "example.gs.xml" or "mypackage/example.gs.xml". Analogous to mapping
	 * entries in a GigaSpaces XML config file. Alternative to the more generic
	 * setMappingLocations method.
	 * <p>
	 * Can be used to add to mappings from a GigaSpaces XML config file, or to
	 * specify all mappings locally.
	 *
	 * @param mappingResources
	 * @throws IOException
	 */
	public void setMappingResources(String[] mappingResources)
			throws IOException
	{
		((Pojo2ExternalEntryConverter)converter).setMappingResources(mappingResources);

	}
	public GigaSpacesTemplate(IJSpace space)
	{
		this();
		setSpace(space);

	}

	/**
	 * Override the method in JavaSpaceTemplate not, throw exception if space is null
	 *@Override
	 */
	public void afterPropertiesSet()
    {
    }

	/**
	 * Checks if the space is null before  execute
	 */
	public Object execute(JavaSpaceCallback jsc)
    {
        if(getSpace() == null){
        	throw new GigaSpacesException("The space property is required and can not be null.");
        }
		return super.execute(jsc);
    }


	// Standard Space calls

	/**
	 * Snapshot the pojo
	 *
	 * @param pojo
	 * @return the pojo
	 */
	public Object snapshot(Object obj)
	{
		Entry entry = converter.toEntry(obj);
		return converter.toPojo((Entry)snapshot(entry));
	}

	/**
	 * Take the pojo form the space
	 *
	 * @param template the template
	 * @param millis the time for waiting
	 * @return the taken pojo
	 */
	public Object take(Object template, long millis)
	{
		//no generate pk if there is declaration to auto pk generate.
		Entry entry = converter.toEntry(template,true);
		return converter.toPojo((Entry)take(entry, millis));
	}

	/**
	 * Take the pojo form the space if exists
	 *
	 * @param template the template
	 * @param millis the time for waiting
	 * @return the taken pojo
	 */
	public Object takeIfExists(Object template, long millis)
	{
		Entry entry = converter.toEntry(template,true);
		return converter.toPojo((Entry)takeIfExists(entry, millis));
	}

	/**
	 * Write the pojo to the space
	 * @param pojo the pojo
	 * @param lease the requested lease time, in milliseconds
	 * @return the Lease
	 */
	public Lease write(Object pojo, long lease)
	{
		Entry entry = converter.toEntry(pojo);
		return write(entry,lease);
	}

	/**
	 * Write the pojo to the space with lease long.MAX_VALUE
	 * @param pojo the pojo
	 * @return the Lease
	 */
	public Lease write(Object pojo)
	{
		Entry entry = converter.toEntry(pojo);
		return write((Entry)entry,GigaSpacesConstants.MAX_VALUE);
	}

	/**
	 * Counts the number of entries that match the specified template and the
	 * specified transaction..
	 *
	 * @param entry the entry
	 * @return the count number
	 */
	public int count(final Entry entry)
	{
		return ((Integer) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException,
			TransactionException, UnusableEntryException, InterruptedException {
				return new Integer(((IJSpace)js).count(entry, tx));
			}
		})).intValue();
	}

	/**
	 * Counts the number of entries that match the specified template and the
	 * specified transaction..
	 *
	 * @param pojo the pojo
	 * @return the count number
	 */
	public int count(Object pojo)
	{
		Entry entry = converter.toEntry(pojo,true);
		return count(entry);
	}

	/**
	 * Removes the entries that match the specified template and the specified
	 * @param entry the entry
	 * @param txn the transaction
	 */
	public void clear(final Entry entry)
	{
		execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException,
			TransactionException, UnusableEntryException, InterruptedException {
				((IJSpace)js).clear(entry, tx);
				return null;
			}
		});
	}

	/**
	 * Removes the entries that match the specified template and the specified
	 * transaction from this space.
	 *
	 * @param pojo the pojo
	 */
	public void clear(final Object pojo)
	{
		Entry entry = converter.toEntry(pojo);
		clear(entry);
	}

	/**
	 * Cleans this space.
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
	 *
	 * @return the name
	 */
	public String getName()
	{
		return (String) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				return ((IJSpace)js).getName();
			}
		});
	}

	/**
	 * Returns the admin object to the remote part of this space
	 *
	 * @return the admin object to the remote part of this space.
	 */
	public Object getAdmin()
	{
		return execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				return ((IJSpace)js).getAdmin();
			}
		});
	}

	/**
	 * Writes the specified entries to this space.
	 *
	 * @param entries the templates
	 * @param lease - the requested lease time, in milliseconds
	 * @return array of lease object
	 */
	public Lease[] writeMultiple(final Entry[] entries, final long lease)
	{
		return (Lease[]) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException{
				return ((IJSpace)js).writeMultiple(entries, tx, lease);
			}
		});
	}

	/**
	 * Writes the specified entries to this space.
	 *
	 * @param pojos the templates
	 * @param lease - the requested lease time, in milliseconds
	 * @return array of lease object
	 */
	public Lease[] writeMultiple(Object[] pojos, long lease)
	{
		Entry[] entries = new Entry[pojos.length];
		for (int qq = 0; qq < pojos.length; qq++)
		{
			entries[qq] = converter.toEntry(pojos[qq]);
		}
		return writeMultiple(entries, lease);
	}

	/**
	 * Reads all the entries matching the specified template from this space.
	 *
	 * @param entries the templates
	 * @param maxEntries a limit on the number of entries to be read. Use
	 *            Integer.MAX_VALUE for the maximum value
	 * @return array of entry object
	 */
	public Entry[] readMultiple(final Entry entry, final int maxEntries)
	{
		return (Entry[]) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException, UnusableEntryException{
				return ((IJSpace)js).readMultiple(entry, tx, maxEntries);
			}
		});
	}

	/**
	 * Reads all the entries matching the specified template from this space.
	 *
	 * @param pojos the templates
	 * @param maxEntries a limit on the number of entries to be read. Use
	 *            Integer.MAX_VALUE for the maximum value
	 * @return array of entry object
	 */
	public List readMultiple(Object pojo, int maxEntries)
	{
		Entry entry = converter.toEntry(pojo,true);
		Entry[] enrties = readMultiple(entry,maxEntries);
		List pojos = new ArrayList();
		for (int qq = 0; qq < enrties.length; qq++)
		{
			pojos.add(converter.toPojo(enrties[qq]));
		}
		return pojos;
	}

	/**
	 * Takes all the entries matching the specified template from this space.
	 *
	 * @param entry the template to use for matching.
	 * @param a limit on the number of entries to be taken. Use
	 *            Integer.MAX_VALUE for the maximum value.
	 * @return an array of entries that match the template, or empty array in
	 *         case if no suitable entry was found.
	 */
	public Entry[] takeMultiple(final Entry entry, final int maxEntries)
	{
		return (Entry[]) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException, UnusableEntryException{
				return ((IJSpace)js).takeMultiple(entry, tx, maxEntries);
			}
		});
	}

	/**
	 * Takes all the entries matching the specified template from this space.
	 *
	 * @param pojo the template to use for matching.
	 * @param a limit on the number of entries to be taken. Use
	 *            Integer.MAX_VALUE for the maximum value.
	 * @return an array of pojos that match the template, or empty array in case
	 *         if no suitable entry was found.
	 */
	public List takeMultiple(Object pojo, int maxEntries)
	{
		Entry entry = converter.toEntry(pojo,true);
		Entry[] enrties = takeMultiple(entry,maxEntries);
		List pojos = new ArrayList();
		for (int qq = 0; qq < enrties.length; qq++)
		{
			pojos.add(converter.toPojo(enrties[qq]));
		}
		return pojos;
	}

	/**
	 * Checks whether the space is alive and accessible.
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
	 * Replaces the first entry matching the specified template, if found and
	 * there is no transaction conflict.
	 *
	 * @param template the template to use for matching.
	 * @param newEntry the value of the new entry.
	 * @param lease the lease time of the new entry
	 * @return an array of 2 elements: the first element is the old object and
	 *         the second element is the new lease. If not matching entry was
	 *         found, null is returned.
	 */
	public Object[] replace(final Entry template, final Entry newEntry, final long lease)
	{
		return (Object[]) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException, UnusableEntryException{
				return ((IJSpace)js).replace(template, newEntry, tx, lease);
			}
		});
	}

	/**
	 * Replaces the first pojo matching the specified template, if found and
	 * there is no transaction conflict.
	 *
	 * @param pojo the template to use for matching.
	 * @param newPojo the value of the new entry.
	 * @param lease the lease time of the new entry
	 * @return an array of 2 elements: the first element is the old object and
	 *         the second element is the new lease. If not matching entry was
	 *         found, null is returned.
	 */
	public List replace(Object pojo, Object newPojo,long lease)
	{
		Entry entry = converter.toEntry(pojo,true);
		Entry newEntry = converter.toEntry(newPojo);
		Object[] objects = replace(entry,newEntry,lease);
		List pojos = new ArrayList ();
		if (objects != null)
		{
			pojos.add(converter.toPojo((Entry) objects[0]));
			pojos.add(objects[1]);// the lease
		}
		return pojos;
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
	 */
	public Entry update(final Entry newEntry, final long lease, final long timeout)
	{
		return (Entry) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, InterruptedException, TransactionException, UnusableEntryException{
				return ((IJSpace)js).update(newEntry, tx, lease, timeout);

			}
		});
	}

	/**
	 * Updates the first entry matching the specified template, if found and
	 * there is no transaction conflict.
	 *
	 * @param newPojo the new value of the pojo, must contain the primary key.
	 * @param lease The lease time of the updated entry, 0 means retain the
	 *            original lease
	 * @param timeout "IfExists" timeout
	 * @return the pojo
	 */
	public Object update(Object newPojo, long lease, long timeout)
	{
		Entry entry = converter.toEntry(newPojo,true);
		Entry newEnrty = update(entry,lease,timeout);
		return converter.toPojo(newEnrty);
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
	 */
	public Entry update(final Entry newEntry, final long lease, final long timeout, final int updateModifiers)
	{
		return (Entry) execute(new JavaSpaceCallback() {
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
	 * Updates the first entry matching the specified template, if found and
	 * there is no transaction conflict.
	 *
	 * @param newPojo the new value of the pojo, must contain the primary key.
	 * @param lease The lease time of the updated entry, 0 means retain the
	 *            original lease
	 * @param timeout "IfExists" timeout
	 * @param updateModifiers - operation modifiers, values from UpdateModifiers
	 * @return the pojo
	 */
	public Object update(Object newPojo, long lease, long timeout, int updateModifiers)
	{
		Entry entry = converter.toEntry(newPojo,true);
		Entry newEnrty = update(entry,lease,timeout,updateModifiers);
		return converter.toPojo(newEnrty);
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
	 */
	public Object[] updateMultiple(final Entry[] entries, final long[] leases)
	{
		return (Object[]) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, InterruptedException, TransactionException, UnusableEntryException{
				return ((IJSpace)js).updateMultiple(entries, tx, leases);
			}
		});
	}

	/**
	 * Updates a group of pojo's. The pojo's with the new values must contain
	 * the pks and (optionally) versionIDs
	 *
	 * @param pojos the array of pojo's containing the new values , each pojo
	 *            must contain its primary key value.
	 * @param leases The lease time of the updated entries, 0 means retain the
	 *            original lease
	 * @return array of objects which correspond to the input entries array. An
	 *         object can be - an Entry, if the update was successful - null if
	 *         no update performed for the corresponding input - an Exception
	 *         object, in case of an exception
	 */
	public List updateMultiple(Object[] pojos, long[] leases)
	{
		Entry[] entries = new Entry[pojos.length];
		for (int qq = 0; qq < pojos.length; qq++)
		{
			entries[qq] = converter.toEntry(pojos[qq],true);
		}
		Object[] objects = updateMultiple(entries,leases);
		List  pojosRes = new ArrayList ();
		for (int ww = 0; ww < objects.length; ww++)
		{
			pojosRes.add(converter.toPojo((Entry) objects[ww]));
		}
		return pojosRes;
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
	 */
	public Object[] updateMultiple(final Entry[] entries, final long[] leases,
			final int updateModifiers)
	{
		return (Object[]) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, InterruptedException, TransactionException, UnusableEntryException{
				return ((IJSpace)js).updateMultiple(	entries,
				                                    	tx,
				                                    	leases,
				                                    	updateModifiers);
			}
		});
	}

	/**
	 * Updates a group of pojo's. The pojo's with the new values must contain
	 * the pks and (optionally) versionIDs
	 *
	 * @param pojos the array of pojo's containing the new values , each pojo
	 *            must contain its primary key.
	 * @param leases The lease time of the updated entries, 0 means retain the
	 *            original lease
	 * @param updateModifiers - operation modifiers, values from UpdateModifiers
	 * @return array of objects which correspond to the input entries array. An
	 *         object can be - an Entry, if the update was successful - null if
	 *         no update performed for the corresponding input - an Exception
	 *         object, in case of an exception
	 */
	public List updateMultiple(Object[] pojos, long[] leases, int updateModifiers)
	{

		Entry[] entries = new Entry[pojos.length];
		for (int qq = 0; qq < pojos.length; qq++)
		{
			entries[qq] = converter.toEntry(pojos[qq],true);
		}
		Object[] objects = updateMultiple(entries,leases,updateModifiers);
		List  pojosRes = new ArrayList ();
		for (int ww = 0; ww < objects.length; ww++)
		{
			pojosRes.add(converter.toPojo((Entry) objects[ww]));
		}
		return pojosRes;
	}

	/**
	 * Returns an indication : is this space secured. If for this space defined
	 * Security Filter, the space will be secured.
	 *
	 * @return boolean true if this space secured, otherwise false.
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
	 * Sets default transaction to use in case that null parameter is used as transaction
	 * parameter with JavaSpace methods. Notice: after commit/abort the default transaction
	 * should be set to null otherwise the TransactionException will be thrown on the next
	 * space operation call.
	 * @param transaction the default transaction.
	 *
	 */

	/*
	 public void setDefaultTransaction(final Transaction transaction)
	 {
	 execute(new JavaSpaceCallback() {
	 public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
	 ((IJSpace)js).setDefaultTransaction(transaction);
	 return null;
	 }
	 });

	 }
	 */

	/**
	 * Sets the update mode modifiers for proxy level.
	 * @param newModifiers new values for modifiers Values are defined in UpdateModifiers
	 * @return old value of modifiers.
	 */
	public int setUpdateModifiers(final int newModifiers)
	{
		return ((Integer) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				return new Integer(((IJSpace)js).setUpdateModifiers(newModifiers));
			}
		})).intValue();
	}

	/**Gets the proxyUpdateModifiers. Values are defined in UpdateModifiers class.
	 Default value: UPDATE_ONLY.
	 *
	 * @return value of modifiers.

	 */
	public int getUpdateModifiers()
	{
		return ((Integer) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				return new Integer(((IJSpace)js).getUpdateModifiers());
			}
		})).intValue();
	}

	/**
	 * Sets the read-take mode modifiers for proxy level.
	 * @param newModifiers new values for modifiers Values are defined in ReadTakeModifiers
	 * @return old value of modifiers.
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
	 * Gets the proxyReadTakeModifiers.
	 values are from ReadTakeModifiers.
	 * @return value of modifiers.
	 */
	public int getReadTakeModifiers()
	{
		return ((Integer) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException{
				return new Integer(((IJSpace)js).getReadTakeModifiers());
			}
		})).intValue();
	}

	/**
	 * Read the pojo from the space
	 * @param pojo the pojo
	 * @param lease the requested lease time, in milliseconds
	 * @return the pojo
	 */
	public Object read(Object pojo, long lease)
	{
		Entry entry = converter.toEntry(pojo,true);
		return converter.toPojo(read(entry,lease));
	}


	/**
	 * Read the pojo from the space if exist
	 * @param pojo the pojo
	 * @param lease the requested lease time, in milliseconds
	 * @return the pojo
	 */
	public Object readIfExists(Object pojo, long lease)
	{
		Entry entry = converter.toEntry(pojo,true);
		return converter.toPojo(readIfExists(entry,lease));
	}


	/**
	 * Gets the pojo to entry convertor
	 * @return IPojoToEntryConverter
	 */
	public IPojoToEntryConverter getPojoToEntryConverter()
	{
		return converter;
	}

	/**
	 * Sets the pojo to entry convertor
	 * @param converter
	 */
	public void setPojoToEntryConverter(IPojoToEntryConverter converter)
	{
		this.converter = converter;
	}

	/**
	 * When Pojo's are written that match this template notify the given listener with a RemoteEvent that includes the handback object. Matching is done as for read.
	 * using  NotifyDelegator for notify
	 * @param listener -The remote event listener to notify.
	 * @param templatePojo - The template used for matching. Matching is done against template with null fields being
	 * @param txn - The transaction (if any) under which to work.
	 * @param handback - An object to send to the listener as part of the event notification.
	 * @param fifoEnabled - enables/disables ordered notifications.
	 * @param lease - the requested lease time, in milliseconds
	 * @param notifyMask -Available Notify Types: NotifyModifiers.NOTIFY_WRITE NotifyModifiers.NOTIFY_UPDATE NotifyModifiers.NOTIFY_TAKE NotifyModifiers.NOTIFY_LEASE_EXPIRATION NotifyModifiers.NOTIFY_ALL NotifyDelegator nd = new NotifyDelegator(space, template, transaction, listener, Lease.FOREVER, handback, true, NotifyModifiers.NOTIFY_WRITE | NotifyModifiers.NOTIFY_TAKE
	 * @return NotifyDelegator the NotifyDelegator
	 */
	public NotifyDelegator addNotifyDelegatorListener(RemoteEventListener listener,Object templatePojo, MarshalledObject handback,boolean  fifoEnabled,long lease, int notifyMask)
	{
		Entry entry = (Entry)getPojoToEntryConverter().toEntry(templatePojo,true);
		JavaSpaceListener javaSpaceListener = new JavaSpaceListener();
		javaSpaceListener.setHandback(handback);
		javaSpaceListener.setLease(lease);
		javaSpaceListener.setListener(listener);
		javaSpaceListener.setTemplate(entry);
		return addNotifyDelegatorListener(javaSpaceListener, fifoEnabled, notifyMask);
	}

	/**
	 * When entries are written that match this template notify the given listener with a RemoteEvent that includes the handback object. Matching is done as for read.
	 * using  NotifyDelegator for notify
	 * @param javaSpaceListener - the javaSpaceListener.
	 * @param fifoEnabled - enables/disables ordered notifications.
	 * @param lease - the requested lease time, in milliseconds
	 * @param notifyMask -Available Notify Types: NotifyModifiers.NOTIFY_WRITE NotifyModifiers.NOTIFY_UPDATE NotifyModifiers.NOTIFY_TAKE NotifyModifiers.NOTIFY_LEASE_EXPIRATION NotifyModifiers.NOTIFY_ALL NotifyDelegator nd = new NotifyDelegator(space, template, transaction, listener, Lease.FOREVER, handback, true, NotifyModifiers.NOTIFY_WRITE | NotifyModifiers.NOTIFY_TAKE
	 * @return NotifyDelegator the NotifyDelegator
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
	 * @param templatePojo - The template used for matching. Matching is done against template with null fields being
	 * @param listener -The remote event listener to notify.
	 * @param millis - the requested lease time, in milliseconds
	 * @param handback - An object to send to the listener as part of the event notification.
	 * @param txn - The transaction (if any) under which to work.
	 * @return EventRegistration - the event registration to the the registrant
	 */
	public EventRegistration notify(Object templatePojo, RemoteEventListener listener,
			long millis, MarshalledObject handback, Transaction tx) {
		Entry template = converter.toEntry(templatePojo,true);
		return notify(template, listener, millis,  handback, tx);
	}

	/**
	 * When entries are written that match this template notify the given listener with a RemoteEvent that includes the handback object. Matching is done as for read.
	 * @param entry - The template used for matching. Matching is done against template with null fields being
	 * @param listener -The remote event listener to notify.
	 * @param millis - the requested lease time, in milliseconds
	 * @param handback - An object to send to the listener as part of the event notification.
	 * @param txn - The transaction (if any) under which to work.
	 * @return EventRegistration - the event registration to the the registrant
	 */
	public EventRegistration notify(final Entry template, final RemoteEventListener listener,
			final long millis, final MarshalledObject handback, final Transaction tx) {
		return (EventRegistration) execute(new JavaSpaceCallback() {
			public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException, TransactionException{
				return js.notify(template, tx, listener, millis, handback);
			}
		});
	}

}
