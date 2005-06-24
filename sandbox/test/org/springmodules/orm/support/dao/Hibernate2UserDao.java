/*
 * Copyright 2004-2005 the original author or authors.
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
package org.springmodules.orm.support.dao;

import net.sf.hibernate.SessionFactory;

import org.springframework.orm.hibernate.HibernateTemplate;
import org.springmodules.orm.support.domain.User;
import org.springmodules.orm.support.validation.ValidationException;

/**
 * 
 * 
 * @author Steven Devijver
 * @since Jun 19, 2005
 */
public class Hibernate2UserDao implements UserDao {

	private HibernateTemplate hibernateTemplate = null;
	
	public Hibernate2UserDao() {
		super();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory, true);
	}
	
	public void addUser(User user) throws ValidationException {
		this.hibernateTemplate.save(user);
	}

	public User findUser(String username) throws ValidationException {
		return ((User)this.hibernateTemplate.find("from User user where user.username = ?", username).iterator().next());
	}

	public void removeUser(User user) throws ValidationException {
		this.hibernateTemplate.delete(user);
	}

	public void saveUser(User user) throws ValidationException {
		this.hibernateTemplate.update(user);
		this.hibernateTemplate.flush();
	}
}
