/*
 * Créé le 21 avr. 05
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
package org.springmodules.lucene.index.core;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.resource.ResourceException;
import org.springmodules.resource.ResourceManager;
import org.springmodules.resource.ResourceStatus;
import org.springmodules.resource.support.ResourceSynchronization;
import org.springmodules.resource.support.ResourceSynchronizationManager;

/**
 * @author ttemplier
 *
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public abstract class AbstractResourceManager implements ResourceManager {

	/** Transient to optimize serialization */
	protected transient Log logger = LogFactory.getLog(getClass());

	/**
	 * Trigger beforeClose callback.
	 */
	private void triggerBeforeClose(ResourceStatus status,Throwable ex) {
		if( logger.isDebugEnabled() ) {
			logger.debug("Triggering beforeClose synchronization");
		}

		for(Iterator it = ResourceSynchronizationManager.getSynchronizations().iterator(); it.hasNext();) {
			ResourceSynchronization synchronization = (ResourceSynchronization) it.next();
			synchronization.beforeClose(status);
		}
	}

	/**
	 * Trigger beforeClose callback.
	 */
	private void triggerAfterClose(ResourceStatus status,int completionStatus,Throwable ex) {
		if( logger.isDebugEnabled() ) {
			logger.debug("Triggering afterClose synchronization");
		}

		for(Iterator it = ResourceSynchronizationManager.getSynchronizations().iterator(); it.hasNext();) {
			ResourceSynchronization synchronization = (ResourceSynchronization) it.next();
			synchronization.afterClose(completionStatus);
		}
	}

	private final void initBeforeOpen() {
		ResourceSynchronizationManager.initSynchronization();
	}

	/**
	 * @see org.springmodules.resource.ResourceManager#open()
	 */
	public final ResourceStatus open() throws ResourceException {
		System.err.println("ResourceManager.open");
		initBeforeOpen();
		Object resource=doGetResource();
		doOpen(resource);
		ResourceStatus status=new ResourceStatus();
		status.setOpened(true);
		status.setResource(resource);
		return status;
	}

	protected abstract Object doGetResource();

	protected abstract void doOpen(Object resource) throws ResourceException;

	private final void cleanupAfterClose(ResourceStatus status) {
		status.setOpened(false);
		ResourceSynchronizationManager.clearSynchronization();
		doCleanupAfterClose(status.getResource());
		ResourceSynchronizationManager.setSynchronizationActive(false);
	}

	protected void doCleanupAfterClose(Object resource) {
	}

	/**
	 * @see org.springmodules.resource.ResourceManager#close()
	 */
	public final void close(ResourceStatus status) throws ResourceException {
		System.err.println("ResourceManager.close");

		try {
			boolean beforeCloseInvoked = false;
			try {
				triggerBeforeClose(status,null);
				beforeCloseInvoked = true;
				doClose(status);
			}
			catch (ResourceException ex) {
				triggerAfterClose(status, ResourceSynchronization.STATUS_UNKNOWN, ex);
				throw ex;
			}
			catch (RuntimeException ex) {
				if (!beforeCloseInvoked) {
					triggerBeforeClose(status, ex);
				}
				throw ex;
			}
			catch (Error err) {
				if (!beforeCloseInvoked) {
					triggerBeforeClose(status, err);
				}
				throw err;
			}
			triggerAfterClose(status, ResourceSynchronization.STATUS_CLOSED, null);
		}
		finally {
			cleanupAfterClose(status);
		}
	}

	protected abstract void doClose(ResourceStatus status) throws ResourceException;
}
