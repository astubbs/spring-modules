/**
 * Created on Mar 12, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.javaspaces;

import java.rmi.MarshalledObject;

import net.jini.core.entry.Entry;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.lease.Lease;

/**
 * Plain JavaBean used as a container for passing information to notify method in the JavaSpace
 * interface. 
 * 
 * @author Costin Leau
 *
 */
public class JavaSpaceListener {

	private Entry template;
	private RemoteEventListener listener;
	private long lease = Lease.ANY;
	private MarshalledObject handback;
	
	public JavaSpaceListener() {
	}

	/**
	 * @param listener
	 * @param lease
	 */
	public JavaSpaceListener(Entry template, RemoteEventListener listener) {
		this.template = template;
		this.listener = listener;
	}

	/**
	 * @return Returns the handback.
	 */
	public MarshalledObject getHandback() {
		return handback;
	}

	/**
	 * @param handback The handback to set.
	 */
	public void setHandback(MarshalledObject handback) {
		this.handback = handback;
	}

	/**
	 * @return Returns the lease.
	 */
	public long getLease() {
		return lease;
	}

	/**
	 * @param lease The lease to set.
	 */
	public void setLease(long lease) {
		this.lease = lease;
	}

	/**
	 * @return Returns the listener.
	 */
	public RemoteEventListener getListener() {
		return listener;
	}

	/**
	 * @param listener The listener to set.
	 */
	public void setListener(RemoteEventListener listener) {
		this.listener = listener;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer().append("[listener=").append(this.listener).append("handback=").append(this.handback).append(
				"lease=").append(this.lease).append("template=").append(this.template).append("]").toString();
	}

	/**
	 * @return Returns the template.
	 */
	public Entry getTemplate() {
		return template;
	}

	/**
	 * @param template The template to set.
	 */
	public void setTemplate(Entry template) {
		this.template = template;
	}
}
