package org.springframework.samples.petclinic.config;

import java.util.Collection;

import org.springframework.samples.petclinic.Person;
import org.springframework.samples.petclinic.Specialty;
import org.springframework.samples.petclinic.Vet;
import org.springframework.util.CollectionUtils;

/**
 * @author <a href="mailto:irbouh@gmail.com">Omar Irbouh</a>
 * @since Aug 30, 2006
 */
public class VetFactory extends PersonFactory {

	private Vet vet = new Vet();

	protected Person getPerson() {
		return this.vet;
	}

	public void setSpecialties(Collection<Specialty> specialties) {
		if (!CollectionUtils.isEmpty(specialties)) {
			for (Specialty specialty : specialties) {
				this.vet.addSpecialty(specialty);
			}
		}
	}

}