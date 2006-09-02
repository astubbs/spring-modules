package org.springframework.samples.petclinic.config;

import org.springframework.samples.petclinic.BaseEntity;
import org.springframework.samples.petclinic.Person;

/**
 * @author <a href="mailto:irbouh@gmail.com">Omar Irbouh</a>
 * @since Aug 31, 2006
 */
public abstract class PersonFactory extends BaseEntityFactory {

	protected abstract Person getPerson();

	protected BaseEntity getEntity() {
		return getPerson();
	}

	public void setFirstName(String firstName) {
		getPerson().setFirstName(firstName);
	}

	public void setLastName(String lastName) {
		getPerson().setLastName(lastName);
	}

	public void setAddress(String address) {
		getPerson().setAddress(address);
	}

	public void setCity(String city) {
		getPerson().setCity(city);
	}

	public void setTelephone(String telephone) {
		getPerson().setTelephone(telephone);
	}

}