/**
 * Created on Nov 5, 2005
 *
 * $Id: ObjectContainerFactoryBean.java,v 1.1 2007/02/27 16:43:58 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.db4o;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.config.Configuration;
import com.db4o.ext.ExtDb4o;
import com.db4o.ext.MemoryFile;

/**
 * Class used for creating an {@link ObjectContainer} in a singleton manner as
 * these are thread-safe.
 * 
 * <p/> The factory bean will try to create using the available properties in
 * the following order:
 * <ol>
 * <li>if databaseFile is set, a local file based client will be created.</li>
 * <li>if memory file is set, a local memory based client will be created.
 * <li>
 * <li>if server is set, a client within the same VM as the server will be
 * created.</li>
 * <li>if all the above fail the client will be opened using hostName, port,
 * user, password to a remote server.</li>
 * </ol>
 * 
 * 
 * <p/> Accepts a {@link Configuration} object for local configurations. If none
 * is given, the global db4o configuration will be used.
 * 
 * @see com.db4o.Db4o
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

	private Configuration configuration;

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
		// initialize the configuration to use only one method variant
		if (configuration == null)
			configuration = Db4o.configure();

		if (databaseFile != null) {
			container = Db4o.openFile(configuration, databaseFile.getFile().getAbsolutePath());
			
			log.info("opened db4o local file-based objectContainer @" + ObjectUtils.getIdentityHexString(container));
		}
		else if (memoryFile != null) {
			container = ExtDb4o.openMemoryFile(configuration, memoryFile);
			log.info("opened db4o local memory-based objectContainer @" + ObjectUtils.getIdentityHexString(container));
		}
		else if (server != null) {
			container = server.openClient();
			log.info("opened db4o embedded server-based objectContainer @" + ObjectUtils.getIdentityHexString(container));

		}
		else {
			// sanity check
			if ((hostName == null) || (port <= 0) || (user == null)) {
				throw new IllegalArgumentException(
						"mandatory fields are not set; databaseFile or memoryFile or container or (hostName, port, user) are required");
			}
			container = Db4o.openClient(configuration, hostName, port, user, password);
			log.info("opened db4o remote server-based objectContainer @" + ObjectUtils.getIdentityHexString(container));
		}
		log.info(Db4o.version());
	}

	/**
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		log.info("closing object container " + ObjectUtils.getIdentityHexString(container));
		container.close();
	}

	/**
	 * @param file The file to set.
	 */
	public void setDatabaseFile(Resource file) {
		this.databaseFile = file;
	}

	/**
	 * @param hostName The hostName to set.
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param port The port to set.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @param server The server to set.
	 */
	public void setServer(ObjectServer server) {
		this.server = server;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @param memoryFile The memoryFile to set.
	 */
	public void setMemoryFile(MemoryFile memoryFile) {
		this.memoryFile = memoryFile;
	}

	/**
	 * Set the configuration object to be used when creating the client. If none
	 * is specified, the global db4o configuration is used.
	 * 
	 * @param configuration The configuration to set.
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
}
