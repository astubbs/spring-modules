/*
 * Copyright 2002-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.orm.ojb;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.IdentityFactory;
import org.apache.ojb.broker.PBKey;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.easymock.MockControl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springmodules.beans.TestBean;

/**
 * @author Juergen Hoeller
 * @since 08.07.2004
 */
public class PersistenceBrokerTemplateTests extends TestCase {

	protected void setUp() throws Exception {
		Resource res = new ClassPathResource("OJB.properties");
		System.setProperty("OJB.properties", res.getFile().getAbsolutePath());
	}

	public void testPbKey() {
		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate();
		assertEquals(PersistenceBrokerFactory.getDefaultKey(), template.getPbKey());

		template = new PersistenceBrokerTemplate();
		template.setJcdAlias("alias1");
		assertEquals("alias1", template.getJcdAlias());
		assertEquals("alias1", template.getPbKey().getAlias());

		template = new PersistenceBrokerTemplate();
		template.setPbKey(new PBKey("alias2", "user2", "password2"));
		assertEquals("alias2", template.getJcdAlias());
		assertEquals("alias2", template.getPbKey().getAlias());
		assertEquals("user2", template.getPbKey().getUser());
		assertEquals("password2", template.getPbKey().getPassword());
	}

	public void testGetObjectById() {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		MockControl idfControl = MockControl.createControl(IdentityFactory.class);
		final IdentityFactory idf = (IdentityFactory) idfControl.getMock();

		Identity identity = new Identity(String.class, Object.class, new Object[] {"id"});
		pb.serviceIdentity();
		pbControl.setReturnValue(idf, 1);
		idf.buildIdentity(String.class, "id");
		idfControl.setReturnValue(identity, 1);
		pb.getObjectByIdentity(identity);
		pbControl.setReturnValue("", 1);
		pb.close();
		pbControl.setReturnValue(true, 1);
		pbControl.replay();
		idfControl.replay();

		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};
		assertEquals("", template.getObjectById(String.class, "id"));
		pbControl.verify();
		idfControl.verify();
	}

	public void testGetObjectByQuery() {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		Query query = new QueryByCriteria(TestBean.class);
		pb.getObjectByQuery(query);
		pbControl.setReturnValue("", 1);
		pb.close();
		pbControl.setReturnValue(true, 1);
		pbControl.replay();

		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};
		assertEquals("", template.getObjectByQuery(query));
		pbControl.verify();
	}

	public void testGetCollectionByQuery() {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		Query query = new QueryByCriteria(TestBean.class);
		Collection result = new HashSet();
		pb.getCollectionByQuery(query);
		pbControl.setReturnValue(result, 1);
		pb.close();
		pbControl.setReturnValue(true, 1);
		pbControl.replay();

		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};
		assertEquals(result, template.getCollectionByQuery(query));
		pbControl.verify();
	}

	public void testGetIteratorByQuery() {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		Query query = new QueryByCriteria(TestBean.class);
		Iterator result = new HashSet().iterator();
		pb.getIteratorByQuery(query);
		pbControl.setReturnValue(result, 1);
		pb.close();
		pbControl.setReturnValue(true, 1);
		pbControl.replay();

		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};
		assertEquals(result, template.getIteratorByQuery(query));
		pbControl.verify();
	}

	public void testGetReportQueryIteratorByQuery() {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		Query query = new QueryByCriteria(TestBean.class);
		Iterator result = new HashSet().iterator();
		pb.getReportQueryIteratorByQuery(query);
		pbControl.setReturnValue(result, 1);
		pb.close();
		pbControl.setReturnValue(true, 1);
		pbControl.replay();

		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};
		assertEquals(result, template.getReportQueryIteratorByQuery((query)));
		pbControl.verify();
	}

	public void testGetCount() {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		Query query = new QueryByCriteria(TestBean.class);
		pb.getCount(query);
		pbControl.setReturnValue(0, 1);
		pb.close();
		pbControl.setReturnValue(true, 1);
		pbControl.replay();

		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};
		assertEquals(0, template.getCount(query));
		pbControl.verify();
	}

	public void testRemoveFromCache() {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		Object entity = new Object();
		pb.removeFromCache(entity);
		pbControl.setVoidCallable(1);
		pb.close();
		pbControl.setReturnValue(true, 1);
		pbControl.replay();

		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};
		template.removeFromCache(entity);
		pbControl.verify();
	}

	public void testClearCache() {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		pb.clearCache();
		pbControl.setVoidCallable(1);
		pb.close();
		pbControl.setReturnValue(true, 1);
		pbControl.replay();

		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};
		template.clearCache();
		pbControl.verify();
	}

	public void testStore() {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		Object entity = new Object();
		pb.store(entity);
		pbControl.setVoidCallable(1);
		pb.close();
		pbControl.setReturnValue(true, 1);
		pbControl.replay();

		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};
		template.store(entity);
		pbControl.verify();
	}

	public void testDelete() {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		Object entity = new Object();
		pb.delete(entity);
		pbControl.setVoidCallable(1);
		pb.close();
		pbControl.setReturnValue(true, 1);
		pbControl.replay();

		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};
		template.delete(entity);
		pbControl.verify();
	}

	public void testDeleteByQuery() {
		MockControl pbControl = MockControl.createControl(PersistenceBroker.class);
		final PersistenceBroker pb = (PersistenceBroker) pbControl.getMock();
		Query query = new QueryByCriteria(TestBean.class);
		pb.deleteByQuery(query);
		pbControl.setVoidCallable(1);
		pb.close();
		pbControl.setReturnValue(true, 1);
		pbControl.replay();

		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate() {
			protected PersistenceBroker getPersistenceBroker() {
				return pb;
			}
		};
		template.deleteByQuery(query);
		pbControl.verify();
	}

}
