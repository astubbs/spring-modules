package org.springframework.samples.petclinic.config;

import java.util.Collection;

import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Person;
import org.springframework.samples.petclinic.Pet;
import org.springframework.util.CollectionUtils;

/**
 * @author <a href="mailto:irbouh@gmail.com">Omar Irbouh</a>
 * @since Aug 31, 2006
 */
public class OwnerFactory extends PersonFactory {

	private Owner owner = new Owner();

	protected Person getPerson() {
		return this.owner;
	}

	public void setPets(Collection<Pet> pets) {
		if (!CollectionUtils.isEmpty(pets)) {
			for (Pet pet : pets) {
				this.owner.addPet(pet);
			}
		}
	}

}