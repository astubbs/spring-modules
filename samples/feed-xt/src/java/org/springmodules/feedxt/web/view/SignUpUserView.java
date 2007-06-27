package org.springmodules.feedxt.web.view;

import java.util.Date;
import org.springmodules.feedxt.domain.User;
import org.springmodules.feedxt.domain.repository.UserRepository;
import org.springmodules.xt.model.generator.annotation.ConstructorArg;
import org.springmodules.xt.model.generator.annotation.ConstructorArgType;
import org.springmodules.xt.model.generator.annotation.FactoryMethod;
import org.springmodules.xt.model.generator.annotation.Property;
import org.springmodules.xt.model.generator.annotation.Value;

/**
 * {@link org.springmodules.feedxt.domain.User} factory view to use for signing up an account.
 *
 * @author Sergio Bossa
 */
public interface SignUpUserView {
    
    @ConstructorArg(position=0)
    @ConstructorArgType(type=UserRepository.class)
    public void setUserRepository(UserRepository repository);
    
    public UserRepository getUserRepository();
    
    @Property()
    public void setFirstname(String firstname);
    
    public String getFirstname();
    
    @Property()
    public void setSurname(String surname);
    
    public String getSurname();
    
    @Property()
    public void setBirthdate(Date date);
    
    public Date getBirthdate();
    
    @Value()
    public void setUsername(String username);
    
    public String getUsername();
    
    @Value()
    public void setPassword(String password);
    
    public String getPassword();
    
    @Value()
    public void setConfirmedPassword(String password);
    
    public String getConfirmedPassword();
    
    @FactoryMethod()
    public User makeUser();
}
