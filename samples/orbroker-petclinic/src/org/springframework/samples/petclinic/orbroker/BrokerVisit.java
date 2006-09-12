package org.springframework.samples.petclinic.orbroker;

import java.sql.Date;

import org.springframework.samples.petclinic.Visit;

/**
 * Subclass of Pet that carries java.sql.Date properties
 * which are only relevant for AbstractBrokerClinic.
 *
 * @author Omar Irbouh
 * @since 2005.06.04
 * @see AbstractBrokerClinic
 */
public class BrokerVisit extends Visit {

  /**
   * empty constructor
   */
  public BrokerVisit() {
  }

  /**
   * Copy constructor
   *
   * @param visit Visit object to copy from
   */
  public BrokerVisit(Visit visit) {
    if (visit == null)
      throw new IllegalStateException("visit can not be null");
    setId(visit.getId());
    setDate(visit.getDate());
    setDescription(visit.getDescription());
    setPet(visit.getPet());
  }

  public void setSqlDate(Date sqlDate) {
    if (sqlDate == null)
      setDate(null);
    else
      setDate(new java.util.Date(sqlDate.getTime()));
  }

  public Date getSqlDate() {
    return (getDate() == null ? null : new Date(getDate().getTime()));
  }

}