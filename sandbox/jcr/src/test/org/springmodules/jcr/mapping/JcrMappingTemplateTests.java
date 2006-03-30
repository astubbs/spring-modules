package org.springmodules.jcr.mapping;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;

import junit.framework.TestCase;

import org.apache.portals.graffito.jcr.exception.JcrMappingException;
import org.apache.portals.graffito.jcr.mapper.Mapper;
import org.apache.portals.graffito.jcr.persistence.PersistenceManager;
import org.apache.portals.graffito.jcr.query.Query;
import org.easymock.MockControl;
import org.springmodules.jcr.JcrSystemException;
import org.springmodules.jcr.SessionFactory;

public class JcrMappingTemplateTests extends TestCase {

    private JcrMappingTemplate template;

    MockControl factoryCtrl;

    SessionFactory factory;

    MockControl sessionCtrl;

    Session session;

    MockControl valueFactoryCtrl;

    ValueFactory valueFactory;

    MockControl managerCtrl;

    PersistenceManager manager;

    MockControl mapperCtrl;

    Mapper mapper;

    protected void setUp() throws Exception {
        super.setUp();

        factoryCtrl = MockControl.createControl(SessionFactory.class);
        factory = (SessionFactory) factoryCtrl.getMock();
        sessionCtrl = MockControl.createControl(Session.class);
        session = (Session) sessionCtrl.getMock();
        valueFactoryCtrl = MockControl.createControl(ValueFactory.class);
        valueFactory = (ValueFactory) valueFactoryCtrl.getMock();
        managerCtrl = MockControl.createControl(PersistenceManager.class);
        manager = (PersistenceManager) managerCtrl.getMock();
        mapperCtrl = MockControl.createControl(Mapper.class);
        mapper = (Mapper) mapperCtrl.getMock();

        // add the mock for testing
        template = new JcrMappingTemplate(factory, mapper) {

            /**
             * @see org.springmodules.jcr.mapping.JcrMappingTemplate#createPersistenceManager(javax.jcr.Session)
             */
            protected PersistenceManager createPersistenceManager(Session session) throws RepositoryException, JcrMappingException {
                return manager;
            }

        };
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        try {
            factoryCtrl.verify();
            sessionCtrl.verify();
            valueFactoryCtrl.verify();
            managerCtrl.verify();
            mapperCtrl.verify();
        } catch (IllegalStateException e) {
        }
    }

    public void testAfterPropertiesSet() throws Exception {
        template.setMapper((Mapper) null);
        try {
            template.afterPropertiesSet();
        } catch (IllegalArgumentException e) {
            // it's okay
        }
    }

    /*
     * Test method for 'org.springmodules.jcr.mapping.JcrMappingTemplate.execute(JcrMappingCallback, boolean)'
     */
    public void testExecuteJcrMappingCallbackBoolean() throws Exception {
        template.setAllowCreate(true);

        factoryCtrl.expectAndReturn(factory.getSession(), session);
        session.logout();

        factoryCtrl.replay();
        sessionCtrl.replay();
        valueFactoryCtrl.replay();

        template.execute(new JcrMappingCallback() {
            public Object doInJcrMapping(PersistenceManager manager) throws JcrMappingException {
                assertNotNull("persistenceManager is null", manager);
                return null;
            }
        });
    }

    /*
     * Test method for 'org.springmodules.jcr.mapping.JcrMappingTemplate.insert(String, Object)'
     */
    public void testInsert() throws Exception {
        template.setAllowCreate(true);

        factoryCtrl.expectAndReturn(factory.getSession(), session);
        session.logout();

        final String path = new String("some string");
        final Object obj = new Object();

        manager.insert(path, obj);

        managerCtrl.replay();
        factoryCtrl.replay();
        sessionCtrl.replay();
        valueFactoryCtrl.replay();

        template.insert(path, obj);
    }

    /*
     * Test method for 'org.springmodules.jcr.mapping.JcrMappingTemplate.update(String, Object)'
     */
    public void testUpdate() throws Exception {
        template.setAllowCreate(true);

        factoryCtrl.expectAndReturn(factory.getSession(), session);
        session.logout();

        final String path = new String("some string");
        final Object obj = new Object();

        manager.update(path, obj);

        managerCtrl.replay();
        factoryCtrl.replay();
        sessionCtrl.replay();
        valueFactoryCtrl.replay();

        template.update(path, obj);
    }

    /*
     * Test method for 'org.springmodules.jcr.mapping.JcrMappingTemplate.remove(String)'
     */
    public void testRemove() throws Exception {
        template.setAllowCreate(true);

        factoryCtrl.expectAndReturn(factory.getSession(), session);
        session.logout();

        final String path = new String("some string");
        final Object obj = new Object();

        manager.remove(path);

        managerCtrl.replay();
        factoryCtrl.replay();
        sessionCtrl.replay();
        valueFactoryCtrl.replay();

        template.remove(path);
    }

    /*
     * Test method for 'org.springmodules.jcr.mapping.JcrMappingTemplate.getObject(Class, String)'
     */
    public void testGetObject() throws Exception {
        template.setAllowCreate(true);

        factoryCtrl.expectAndReturn(factory.getSession(), session);
        session.logout();

        final Object obj = new Object();
        final String path = new String("some string");
        final Class clazz = obj.getClass();

        managerCtrl.expectAndReturn(manager.getObject(clazz, path), obj);

        managerCtrl.replay();
        factoryCtrl.replay();
        sessionCtrl.replay();
        valueFactoryCtrl.replay();

        assertSame("not the same object", obj, template.getObject(clazz, path));
    }

    /*
     * Test method for 'org.springmodules.jcr.mapping.JcrMappingTemplate.getObjects(Query)'
     */
    public void testGetObjects() throws Exception {
        template.setAllowCreate(true);

        factoryCtrl.expectAndReturn(factory.getSession(), session);
        session.logout();

        final List list = new ArrayList();
        MockControl queryCtrl = MockControl.createControl(Query.class);
        Query query = (Query) queryCtrl.getMock();

        managerCtrl.expectAndReturn(manager.getObjects(query), list);

        managerCtrl.replay();
        factoryCtrl.replay();
        sessionCtrl.replay();
        valueFactoryCtrl.replay();

        assertSame("not the same collection", list, template.getObjects(query));
    }

    /*
     * Test method for 'org.springmodules.jcr.mapping.JcrMappingTemplate.convertMappingAccessException(Exception)'
     */
    public void testConvertMappingAccessException() throws Exception {
        template.setAllowCreate(true);

        try {
            createTemplate().execute(new JcrMappingCallback() {
                public Object doInJcrMapping(PersistenceManager manager) throws JcrMappingException {
                    throw new JcrMappingException("some jcr mapping exception");
                }
            });
            fail("Should have thrown JcrSystemException");
        } catch (JcrSystemException e) {
            // expected 
        }
    }

    private JcrMappingOperations createTemplate() throws Exception {

        factoryCtrl.reset();
        factoryCtrl.expectAndReturn(factory.getSession(), session);
        sessionCtrl.expectAndReturn(session.getValueFactory(), valueFactory, 14);
        session.logout();

        JcrMappingTemplate mappingTemplate = new JcrMappingTemplate(factory, mapper);
        mappingTemplate.setAllowCreate(true);
        sessionCtrl.replay();
        mapperCtrl.replay();
        factoryCtrl.replay();

        return mappingTemplate;
    }

}
