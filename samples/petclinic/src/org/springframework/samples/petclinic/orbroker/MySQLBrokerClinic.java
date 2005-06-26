package org.springframework.samples.petclinic.orbroker;

/**
 * @author Omar Irbouh
 * @since 2005.06.04
 */
public class MySQLBrokerClinic extends AbstractBrokerClinic {

  private final String IDENTITY_STATEMENT = "getMySqlIdentity";

  protected boolean supportsIdentity() {
    return true;
  }

  protected String getIdentityStatementId() {
    return IDENTITY_STATEMENT;
  }

  protected boolean supportsSequence() {
    return false;
  }

  protected String getSequenceStatementId() {
    throw new UnsupportedOperationException("MySQL does not support identity key generation");
  }

}