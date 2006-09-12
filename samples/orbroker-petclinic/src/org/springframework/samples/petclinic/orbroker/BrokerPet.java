package org.springframework.samples.petclinic.orbroker;

import java.sql.Date;

import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;

/**
 * Subclass of Pet that carries java.sql.Date properties
 * which are only relevant for AbstractBrokerClinic.
 *
 * @author Omar Irbouh
 * @since 2005.06.04
 * @see AbstractBrokerClinic
 */
public class BrokerPet extends Pet {

  /**
   * empty constructor
   */
  public BrokerPet() {
  }

  /**
   * Copy constructor
   *
   * @param pet Pet object to copy from
   */
  public BrokerPet(Pet pet) {
    if (pet == null)
      throw new IllegalStateException("pet can not be null");

    setId(pet.getId());
    setName(pet.getName());
    setType(pet.getType());
    setBirthDate(pet.getBirthDate());
    setOwner(pet.getOwner());
  }

  public void setSqlBirthDate(Date sqlBirthDate) {
    if (sqlBirthDate == null)
      setBirthDate(null);
    else
      setBirthDate(new java.util.Date(sqlBirthDate.getTime()));
  }

  public Date getSqlBirthDate() {
    return (getBirthDate() == null ? null : new Date(getBirthDate().getTime()));
  }

  // it seems there is a bug in ORBroker!!! In this case, ORBroker does not see
  // getter/setter of property type that are inherited from class Pet
  // we need to redefine two methods that deleate to super getter/setter

  public void setType(PetType petType) {
    super.setType(petType);
  }

  public PetType getType() {
    return super.getType();
  }
}