package org.springframework.samples.petclinic.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Visit;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * In-Memory implementation of the Clinic interface.
 *
 * @author Omar Irbouh
 * @since 2006.08.20
 */
public class InMemoryClinic implements Clinic {

	// In-Memory storage
	private AtomicInteger counter = new AtomicInteger(1000);

	private Set<Vet> vets = new HashSet<Vet>();
	private Set<PetType> petTypes = new HashSet<PetType>();
	private Map<Integer, Owner> owners = new HashMap<Integer, Owner>();

	public void setVets(Collection<Vet> vets) {
		this.vets = new HashSet<Vet>(vets);
	}

	public void setPetTypes(Collection<PetType> petTypes) {
		this.petTypes = new HashSet<PetType>(petTypes);
	}

	public void setOwners(Collection<Owner> owners) {
		if (!CollectionUtils.isEmpty(owners)) {
			for (Owner owner : owners) {
				this.owners.put(owner.getId(), owner);
			}
		}
	}

	public Collection<Vet> getVets() throws DataAccessException {
		return vets;
	}

	public Collection<PetType> getPetTypes() throws DataAccessException {
		return petTypes;
	}

	public Collection<Owner> findOwners(String lastName) throws DataAccessException {
		List<Owner> found = new ArrayList<Owner>();
		for (Owner owner : owners.values()) {
			if (owner.getLastName().startsWith(lastName)) {
				found.add(owner);
			}
		}
		return found;
	}

	public Owner loadOwner(int id) throws DataAccessException {
		Owner owner = loadOwnerInternal(id);
		return cloneOwner(owner);
	}

	public Pet loadPet(int id) throws DataAccessException {
		Pet pet = loadPetInternal(id);

		// clone the object graph
		Owner owner = cloneOwner(pet.getOwner());

		// return the cloned pet
		return EntityUtils.getById(owner.getPets(), Pet.class, id);
	}

	public void storeOwner(Owner owner) throws DataAccessException {
		if (owner.isNew()) {
			owner.setId(counter.incrementAndGet());
		}

		owners.put(owner.getId(), owner);
	}

	public void storePet(Pet pet) throws DataAccessException {
		if (pet.isNew()) {
			pet.setId(counter.incrementAndGet());
		}

		Owner owner = pet.getOwner();
		storeOwner(owner);
	}

	public void storeVisit(Visit visit) throws DataAccessException {
		if (visit.isNew()) {
			visit.setId(counter.incrementAndGet());
		}

		Pet pet = visit.getPet();
		storePet(pet);
	}

	protected Owner loadOwnerInternal(int id) throws DataAccessException {
		Owner owner = owners.get(id);
		if (owner == null) {
			throw new ObjectRetrievalFailureException(Owner.class, id);
		}
		return owner;
	}

	protected Pet loadPetInternal(int id) throws DataAccessException {
		Pet pet = null;
		for (Owner owner : owners.values()) {
			try {
				pet = EntityUtils.getById(owner.getPets(), Pet.class, id);
				return pet;
			} catch (DataAccessException dae) {
				// ignore
			}
		}
		throw new ObjectRetrievalFailureException(Owner.class, id);
	}

	/**
	 * Deep cloning of Owner
	 *
	 * @param owner Owner to clone
	 * @return cloned Owner
	 */
	protected Owner cloneOwner(Owner owner) {
		Owner clone = new Owner();
		clone.setId(owner.getId());
		clone.setFirstName(new String(owner.getFirstName()));
		clone.setLastName(new String(owner.getLastName()));
		clone.setAddress(new String(owner.getAddress()));
		clone.setCity(new String(owner.getCity()));
		clone.setTelephone(new String(owner.getTelephone()));

		for (Pet pet : owner.getPets()) {
			clone.addPet(clonePet(pet));
		}

		return clone;
	}

	/**
	 * Deep cloning of Pet
	 *
	 * @param pet Pet to clone
	 * @return cloned Pet
	 */
	protected Pet clonePet(Pet pet) {
		Pet clone = new Pet();
		clone.setId(pet.getId());
		clone.setName(new String(pet.getName()));
		clone.setType(pet.getType());

		if (pet.getBirthDate() != null) {
			clone.setBirthDate(new Date(pet.getBirthDate().getTime()));
		}

		// add visits
		for (Visit visit : pet.getVisits()) {
			clone.addVisit(cloneVisit(visit));
		}

		return clone;
	}

	/**
	 * Deep cloning of Visit
	 *
	 * @param visit
	 * @return cloned visit
	 */
	protected Visit cloneVisit(Visit visit) {
		Visit clone = new Visit();
		clone.setId(visit.getId());
		clone.setDate(new Date(visit.getDate().getTime()));

		if (StringUtils.hasText(visit.getDescription())) {
			clone.setDescription(new String(visit.getDescription()));
		}

		return clone;
	}

}