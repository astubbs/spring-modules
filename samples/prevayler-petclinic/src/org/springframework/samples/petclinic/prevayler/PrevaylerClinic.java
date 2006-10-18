package org.springframework.samples.petclinic.prevayler;

import java.util.Collection;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Visit;
import org.springmodules.prevayler.support.PrevaylerDaoSupport;

/**
 *
 * @author Sergio Bossa
 */
public class PrevaylerClinic extends PrevaylerDaoSupport implements Clinic {
    
    public Collection getVets() throws DataAccessException {
        return this.getPrevaylerTemplate().get(Vet.class);
    }

    public Collection getPetTypes() throws DataAccessException {
        return this.getPrevaylerTemplate().get(PetType.class);
    }

    public Collection findOwners(String lastName) throws DataAccessException {
        FindOwnersByLastnameCallback callback = new FindOwnersByLastnameCallback(lastName);
        return (Collection) this.getPrevaylerTemplate().execute(callback);
    }

    public Owner loadOwner(int id) throws DataAccessException {
        return (Owner) this.getPrevaylerTemplate().get(Owner.class, new Integer(id));
    }

    public Pet loadPet(int id) throws DataAccessException {
        return (Pet) this.getPrevaylerTemplate().get(Pet.class, new Integer(id));
    }

    public void storeOwner(Owner owner) throws DataAccessException {
        if (owner.getId() == null) {
            Owner newOwner = (Owner) this.getPrevaylerTemplate().save(owner);
            owner.setId(newOwner.getId());
        }
        else {
            this.getPrevaylerTemplate().update(owner);
        }
    }

    public void storePet(Pet pet) throws DataAccessException {
        if (pet.getId() == null) {
            Pet newPet = (Pet) this.getPrevaylerTemplate().save(pet);
            pet.setId(newPet.getId());
        }
        else {
            this.getPrevaylerTemplate().update(pet);
        }
    }

    public void storeVisit(Visit visit) throws DataAccessException {
        if (visit.getId() == null) {
            Visit newVisit = (Visit) this.getPrevaylerTemplate().save(visit);
            visit.setId(newVisit.getId());
        }
        else {
            this.getPrevaylerTemplate().update(visit);
        }
    }

    public void initClinic() throws Exception {
        InitClinicCallback callback = new InitClinicCallback();
        this.getPrevaylerTemplate().execute(callback);
    }
}
