package org.springframework.samples.petclinic.config;

import java.util.Collection;
import java.util.Date;

import org.springframework.samples.petclinic.BaseEntity;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Visit;
import org.springframework.util.CollectionUtils;

/**
 * @author <a href="mailto:irbouh@gmail.com">Omar Irbouh</a>
 * @since Aug 30, 2006
 */
public class PetFactory extends BaseEntityFactory {

	private Pet pet = new Pet();

	protected BaseEntity getEntity() {
		return this.pet;
	}

	public void setName(String name) {
		this.pet.setName(name);
	}

	public void setBirthDate(Date birthDate) {
		this.pet.setBirthDate(birthDate);
	}

	public void setType(PetType type) {
		this.pet.setType(type);
	}

	public void setVisits(Collection<Visit> visits) {
		if (!CollectionUtils.isEmpty(visits)) {
			for (Visit visit : visits) {
				this.pet.addVisit(visit);
			}
		}
	}

}