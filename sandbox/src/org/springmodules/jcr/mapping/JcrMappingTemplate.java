/**
 * Created on Oct 2, 2005
 *
 * $Id: JcrMappingTemplate.java,v 1.1 2005/10/10 09:27:27 costin Exp $
 * $Revision: 1.1 $
 */
package org.springmodules.jcr.mapping;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.portals.graffito.jcr.exception.JcrMappingException;
import org.apache.portals.graffito.jcr.mapper.Mapper;
import org.apache.portals.graffito.jcr.mapper.model.MappingDescriptor;
import org.apache.portals.graffito.jcr.persistence.PersistenceManager;
import org.apache.portals.graffito.jcr.persistence.atomictypeconverter.BinaryTypeConverter;
import org.apache.portals.graffito.jcr.persistence.atomictypeconverter.BooleanTypeConverter;
import org.apache.portals.graffito.jcr.persistence.atomictypeconverter.ByteArrayTypeConverter;
import org.apache.portals.graffito.jcr.persistence.atomictypeconverter.CalendarTypeConverter;
import org.apache.portals.graffito.jcr.persistence.atomictypeconverter.DoubleTypeConverter;
import org.apache.portals.graffito.jcr.persistence.atomictypeconverter.IntTypeConverter;
import org.apache.portals.graffito.jcr.persistence.atomictypeconverter.LongTypeConverter;
import org.apache.portals.graffito.jcr.persistence.atomictypeconverter.StringTypeConverter;
import org.apache.portals.graffito.jcr.persistence.atomictypeconverter.TimestampTypeConverter;
import org.apache.portals.graffito.jcr.persistence.atomictypeconverter.UtilDateTypeConverter;
import org.apache.portals.graffito.jcr.persistence.impl.PersistenceManagerImpl;
import org.apache.portals.graffito.jcr.query.Query;
import org.apache.portals.graffito.jcr.query.QueryManager;
import org.apache.portals.graffito.jcr.query.impl.QueryManagerImpl;
import org.springframework.dao.DataAccessException;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrSystemException;
import org.springmodules.jcr.JcrTemplate;
import org.springmodules.jcr.SessionFactory;
import org.springmodules.jcr.mapping.support.MapperSupport;

/**
 * Template whichs adds mapping support for the Java Content Repository.
 * <p/>
 * For PersistenceManagers the template creates internally the set of default converters.
 * 
 * 
 * @see org.apache.portals.graffito.jcr.persistence.PersistenceManager
 * @author Costin Leau
 * 
 */
public class JcrMappingTemplate extends JcrTemplate implements JcrMappingOperations {

    private Mapper mapper;

    /**
     * Default constructor for JcrTemplate
     */
    public JcrMappingTemplate() {
        super();

    }

    /**
     * @param sessionFactory
     */
    public JcrMappingTemplate(SessionFactory sessionFactory, Mapper mapper) {
        setSessionFactory(sessionFactory);
        setMapper(mapper);
        afterPropertiesSet();
    }

    /**
     * @param sessionFactory
     */
    public JcrMappingTemplate(SessionFactory sessionFactory, MappingDescriptor descriptor) {
        setSessionFactory(sessionFactory);
        setMapper(descriptor);
        afterPropertiesSet();
    }

