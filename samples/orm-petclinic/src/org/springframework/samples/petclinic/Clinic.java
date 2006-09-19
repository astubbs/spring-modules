/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springframework.samples.petclinic;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

/**
 * The high-level PetClinic business interface.
 *
 * <p>This is basically a data access object,
 * as PetClinic doesn't have dedicated business logic.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public interface Clinic {

	/**
	 * Retrieve all <code>Vet</code>s from the datastore.
	 * @return a <code>Collection</code> of <code>Vet</code>s
	 */
	Collection getVets() throws DataAccessException;

	/**
	 * Retrieve all <code>PetType</code>s from the datastore.
	 * @return a <code>Collection</code> of <code>PetType</code>s
	 */
	Collection getPetTypes() throws DataAccessException;

	/**
	 * Retrieve <code>Owner</code>s from the datastore by last name,
	 * returning all owners whose last name <i>starts</i> with the given name.
	 * @param lastName Value to search for
	 * @return a <code>Collection</code> of matching <code>Owner</code>s
	 * (or an empty <code>Collection</code> if none found)
	 */
	Collection findOwners(String lastName) throws DataAccessException;

	/**
	 * Retrieve an <code>Owner</code> from the datastore by id.
	 * @param id the id to search for
	 * @return the <code>Owner</code> if found
	 * @throws org.springframework.dao.DataRetrievalFailureException if not found
	 */
	Owner loadOwner(int id) throws DataAccessException;

	/**
	 * Retrieve a <code>Pet</code> from the datastore by id.
	 * @param id the id to search for
	 * @return the <code>Pet</code> if found
	 * @throws org.springframework.dao.DataRetrievalFailureException if not found
	 */
	Pet loadPet(int id) throws DataAccessException;

	/**
	 * Save an <code>Owner</code> to the datastore,
	 * either inserting or updating it.
	 * @param owner the <code>Owner</code> to save
	 * @see BaseEntity#isNew
	 */
	void storeOwner(Owner owner) throws DataAccessException;

	/**
	 * Save a <code>Pet</code> to the datastore,
	 * either inserting or updating it.
	 * @param pet the <code>Pet</code> to save
	 * @see BaseEntity#isNew
	 */
	void storePet(Pet pet) throws DataAccessException;

	/**
	 * Save a <code>Visit</code> to the datastore,
	 * either inserting or updating it.
	 * @param visit the <code>Visit</code> to save
	 * @see BaseEntity#isNew
	 */
	void storeVisit(Visit visit) throws DataAccessException;

}
