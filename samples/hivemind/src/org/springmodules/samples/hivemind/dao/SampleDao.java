package org.springmodules.samples.hivemind.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Sample dao implementation
 * 
 * @author Thierry Templier
 */
public class SampleDao implements ISampleDao {
	private String configurationFile;

	private static final Log log = LogFactory.getLog(SampleDao.class);

	public SampleDao() {
		if( log.isDebugEnabled() ) {
			System.out.println("Creation of a new instance of SampleDao ("+toString()+")");
		}
	}

	/**
	 * @see dao.IMonDao#executeDao(java.lang.String)
	 */
	public void executeDao(String param) {
		if( log.isDebugEnabled() ) {
			System.out.println("The instance of the service is "+toString());
			System.out.println("The value of configurationFile is "+configurationFile);
			System.out.println("Execution of the executeService method with the param "+param);
		}
	}

	/**
	 * @return
	 */
	public String getConfigurationFile() {
		return configurationFile;
	}

	/**
	 * @param string
	 */
	public void setConfigurationFile(String configurationFile) {
		this.configurationFile = configurationFile;
	}

}
