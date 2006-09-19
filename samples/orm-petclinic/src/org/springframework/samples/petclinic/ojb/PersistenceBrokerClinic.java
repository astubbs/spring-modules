package org.springframework.samples.petclinic.ojb;

import java.util.Collection;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Visit;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

/**
 * OJB PersistenceBroker implementation of the Clinic interface.
 *
 * <p>The mappings are defined in "OJB-repository.xml",
 * located in the root of the class path.
 *
 * @author Juergen Hoeller
 * @author Omar Irbouh
 * @since 04.07.2004
 */
public class PersistenceBrokerClinic extends PersistenceBrokerDaoSupport implements Clinic {

	public Collection getVets() throws DataAccessException {
		QueryByCriteria query = new QueryByCriteria(Vet.class);
		query.addOrderByAscending("lastName");
		query.addOrderByAscending("firstName");
		return getPersistenceBrokerTemplate().getCollectionByQuery(query);
	}

	public Collection getPetTypes() throws DataAccessException {
		QueryByCriteria query = new QueryByCriteria(PetType.class);
		query.addOrderByAscending("name");
		return getPersistenceBrokerTemplate().getCollectionByQuery(query);
	}

	public Collection findOwners(String lastName) throws DataAccessException {
		Criteria criteria = new Criteria();
		criteria.addLike("lastName", lastName + "%");
		Query query = new QueryByCriteria(Owner.class, criteria);
		return getPersistenceBrokerTemplate().getCollectionByQuery(query);
	}

	public Owner loadOwner(int id) throws DataAccessException {
		return (Owner) getPersistenceBrokerTemplate().getObjectById(Owner.class, new Integer(id));
	}

	public Pet loadPet(int id) throws DataAccessException {
		return (Pet) getPersistenceBrokerTemplate().getObjectById(Pet.class, new Integer(id));
	}

	public void storeOwner(Owner owner) throws DataAccessException {
		getPersistenceBrokerTemplate().store(owner);
	}

	public void storePet(Pet pet) throws DataAccessException {
		getPersistenceBrokerTemplate().store(pet);
	}

	public void storeVisit(Visit visit) {
		getPersistenceBrokerTemplate().store(visit);
	}

}
