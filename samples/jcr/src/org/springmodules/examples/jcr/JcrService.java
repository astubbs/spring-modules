package org.springmodules.examples.jcr;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

/**
 * Sample class for working with the JCR repository.
 * 
 * @author Costin Leau
 *
 */
public class JcrService {
    private static final Log log = LogFactory.getLog(JcrService.class);

    private JcrTemplate template;

    /**
     * Save something inside the repository;
     *
     */
    public void saveWithRollback() {
        saveSmth();
        throw new RuntimeException("do rollback");
    }

    public void saveSmth() {
        template.execute(new JcrCallback() {

            public Object doInJcr(Session session) throws RepositoryException {
                Node root = session.getRootNode();
                log.info("starting from root node " + root);
                Node sample = root.addNode("sample node");
                sample.setProperty("sample property", "bla bla");
                log.info("saved property " + sample);
                session.save();
                return null;
            }
        });
    }

    /**
     * @return Returns the template.
     */
    public JcrTemplate getTemplate() {
        return template;
    }

    /**
     * @param template The template to set.
     */
    public void setTemplate(JcrTemplate template) {
        this.template = template;
    }

}
