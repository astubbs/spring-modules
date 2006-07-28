package org.springmodules.validation.bean.conf.namespace;

import org.springmodules.validation.bean.conf.annotation.handler.Email;
import org.springmodules.validation.bean.conf.annotation.handler.Length;
import org.springmodules.validation.bean.conf.annotation.handler.Min;
import org.springmodules.validation.bean.conf.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.annotation.handler.NotNull;
import org.springmodules.validation.bean.conf.annotation.handler.Expression;

/**
 * @author Uri Boness
 */
@Expression(value = "password == confirmPassword", errorCode = "passwords.do.not.match")
public class AnnotatedPerson {

    @NotNull @NotBlank @Expression("tupper(firstName) == 'URI'")
    private String firstName;

    @NotNull @NotBlank @IsFirstLetterCapitalized
    private String lastName;

    @Min(0)
    private int age;

    @Email
    private String email;

    @Length(min = 3)
    private String password;

    private String confirmPassword;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
