package org.springframework.samples.petclinic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

/**
 * Simple JavaBean business object representing a pet.
 *
 * @author  Ken Krebs
 * @author Juergen Hoeller
 */
public class Pet extends NamedEntity {

	private Date birthDate;

	private PetType type;

	private Owner owner;

	private Set visits;

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setType(PetType type) {
		this.type = type;
	}

	public PetType getType() {
		return type;
	}

	protected void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Owner getOwner() {
		return owner;
	}

	protected void setVisitsInternal(Set visits) {
		this.visits = visits;
	}

	protected Set getVisitsInternal() {
		if (this.visits == null) {
			this.visits = new HashSet();
		}
		return this.visits;
	}

	public List getVisits() {
		List sortedVisits = new ArrayList(getVisitsInternal());
		PropertyComparator.sort(sortedVisits, new MutableSortDefinition("date", false, false));
		return Collections.unmodifiableList(sortedVisits);
	}

	public void addVisit(Visit visit) {
		getVisitsInternal().add(visit);
		visit.setPet(this);
	}

}
