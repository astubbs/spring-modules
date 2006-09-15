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

package org.springmodules.orm.ojb.support;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import org.springmodules.orm.ojb.PersistenceBrokerTemplate;

/**
 * @author Juergen Hoeller
 * @since 08.07.2004
 */
public class PersistenceBrokerDaoSupportTests extends TestCase {

	public void testWithDefaultSettings() throws Exception {
		final List test = new ArrayList();
		PersistenceBrokerDaoSupport dao = new PersistenceBrokerDaoSupport() {
			protected void initDao() {
				test.add("test");
			}
		};
		dao.afterPropertiesSet();
		assertEquals(PersistenceBrokerFactory.getDefaultKey(), dao.getPersistenceBrokerTemplate().getPbKey());
		assertEquals("initDao called", test.size(), 1);
	}

	public void testWithJcdAlias() throws Exception {
		final List test = new ArrayList();
		PersistenceBrokerDaoSupport dao = new PersistenceBrokerDaoSupport() {
			protected void initDao() {
				test.add("test");
			}
		};
		dao.setJcdAlias("alias1");
		dao.afterPropertiesSet();
		assertEquals("alias1", dao.getPersistenceBrokerTemplate().getJcdAlias());
		assertEquals("initDao called", test.size(), 1);
	}

	public void testWithPersistenceBrokerTemplate() throws Exception {
		PersistenceBrokerTemplate template = new PersistenceBrokerTemplate();
		final List test = new ArrayList();
		PersistenceBrokerDaoSupport dao = new PersistenceBrokerDaoSupport() {
			protected void initDao() {
				test.add("test");
			}
		};
		dao.setPersistenceBrokerTemplate(template);
		dao.afterPropertiesSet();
		assertEquals("Correct PersistenceBrokerTemplate", template, dao.getPersistenceBrokerTemplate());
		assertEquals("initDao called", test.size(), 1);
	}

}
