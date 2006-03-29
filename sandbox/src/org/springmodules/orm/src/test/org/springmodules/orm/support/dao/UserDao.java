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

import org.springmodules.orm.support.domain.User;
import org.springmodules.orm.support.validation.ValidationException;

/**
 * 
 * 
 * @author Steven Devijver
 * @since Jun 19, 2005
 */
public interface UserDao {

	public void addUser(User user) throws ValidationException;
	
	public User findUser(String username) throws ValidationException;
	
	public void saveUser(User user) throws ValidationException;
	
	public void removeUser(User user) throws ValidationException;
	
}
