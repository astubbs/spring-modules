package org.springmodules.examples.jcr;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

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
	public String saveWithRollback(String nodeName, String propertyName) {
		saveSmth(nodeName, propertyName);
		throw new RuntimeException("do rollback");
	}

	public String saveSmth(final String nodeName, final String propertyName) {
		return (String) template.execute(new JcrCallback() {

			public Object doInJcr(Session session) throws RepositoryException {
				Node root = session.getRootNode();
				log.info("starting from root node " + root.getPath());
				Node sample = root.addNode(nodeName);
				sample.setProperty(propertyName, "bla bla");
				log.info("saved property " + sample.getPath());
				session.save();
				return sample.getPath();
			}
		});
	}

	public boolean checkNode(final String absPath) {
		return template.itemExists(absPath);
	}

	public Value getNodeProperty(final String absPath, final String propertyName) {
		return (Value) template.execute(new JcrCallback() {

			public Object doInJcr(Session session) throws RepositoryException {
				Item item = session.getItem(absPath);
				if (item instanceof Node)
					return ((Node) item).getProperty(propertyName).getValue();

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
	 * @param template
	 *            The template to set.
	 */
	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}

}
