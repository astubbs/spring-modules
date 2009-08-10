/**
 * Created on Sep 12, 2005
 *
 * $Id: OpenSessionInViewInterceptor.java,v 1.2 2006/03/07 13:09:30 costin Exp $
 * $Revision: 1.2 $
 */
package org.springmodules.jcr.support;

import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springmodules.jcr.SessionFactory;
import org.springmodules.jcr.SessionFactoryUtils;
import org.springmodules.jcr.SessionHolder;

/**
 * Spring web HandlerInterceptor that binds a JCR Session to the
 * thread for the entire processing of the request. Intended for the "Open
 * Session in View" pattern, i.e. to allow for lazy loading in web
 * views despite the original transactions already being completed.
 * 
 * <p>
 * This filter works similar to the AOP JcrInterceptor: It just makes JCR
 * Sessions available via the thread. It is suitable for
 * non-transactional execution but also for middle tier transactions via
 * JcrTransactionManager or JtaTransactionManager. In the latter case,
 * Sessions pre-bound by this filter will automatically be used for
 * the transactions.
 * 
 * <p>
 * In contrast to OpenSessionInViewFilter, this interceptor is set up
 * in a Spring application context and can thus take advantage of bean wiring.
 * It derives from JcrAccessor to inherit common JCR configuration properties.
 * 
 * @author Costin Leau
 */
public class OpenSessionInViewInterceptor extends HandlerInterceptorAdapter implements InitializingBean {
	/**
	 * Suffix that gets appended to the SessionFactory toString
	 * representation for the "participate in existing persistence manager
	 * handling" request attribute.
	 * 
	 * @see #getParticipateAttributeName
	 */
	public static final String PARTICIPATE_SUFFIX = ".PARTICIPATE";

	protected final Log logger = LogFactory.getLog(getClass());

	private SessionFactory sessionFactory;

	/**
	 * Set the JCR JcrSessionFactory that should be used to create
	 * Sessions.
	 */
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	/**
	 * Return the JCR JcrSessionFactory that should be used to create
	 * Sessions.
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws DataAccessException {

		if (TransactionSynchronizationManager.hasResource(getSessionFactory())) {
			// do not modify the Session: just mark the request
			// accordingly
			String participateAttributeName = getParticipateAttributeName();
			Integer count = (Integer) request.getAttribute(participateAttributeName);
			int newCount = (count != null) ? count.intValue() + 1 : 1;
			request.setAttribute(getParticipateAttributeName(), new Integer(newCount));
		}

		else {
			logger.debug("Opening JCR session in OpenSessionInViewInterceptor");
			Session s = SessionFactoryUtils.getSession(getSessionFactory(), true);
			TransactionSynchronizationManager.bindResource(getSessionFactory(),
					getSessionFactory().getSessionHolder(s));
		}

		return true;
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) throws DataAccessException {

		String participateAttributeName = getParticipateAttributeName();
		Integer count = (Integer) request.getAttribute(participateAttributeName);
		if (count != null) {
			// do not modify the Session: just clear the marker
			if (count.intValue() > 1) {
				request.setAttribute(participateAttributeName, new Integer(count.intValue() - 1));
			}
			else {
				request.removeAttribute(participateAttributeName);
			}
		}

		else {
			SessionHolder sesHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(getSessionFactory());
			logger.debug("Closing JCR session in OpenSessionInViewInterceptor");
			SessionFactoryUtils.releaseSession(sesHolder.getSession(), getSessionFactory());
		}
	}

	/**
	 * Return the name of the request attribute that identifies that a request
	 * is already filtered. Default implementation takes the toString
	 * representation of the JcrSessionFactory instance and appends
	 * ".FILTERED".
	 * 
	 * @see #PARTICIPATE_SUFFIX
	 */
	protected String getParticipateAttributeName() {
		return getSessionFactory().toString() + PARTICIPATE_SUFFIX;
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if (sessionFactory == null)
			throw new IllegalArgumentException("sessionFactory is required");
	}
}
