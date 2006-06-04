/**
 * Created on Mar 11, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.javaspaces;

import java.rmi.RemoteException;
import java.util.Arrays;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Base FactoryBean for retrieving JavaSpace proxies. Contains common functionality like registering
 * notifiers.
 * 
 * TODO: maybe add RMI registration at startup and shutdown (check if they are registered or not)
 * 
 * @author Costin Leau
 *
 */
public abstract class AbstractJavaSpaceFactoryBean implements FactoryBean, InitializingBean, DisposableBean {

	protected final Log logger = LogFactory.getLog(getClass());

	private JavaSpace space;
	
	// listeners to register
	private JavaSpaceListener[] listeners = null;

	/**
	 * @see org.springmodules.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return space;
	}

	/**
	 * @see org.springmodules.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return (space == null ? JavaSpace.class : space.getClass());
	}

	/**
	 * @see org.springmodules.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @see org.springmodules.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		// set the system properties
		logger.info("creating JavaSpace");
		this.space = createSpace();

		addListeners(this.space);
	}
	
	/**
	 * Add listeners to the given JavaSpace.
	 * 
	 * @param javaSpace
	 */
	protected void addListeners(JavaSpace javaSpace)
	{
		// use the space template
		JavaSpaceTemplate template = new JavaSpaceTemplate(javaSpace);

		if (listeners != null && listeners.length > 0) {
			logger.info("registering listeners " + Arrays.asList(listeners).toString());
			
			// use the template to do the registration
			template.execute(new JavaSpaceCallback() {
				/**
				 * @see org.springmodules.javaspaces.JavaSpaceCallback#doInSpace(net.jini.space.JavaSpace, org.springmodules.jini.JiniTransactionStatus)
				 */
				public Object doInSpace(JavaSpace js, Transaction tx) throws RemoteException,
						TransactionException, UnusableEntryException, InterruptedException {

					for (int i = 0; i < listeners.length; i++) {
						JavaSpaceListener listDef = listeners[i];
						js.notify(listDef.getTemplate(), tx, listDef.getListener(),
								listDef.getLease(), listDef.getHandback());

					}
					return null;
				}
			});
		}
	}
	
	/**
	 * Subclasses should implement space provider specific 'closing' method.  
	 * 
	 * @see org.springmodules.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
	}

	/**
	 * Actual method used by subclasses to create the java space.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract JavaSpace createSpace() throws Exception;


	/**
	 * @return Returns the listeners.
	 */
	public JavaSpaceListener[] getListeners() {
		return listeners;
	}

	/**
	 * @param listeners The listeners to set.
	 */
	public void setListeners(JavaSpaceListener[] listeners) {
		this.listeners = listeners;
	}

}
