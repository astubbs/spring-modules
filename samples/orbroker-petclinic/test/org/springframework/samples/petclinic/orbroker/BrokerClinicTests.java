package org.springframework.samples.petclinic.orbroker;

import org.springframework.samples.petclinic.AbstractClinicTests;

/**
 * Live unit tests for HsqlBrokerClinic implementation.
 * "applicationContext-broker.xml" determines the actual beans to test.
 *
 * @author Juergen Hoeller
 * @author Omar Irbouh
 */
public class BrokerClinicTests extends AbstractClinicTests {

	protected String[] getConfigLocations() {
		return new String[] { "/org/springframework/samples/petclinic/orbroker/applicationContext-broker.xml" };
	}

}
