/*
 * OwnerTests.java
 *
 */
 
package org.springframework.samples.petclinic;

import junit.framework.*;

/**
 * 	JUnit test for Owner
 * 
 * @author Ken Krebs
 */
public class OwnerTests extends TestCase {

	public void testHasPet() {
		Owner owner = new Owner();
		Pet fido = new Pet();
		fido.setName("Fido");
		assertNull(owner.getPet("Fido"));
		assertNull(owner.getPet("fido"));
		owner.addPet(fido);
		assertEquals(fido, owner.getPet("Fido"));
		assertEquals(fido, owner.getPet("fido"));
	}

}
