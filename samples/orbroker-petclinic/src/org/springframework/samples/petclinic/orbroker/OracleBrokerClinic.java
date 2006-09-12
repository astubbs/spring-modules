package org.springframework.samples.petclinic.orbroker;

/**
 * @author Omar Irbouh
 * @since 2006.08.01
 */
public class OracleBrokerClinic extends AbstractBrokerClinic {

	private static final String SEQUENCE_STATEMENT = "getOracleSequenceNextValue";

	protected boolean supportsIdentity() {
		return false;
	}

	protected String getIdentityStatementId() {
		throw new UnsupportedOperationException("Oracle does not support identity key generation");
	}

	protected boolean supportsSequence() {
		return true;
	}

	protected String getSequenceStatementId() {
		return OracleBrokerClinic.SEQUENCE_STATEMENT;
	}

}
