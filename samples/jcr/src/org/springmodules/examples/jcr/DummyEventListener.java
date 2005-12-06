/**
 * Created on Nov 24, 2005
 *
 * $Id: DummyEventListener.java,v 1.1 2005/12/06 10:37:10 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.examples.jcr;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.springmodules.jcr.SessionFactoryUtils;

/**
 * An event listener.
 * 
 * @author Costin Leau
 *
 */
public class DummyEventListener implements EventListener {
	
	public DummyEventListener()
	{
		System.out.println("constructor called");
	}
	/**
	 * @see javax.jcr.observation.EventListener#onEvent(javax.jcr.observation.EventIterator)
	 */
	public void onEvent(EventIterator iterator) {
		System.out.println("received events: ");
		for (; iterator.hasNext();) {
			try {
				System.out.println(toStringEvent(iterator.nextEvent()));
			}
			catch (RepositoryException e) {
				System.err.println("exception while listnening " + SessionFactoryUtils.translateException(e));
			}
		}
	}
	
	private String toStringEvent(Event event) throws RepositoryException
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("Event[path=");
		buffer.append(event.getPath());
		buffer.append("|type=");
		buffer.append(event.getType());
		buffer.append("|userID=");
		buffer.append(event.getUserID());
		buffer.append("]");
		return buffer.toString();
	}

}
