package org.springmodules.prevayler.support;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.support.DaoSupport;
import org.springmodules.prevayler.PrevaylerTemplate;

/**
 * Helper class to extend in your Data Access Objects, providing access to a configured instance of
 * {@link org.springmodules.prevayler.PrevaylerTemplate}.
 *
 * @author Sergio Bossa
 */
public class PrevaylerDaoSupport extends DaoSupport {
    
    private PrevaylerTemplate prevaylerTemplate;
    
    protected void checkDaoConfig() {
        if (this.prevaylerTemplate != null) {
            try {
                this.prevaylerTemplate.afterPropertiesSet();
            } 
            catch (Exception ex) {
                throw new InvalidDataAccessApiUsageException("Invalid prevayler template configuration!");
            }
        }
        else throw new IllegalStateException("No prevayler template set!");
    }

    public PrevaylerTemplate getPrevaylerTemplate() {
        return this.prevaylerTemplate;
    }

    public void setPrevaylerTemplate(PrevaylerTemplate prevaylerTemplate) {
        this.prevaylerTemplate = prevaylerTemplate;
    }
}
