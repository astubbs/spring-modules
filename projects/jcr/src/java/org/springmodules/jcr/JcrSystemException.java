package org.springmodules.jcr;

import org.springframework.dao.UncategorizedDataAccessException;

/**
 * Runtime (unchecked) exception used for wrapping the JSR-170 specific checked exceptions.
 * 
 * @see javax.jcr.RepositoryException
 * 
 * @author Guillaume Bort <guillaume.bort@zenexity.fr>
 * @author Costin Leau
 *
 */
public class JcrSystemException extends UncategorizedDataAccessException {

    public JcrSystemException(String message, Throwable ex) {
        super(message, ex);
    }
    
    /**
     * 
     * @param ex
     */
    public JcrSystemException(Throwable ex) {
        super("Repository access exception", ex);
    }
}
