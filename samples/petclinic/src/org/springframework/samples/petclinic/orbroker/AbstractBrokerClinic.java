package org.springframework.samples.petclinic.orbroker;

import net.sourceforge.orbroker.BrokerException;
import net.sourceforge.orbroker.Executable;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springmodules.orm.orbroker.BrokerCallback;
import org.springmodules.orm.orbroker.support.BrokerDaoSupport;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.Visit;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Specialty;
import org.springframework.samples.petclinic.util.EntityUtils;

import java.util.Collection;
import java.util.List;
import java.util.Iterator;

/**
 * Base class for O/RBroker implementations of the Clinic interface.
 *
 * @author Omar Irbouh
 * @since 2005.06.04
 */
public abstract class AbstractBrokerClinic extends BrokerDaoSupport implements Clinic {

  private static final String SEQUENCE_PARAMETER = "sequenceName";
  private static final String OWNERS_SEQUENCE = "seq_owners";
  private static final String PETS_SEQUENCE = "seq_pets";
  private static final String VISITS_SEQUENCE = "seq_visits";

  public Collection getVets() throws DataAccessException {
    // Retrieve the list of all vets.
    List vets = getBrokerTemplate().selectMany("getVets");

    // Retrieve the list of all possible specialties.
    List specialties = getBrokerTemplate().selectMany("getSpecialties");

    // Build each vet's list of specialties.
    Iterator vi = vets.iterator();
    while (vi.hasNext()) {
      Vet vet = (Vet) vi.next();
      List vetSpecialtiesIds = getBrokerTemplate().selectMany("getSpecialtiesByVet", "id", vet.getId());
      Iterator vsi = vetSpecialtiesIds.iterator();
      while (vsi.hasNext()) {
        int specialtyId = ((Integer) vsi.next()).intValue();
        Specialty specialty = (Specialty) EntityUtils.getById(specialties, Specialty.class, specialtyId);
        vet.addSpecialty(specialty);
      }
    }

    return vets;
  }

  public Collection getPetTypes() throws DataAccessException {
    return getBrokerTemplate().selectMany("getPetTypes");
  }

  public Collection findOwners(String lastName) throws DataAccessException {
    List owners = getBrokerTemplate().selectMany("findOwnersByLastName", "lastName", lastName + "%");
    // load owners pets and visits
    loadOwnersPetsAndVisits(owners);
    return owners;
  }

  public Owner loadOwner(int id) throws DataAccessException {
    Owner owner = (Owner) getBrokerTemplate().selectOne("loadOwnerById", "id", new Integer(id));
    if (owner == null) {
      throw new ObjectRetrievalFailureException(Owner.class, new Integer(id));
    }
    // load pets and visits
    loadPetsAndVisits(owner);
    return owner;
  }

  public Pet loadPet(int id) throws DataAccessException {
    Pet pet = (Pet) getBrokerTemplate().selectOne("loadPetById", "id", new Integer(id));
    if (pet == null) {
      throw new ObjectRetrievalFailureException(Pet.class, new Integer(id));
    }
    //load owner
    Owner owner = (Owner) getBrokerTemplate().selectOne("loadOwnerByPet", "id", pet.getId());
    owner.addPet(pet);
    // load visits
    loadVisits(pet);
    return pet;
  }

  public void storeOwner(Owner owner) throws DataAccessException {
    if (owner.isNew()) {
      if (supportsSequence()) {
        owner.setId(getSequenceNextValue(OWNERS_SEQUENCE));
      }
      getBrokerTemplate().execute("insertOwner", "owner", owner);
      if (supportsIdentity()) {
        owner.setId(getIdentity());
      }
    } else {
      getBrokerTemplate().execute("updateOwner", "owner", owner);
    }
  }

  public void storePet(Pet pet) throws DataAccessException {
    if (pet.isNew()) {
      if (supportsSequence()) {
        pet.setId(getSequenceNextValue(PETS_SEQUENCE));
      }
      getBrokerTemplate().execute("insertPet", "pet", new BrokerPet(pet));
      if (supportsIdentity()) {
        pet.setId(getIdentity());
      }
    } else {
      getBrokerTemplate().execute("updatePet", "pet", new BrokerPet(pet));
    }
  }

  public void storeVisit(Visit visit) throws DataAccessException {
    if (visit.isNew()) {
      if (supportsSequence()) {
        visit.setId(getSequenceNextValue(VISITS_SEQUENCE));
      }
      getBrokerTemplate().execute("insertVisit", "visit", new BrokerVisit(visit));
      if (supportsIdentity()) {
        visit.setId(getIdentity());
      }
    } else {
      throw new UnsupportedOperationException("Visit update not supported");
    }
  }

  protected void loadOwnersPetsAndVisits(List owners) {
    for (Iterator oi = owners.iterator(); oi.hasNext();) {
      Owner owner = (Owner) oi.next();
      loadPetsAndVisits(owner);
    }
  }

  protected void loadPetsAndVisits(Owner owner) {
    List pets = getBrokerTemplate().selectMany("loadPetsByOwner", "id", owner.getId());
    // load visits
    for (Iterator pi = pets.iterator(); pi.hasNext();) {
      Pet pet = (Pet) pi.next();
      loadVisits(pet);
      owner.addPet(pet);
    }
  }

  protected void loadVisits(Pet pet) {
    List visits = getBrokerTemplate().selectMany("getVisitsByPet", "id", pet.getId());
    for (Iterator vi = visits.iterator(); vi.hasNext();) {
      Visit visit = (Visit) vi.next();
      pet.addVisit(visit);
    }
  }

  protected Integer getIdentity() {
    // we need to cast to Number since some JDBC drivers return Integer and others return Long
    final Number value = (Number) getBrokerTemplate().selectOne(getIdentityStatementId());
    return new Integer(value.intValue());
  }

  protected Integer getSequenceNextValue(final String sequenceName) {
    // we need to cast to Number since some JDBC drivers return Integer and others return Long
    final Number value = (Number) getBrokerTemplate().execute(new BrokerCallback() {
      public Object doInBroker(Executable executable) throws BrokerException {
        executable.setTextReplacement(SEQUENCE_PARAMETER, sequenceName);
        return executable.selectOne(getSequenceStatementId());
      }
    });
    return new Integer(value.intValue());
  }

  protected abstract boolean supportsIdentity();

  protected abstract String getIdentityStatementId();

  protected abstract boolean supportsSequence();

  protected abstract String getSequenceStatementId();
}