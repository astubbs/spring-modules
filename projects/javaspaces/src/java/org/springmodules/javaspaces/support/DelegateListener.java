/**
 * Created on Mar 12, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.javaspaces.support;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * RemoteEventListener that uses delegation to pass the events to interested
 * listeners. The main advantage of this class is that, it handles the exporting
 * and unexporting of the listener as well as being only one instance for the entire
 * chain. 
 *  
 * @author Costin Leau
 *
 */
public class DelegateListener implements RemoteEventListener, InitializingBean, DisposableBean {

	private RemoteEventListener[] listeners;

	/**
	 * @see net.jini.core.event.RemoteEventListener#notify(net.jini.core.event.RemoteEvent)
	 */
	public void notify(RemoteEvent event) throws UnknownEventException, RemoteException {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i].notify(event);
		}
	}

	/**
	 * @see org.springmodules.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		UnicastRemoteObject.unexportObject(this, true);
	}

	/**
	 * @see org.springmodules.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		// use this method to allow jdk 5 dynamic stub creation (if possible)
		UnicastRemoteObject.exportObject(this, 0);
	}
}
