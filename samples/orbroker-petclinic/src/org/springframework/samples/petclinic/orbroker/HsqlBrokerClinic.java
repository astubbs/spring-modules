package org.springframework.samples.petclinic.orbroker;

/**
 * @author Omar Irbouh
 * @since 2005.06.04
 */
public class HsqlBrokerClinic extends AbstractBrokerClinic {

  private final String IDENTITY_STATEMENT = "getHsqlIdentity";

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
    throw new UnsupportedOperationException("HsqlDB does not support sequences");
  }

}