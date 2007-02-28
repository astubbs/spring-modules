package org.springmodules.db4o.support;

import junit.framework.TestCase;

import org.springmodules.db4o.Db4oTemplate;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;

import com.db4o.ObjectContainer;

public class Db4oDaoSupportTests extends TestCase {
	private MockControl containerControl;

	private MockControl templateControl;

	private ObjectContainer container;

	private Db4oTemplate template;

	protected void setUp() throws Exception {
		containerControl = MockControl.createStrictControl(ObjectContainer.class);
		container = (ObjectContainer) containerControl.getMock();

		templateControl = MockClassControl.createStrictControl(Db4oTemplate.class);
		template = (Db4oTemplate) templateControl.getMock();
	}

	protected void tearDown() throws Exception {
		containerControl.verify();
		templateControl.verify();
	}

	/*
	 * Test method for
	 * 'org.db4ospring.support.Db4oDaoSupport.setObjectContainer'
	 */
	public void testConfigureWithObjectContainer() {
		containerControl.replay();
		templateControl.replay();

		Db4oDaoSupport dao = new Db4oDaoSupport() {
		};
		dao.setObjectContainer(container);
		// must not throw an exception
		dao.afterPropertiesSet();

		assertNotNull(dao.getDb4oTemplate());
		assertEquals(container, dao.getDb4oTemplate().getObjectContainer());
		assertEquals(container, dao.getObjectContainer());
	}

	/*
	 * Test method for 'org.db4ospring.support.Db4oDaoSupport.setDb4oTemplate'
	 */
	public void testConfigureWithTemplate() {
		templateControl.expectAndReturn(template.getObjectContainer(), container, 2);

		containerControl.replay();
		templateControl.replay();

		Db4oDaoSupport dao = new Db4oDaoSupport() {
		};
		dao.setDb4oTemplate(template);
		dao.afterPropertiesSet();

		assertEquals(template, dao.getDb4oTemplate());
		assertEquals(container, dao.getObjectContainer());
		assertEquals(container, dao.getDb4oTemplate().getObjectContainer());
	}

	/*
	 * Test method for 'org.db4ospring.support.Db4oDaoSupport.checkDaoConfig'
	 */
	public void testMissingConfiguration() {
		containerControl.replay();
		templateControl.replay();
		try {
			Db4oDaoSupport dao = new Db4oDaoSupport() {
			};
			dao.afterPropertiesSet();
			fail("Should have thrown IllegalArgumentException because neither #setObjectContainer nor #setTemplate have been invoked");
		}
		catch (IllegalArgumentException ex) {
			// it's ok. We expect this exception to be thrown
		}
	}
}