    /**
     * Add rule for checking the mapper.
     * 
     * @see org.springmodules.jcr.JcrAccessor#afterPropertiesSet()
     */
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        if (mapper == null)
            throw new IllegalArgumentException("mapper can NOT be null");
    }

    /**
     * Method for creating a query manager. It's unclear where this entity is stateless or not.
     * 
     * @return
     */
    protected QueryManager createQueryManager() {
        return new QueryManagerImpl(mapper);
    }

    /**
     * Creates a persistence manager. It's unclear if this object is stateless/thread-safe or not.
     * However because it depends on session it has to be created per session and it's not per repository.
     * 
     * 
     * @param session
     * @return
     * @throws JcrMappingException
     */
    protected PersistenceManager createPersistenceManager(Session session) throws RepositoryException, JcrMappingException {
        return new PersistenceManagerImpl(mapper, createDefaultConverters(session), createQueryManager(), session);
    }

    /**
     * Due to the way the actual jcr-mapping is made we have to create the converters for
     * each session.
     * 
     * @param session
     * @return
     */
    protected Map createDefaultConverters(Session session) throws RepositoryException {
        Map map = new HashMap(14);

        map.put(String.class, new StringTypeConverter(session.getValueFactory()));
        map.put(InputStream.class, new BinaryTypeConverter(session.getValueFactory()));
        map.put(long.class, new LongTypeConverter(session.getValueFactory()));
        map.put(Long.class, new LongTypeConverter(session.getValueFactory()));
        map.put(int.class, new IntTypeConverter(session.getValueFactory()));
        map.put(Integer.class, new IntTypeConverter(session.getValueFactory()));
        map.put(double.class, new DoubleTypeConverter(session.getValueFactory()));
        map.put(Double.class, new DoubleTypeConverter(session.getValueFactory()));
        map.put(boolean.class, new BooleanTypeConverter(session.getValueFactory()));
        map.put(Boolean.class, new BooleanTypeConverter(session.getValueFactory()));
        map.put(Calendar.class, new CalendarTypeConverter(session.getValueFactory()));
        map.put(Date.class, new UtilDateTypeConverter(session.getValueFactory()));
        map.put(byte[].class, new ByteArrayTypeConverter(session.getValueFactory()));
        map.put(Timestamp.class, new TimestampTypeConverter(session.getValueFactory()));

        return map;
    }

    /**
     * @see org.springmodules.jcr.mapping.JcrMappingOperations#execute(org.springmodules.jcr.mapping.JcrMappingCallback, boolean)
     */
    public Object execute(final JcrMappingCallback action, boolean exposeNativeSession) throws DataAccessException {
        return execute(new JcrCallback() {
            /**
             * @see org.springmodules.jcr.JcrCallback#doInJcr(javax.jcr.Session)
             */
            public Object doInJcr(Session session) throws RepositoryException {
                try {
                    return action.doInJcrMapping(createPersistenceManager(session));
                } catch (JcrMappingException e) {
                    throw convertMappingAccessException(e);
                }
            }

        }, exposeNativeSession);
    }

    /**
     * @see org.springmodules.jcr.mapping.JcrMappingOperations#execute(org.springmodules.jcr.mapping.JcrMappingCallback)
     */
    public Object execute(JcrMappingCallback callback) throws DataAccessException {
        return execute(callback, isExposeNativeSession());
    }

    // ----------------
    // Delegate methods
    // ----------------

    /**
     * @see org.springmodules.jcr.mapping.JcrMappingOperations#insert(java.lang.String, java.lang.Object)
     */
    public void insert(final java.lang.String path, final java.lang.Object object) {
        execute(new JcrMappingCallback() {
            public Object doInJcrMapping(PersistenceManager manager) throws JcrMappingException {
                manager.insert(path, object);
                return null;
            }
        }, true);
    }

    /**
     * @see org.springmodules.jcr.mapping.JcrMappingOperations#update(java.lang.String, java.lang.Object)
     */
    public void update(final java.lang.String path, final java.lang.Object object) {
        execute(new JcrMappingCallback() {
            public Object doInJcrMapping(PersistenceManager manager) throws JcrMappingException {
                manager.update(path, object);
                return null;
            }
        }, true);
    }

    /**
     * @see org.springmodules.jcr.mapping.JcrMappingOperations#remove(java.lang.String)
     */
    public void remove(final java.lang.String path) {
        execute(new JcrMappingCallback() {
            public Object doInJcrMapping(PersistenceManager manager) throws JcrMappingException {
                manager.remove(path);
                return null;
            }
        }, true);
    }

    /**
     * @see org.springmodules.jcr.mapping.JcrMappingOperations#getObject(java.lang.Class, java.lang.String)
     */
    public Object getObject(final java.lang.Class pojoClass, final java.lang.String path) {
        return execute(new JcrMappingCallback() {
            public Object doInJcrMapping(PersistenceManager manager) throws JcrMappingException {
                return manager.getObject(pojoClass, path);
            }
        }, true);
    }

    /**
     * @see org.springmodules.jcr.mapping.JcrMappingOperations#getObjects(org.apache.portals.graffito.jcr.query.Query)
     */
    public Collection getObjects(final Query query) {
        return (Collection) execute(new JcrMappingCallback() {
            public Object doInJcrMapping(PersistenceManager manager) throws JcrMappingException {
                return manager.getObjects(query);
            }
        }, true);
    }

    /**
     * Convert the given MappingException to an appropriate exception from
     * the <code>org.springframework.dao</code> hierarchy.
     * <p>
     * Note that because we have no base specific exception we have to catch
     * the generic Exception and translate it into JcrSystemException.
     * <p>
     * May be overridden in subclasses.
     * 
     * @param ex Exception that occured
     * @return the corresponding DataAccessException instance
     */
    public DataAccessException convertMappingAccessException(Exception ex) {
        // repository exception
        if (ex instanceof RepositoryException)
            return super.convertJcrAccessException((RepositoryException) ex);
        return new JcrSystemException(ex);
    }

    /**
     * @return Returns the mapper.
     */
    public Mapper getMapper() {
        return mapper;
    }

    /**
     * @param mapper The mapper to set.
     */
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    /**
     * This method should be used when the client has the mapping descriptions but not
     * the mapper.
     * 
     * @param descriptor The mapping descriptor to set.
     */
    public void setMapper(MappingDescriptor descriptor) {
        this.mapper = new MapperSupport(descriptor);
    }

}
