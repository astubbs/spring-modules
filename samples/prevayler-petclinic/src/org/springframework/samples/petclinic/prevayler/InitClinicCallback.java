package org.springframework.samples.petclinic.prevayler;

import java.util.Date;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Specialty;
import org.springframework.samples.petclinic.Vet;
import org.springmodules.prevayler.PrevaylerCallback;
import org.springmodules.prevayler.system.PrevalentSystem;

/**
 *
 * @author Sergio Bossa
 */
public class InitClinicCallback implements PrevaylerCallback {
    
    public Object doInTransaction(PrevalentSystem system) {
        if (system.get(Owner.class).isEmpty() && system.get(Pet.class).isEmpty() && system.get(Vet.class).isEmpty()) {
            this.initOwnersAndPets(system);
            this.initVets(system);
        }
        return null;
    }
    
    private void initOwnersAndPets(PrevalentSystem system) {   
        PetType pt1 = new PetType();
        PetType pt2 = new PetType();
        pt1.setName("Cat");
        pt2.setName("Dog");
        pt1 = (PetType) system.save(pt1);
        pt2 = (PetType) system.save(pt2);
        
        Pet p1 = new Pet();
        Pet p2 = new Pet();
        p1.setName("Jack");
        p1.setBirthDate(new Date());
        p1.setType(pt1);
        p2.setName("Camilla");
        p2.setBirthDate(new Date());
        p2.setType(pt2);
        p1 = (Pet) system.save(p1);
        p2 = (Pet) system.save(p2);
        
        Owner owner = new Owner();
        owner.setFirstName("Tom");
        owner.setLastName("Cruise");
        owner.addPet(p1);
        owner.addPet(p2);
        owner = (Owner) system.save(owner);
    }

    private void initVets(PrevalentSystem system) {
        Specialty s1 = new Specialty();
        Specialty s2 = new Specialty();
        s1.setName("Surgery");
        s2.setName("Dentist");
        s1 = (Specialty) system.save(s1);
        s2 = (Specialty) system.save(s2);
        
        Vet v1 = new Vet();
        Vet v2 = new Vet();
        v1.setFirstName("Robert");
        v1.setLastName("DeNiro");
        v1.addSpecialty(s1);
        v2.setFirstName("Al");
        v2.setLastName("Pacino");
        v2.addSpecialty(s2);
        v1 = (Vet) system.save(v1);
        v2 = (Vet) system.save(v2);
    }
}
