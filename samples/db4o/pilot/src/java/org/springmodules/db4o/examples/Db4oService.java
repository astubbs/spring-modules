/**
 * Created on Nov 12, 2005
 *
 * $Id: Db4oService.java,v 1.1 2007/02/27 16:42:23 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.db4o.examples;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.db4o.Db4oTemplate;

import com.db4o.ObjectSet;

/**
 * Demo class for working with db4o.
 * @author Costin Leau
 *
 */
public class Db4oService {

	private static final Log log = LogFactory.getLog(Db4oService.class);
	private Db4oTemplate template;

	/**
	 * Save something inside the repository;
	 *
	 */
	public void saveWithRollback() {
		saveSmth();
		throw new RuntimeException("do rollback");
	}

	public void listPilots() {
		Pilot proto = new Pilot(null, 0);
		ObjectSet result = template.get(proto);
		log.info("db contains pilots : " + result);
	}

	public void saveSmth() {
		Pilot pilot1 = new Pilot("Michael Schumacher", 100);
		template.set(pilot1);
		listPilots();
	}

	/**
	 * @return Returns the template.
	 */
	public Db4oTemplate getTemplate() {
		return template;
	}

	/**
	 * @param template The template to set.
	 */
	public void setTemplate(Db4oTemplate template) {
		this.template = template;
	}

}
