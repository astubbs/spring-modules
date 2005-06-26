package org.springframework.samples.petclinic.orbroker;

/**
 * @author Omar Irbouh
 * @since 2005.06.04
 */
public class PosgreSQLBrokerClinic extends AbstractBrokerClinic {

  private static final String SEQUENCE_STATEMENT = "getPostgreSQLSequenceNextValue";

  protected boolean supportsIdentity() {
    return false;
  }

  protected String getIdentityStatementId() {
    throw new UnsupportedOperationException("PosgreSQL does not support identity key generation");
  }

  protected boolean supportsSequence() {
    return true;
  }

  protected String getSequenceStatementId() {
    return SEQUENCE_STATEMENT;
  }

}