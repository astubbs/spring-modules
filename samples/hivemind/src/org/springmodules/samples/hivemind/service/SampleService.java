package org.springmodules.samples.hivemind.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.samples.hivemind.dao.ISampleDao;

/**
 * Sample service implementation
 * 
 * @author Thierry Templier
 */
public class SampleService implements ISampleService {
	private ISampleDao dao;

	private static final Log log = LogFactory.getLog(SampleService.class);

	public SampleService() {
		if( log.isDebugEnabled() ) {
			System.out.println("Creation of a new instance of SampleService ("+toString()+")");
		}
	}

	/**
	 * @see service.IMonService#executeService(java.lang.String)
	 */
	public void executeService(String param) {
		if( log.isDebugEnabled() ) {
			System.out.println("The instance of the service is "+toString());
			System.out.println("Execution of the executeService method with the param "+param);
		}
		dao.executeDao(param);
	}

	/**
	 * @return
	 */
	public ISampleDao getDao() {
		return dao;
	}

	/**
	 * @param dao
	 */
	public void setDao(ISampleDao dao) {
		this.dao = dao;
	}

}
