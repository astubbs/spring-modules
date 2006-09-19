package org.springframework.samples.petclinic.ojb;

import org.springframework.samples.petclinic.AbstractClinicTests;

/**
 * Live unit tests for OJB PersistenceBrokerClinic implementation.
 * "applicationContext-ojb.xml" determines the actual beans to test.
 *
 * @author Juergen Hoeller
 * @since 06.07.2004
 */
public class PersistenceBrokerClinicTests extends AbstractClinicTests {

	protected String[] getConfigLocations() {
		return new String[] { "/org/springframework/samples/petclinic/ojb/applicationContext-ojb.xml" };
	}

}
