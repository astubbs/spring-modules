/**
 * Created on Nov 5, 2005
 *
 * $Id: ObjectServerUtils.java,v 1.1 2007/02/27 16:43:57 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.db4o;

import java.io.IOException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.ObjectNotStorableException;
import com.db4o.ext.OldFormatException;

/**
 * Utils class for ObjectServers. Handles exception translation at the moment.
 * @author Costin Leau
 * 
 */
public class ObjectServerUtils {

	/**
	 * db4o exception translator - it converts specific db4o unchecked/checked
	 * exceptions into unchecked Spring DA exception.
	 * 
	 * As there is no db4o specific base exception, the Db4oSystemException is
	 * used as a marker inside the package for a user specific runtime exception
	 * inside the callbacks for example.
	 * 
	 * 
	 * @param ex
	 * @return
	 */
	public static DataAccessException translateException(Exception ex) {
		if (ex instanceof DatabaseFileLockedException) {
			return new DataAccessResourceFailureException("database is already locked ", ex);
		}
		if (ex instanceof ObjectNotStorableException)
			return new InvalidDataAccessApiUsageException("object not storable ", ex);

		if (ex instanceof OldFormatException)
			return new DataAccessResourceFailureException("database is in old format", ex);

		if (ex instanceof IOException)
			return new DataAccessResourceFailureException("cannot do backup ", ex);

		// fallback
		return new Db4oSystemException(ex);
	}
}
