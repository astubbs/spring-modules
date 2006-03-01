/**
 * Created on Feb 21, 2006
 *
 * $Id: JbpmConfigurationUtils.java,v 1.1 2006/03/01 16:55:25 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.workflow.jbpm31;

import org.hibernate.HibernateException;
import org.jbpm.JbpmException;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;


/**
 * @author Costin Leau
 *
 */
public abstract class JbpmConfigurationUtils {

	/**
	 * Converts Jbpm RuntimeExceptions into Spring specific ones (if possible).
	 * @param ex
	 * @return
	 */
	public static RuntimeException convertJbpmException(JbpmException ex) {
		// decode nested exceptions
		if (ex.getCause() instanceof HibernateException) {
			DataAccessException rootCause = SessionFactoryUtils.convertHibernateAccessException((HibernateException) ex.getCause());
			return rootCause;
		}

		// cannot convert the exception in any meaningful way
		return ex;
	}


}
