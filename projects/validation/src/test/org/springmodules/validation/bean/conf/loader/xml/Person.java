package org.springmodules.validation.bean.conf.loader.xml;

import org.springframework.util.ObjectUtils;

/**
 * @author Uri Boness
 */
public class Person {

    private String firstName;

    private String lastName;

    private int age;

    private String email;

    private String password;

    private String confirmPassword;

    private int smallInteger = 0;

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

    public int getSmallInteger() {
        return smallInteger;
    }

    public void setSmallInteger(int smallInteger) {
        this.smallInteger = smallInteger;
    }

    public boolean confirmPasswords() {
        return ObjectUtils.nullSafeEquals(password, confirmPassword);
    }

    public boolean validateLastNameIsLongerThanTen() {
        return lastName.length() > 10;
    }

}
