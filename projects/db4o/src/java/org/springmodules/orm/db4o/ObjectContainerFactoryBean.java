/**
 * Created on Nov 5, 2005
 *
 * $Id: ObjectContainerFactoryBean.java,v 1.1 2007/02/25 18:45:10 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.orm.db4o;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.ext.ExtDb4o;
import com.db4o.ext.MemoryFile;

/**
 * Class used for creating an ObjectContainer in a singleton manner as these are thread-safe.
 * The factory bean will try to create using the available properties in the following order:
 * <ol>
 * <li>if databaseFile is set, a local file based client will be created.</li>
 * <li>if memory file is set, a local memory based client will be created.<li> 
 * <li>if server is set, a client within the same VM as the server will be created.</li>
 * <li>if all the above fail the client will be opened using hostName, port, user, password to a remote
 * server.</li>
 * </ol>
 * 
 * @author Costin Leau
 *
 */
public class ObjectContainerFactoryBean implements FactoryBean, InitializingBean, DisposableBean {

	private static final Log log = LogFactory.getLog(ObjectContainerFactoryBean.class);

	private ObjectContainer container;
	// Local mode

	// file based
	private Resource databaseFile;
	// memory based
	private MemoryFile memoryFile;

	// Server mode

	// local server mode
	private ObjectServer server;

	// remote server mode
	private String hostName, user, password;
	private int port;

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return container;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return (container != null ? container.getClass() : ObjectContainer.class);
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {

		if (databaseFile != null) {
			container = Db4o.openFile(databaseFile.getFile().getAbsolutePath());
			log.info("opened db4o local file-based objectContainer @" + System.identityHashCode(container));
		}
		else if (memoryFile != null) {
			container = ExtDb4o.openMemoryFile(memoryFile);
			log.info("opened db4o local memory-based objectContainer @" + System.identityHashCode(container));
		}
		else if (server != null) {
			container = server.openClient();
			log.info("opened db4o embedded server-based objectContainer @"
					+ System.identityHashCode(container));

		}
		else {
			// sanity check
			if ((hostName == null) || (port <= 0) || (user == null)) {
				throw new IllegalArgumentException(
						"mandatory fields are not set; databaseFile or memoryFile or container or (hostName, port, user) are required");
			}
			container = Db4o.openClient(hostName, port, user, password);
			log.info("opened db4o remote server-based objectContainer @" + System.identityHashCode(container));
		}
		log.info(Db4o.version());
	}

	/**
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		log.info("closing object container " + System.identityHashCode(container));
		container.close();
	}

	/**
	 * @return Returns the file.
	 */
	public Resource getDatabaseFile() {
		return databaseFile;
	}

	/**
	 * @param file The file to set.
	 */
	public void setDatabaseFile(Resource file) {
		this.databaseFile = file;
	}

	/**
	 * @return Returns the hostName.
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName The hostName to set.
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Returns the port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port The port to set.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return Returns the server.
	 */
	public ObjectServer getServer() {
		return server;
	}

	/**
	 * @param server The server to set.
	 */
	public void setServer(ObjectServer server) {
		this.server = server;
	}

	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return Returns the memoryFile.
	 */
	public MemoryFile getMemoryFile() {
		return memoryFile;
	}

	/**
	 * @param memoryFile The memoryFile to set.
	 */
	public void setMemoryFile(MemoryFile memoryFile) {
		this.memoryFile = memoryFile;
	}

}
