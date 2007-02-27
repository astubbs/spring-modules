/**
 * Created on Nov 5, 2005
 *
 * $Id: Db4oSystemException.java,v 1.1 2007/02/27 16:43:56 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.db4o;

import org.springframework.dao.UncategorizedDataAccessException;

/**
 * Runtime (unchecked) exception used for wrapping the db4o exceptions
 * which do not match the Spring DAO exception hierarchy.
 * 
 * @author Costin Leau
 *
 */
public class Db4oSystemException extends UncategorizedDataAccessException{
	
	public Db4oSystemException(String message, Throwable ex) {
		super(message, ex);
	}

	/**
	 * 
	 * @param ex
	 */
	public Db4oSystemException(Throwable ex) {
		super("db4o access exception", ex);
	}
}
