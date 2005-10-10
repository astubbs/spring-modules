/**
 * Created on Sep 12, 2005
 *
 * $Id: OpenSessionInViewFilter.java,v 1.2 2005/10/10 09:20:49 costin Exp $
 * $Revision: 1.2 $
 */
package org.springmodules.jcr.support;

import java.io.IOException;

import javax.jcr.Session;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springmodules.jcr.SessionFactory;
import org.springmodules.jcr.SessionFactoryUtils;
import org.springmodules.jcr.SessionHolderProvider;

/**
 * Servlet 2.3 Filter that binds a JCR Session to the thread for the
 * entire processing of the request. Intended for the "Open Session
 * in View" pattern, i.e. to allow for lazy loading in web views despite the
 * original transactions already being completed.
 * 
 * <p>
 * This filter works similar to the AOP JcrInterceptor: It just makes JCR
 * Sessions available via the thread. It is suitable for
 * non-transactional execution but also for business layer transactions via
 * JcrTransactionManager or JtaTransactionManager. In the latter case,
 * Sessions pre-bound by this filter will automatically be used for
 * the transactions.
 * 
 * <p>
 * Looks up the SessionFactory in Spring's root web application
 * context. Supports a "SessionFactoryBeanName" filter init-param in
 * <code>web.xml</code>; the default bean name is
 * "SessionFactory". Looks up the SessionFactory on each
 * request, to avoid initialization order issues (when using
 * ContextLoaderServlet, the root application context will get initialized
 * <i>after</i> this filter).
 * 
 * <p>
 * The filter can be configured to use a SessionHolderProvider where implementation specific
 * session holder are required (like JackRabbit which extends the JCR session to provide 
 * transactional support). Support a "SessionHolderProviderBeanName" filter init-param
 * in <code>web.xml</code>; by default, the filter instantiates a DefaultSessionHolderProvider.
 * 
 * @author Costin Leau
 */
public class OpenSessionInViewFilter extends OncePerRequestFilter{
    public static final String DEFAULT_JCR_SESSION_FACTORY_FACTORY_BEAN_NAME = "sessionFactory";
    public static final String DEFAULT_JCR_SESSION_HOLDER_PROVIDER_BEAN_NAME = null;

    private String SessionFactoryBeanName = DEFAULT_JCR_SESSION_FACTORY_FACTORY_BEAN_NAME;
    private String SessionHolderProviderBeanName = DEFAULT_JCR_SESSION_HOLDER_PROVIDER_BEAN_NAME;
    
    /**
     * Set the bean name of the SessionFactory to fetch from Spring's
     * root application context. Default is "SessionFactory".
     * 
     * @see #DEFAULT_JCR_SESSION_FACTORY_FACTORY_BEAN_NAME
     */
    public void setSessionFactoryBeanName(String SessionFactoryBeanName) {
        this.SessionFactoryBeanName = SessionFactoryBeanName;
    }
    
    /**
     * Set the bean name of the SessionHolderProvider to fetch from Spring's
     * root application context. By default, no holder provider is looked up and 
     * the DefaultSessionHolderProvider is instantiated.
     * 
     * @see #DEFAULT_JCR_SESSION_HOLDER_PROVIDER_BEAN_NAME
     */
    public void setSessionHolderProviderBeanName(String SessionHolderProviderBeanName) {
        this.SessionHolderProviderBeanName = SessionHolderProviderBeanName;
    }

    /**
     * Return the bean name of the SessionFactory to fetch from
     * Spring's root application context.
     */
    protected String getSessionFactoryBeanName() {
        return SessionFactoryBeanName;
    }

    /**
     * Return the bean name of the SessionHolderProvider to fetch from
     * Spring's root application context.
     */
    protected String getSessionHolderProviderBeanName() {
        return SessionHolderProviderBeanName;
    }
    
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        SessionFactory sf = lookupSessionFactory(request);
        SessionHolderProvider sessionHolderProvider = lookupSessionHolderProvider(request);
        
        Session session = null;
        boolean participate = false;

        if (TransactionSynchronizationManager.hasResource(sf)) {
            // Do not modify the Session: just set the participate
            // flag.
            participate = true;
        } else {
            logger.debug("Opening JCR session in OpenSessionInViewFilter");
            session = SessionFactoryUtils.getSession(sf, true);
            TransactionSynchronizationManager.bindResource(sf, sessionHolderProvider.createSessionHolder(session));
        }

        try {
            filterChain.doFilter(request, response);
        }

        finally {
            if (!participate) {
                TransactionSynchronizationManager.unbindResource(sf);
                logger.debug("Closing JCR session in OpenSessionInViewFilter");
                SessionFactoryUtils.releaseSession(session, sf);
            }
        }
    }

    protected SessionHolderProvider lookupSessionHolderProvider(HttpServletRequest request) {
        return lookupSessionHolderProvider();
    }

    /**
     * Look up the SessionFactory that this filter should use, taking
     * the current HTTP request as argument.
     * <p>
     * Default implementation delegates to the
     * <code>lookupSessionFactory</code> without arguments.
     * 
     * @return the SessionFactory to use
     * @see #lookupSessionFactory()
     */
    protected SessionFactory lookupSessionFactory(HttpServletRequest request) {
        return lookupSessionFactory();
    }

    /**
     * Look up the SessionFactory that this filter should use. The
     * default implementation looks for a bean with the specified name in
     * Spring's root application context.
     * 
     * @return the SessionFactory to use
     * @see #getSessionFactoryBeanName
     */
    protected SessionFactory lookupSessionFactory() {
        if (logger.isDebugEnabled()) {
            logger.debug("Using session factory '" + getSessionFactoryBeanName() + "' for OpenSessionInViewFilter");
        }
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        return (SessionFactory) wac.getBean(getSessionFactoryBeanName(), SessionFactory.class);
    }
    
    protected SessionHolderProvider lookupSessionHolderProvider()
    {
        if (SessionHolderProviderBeanName == null)
        {
            logger.debug("Using default session holder provider");
            return new DefaultSessionHolderProvider();
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Using session holder provider '" + getSessionHolderProviderBeanName() + "' for OpenSessionInViewFilter");
        }
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        return (SessionHolderProvider) wac.getBean(getSessionHolderProviderBeanName(), SessionHolderProvider.class);
    }
    
}
