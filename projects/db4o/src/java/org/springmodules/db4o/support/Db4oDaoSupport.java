package org.springmodules.db4o.support;

import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;
import org.springmodules.db4o.Db4oTemplate;

import com.db4o.ObjectContainer;

/**
 * Convenient super class for Db4o data access objects.
 * 
 * <p>
 * Requires a ObjectContainer to be set, providing a Db4oTemplate based on it to
 * subclasses. Can alternatively be initialized directly via a Db4oTemplate.
 * 
 * <p>
 * This class will create its own Db4oTemplate if only a ObjectContainer is
 * passed in.
 * 
 * @author Daniel Mitterdorfer
 * @author Costin Leau
 * 
 */
public abstract class Db4oDaoSupport extends DaoSupport {
	private Db4oTemplate template;

	/**
	 * Set the Db4o ObjectContainer to be used by this DAO. Will automatically
	 * create a Db4oTemplate for the given ObjectContainer.
	 * 
	 * @see #createDb4oTemplate
	 * @see #setDb4oTemplate
	 */
	public final void setObjectContainer(ObjectContainer container) {
		template = createDb4oTemplate(container);
	}

	/**
	 * Create a Db4oTemplate for the given ObjectContainer. Only invoked if
	 * populating the DAO with a ObjectContainer reference!
	 * <p>
	 * Can be overridden in subclasses to provide a Db4oTemplate instance with
	 * different configuration, or a custom Db4oTemplate subclass.
	 * 
	 * @param container the Db4o ObjectContainer to create a Db4oTemplate for
	 * @return the new Db4oTemplate instance
	 * @see #setObjectContainer
	 */
	protected Db4oTemplate createDb4oTemplate(ObjectContainer container) {
		return new Db4oTemplate(container);
	}

	/**
	 * @return A valid object container if #setObjectContainer or #setTemplate
	 * have been invoked previously. This should ever be the case if you
	 * configure this class in your application context file and let Spring wire
	 * up the dependencies. If there was no previous call, null is returned.
	 */
	public final ObjectContainer getObjectContainer() {
		return (template != null) ? template.getObjectContainer() : null;
	}

	/**
	 * Set the Db4oTemplate for this DAO explicitly, as an alternative to
	 * specifying an ObjectContainer.
	 * 
	 * @see #setObjectContainer
	 */

	public final void setDb4oTemplate(Db4oTemplate db4oTemplate) {
		template = db4oTemplate;
	}

	public final Db4oTemplate getDb4oTemplate() {
		return template;
	}

	/**
	 * @see org.springframework.dao.support.DaoSupport#checkDaoConfig()
	 */
	protected final void checkDaoConfig() {
		Assert.notNull(template, "objectContainer or db4oTemplate is required");
	}

	/**
	 * Convert the given db4o exception into a Spring unchecked DAO exception.
	 * 
	 * @param ex
	 * @return
	 */
	protected final RuntimeException convertDb4oException(Exception ex) {
		return template.convertDb4oAccessException(ex);
	}
}
