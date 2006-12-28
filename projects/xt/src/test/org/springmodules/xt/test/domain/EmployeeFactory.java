package org.springmodules.xt.test.domain;

import org.springmodules.xt.model.generator.annotation.ConstructorArg;
import org.springmodules.xt.model.generator.annotation.FactoryMethod;
import org.springmodules.xt.model.generator.annotation.Property;

/**
 * {@link IEmployee} factory interface.
 *
 * @author Sergio Bossa
 */
public interface EmployeeFactory {
    
    @ConstructorArg(position = 0)
    void setNickname(String nickname);
    
    @ConstructorArg(position = 1)
    void setMatriculationCode(String matriculationCode);

    @Property(access = Property.AccessType.FIELD)
    void setFirstname(String firstname);

    @Property()
    void setSurname(String surname);
    
    @FactoryMethod()
    IEmployee make();
    
    void setUnsupportedProperty(String value);
}
